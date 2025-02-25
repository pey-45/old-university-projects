namespace java es.udc.ws.app.thrift

// Estructuras de datos

struct ThriftCourseDto {
    1: i64 courseId
    2: string city
    3: string name
    4: i64 startDate
    5: double price
    6: i32 maxPlaces
    7: i32 availablePlaces
    8: optional i64 registrationDate
}

struct ThriftRegistrationDto {
    1: i64 registrationId
    2: i64 courseId
    3: string email
    4: string cardNumber
    5: string registrationDate
    6: string cancellationDate
}

// Excepciones
exception ThriftInstanceNotFoundException {
    1: string instanceId
    2: string instanceType
}

exception ThriftCancellationEmailDoesNotMatchException {
    1: i64 registrationId
    2: string email
}

exception ThriftCancellationOutOfDeadlineException {
    1: i64 registrationId
}

exception ThriftRegistrationAlreadyCancelledException {
    1: i64 registrationId
}

exception ThriftInputValidationException {
    1: string message
}

exception ThriftRegistrationOutOfDeadlineException {
    1: i64 courseId
}

exception ThriftFullCourseException {
    1: i64 courseId
}

exception ThriftEmailAlreadyRegisteredException {
    1: string email
    2: i64 courseId
}

// Servicios
service ThriftAppService {

    // Funcionalidad 1: Add course
    ThriftCourseDto addCourse(1: ThriftCourseDto courseDto) throws (1: ThriftInputValidationException e)

    // Funcionalidad 2: Buscar course por ciudad y fecha
    list<ThriftCourseDto> findCourses(string keywords) throws (1: ThriftInputValidationException e)

    // Funcionalidad 3: Buscar curso por ID
    ThriftCourseDto findCourse(1: i64 courseId)
    throws (
        1: ThriftInstanceNotFoundException e1
    )

    // Funcionalidad 4: Inscribirse en un curso
    ThriftRegistrationDto registerForCourse(
        1: string email,
        2: i64 courseId,
        3: string cardNumber
    ) throws (
        1: ThriftInputValidationException e1,
        2: ThriftInstanceNotFoundException e2,
        3: ThriftRegistrationOutOfDeadlineException e3,
        4: ThriftFullCourseException e4,
        5: ThriftEmailAlreadyRegisteredException e5
    )

    // Cancelar inscripción
    void cancelRegistration(1: i64 registrationId, 2: string email)
    throws (
        1: ThriftInstanceNotFoundException e1,
        2: ThriftCancellationEmailDoesNotMatchException e2,
        3: ThriftCancellationOutOfDeadlineException e3,
        4: ThriftRegistrationAlreadyCancelledException e4
    )

    // Buscar inscripciones de un usuario
    list<ThriftRegistrationDto> findAllRegistrationsFrom(1: string email)
}
