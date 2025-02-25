-- Tables with foreign keys must be deleted first
DROP TABLE IF EXISTS Registration;
DROP TABLE IF EXISTS Course;

CREATE TABLE Course (
    courseId BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    city VARCHAR(255) COLLATE latin1_bin NOT NULL,
    name VARCHAR(255) COLLATE latin1_bin NOT NULL,
    startDate DATETIME NOT NULL,
    price DOUBLE NOT NULL,
    maxPlaces INT NOT NULL,
    registrationDate DATETIME NOT NULL,
    availablePlaces INT NOT NULL
) ENGINE = InnoDB;

CREATE TABLE Registration (
    registrationId BIGINT AUTO_INCREMENT PRIMARY KEY NOT NULL,
    courseId BIGINT NOT NULL,
    email VARCHAR(255) COLLATE latin1_bin NOT NULL,
    cardNumber VARCHAR(16) NOT NULL,
    registrationDate DATETIME NOT NULL,
    cancellationDate DATETIME,
    FOREIGN KEY (courseId) REFERENCES Course(courseId)
) ENGINE = InnoDB;
