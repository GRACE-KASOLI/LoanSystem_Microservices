package com.example.UserService.Config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class LoginBatchScheduler {

    @Autowired
    private JobLauncher jobLauncher;

    @Autowired
    private Job loginBatchJob;

    @Scheduled(fixedRate = 60000) // Run every 1 minute
    public void runLoginBatch() {
        try {
            jobLauncher.run(loginBatchJob, new JobParameters());
            System.out.println("Login batch job executed.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

