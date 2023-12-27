package com.deepak.queue.service;

import com.deepak.queue.model.QueueInformation;
import org.springframework.web.bind.annotation.PathVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface QueueService {

    Flux<QueueInformation> getAllQueueInformation();

    Mono<QueueInformation> getQueueInformationById(int id);

    Mono<QueueInformation> createQueueInformation(Mono<QueueInformation> queueInformation);

    Mono<QueueInformation> updateQueueInformation(@PathVariable int id,
                                                  Mono<QueueInformation> queueInformation);

    Mono<Void> deleteQueueInformation(int id);
}