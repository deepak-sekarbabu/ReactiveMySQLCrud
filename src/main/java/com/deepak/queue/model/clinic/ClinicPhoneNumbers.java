package com.deepak.queue.model.clinic;

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
    private Integer id;

    @Column("ClinicId")
    private Integer clinicId;

    @Column("PhoneNumber")
    private String phoneNumber;

}