package com.deepak.queue.controller;

import com.deepak.queue.model.clinic.ClinicInformation;
import com.deepak.queue.model.clinic.ClinicPhoneNumbers;
import com.deepak.queue.repository.ClinicInformationRepository;
import com.deepak.queue.repository.ClinicPhoneNumbersRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/clinic-information")
@Tag(name = "Clinic Service", description = "Handles CRUD operations for Clinic Information")
public class ClinicController {

    private final ClinicInformationRepository clinicInformationRepository;

    private final ClinicPhoneNumbersRepository clinicPhoneNumbersRepository;

    public ClinicController(ClinicInformationRepository clinicInformationRepository,
                            ClinicPhoneNumbersRepository clinicPhoneNumbersRepository) {
        this.clinicInformationRepository = clinicInformationRepository;
        this.clinicPhoneNumbersRepository = clinicPhoneNumbersRepository;
    }

    @PostMapping
    @Operation(summary = "Create a new clinic")
    public Mono<ClinicInformation> createClinic(@RequestBody ClinicInformation clinicInformation) {
        return this.clinicInformationRepository.save(clinicInformation);
    }

    @GetMapping
    @Operation(summary = "Get all clinic information")
    public Flux<ClinicInformation> getAllClinics(
            @Parameter(description = "page number") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "page size") @RequestParam(defaultValue = "10") int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "clinicId"));
        return this.clinicInformationRepository.findAllPaged(pageable);
    }

    /*    @PostMapping("/{clinicId}/phoneNumbers")
        public Flux<ClinicPhoneNumbers> addPhoneNumbersToClinic(@PathVariable Integer clinicId,
                @RequestBody List<ClinicPhoneNumbers> clinicPhoneNumbers) {
            List<Mono<ClinicPhoneNumbers>> savedPhoneNumbers = clinicPhoneNumbers.stream()
                    .peek(phoneNumber -> phoneNumber.setClinicId(clinicId))
                    .map(this.clinicPhoneNumbersRepository::save)
                    .toList();

            return Flux.merge(savedPhoneNumbers)
                    .onErrorMap(exception -> {
                        if (exception instanceof DataIntegrityViolationException) {
                            return new ResponseStatusException(HttpStatusCode.valueOf(404));
                        } else {
                            return exception;
                        }
                    });
        }*/
    @PostMapping("/{clinicId}/phoneNumbers")
    @Operation(summary = "Save phone numbers for a clinic")
    public Flux<ClinicPhoneNumbers> addPhoneNumbersToClinic(@PathVariable Integer clinicId,
                                                            @RequestBody List<ClinicPhoneNumbers> clinicPhoneNumbers) {
        List<Mono<ClinicPhoneNumbers>> savedPhoneNumbers = clinicPhoneNumbers.stream()
                .map(phoneNumber -> {
                    phoneNumber.setClinicId(clinicId);
                    return this.clinicPhoneNumbersRepository.save(phoneNumber);
                })
                .toList();

        return Flux.merge(savedPhoneNumbers)
                .onErrorMap(exception -> {
                    if (exception instanceof DataIntegrityViolationException) {
                        return new ResponseStatusException(HttpStatus.NOT_FOUND);
                    } else {
                        return exception;
                    }
                });
    }


    @GetMapping("/{clinicId}/phoneNumbers")
    @Operation(summary = "get phone numbers for a clinic")
    public Flux<ClinicPhoneNumbers> getPhoneNumbersForClinic(@PathVariable Integer clinicId) {
        return this.clinicPhoneNumbersRepository.findAllByClinicId(clinicId);
    }

    @GetMapping("/{clinicId}")
    @Operation(summary = "Retrieve clinic information by id along with associated phone numbers")
    public Mono<ClinicInformation> getClinicWithPhoneNumbers(@PathVariable Integer clinicId) {

        return this.clinicInformationRepository.findById(clinicId)
                .flatMap(clinicInfo -> {
                    Mono<List<ClinicPhoneNumbers>> phoneNumbersMono = this
                            .getPhoneNumbersForClinic(clinicInfo.getClinicId())
                            .collectList();
                    return phoneNumbersMono.map(phoneNumbers -> {
                        clinicInfo.setPhoneNumbers(phoneNumbers);
                        return clinicInfo;
                    });
                });

    }

    @PutMapping("/{clinicId}/phoneNumbers")
    @Operation(summary = "updates phone numbers for a clinic by clinic id")
    public Mono<ResponseEntity<List<ClinicPhoneNumbers>>> updatePhoneNumbersByClinicId(
            @PathVariable Integer clinicId,
            @RequestBody List<ClinicPhoneNumbers> clinicPhoneNumbers) {

        return this.clinicInformationRepository.existsByClinicId(clinicId)
                .flatMap(clinicExists -> {
                    if (clinicExists.booleanValue()) {
                        clinicPhoneNumbers.forEach(phoneNumber -> phoneNumber.setClinicId(clinicId));
                        return this.clinicPhoneNumbersRepository.saveAll(clinicPhoneNumbers)
                                .collectList()
                                .map(ResponseEntity::ok);
                    } else {
                        throw new ResponseStatusException(HttpStatus.NOT_FOUND,
                                "Clinic with id " + clinicId + " not found");
                    }
                })
                .onErrorResume(err -> Mono.just(ResponseEntity.status(HttpStatus.NOT_FOUND).build()));
    }

    @DeleteMapping("/{clinicId}")
    @Operation(summary = "Delete clinic by Id")
    public Mono<Void> deleteClinicById(@PathVariable Integer clinicId) {
        return this.clinicPhoneNumbersRepository.deleteById(clinicId);
    }
}