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

    @Column("queue_id")
    private int queueId;

    @Column("current_queue_id")
    private int currentQueueId;

    @Column("queue_start_time")
    private LocalDateTime queueStartTime;

    @Column("user_name")
    @NotBlank(message = "Name cannot be blank")
    private String name;

    @Column("phone_number")
    @Pattern(regexp = "\\d{10}", message = "Invalid phone number format")
    @NotBlank(message = "PhoneNumber cannot be blank")
    private String phoneNumber;

    @Column("user_id")
    private String userId;

    @Column("clinic_id")
    private String clinicId;

    @Column("appointment_id")
    private String appointmentId;

    @Column("appointment_status")
    private Boolean appointmentStatus;

    @Column("advance_paid_for_queue")
    private Boolean advancePaidForQueue;

    @Column("followup_consultation")
    private Boolean followupConsultation;

    @Column("appointment_source")
    private String appointmentSource;

    @Column("doctor_name")
    private String doctorName;

    @Column("patient_reached_clinic")
    private Boolean patientReachedClinic;
}