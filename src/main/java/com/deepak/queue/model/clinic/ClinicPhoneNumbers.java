package com.deepak.queue.model.clinic;

import io.swagger.v3.oas.annotations.Hidden;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table("ClinicPhoneNumbers")
@Getter
@Setter
public class ClinicPhoneNumbers {
    @Id
    @Hidden
    private Integer id;

    @Column("ClinicId")
    @Hidden
    private Integer clinicId;

    @Column("PhoneNumber")
    @Pattern(regexp = "^\\+(?:[0-9] ?){6,14}[0-9]$", message = "Invalid phone number format")
    private String phoneNumber;

}