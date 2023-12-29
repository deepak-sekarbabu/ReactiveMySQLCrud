package com.deepak.queue.model;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;


@NoArgsConstructor
@AllArgsConstructor
@ToString
@Getter
@Setter
@Builder
@Table(name = "QueueInformation")
@Schema(description = "Queue Information")
public class QueueInformation {

    @Id
    @Hidden
    @Schema(description = "Auto-generated ID")
    private int id;

    @Column("QueueID")
    private int queueId;

    @Column("CurrentQueueID")
    private int currentQueueId;

    @Column("QueueStartTime")
    private LocalDateTime queueStartTime;

    @Column("UserName")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column("PhoneNumber")
    @Pattern(regexp = "\\d{10}", message = "Invalid phone number format")
    @NotBlank(message = "PhoneNumber cannot be blank")
    private String phoneNumber;

    @Column("UserId")
    private String userId;

    @Column("ClinicId")
    private String clinicId;

    @Column("RequestedAppointmentDate")
    private LocalDateTime requestedAppointmentDate;

    @Column("JoinTheQueue")
    private boolean joinTheQueue;

    @Column("AppointmentId")
    private String appointmentId;

    @Column("AppointmentStatus")
    private Boolean appointmentStatus;

    @Column("AdvancePaidForQueue")
    private Boolean advancePaidForQueue;

    @Column("FollowupConsultation")
    private Boolean followupConsultation;

    @Column("AppointmentSource")
    private String appointmentSource;

    @Column("DoctorName")
    private String doctorName;

    @Column("PatentReachedClinic")
    private Boolean patientReachedClinic;
}