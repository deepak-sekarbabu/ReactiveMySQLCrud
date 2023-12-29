DROP DATABASE IF EXISTS QueueManagement;
CREATE DATABASE IF NOT EXISTS QueueManagement;
USE QueueManagement;

DROP TABLE IF EXISTS QueueInformation;
DROP TABLE IF EXISTS ClinicInformation;
DROP TABLE IF EXISTS ClinicPhoneNumbers;
DROP TABLE IF EXISTS DoctorInformation;
DROP TABLE IF EXISTS DoctorShiftAvailability;

-- TABLE 1 ----------------------------------------------------------------------------
-- Onboarding - Clinic Information

-- ClinicInformation table definition
CREATE TABLE IF NOT EXISTS ClinicInformation
(
    ClinicId      INT AUTO_INCREMENT PRIMARY KEY,
    ClinicName    VARCHAR(100),
    ClinicAddress VARCHAR(255),
    Latitude      DECIMAL(10, 8),
    Longitude     DECIMAL(11, 8),
    ClinicPinCode VARCHAR(10),
    NoOfDoctors   INT
);

-- Inserting sample clinic information
INSERT INTO ClinicInformation (ClinicName, ClinicAddress, Latitude, Longitude, ClinicPinCode, NoOfDoctors)
VALUES ('Sample Clinic 1', '123 Sample St', 40.7128, -74.0060, '12345', 5),
       ('Sample Clinic 2', '456 Test Ave', 34.0522, -118.2437, '54321', 8),
       ('Sample Clinic 3', '789 Example Rd', 51.5074, -0.1278, '67890', 3);

-- TABLE 2 ----------------------------------------------------------------------------
-- ClinicPhoneNumbers table definition

CREATE TABLE IF NOT EXISTS ClinicPhoneNumbers
(
    ID          INT AUTO_INCREMENT PRIMARY KEY,
    ClinicId    INT,
    PhoneNumber VARCHAR(20),
    FOREIGN KEY (ClinicId) REFERENCES ClinicInformation (ClinicId)
);

-- Inserting phone numbers for clinics
INSERT INTO ClinicPhoneNumbers (ClinicId, PhoneNumber)
VALUES (1, '+1234567890'),
       (1, '+1987654321'),
       (2, '+1555123456'),
       (3, '+23423432424');

-- Main Table For Storing Queue Details
-- TABLE 3 ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS QueueInformation
(
    id                       INT AUTO_INCREMENT PRIMARY KEY,
    QueueID                  INT       NOT NULL,
    CurrentQueueID           INT       NOT NULL,
    QueueStartTime           TIMESTAMP NOT NULL,
    UserName                 VARCHAR(255),
    UserId                   VARCHAR(255),
    PhoneNumber              VARCHAR(20),
    ClinicId                 INT,
    RequestedAppointmentDate TIMESTAMP,
    JoinTheQueue             BOOLEAN,
    AppointmentId            VARCHAR(50),
    AppointmentStatus        BOOLEAN,
    AdvancePaidForQueue      BOOLEAN,
    FollowupConsultation     BOOLEAN,
    AppointmentSource        VARCHAR(100),
    DoctorId                 INT,
    PatentReachedClinic      BOOLEAN
);

ALTER TABLE QueueInformation
    ADD CONSTRAINT fk_queue_clinic
        FOREIGN KEY (ClinicId) REFERENCES ClinicInformation (ClinicId);

CREATE INDEX idx_clinicid ON QueueInformation (ClinicId);


-- Inserting sample data into QueueInformation table
INSERT INTO QueueInformation (QueueID,
                              CurrentQueueID,
                              QueueStartTime,
                              UserName,
                              UserId,
                              PhoneNumber,
                              ClinicId,
                              RequestedAppointmentDate,
                              JoinTheQueue,
                              AppointmentId,
                              AppointmentStatus,
                              AdvancePaidForQueue,
                              FollowupConsultation,
                              AppointmentSource,
                              DoctorId,
                              PatentReachedClinic)
VALUES (1, 1, '2023-03-01 09:00:00', 'John Doe', '658eaa214cfc2963523fc572', '123-456-7890', 1, '2023-03-01', FALSE,
        '9499355426_Deepak_2023-12-29T12:14:41.729999100', TRUE, TRUE, TRUE, 'Online', 1, TRUE),

       (2, 2, '2023-03-02 10:30:00', 'Jane Smith', '658be49c82e1747aa30fd410', '987-654-3210', 1, '2023-03-02', FALSE,
        '6231883832_Deepak_2023-12-27T09:47:24.675445800', FALSE, FALSE, FALSE, 'In-person', 1, FALSE),

       (3, 3, '2023-03-03 11:45:00', 'Alice Brown', '658be578207cde1e58449ba0', '555-123-4567', 1, '2023-03-03', FALSE,
        '6234751916_Deepak_2023-12-27T09:51:04.794603400', FALSE, FALSE, FALSE, 'In-person', 1, FALSE);


-- TABLE 4 ----------------------------------------------------------------------------
-- DoctorInformation Table:
-- This table contains general information about the doctor.
CREATE TABLE IF NOT EXISTS DoctorInformation
(
    DoctorId   INT AUTO_INCREMENT PRIMARY KEY,
    ClinicId   INT,
    DoctorName VARCHAR(100),
    FOREIGN KEY (ClinicId) REFERENCES ClinicInformation (ClinicId)
);

-- Sample Doctor Information
INSERT INTO DoctorInformation (ClinicId, DoctorName)
VALUES (1, 'Dr. John Doe'),
       (2, 'Dr. Jane Smith'),
       (1, 'Dr. Emily Johnson');

-- TABLE 5 ----------------------------------------------------------------------------
-- DoctorAvailability Table:
-- This table stores the availability of doctors for each day and shift.

CREATE TABLE IF NOT EXISTS DoctorAvailability
(
    AvailabilityId INT AUTO_INCREMENT PRIMARY KEY,
    DoctorId       INT,
    DayOfWeek      ENUM ('SUNDAY', 'MONDAY', 'TUESDAY', 'WEDNESDAY', 'THURSDAY', 'FRIDAY', 'SATURDAY'),
    TimeOfDay      ENUM ('MORNING', 'AFTERNOON', 'EVENING'),
    FOREIGN KEY (DoctorId) REFERENCES DoctorInformation (DoctorId)
);

INSERT INTO DoctorAvailability (DoctorId, DayOfWeek, TimeOfDay)
VALUES (1, 'MONDAY', 'MORNING'),
       (1, 'MONDAY', 'AFTERNOON'),
       (1, 'TUESDAY', 'MORNING'),
       (2, 'WEDNESDAY', 'AFTERNOON'),
       (2, 'FRIDAY', 'EVENING'),
       (3, 'THURSDAY', 'MORNING');

-- TABLE 6 ----------------------------------------------------------------------------
-- This table stores the specific shift timings for each doctor's availability.

CREATE TABLE IF NOT EXISTS DoctorShiftTimings
(
    ShiftId        INT AUTO_INCREMENT PRIMARY KEY,
    AvailabilityId INT,
    ShiftStartTime TIME,
    ShiftEndTime   TIME,
    Availability   BOOLEAN,
    FOREIGN KEY (AvailabilityId) REFERENCES DoctorAvailability (AvailabilityId)
);

INSERT INTO DoctorShiftTimings (AvailabilityId, ShiftStartTime, ShiftEndTime, Availability)
VALUES (1, '08:00:00', '12:00:00', TRUE),
       (2, '13:00:00', '18:00:00', TRUE),
       (3, '09:00:00', '12:00:00', TRUE),
       (4, '13:00:00', '17:00:00', TRUE),
       (5, '18:00:00', '21:00:00', FALSE),
       (6, '09:30:00', '13:30:00', TRUE);