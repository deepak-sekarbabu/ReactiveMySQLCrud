DROP DATABASE QueueManagement;
CREATE DATABASE IF NOT EXISTS QueueManagement;
USE QueueManagement;
DROP TABLE QueueInformation;
DROP TABLE ClinicInformation;
DROP TABLE ClinicPhoneNumbers;
DROP TABLE DoctorInformation;
DROP TABLE DoctorShiftAvailability;

-- Main Table For Storing Queue Details
-- TABLE 1 ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS QueueInformation (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                QueueID INT NOT NULL,
                                                CurrentQueueID INT NOT NULL,
                                                QueueStartTime TIMESTAMP NOT NULL,
                                                UserName VARCHAR(255),
                                                UserId VARCHAR(255),
                                                PhoneNumber VARCHAR(20),
                                                ClinicId VARCHAR(50),
                                                RequestedAppointmentDate TIMESTAMP,
                                                JoinTheQueue TINYINT(1),

                                                AppointmentId VARCHAR(50),
                                                AppointmentStatus TINYINT(1),
                                                AdvancePaidForQueue TINYINT(1),
                                                FollowupConsultation TINYINT(1),
                                                AppointmentSource VARCHAR(100),
                                                DoctorName VARCHAR(100),
                                                PatentReachedClinic TINYINT(1)
);

ALTER TABLE QueueInformation
ADD CONSTRAINT fk_queue_clinic
FOREIGN KEY (ClinicId) REFERENCES ClinicInformation(ClinicId);

CREATE INDEX idx_clinicid ON QueueInformation(ClinicId);

-- Inserting sample data into QueueInformation table
INSERT INTO QueueInformation (
    QueueID,
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
    DoctorName,
    PatentReachedClinic
) VALUES
   (1, 1, '2023-03-01 09:00:00', 'John Doe', 'user123', '123-456-7890', 'clinic1', '2023-03-01', 1, 'app123', 1, 1, 0, 'Online', 'Dr. Smith', 1),

   (2, 2, '2023-03-02 10:30:00', 'Jane Smith', 'user456', '987-654-3210', 'clinic2', '2023-03-02', 1, 'app456', 0, 0, 1, 'In-person', 'Dr. Johnson', 0),

   (3, 3, '2023-03-03 11:45:00', 'Alice Brown', 'user789', '555-123-4567', 'clinic3', '2023-03-03', 0, 'app789', 1, 1, 1, 'Referral', 'Dr. Williams', 1);

-- TABLE 2 ----------------------------------------------------------------------------
-- Onboarding - Clinic Information

-- ClinicInformation table definition
CREATE TABLE IF NOT EXISTS ClinicInformation (
    ClinicId INT AUTO_INCREMENT PRIMARY KEY,
    ClinicName VARCHAR(100),
    ClinicAddress VARCHAR(255),
    Latitude DECIMAL(10, 8),
    Longitude DECIMAL(11, 8),
    ClinicPinCode VARCHAR(10),
    NoOfDoctors INT
);

-- Inserting sample clinic information
INSERT INTO ClinicInformation (ClinicName, ClinicAddress, Latitude, Longitude, ClinicPinCode, NoOfDoctors)
VALUES
('Sample Clinic 1', '123 Sample St', 40.7128, -74.0060, '12345', 5),
('Sample Clinic 2', '456 Test Ave', 34.0522, -118.2437, '54321', 8),
('Sample Clinic 3', '789 Example Rd', 51.5074, -0.1278, '67890', 3);

-- ClinicPhoneNumbers table definition

CREATE TABLE IF NOT EXISTS ClinicPhoneNumbers (
    ID INT AUTO_INCREMENT PRIMARY KEY,
    ClinicId INT,
    PhoneNumber VARCHAR(20),
    FOREIGN KEY (ClinicId) REFERENCES ClinicInformation(ClinicId)
);

-- Inserting phone numbers for clinics
INSERT INTO ClinicPhoneNumbers (ClinicId, PhoneNumber) VALUES
(1, '+1234567890'),
(1, '+1987654321'),
(2, '+1555123456');


-- TABLE 4 ----------------------------------------------------------------------------
-- Onboarding + Doctor Management through APP
CREATE TABLE IF NOT EXISTS DoctorInformation (
    DoctorId INT AUTO_INCREMENT PRIMARY KEY,
    ClinicId INT,
    DoctorName VARCHAR(100),
    AvailableDays VARCHAR(50),
    TimeOfDay ENUM('MORNING', 'AFTERNOON', 'EVENING'),
    StartTime TIME,
    EndTime TIME,
    FOREIGN KEY (ClinicId) REFERENCES ClinicInformation(ClinicId)
);

INSERT INTO DoctorInformation (ClinicId, DoctorName, AvailableDays, TimeOfDay, StartTime, EndTime)
VALUES
(1, 'Dr. Smith', 'MON', 'MORNING', '08:00:00', '12:00:00'),
(1, 'Dr. Smith', 'THU', 'AFTERNOON', '13:00:00', '18:00:00'),
(2, 'Dr. Mary', 'MON,WED,FRI', 'MORNING', '09:00:00', '12:00:00');

-- TABLE 5 ----------------------------------------------------------------------------
CREATE TABLE IF NOT EXISTS DoctorShiftAvailability (
    Id INT PRIMARY KEY AUTO_INCREMENT,
    DoctorId INT,
    ClinicId INT,
    ShiftDate DATE,
    ShiftStartTime TIME,
    ShiftEndTime TIME,
    Availability BOOLEAN,
    FOREIGN KEY (DoctorId) REFERENCES DoctorInformation(DoctorId),
    FOREIGN KEY (ClinicId) REFERENCES DoctorInformation(ClinicId)
);

INSERT INTO DoctorShiftAvailability (DoctorId, ClinicId, ShiftDate, ShiftStartTime, ShiftEndTime, Availability)
VALUES
(1, 1, '2023-03-01', '08:00:00', '12:00:00', TRUE),
(1, 1, '2023-03-02', '13:00:00', '18:00:00', FALSE),
(2, 2, '2023-03-01', '09:00:00', '12:00:00', TRUE);