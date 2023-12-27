package com.deepak.queue.repository;

import com.deepak.queue.model.QueueInformation;
import org.springframework.data.r2dbc.repository.R2dbcRepository;

public interface QueueInformationRepository extends R2dbcRepository<QueueInformation, Integer> {
}