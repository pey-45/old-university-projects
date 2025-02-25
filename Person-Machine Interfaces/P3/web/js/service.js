async function requestHandler(method, url, data = null) {
    try {
        const options = {
            method: method,
            headers: {
                'Content-Type': 'application/json',
            },
        };

        if (data) {
            options.body = JSON.stringify(data);
        }

        const response = await fetch(url, options);

        if (response.ok) {
            return await response.json();
        } else {
            const errorDetails = await response.text();
            throw new Error(`Error ${response.status}: ${response.statusText}\n${errorDetails}`);
        }
    } catch (error) {
        if (error instanceof TypeError) {
            console.error("Network error or CORS issue:", error);
            throw new Error("Network error: Unable to connect to the server.");
        } else {
            console.error("Error:", error);
            throw error;
        }
    }
}


async function getPatientByCode(code) {
    const url = `${SERVER_URL}/patients?code=${code}`;
    return await requestHandler('GET', url);
}

async function getMedications(patientId) {
    if (!patientId) {
        console.error("Invalid patient ID");
        return [];
    }
    const url = `${SERVER_URL}/patients/${patientId}/medications`;
    return await requestHandler('GET', url);
}

async function getPosologies(patientId, medicationId) {
    console.log(`Fetching posologies for patient ${patientId}, medication ${medicationId}`);
    const url = `${SERVER_URL}/patients/${patientId}/medications/${medicationId}/posologies`;
    return await requestHandler('GET', url);
}

async function getIntakes(patientId, medicationId) {
    console.log(`Fetching intakes for patient ${patientId}, medication ${medicationId}`);
    const url = `${SERVER_URL}/patients/${patientId}/medications/${medicationId}/intakes`;
    return await requestHandler('GET', url);
}