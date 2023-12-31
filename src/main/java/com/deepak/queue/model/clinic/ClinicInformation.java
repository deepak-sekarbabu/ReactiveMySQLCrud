package com.deepak.queue.model.clinic;

import io.swagger.v3.oas.annotations.Hidden;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.util.List;

@Getter
@Setter
@Table(name = "ClinicInformation")
@ToString
public class ClinicInformation {
    @Id
    @Column("ClinicId")
    @Hidden
    private Integer clinicId;

    @Column("ClinicName")
    @Max(value = 200)
    @Schema(description = "Clinic Name", example = "Santosh Child Clinic")
    private String clinicName;

    @Column("ClinicAddress")
    @Max(value = 200)
    @Schema(description = "Clinic Address", example = "123, Kaveri Street, Madipakkam, Chennai, Tamil Nadu ")
    private String clinicAddress;

    @Column("Latitude")
    @Hidden
    private Double latitude;

    @Column("Longitude")
    @Hidden
    private Double longitude;

    @Column("ClinicPinCode")
    @Schema(description = "Clinic PinCode", example = "600091")
    @Max(value = 6)
    private String clinicPinCode;

    @Column("NoOfDoctors")
    @Schema(description = "No of Doctors", example = "2")
    @Min(value = 1)
    @Max(value = 100)
    private Integer noOfDoctors;

    @Setter
    private List<ClinicPhoneNumbers> phoneNumbers;

}