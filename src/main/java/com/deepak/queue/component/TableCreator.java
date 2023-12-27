package com.deepak.queue.component;

import io.r2dbc.spi.ConnectionFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class TableCreator {

    private final ConnectionFactory connectionFactory;

    @Autowired
    public TableCreator(ConnectionFactory connectionFactory) {
        this.connectionFactory = connectionFactory;
    }

    public Mono<Void> createTable() {
        return Mono.from(connectionFactory.create())
                .flatMapMany(connection -> Mono.from(connection.createStatement(
                                        """
                                        CREATE TABLE IF NOT EXISTS QueueInformation (
                                            id INT AUTO_INCREMENT PRIMARY KEY,
                                            QueueID INT NOT NULL,
                                            CurrentQueueID INT NOT NULL,
                                            QueueStartTime TIMESTAMP NOT NULL,
                                            UserName VARCHAR(255),
                                            PhoneNumber VARCHAR(20),
                                            UserId VARCHAR(50),
                                            ClinicId VARCHAR(50),
                                            AppointmentId VARCHAR(50),
                                            AppointmentStatus TINYINT(1),
                                            AdvancePaidForQueue TINYINT(1),
                                            FollowupConsultation TINYINT(1),
                                            AppointmentSource VARCHAR(100),
                                            DoctorName VARCHAR(100),
                                            PatentReachedClinic TINYINT(1)
                                        );
                                        """)
                                .execute())
                        .doFinally(signalType -> connection.close())
                )
                .then()
                .doOnError(error -> System.err.println("Table creation failed: " + error.getMessage()))
                .then();
    }
}