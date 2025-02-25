const SERVER_URL = "http://localhost:8000";

const daysOptions = document.getElementById("last-days-options");
const dateRangeOptions = document.getElementById("date-range-options");
const periodSelector = document.getElementById("period");

const loadingIndicator = document.getElementById("loading-indicator");


function toggleVisibility(element, show) {
    if (element) {
        if (show) {
            element.classList.remove('hidden');
            element.classList.add('visible');
        } else {
            element.classList.remove('visible');
            element.classList.add('hidden');
        }
    }
}

async function searchPatient() {
    const patientCode = document.getElementById("patient-search-input").value.trim();

    if (!patientCode) {
        alert("Please enter a valid patient code.");
        return;
    }

    const filterDates = getFilterDates();
    if (!filterDates) {
        return;
    }

    toggleVisibility(loadingIndicator, true);

    try {
        const patientData = await getPatientByCode(patientCode);
        console.log("patient data: " + patientData);

        if (!patientData || !patientData.id) {
            alert("Patient not found. Please check the code and try again.");
            return;
        }

        document.getElementById("patient-data-span").textContent = `${patientData.name} ${patientData.surname} (${patientData.code})`;

        const medications = await getMedications(patientData.id);
        if (!medications || medications.length === 0) {
            alert("No medications found for this patient.");
            return;
        }

        await displayMedications(medications, patientData.id);

    } catch (error) {
        if (error.message.includes("Error ")) {
            alert("Patient not found");
        } else if (error.message.includes("Network")) {
            alert("Network error: Unable to reach the server. Please check your connection.");
        } else if (error.message.includes("Error:")) {
            alert("An unexpected error occurred while searching for the patient. Please try again later.");
        }
    } finally {
        toggleVisibility(loadingIndicator, false);
    }
}


async function displayMedications(medications, patientId) {
    console.log("Displaying medications for patient ID:", patientId);
    console.log("Medications data:", medications);

    const reportContent = document.getElementById('content-resume');
    reportContent.innerHTML = '';

    if (!medications || medications.length === 0) {
        reportContent.innerHTML = '<p>No medications found for this patient.</p>';
        return;
    }

    for (const medication of medications) {
        const startDateMed = new Date(medication.start_date);
        const endDateMed = new Date(startDateMed);
        endDateMed.setDate(startDateMed.getDate() + medication.treatment_duration);

        console.log("Processing medication:", medication);

        const medicationArticle = document.createElement('article');
        medicationArticle.className = 'medication';

        try {
            const posologies = await getPosologies(patientId, medication.id);
            const intakes = await getIntakes(patientId, medication.id);

            posologies.sort((a, b) => (a.hour * 60 + a.minute) - (b.hour * 60 + b.minute));

            console.log("Posologies: " + posologies.map(p => p.hour + ":" + p.minute));

            const medicationTitle = document.createElement('h3');
            medicationTitle.textContent = `${medication.name} ${medication.dosage}`;
            medicationArticle.appendChild(medicationTitle);

            const dateRangeInfo = document.createElement('h4');
            dateRangeInfo.textContent = `Date range: ${startDateMed.getDate().toString().padStart(2, '0')}/${(startDateMed.getMonth() + 1).toString().padStart(2, '0')}/${startDateMed.getFullYear()} - ${endDateMed.getDate().toString().padStart(2, '0')}/${(endDateMed.getMonth() + 1).toString().padStart(2, '0')}/${endDateMed.getFullYear()}`;
            medicationArticle.appendChild(dateRangeInfo);

            const medicationPosologies = document.createElement('h4');
            let formattedPosologies = posologies.map(posology => 
                `${posology.hour.toString().padStart(2, '0')}:${posology.minute.toString().padStart(2, '0')}`
            ).join(', ');
            medicationPosologies.textContent = `Posologies: ${formattedPosologies}`;
            medicationArticle.appendChild(medicationPosologies);

            console.log("Posologies for medication:", posologies);
            console.log("Intakes for medication:", intakes);
            const table = createTable(posologies, intakes, startDateMed, endDateMed);
            medicationArticle.appendChild(table);
        } catch (error) {
            console.error("Error fetching medication data:", error);
            alert("An error occurred while loading the medication data. Please try again later.");
            medicationArticle.appendChild(document.createElement('p')).textContent = "Error loading intake data.";
        }

        reportContent.appendChild(medicationArticle);
    }
}

function createTable(posologies, intakes, startDateMed, endDateMed) {
    const table = document.createElement('table');
    const thead = document.createElement('thead');

    thead.innerHTML = `
        <tr>
            <th scope="col">Day</th>
            <th scope="col">Expected intake time</th>
            <th scope="col">Intake time</th>
            <th scope="col">Difference</th>
        </tr>
    `;
    table.appendChild(thead);

    const tbody = document.createElement('tbody');

    let intakeIndex = 0;
    const filterDates = getFilterDates();
    const effectiveStartDate = new Date(Math.max(filterDates.startDate, startDateMed));
    const effectiveEndDate = new Date(Math.min(filterDates.endDate, endDateMed));

    while (intakeIndex < intakes.length && compareDatesByDay(DBToDate(intakes[intakeIndex].date), effectiveStartDate) < 0) {
        intakeIndex++;
    }

    for (let currentDate = new Date(effectiveStartDate); compareDatesByDay(currentDate, effectiveEndDate) <= 0; currentDate.setDate(currentDate.getDate() + 1)) {
        posologies.forEach(posology => {
            const row = document.createElement('tr');

            const dayTableData = document.createElement('td');
            const expectedIntakeTimeTableData = document.createElement('td');
            const intakeTimeTableData = document.createElement('td');
            const differenceTableData = document.createElement('td');

            dayTableData.textContent = formatDate(currentDate).substring(0, 10);
            expectedIntakeTimeTableData.textContent = `${posology.hour.toString().padStart(2, '0')}:${posology.minute.toString().padStart(2, '0')}`;

            // Buscar ingesta correspondiente a la posología esperada
            if (intakeIndex < intakes.length && compareDatesByDay(DBToDate(intakes[intakeIndex].date), currentDate) === 0) {
                const intakeDate = DBToDate(intakes[intakeIndex].date);
                const closestPosology = getClosestPosology(intakeDate, posologies);

                if (closestPosology.posology === posology) {
                    intakeTimeTableData.textContent = formatDate(intakeDate).substring(12, 17);
                    differenceTableData.textContent = closestPosology.timeDifference;
                    intakeIndex++;
                } else {
                    intakeTimeTableData.textContent = "Not taken";
                    differenceTableData.textContent = "-";
                }
            } else {
                intakeTimeTableData.textContent = "Not taken";
                differenceTableData.textContent = "-";
            }

            row.appendChild(dayTableData);
            row.appendChild(expectedIntakeTimeTableData);
            row.appendChild(intakeTimeTableData);
            row.appendChild(differenceTableData);
            tbody.appendChild(row);
        });
    }

    table.appendChild(tbody);
    return table;
}

function getClosestPosology(intakeDate, posologies) {
    let closestPosology = null;
    let minDifference = Infinity;
    let timeDifference = '';
    let isEarlier = false;

    for (const posology of posologies) {
        const posologyDate = new Date(intakeDate);
        posologyDate.setHours(posology.hour, posology.minute, 0, 0);
        const difference = Math.abs(intakeDate - posologyDate);

        if (difference < minDifference) {
            minDifference = difference;
            closestPosology = posology;
            isEarlier = intakeDate < posologyDate;
        }
    }

    const diffInMinutes = Math.floor(minDifference / (1000 * 60));
    const hours = Math.floor(diffInMinutes / 60);
    const minutes = diffInMinutes % 60;

    timeDifference = `${hours > 0 ? `${hours} hour${hours !== 1 ? 's' : ''} ` : ''}` +
                     `${minutes > 0 ? `${minutes} minute${minutes !== 1 ? 's' : ''} ` : ''}` +
                     (hours > 0 || minutes > 0 ? (isEarlier ? 'earlier' : 'later') : '');

    return { posology: closestPosology, timeDifference };
}


function DBToDate(dateString) {
    if (!dateString || typeof dateString !== 'string') {
        console.error("Invalid date string:", dateString);
        return null;
    }

    const parsedDate = new Date(dateString);
    if (isNaN(parsedDate)) {
        console.error("Unable to parse date string:", dateString);
        return null;
    }
    return parsedDate;
}

function formatDate(date) {
    if (!(date instanceof Date) || isNaN(date)) {
        console.error("Invalid date:", date);
        return 'Invalid date';
    }
    return date.toLocaleDateString('es-ES', { year: 'numeric', month: '2-digit', day: '2-digit', hour: '2-digit', minute: '2-digit' });
}


function compareDatesByDay(date1, date2) {
    if (date1.getFullYear() < date2.getFullYear()) return -1;
    if (date1.getFullYear() > date2.getFullYear()) return 1;

    if (date1.getMonth() < date2.getMonth()) return -1;
    if (date1.getMonth() > date2.getMonth()) return 1;

    if (date1.getDate() < date2.getDate()) return -1;
    if (date1.getDate() > date2.getDate()) return 1;

    return 0;
}


function setDefaultDateRange() {
    const today = new Date();
    const sevenDaysAgo = new Date();
    sevenDaysAgo.setDate(today.getDate() - 7);

    startDateInput.value = sevenDaysAgo.toISOString().split('T')[0];
    endDateInput.value = today.toISOString().split('T')[0];
}

function getFilterDates() {
    const period = document.getElementById("period").value;
    const today = new Date();
    let startDate, endDate;

    switch (period) {
        case "last-month":
            startDate = new Date(today.getFullYear(), today.getMonth() - 1, today.getDate());
            endDate = today;
            break;
        case "last-days":
            const numDays = parseInt(document.getElementById("num-days").value);
            if (isNaN(numDays) || numDays <= 0) {
                alert("Please enter a valid number of days.");
                return null;
            }
            startDate = new Date(today.getTime() - numDays * 24 * 60 * 60 * 1000);
            endDate = today;
            break;
        case "date-range":
            startDate = new Date(startDateInput.value);
            endDate = new Date(endDateInput.value);

            if (isNaN(startDate.getTime()) || isNaN(endDate.getTime()) || startDate > endDate) {
                alert("Please select a valid date range.");
                return null;
            }
            break;
    }

    return { startDate, endDate };
}



// DEFAULT VALUES

toggleVisibility(daysOptions, false);
toggleVisibility(dateRangeOptions, false);

const startDateInput = document.getElementById("start-date");
const endDateInput = document.getElementById("end-date");

setDefaultDateRange();

// EVENT LISTENERS

periodSelector.addEventListener("change", () => {
    toggleVisibility(daysOptions, periodSelector.value === "last-days");
    toggleVisibility(dateRangeOptions, periodSelector.value === "date-range");
});

document.querySelector('#search-form button[type="button"]').addEventListener('click', (event) => {
    event.preventDefault();
    searchPatient();
});

document.getElementById('form-filters').addEventListener('submit', (event) => {
    event.preventDefault();
    searchPatient();
});