package com.deepak.queue.component;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TableCreationRunner implements CommandLineRunner {

    private final TableCreator tableCreator;

    public TableCreationRunner(TableCreator tableCreator) {
        this.tableCreator = tableCreator;
    }

    @Override
    public void run(String... args) {
        tableCreator.createTable().block(); // Blocking call in this example
    }
}