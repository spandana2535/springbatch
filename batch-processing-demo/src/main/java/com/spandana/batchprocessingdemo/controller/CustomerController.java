package com.spandana.batchprocessingdemo.controller;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/customers")
public class CustomerController {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job job;

    @PostMapping("/launch")
    public String launchJob() {
        try {
            jobLauncher.run(job, new JobParameters());
            return "Job launched successfully";
        } catch (Exception e) {
            e.printStackTrace();
            return "Job failed to launch";
        }
    }
}
