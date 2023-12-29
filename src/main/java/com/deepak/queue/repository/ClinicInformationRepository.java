package com.deepak.queue.repository;

import com.deepak.queue.model.clinic.ClinicInformation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ClinicInformationRepository extends R2dbcRepository<ClinicInformation, Integer> {

    @Query("SELECT * FROM ClinicInformation")
    Flux<ClinicInformation> findAllPaged(Pageable pageable);

    Mono<Boolean> existsByClinicId(Integer clinicId);
}