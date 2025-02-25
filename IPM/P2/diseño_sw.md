```mermaid

sequenceDiagram
    actor U as Usuario
    participant V as View (wear/mobile)
    participant P as Providers
    participant S as Services
    participant RH as RequestHandler
    participant API as API Externa

    Note over U,API: log-in
    U->>V: Introducir código de paciente 
    V->>P: Leer patientsProvider
    P->>S: getPatientByCode(String code)
    S->>RH: handleRequest('GET', url, "Patient not found")
    RH->>API: GET /patients?code=$code
    API-->>RH: Response
    RH-->>S: JSON data
    S-->>P: Comprobar existencia
    P-->>V: Cambiar a pantalla consultar medicaciones
    V-->>U: Muestra pantalla de consultar medicaciones

    Note over U,API: Consultar medicación
    U->>V: Solicita lista de medicación
    V->>P: Leer medicationsProvider
    P->>S: getMedications(patientId)
    S->>RH: handleRequest('GET', url, errorMessage)
    RH->>API: GET /patients/{patientId}/medications
    API-->>RH: Response
    RH-->>S: JSON data
    S-->>P: List<Medication>
    P-->>V: Lista de medicaciones
    V-->>U: Muestra lista de medicación

    Note over U,API: Consultar medicación pendiente
    U->>V: Solicita medicación pendiente
    V->>P: Leer medicationsProvider
    P->>S: getPendingMedications(patientId)
    S->>RH: handleRequest('GET', url, errorMessage)
    RH->>API: GET /patients/{patientId}/medications?pending=true
    API-->>RH: Response
    RH-->>S: JSON data
    S-->>P: List<Medication>
    P-->>V: Lista de medicaciones pendientes
    V-->>U: Muestra lista de medicación pendiente

    Note over U,API: Marcar toma de medicación
    U->>V: Marca medicación como tomada
    V->>P: Actualizar medicationsProvider
    P->>S: postIntake(patientId, medicationId, intake)
    S->>RH: handleRequest('POST', url, errorMessage, data)
    RH->>API: POST /patients/{patientId}/medications/{medicationId}/intakes
    API-->>RH: Response
    RH-->>S: JSON data
    S-->>P: Intake
    P-->>V: Confirmación de actualización
    V-->>U: Muestra confirmación

    Note over U,API: Notificaciones de medicación
    U->>V: Solicita notificaciones de medicación
    V->>P: Leer posologiesProvider
    P->>S: getPosologies(patientId, medicationId)
    S->>RH: handleRequest('GET', url, errorMessage)
    RH->>API: GET /patients/{patientId}/medications/{medicationId}/posologies
    API-->>RH: Response
    RH-->>S: JSON data
    S-->>P: List<Posology>
    P-->>V: Lista de notificaciones de medicación
    V-->>U: Muestra notificaciones sobre cuándo tomar medicación
