package com.example.UserService.Config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.batch.core.job.builder.JobBuilder;
import org.springframework.batch.core.step.builder.StepBuilder;

@Configuration
@EnableBatchProcessing
public class LoginBatchConfig {

    @Bean
    public Job loginBatchJob(JobRepository jobRepository, Step processLoginStep) {
        return new JobBuilder("loginBatchJob", jobRepository)
                .start(processLoginStep)
                .build();
    }

    @Bean
    public Step processLoginStep(JobRepository jobRepository, PlatformTransactionManager transactionManager) {
        return new StepBuilder("processLoginStep", jobRepository)
                .tasklet(loginTasklet(), transactionManager)
                .build();
    }

    @Bean
    public Tasklet loginTasklet() {
        return (contribution, chunkContext) -> {
            System.out.println("Processing batch login requests...");
            // Process multiple login requests here
            return RepeatStatus.FINISHED;
        };
    }
}
