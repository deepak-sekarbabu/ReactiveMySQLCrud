package com.deepak.queue.controller;

import com.deepak.queue.model.clinic.ClinicInformation;
import com.deepak.queue.model.clinic.ClinicPhoneNumbers;
import com.deepak.queue.repository.ClinicInformationRepository;
import com.deepak.queue.repository.ClinicPhoneNumbersRepository;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/clinic-information")
@Tag(name = "Clinic Service", description = "Handles CRUD operations for Clinic Information")
public class ClinicController {


    private final ClinicInformationRepository clinicInformationRepository;


    private final ClinicPhoneNumbersRepository clinicPhoneNumbersRepository;

    public ClinicController(ClinicInformationRepository clinicInformationRepository, ClinicPhoneNumbersRepository clinicPhoneNumbersRepository) {
        this.clinicInformationRepository = clinicInformationRepository;
        this.clinicPhoneNumbersRepository = clinicPhoneNumbersRepository;
    }

    @PostMapping
    public Mono<ClinicInformation> createClinic(@RequestBody ClinicInformation clinicInformation) {
        return clinicInformationRepository.save(clinicInformation);
    }

    @GetMapping
    public Flux<ClinicInformation> getAllClinics(
            @Parameter(description = "page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "page size") @RequestParam(defaultValue = "10") int size
    ) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "clinicId"));
        return clinicInformationRepository.findAllPaged(pageable);
    }


    @GetMapping("/{id}")
    public Mono<ClinicInformation> getClinicById(@PathVariable Integer id) {
        return clinicInformationRepository.findById(id);
    }

    @PostMapping("/{clinicId}/phoneNumbers")
    public Flux<ClinicPhoneNumbers> addPhoneNumbersToClinic(@PathVariable Integer clinicId, @RequestBody List<ClinicPhoneNumbers> clinicPhoneNumbers) {
        List<Mono<ClinicPhoneNumbers>> savedPhoneNumbers = clinicPhoneNumbers.stream()
                .peek(phoneNumber -> phoneNumber.setClinicId(clinicId))
                .map(clinicPhoneNumbersRepository::save)
                .collect(Collectors.toList());

        return Flux.merge(savedPhoneNumbers)
                .onErrorMap(exception -> {
                    if (exception instanceof DataIntegrityViolationException) {
                        return new ResponseStatusException(HttpStatusCode.valueOf(404));
                    } else {
                        return exception;
                    }
                });
    }


    @GetMapping("/{clinicId}/phoneNumbers")
    public Flux<ClinicPhoneNumbers> getPhoneNumbersForClinic(@PathVariable Integer clinicId) {
        return clinicPhoneNumbersRepository.findAllByClinicId(clinicId);
    }

    @PutMapping("/{clinicId}/phoneNumbers")
    public Mono<ResponseEntity<List<ClinicPhoneNumbers>>> updatePhoneNumbersByClinicId(
            @PathVariable Integer clinicId,
            @RequestBody List<ClinicPhoneNumbers> clinicPhoneNumbers) {

        return clinicInformationRepository.existsByClinicId(clinicId)
                .flatMap(clinicExists -> {
                    if (clinicExists) {
                        clinicPhoneNumbers.forEach(phoneNumber -> phoneNumber.setClinicId(clinicId));
                        return clinicPhoneNumbersRepository.saveAll(clinicPhoneNumbers)
                                .collectList()
                                .map(ResponseEntity::ok);
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Clinic with id " + clinicId + " not found");
                    }
                })
                .onErrorResume(err -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }
}