package com.deepak.queue.controller;

import com.deepak.queue.model.QueueInformation;
import com.deepak.queue.service.QueueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/queue-information")
@Tag(name = "Queue Service", description = "Handles CRUD operations for Queue Handling")
public class QueueController {

    private final QueueService service;

    public QueueController(@Qualifier("queueServiceImpl") QueueService service) {
        this.service = service;
    }

    @Operation(summary = "Get all queue information")
    @GetMapping("/all")
    @ApiResponse(responseCode = "200", description = "Information Retrieved")
    @ApiResponse(responseCode = "404", description = "Information does not exist")
    public Flux<QueueInformation> getAllQueueInformation() {
        return service.getAllQueueInformation();
    }

    @Operation(summary = "Get all queue information")
    @GetMapping({"/{page}/{size}"})
    @ApiResponse(responseCode = "200", description = "Information Retrieved")
    @ApiResponse(responseCode = "404", description = "Information does not exist")
    public ResponseEntity<Flux<QueueInformation>> getAllQueueInformationUsingPagination(
            @Parameter(description = "page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "page size") @RequestParam(defaultValue = "10") int size
    ) {
        Flux<QueueInformation> pagedQueueInformation = service.getPaginatedQueueInformation(page, size);
        return ResponseEntity.ok().body(pagedQueueInformation);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get queue information by ID")
    public Mono<ResponseEntity<QueueInformation>> getQueueInformationById(@PathVariable int id) {
        return service.getQueueInformationById(id).map(queueInfo -> ResponseEntity.ok().body(queueInfo)).defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @PostMapping
    @Operation(summary = "Create queue information")
    @ResponseStatus(HttpStatus.CREATED)
    public Mono<QueueInformation> createQueueInformation(@RequestBody Mono<QueueInformation> queueInformation) {
        return service.createQueueInformation(queueInformation);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update queue information by ID")
    @ResponseStatus(HttpStatus.OK)
    public Mono<QueueInformation> updateQueueInformation(@PathVariable int id, @RequestBody Mono<QueueInformation> queueInformationMono) {
        return service.updateQueueInformation(id, queueInformationMono);

    }


    @DeleteMapping("/{id}")
    @Operation(summary = "Delete queue information by ID")
    @ResponseStatus(HttpStatus.OK)
    public Mono<ResponseEntity<Void>> deleteQueueInformation(@PathVariable int id) {
        return service.getQueueInformationById(id).flatMap(existingQueueInfo -> service.deleteQueueInformation(id).then(Mono.just(ResponseEntity.ok().<Void>build()))).defaultIfEmpty(ResponseEntity.notFound().build());
    }
}