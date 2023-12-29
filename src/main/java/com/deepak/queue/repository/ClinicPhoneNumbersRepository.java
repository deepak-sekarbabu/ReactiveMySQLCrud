package com.deepak.queue.repository;

import com.deepak.queue.model.clinic.ClinicPhoneNumbers;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface ClinicPhoneNumbersRepository extends R2dbcRepository<ClinicPhoneNumbers, Integer> {
    Flux<ClinicPhoneNumbers> findAllByClinicId(Integer clinicId);
}