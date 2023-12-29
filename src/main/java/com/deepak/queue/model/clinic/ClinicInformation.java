package com.deepak.queue.model.clinic;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Getter
@Setter
@Table(name = "ClinicInformation")
public class ClinicInformation {
    @Id
    @Column("ClinicId")
    private Integer clinicId;

    @Column("ClinicName")
    private String clinicName;

    @Column("ClinicAddress")
    private String clinicAddress;

    @Column("Latitude")
    private Double latitude;

    @Column("Longitude")
    private Double longitude;

    @Column("ClinicPinCode")
    private String clinicPinCode;

    @Column("NoOfDoctors")
    private Integer noOfDoctors;

}