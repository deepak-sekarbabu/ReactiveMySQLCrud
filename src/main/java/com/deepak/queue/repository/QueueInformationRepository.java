package com.deepak.queue.repository;

import com.deepak.queue.model.QueueInformation;
import org.springframework.data.domain.Pageable;
import org.springframework.data.r2dbc.repository.R2dbcRepository;
import reactor.core.publisher.Flux;

public interface QueueInformationRepository extends R2dbcRepository<QueueInformation, Integer> {

    Flux<QueueInformation> findAllBy(Pageable pageable);
}