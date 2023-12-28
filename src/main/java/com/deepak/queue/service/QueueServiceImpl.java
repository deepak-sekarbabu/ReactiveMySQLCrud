package com.deepak.queue.service;

import com.deepak.queue.model.QueueInformation;
import com.deepak.queue.repository.QueueInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

@Service
public class QueueServiceImpl implements QueueService {

    private final QueueInformationRepository queueInformationRepository;

    @Autowired
    public QueueServiceImpl(QueueInformationRepository queueInformationRepository) {
        this.queueInformationRepository = queueInformationRepository;
    }

    @Override
    public Flux<QueueInformation> getAllQueueInformation() {
        return queueInformationRepository.findAll();
    }

    @Override
    public Flux<QueueInformation> getPaginatedQueueInformation(int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("id").ascending());
        return queueInformationRepository.findAllBy(pageable);
    }

    @Override
    public Mono<QueueInformation> getQueueInformationById(int id) {
        return queueInformationRepository.findById(id);
    }

    public Mono<QueueInformation> createQueueInformation(Mono<QueueInformation> queueInformationMono) {
        return queueInformationMono.flatMap(queueInformation -> {
            queueInformation.setQueueStartTime(LocalDateTime.now());
            return queueInformationRepository.save(queueInformation);
        });
    }

    @Override
    public Mono<QueueInformation> updateQueueInformation(int id, Mono<QueueInformation> queueInformationMono) {
        return queueInformationRepository.findById(id)
                .flatMap(existingQueueInfo -> queueInformationMono.map(updatedQueueInfo -> {
                    if (updatedQueueInfo.getQueueId() != 0) {
                        existingQueueInfo.setQueueId(updatedQueueInfo.getQueueId());
                    }
                    if (updatedQueueInfo.getCurrentQueueId() != 0) {
                        existingQueueInfo.setCurrentQueueId(updatedQueueInfo.getCurrentQueueId());
                    }
                    if (updatedQueueInfo.getName() != null && !updatedQueueInfo.getName().isEmpty()) {
                        existingQueueInfo.setName(updatedQueueInfo.getName());
                    }
                    if (updatedQueueInfo.getPhoneNumber() != null && !updatedQueueInfo.getPhoneNumber().isEmpty()) {
                        existingQueueInfo.setPhoneNumber(updatedQueueInfo.getPhoneNumber());
                    }
                    if (updatedQueueInfo.getUserId() != null && !updatedQueueInfo.getUserId().isEmpty()) {
                        existingQueueInfo.setUserId(updatedQueueInfo.getUserId());
                    }
                    if (updatedQueueInfo.getClinicId() != null && !updatedQueueInfo.getClinicId().isEmpty()) {
                        existingQueueInfo.setClinicId(updatedQueueInfo.getClinicId());
                    }
                    if (updatedQueueInfo.getAppointmentId() != null && !updatedQueueInfo.getAppointmentId().isEmpty()) {
                        existingQueueInfo.setAppointmentId(updatedQueueInfo.getAppointmentId());
                    }
                    if (updatedQueueInfo.getAppointmentStatus() != null) {
                        existingQueueInfo.setAppointmentStatus(updatedQueueInfo.getAppointmentStatus());
                    }
                    if (updatedQueueInfo.getAdvancePaidForQueue() != null) {
                        existingQueueInfo.setAdvancePaidForQueue(updatedQueueInfo.getAdvancePaidForQueue());
                    }
                    if (updatedQueueInfo.getFollowupConsultation() != null) {
                        existingQueueInfo.setFollowupConsultation(updatedQueueInfo.getFollowupConsultation());
                    }
                    if (updatedQueueInfo.getAppointmentSource() != null) {
                        existingQueueInfo.setAppointmentSource(updatedQueueInfo.getAppointmentSource());
                    }
                    if (updatedQueueInfo.getDoctorName() != null) {
                        existingQueueInfo.setDoctorName(updatedQueueInfo.getDoctorName());
                    }
                    if (updatedQueueInfo.getPatientReachedClinic() != null) {
                        existingQueueInfo.setPatientReachedClinic(updatedQueueInfo.getPatientReachedClinic());
                    }
                    return existingQueueInfo;
                }))
                .flatMap(queueInformationRepository::save);
    }


    @Override
    public Mono<Void> deleteQueueInformation(int id) {
        return queueInformationRepository.deleteById(id);
    }
}