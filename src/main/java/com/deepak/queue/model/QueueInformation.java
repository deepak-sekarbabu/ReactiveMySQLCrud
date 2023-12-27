package com.deepak.queue.model;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
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
    @Schema(description = "AutogenerateID")
    private int id;

    @Column("QueueID")
    private int queueID;

    @Column("CurrentQueueID")
    private int currentQueueID;

    @Column("QueueStartTime")
    private LocalDateTime queueStartTime;

    @Column("UserName")
    private String name;

    @Column("PhoneNumber")
    private String phoneNumber;

    @Column("UserId")
    private String userId;

    @Column("ClinicId")
    private String clinicId;

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
    private Boolean patentReachedClinic;
}