package com.deepak.queue.service;

import com.deepak.queue.model.QueueInformation;
import com.deepak.queue.repository.QueueInformationRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
                    existingQueueInfo.setQueueID(updatedQueueInfo.getQueueID());
                    existingQueueInfo.setCurrentQueueID(updatedQueueInfo.getCurrentQueueID());
                    existingQueueInfo.setName(updatedQueueInfo.getName());
                    existingQueueInfo.setPhoneNumber(updatedQueueInfo.getPhoneNumber());
                    existingQueueInfo.setUserId(updatedQueueInfo.getUserId());
                    existingQueueInfo.setClinicId(updatedQueueInfo.getClinicId());
                    existingQueueInfo.setAppointmentId(updatedQueueInfo.getAppointmentId());
                    existingQueueInfo.setAppointmentStatus(updatedQueueInfo.getAppointmentStatus());
                    existingQueueInfo.setAdvancePaidForQueue(updatedQueueInfo.getAdvancePaidForQueue());
                    existingQueueInfo.setFollowupConsultation(updatedQueueInfo.getFollowupConsultation());
                    existingQueueInfo.setAppointmentSource(updatedQueueInfo.getAppointmentSource());
                    existingQueueInfo.setDoctorName(updatedQueueInfo.getDoctorName());
                    existingQueueInfo.setPatentReachedClinic(updatedQueueInfo.getPatentReachedClinic());
                    return existingQueueInfo;
                }))
                .flatMap(queueInformationRepository::save);
    }


    @Override
    public Mono<Void> deleteQueueInformation(int id) {
        return queueInformationRepository.deleteById(id);
    }
}