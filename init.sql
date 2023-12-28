CREATE DATABASE IF NOT EXISTS QueueManagement;
USE QueueManagement;

CREATE TABLE IF NOT EXISTS QueueInformation (
                                                id INT AUTO_INCREMENT PRIMARY KEY,
                                                queue_id INT NOT NULL,
                                                current_queue_id INT NOT NULL,
                                                queue_start_time TIMESTAMP NOT NULL,
                                                user_name VARCHAR(255),
                                                user_id VARCHAR(255),
                                                phone_number VARCHAR(20),
                                                clinic_id VARCHAR(50),
                                                appointment_id VARCHAR(50),
                                                appointment_status TINYINT(1),
                                                advance_paid_for_queue TINYINT(1),
                                                followup_consultation TINYINT(1),
                                                appointment_source VARCHAR(100),
                                                doctor_name VARCHAR(100),
                                                patient_reached_clinic TINYINT(1)
);
-- Inserting sample data into QueueInformation table
INSERT INTO QueueInformation (
    QueueID,
    CurrentQueueID,
    QueueStartTime,
    UserName,
    PhoneNumber,
    UserId,
    ClinicId,
    AppointmentId,
    AppointmentStatus,
    AdvancePaidForQueue,
    FollowupConsultation,
    AppointmentSource,
    DoctorName,
    PatentReachedClinic
) VALUES
      (1, 1, '2023-12-28 09:00:00', 'John Doe', '123-456-7890', 'user123', 'clinic1', 'app123', 1, 1, 0, 'Online', 'Dr. Smith', 1),
      (2, 2, '2023-12-29 10:30:00', 'Jane Smith', '987-654-3210', 'user456', 'clinic2', 'app456', 0, 0, 1, 'In-person', 'Dr. Johnson', 0),
      (3, 3, '2023-12-30 11:45:00', 'Alice Brown', '555-123-4567', 'user789', 'clinic3', 'app789', 1, 1, 1, 'Referral', 'Dr. Williams', 1);
-- Onboarding
CREATE TABLE IF NOT EXISTS ClinicInformation (
                                   ClinicId INT PRIMARY KEY,
                                   ClinicName VARCHAR(100),
                                   ClinicAddress VARCHAR(255),
                                   Latitude DECIMAL(10, 8), -- Precision up to 8 decimal places for latitude
                                   Longitude DECIMAL(11, 8), -- Precision up to 8 decimal places for longitude
                                   ClinicPinCode VARCHAR(10),
                                   ClinicPhoneNumbers VARCHAR(255), -- Storing phone numbers as a comma-separated string
                                   NoOfDoctors INT
);

INSERT INTO ClinicInformation (ClinicId, ClinicName, ClinicAddress, Latitude, Longitude, ClinicPinCode, ClinicPhoneNumbers, NoOfDoctors)
VALUES
    (1, 'Sample Clinic 1', '123 Sample St', 40.7128, -74.0060, '12345', '123-456-7890, 987-654-3210', 5),
    (2, 'Sample Clinic 2', '456 Test Ave', 34.0522, -118.2437, '54321', '111-222-3333, 444-555-6666', 8),
    (3, 'Sample Clinic 3', '789 Example Rd', 51.5074, -0.1278, '67890', '777-888-9999, 999-888-7777', 3);

CREATE TABLE IF NOT EXISTS DoctorInformation (
                                   PKId INT AUTO_INCREMENT PRIMARY KEY,
                                   DoctorId INT,
                                   ClinicId INT,
                                   DoctorName VARCHAR(100),
                                   AvailableDays VARCHAR(50),
                                   TimeOfDay ENUM('MORNING', 'AFTERNOON', 'EVENING'),
                                   StartTime TIME,
                                   EndTime TIME
);
-- Onboarding + Doctor Management through APP
INSERT INTO DoctorInformation (DoctorId, ClinicId, DoctorName, AvailableDays, TimeOfDay, StartTime, EndTime)
VALUES
    (1, 1, 'Dr. Smith', 'MON,TUE,WED', 'MORNING', '08:00:00', '12:00:00'),
    (2, 1, 'Dr. Johnson', 'TUE,WED,THU', 'AFTERNOON', '13:00:00', '17:00:00'),
    (3, 2, 'Dr. Williams', 'WED,THU,FRI', 'EVENING', '18:00:00', '20:00:00');

CREATE TABLE IF NOT EXISTS DoctorAvailabilityInformation (
                                               PK INT AUTO_INCREMENT PRIMARY KEY,
                                               ClinicId INT,
                                               DoctorId INT,
                                               AppointmentDate DATE,
                                               StartTime TIME,
                                               EndTime TIME,
                                               AvailableForAppointment BOOLEAN DEFAULT FALSE
);
-- Inserting sample data into DoctorAvailabilityInformation table Create Entries Using Batch Jobs & Also for Input for Queue Management & appointment 
INSERT INTO DoctorAvailabilityInformation (ClinicId, DoctorId, AppointmentDate, StartTime, EndTime, AvailableForAppointment)
VALUES
    (1, 101, '2023-12-28', '09:00:00', '12:00:00', TRUE),
    (2, 102, '2023-12-29', '13:30:00', '15:30:00', TRUE),
    (3, 103, '2023-12-30', '10:00:00', '11:30:00', FALSE),
    (1, 101, '2023-12-31', '14:00:00', '16:00:00', TRUE),
    (2, 104, '2024-01-01', '11:00:00', '13:00:00', FALSE);

CREATE TABLE IF NOT EXISTS DoctorShiftAvailability (
                                         PK INT AUTO_INCREMENT PRIMARY KEY,
                                         ClinicId INT,
                                         DoctorId INT,
                                         AppointmentDate DATE,
                                         MorningAvailability BOOLEAN DEFAULT FALSE,
                                         AfternoonAvailability BOOLEAN DEFAULT FALSE,
                                         EveningAvailability BOOLEAN DEFAULT FALSE,
                                         ReasonMessage VARCHAR(255)
);
-- Inserting sample data into DoctorShiftAvailability table To Record Absence -  Doctor Management through APP
INSERT INTO DoctorShiftAvailability (ClinicId, DoctorId, AppointmentDate, MorningAvailability, AfternoonAvailability, EveningAvailability, ReasonMessage)
VALUES
    (1, 101, '2023-12-28', TRUE, TRUE, FALSE, 'Regular shift'),
    (2, 102, '2023-12-29', TRUE, FALSE, TRUE, 'Morning appointment scheduled'),
    (3, 103, '2023-12-30', FALSE, TRUE, TRUE, 'Doctor on leave'),
    (1, 101, '2023-12-31', FALSE, FALSE, FALSE, 'Holiday'),
    (2, 104, '2024-01-01', FALSE, FALSE, TRUE, 'Evening shift only');