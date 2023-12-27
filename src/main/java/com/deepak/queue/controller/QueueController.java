package com.deepak.queue.controller;

import com.deepak.queue.model.QueueInformation;
import com.deepak.queue.service.QueueService;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/queue-information")
public class QueueController {

    private final QueueService service;

    public QueueController(@Qualifier("queueServiceImpl") QueueService service) {
        this.service = service;
    }

    @GetMapping
    public Flux<QueueInformation> getAllQueueInformation() {
        return service.getAllQueueInformation();
    }

    @GetMapping("/{id}")
    public Mono<ResponseEntity<QueueInformation>> getQueueInformationById(@PathVariable int id) {
        return service.getQueueInformationById(id).map(queueInfo -> ResponseEntity.ok().body(queueInfo)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<QueueInformation> createQueueInformation(@RequestBody Mono<QueueInformation> queueInformation) {
        return service.createQueueInformation(queueInformation);
    }

    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<QueueInformation> updateQueueInformation(@PathVariable int id, @RequestBody Mono<QueueInformation> queueInformationMono) {
        return service.updateQueueInformation(id, queueInformationMono);

    }


    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Void>> deleteQueueInformation(@PathVariable int id) {
        return service.getQueueInformationById(id).flatMap(existingQueueInfo -> service.deleteQueueInformation(id).then(Mono.just(ResponseEntity.ok().<Void>build()))).defaultIfEmpty(ResponseEntity.notFound().build());
    }
}