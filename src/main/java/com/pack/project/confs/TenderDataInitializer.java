package com.pack.project.confs;

import com.pack.project.services.TenderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class TenderDataInitializer implements CommandLineRunner {

    private final TenderService tenderService;

    @Autowired
    public TenderDataInitializer(TenderService tenderService) {
        this.tenderService = tenderService;
    }

    @Override
    public void run(String... args) {
        tenderService.parseAndSaveTenders();
    }
}

