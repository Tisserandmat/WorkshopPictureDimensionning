package org.aenori.SpringBatchJobWorkshop.config;

import org.aenori.SpringBatchJobWorkshop.entities.Media;
import org.aenori.SpringBatchJobWorkshop.repositories.MediaRepository;
import org.aenori.SpringBatchJobWorkshop.services.MediaProcessingService;
import org.aenori.SpringBatchJobWorkshop.services.MediaService;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.JobRegistry;
import org.springframework.batch.core.configuration.annotation.*;
import org.springframework.batch.core.configuration.support.JobRegistryBeanPostProcessor;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.support.SimpleJobLauncher;
import org.springframework.batch.core.repository.JobRepository;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.SimpleAsyncTaskExecutor;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.io.File;
import java.io.InputStream;

@Configuration
@EnableBatchProcessing
public class BatchConfiguration {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private MediaRepository mediaRepository;

    @Autowired
    private JobRepository jobRepository;

    @Autowired
    private MediaProcessingService mediaProcessingService;

    @Bean
    public Job uploadToS3Job(Step uploadToS3) {
        return this.jobBuilderFactory.get("uploadToS3Job")
                .start(uploadToS3)
                .next(makeMediaProcessed(null))
                .build();
    }

    @Bean
    public Job profilePictureJob(Step uploadToS3) {
        return this.jobBuilderFactory.get("uploadToS3Job")
                .start(uploadToS3)
                .next(makeMediaProcessed(null))
                .build();
    }

    // Ce step sert à vérifier le bon fonctionnement du job en simulant un
    // long traitement
    @Bean
    public Step waitStep() {
        return this.stepBuilderFactory.get("waitStep")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        Thread.sleep(30000);
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    @JobScope
    public Step makeMediaProcessed(@Value("#{jobParameters['mediaId']}") Integer mediaId) {
        return this.stepBuilderFactory.get("makeAllImagesProcessed")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        Media media = mediaRepository.getById(mediaId);
                        media.setStatus(Media.Status.Ok);
                        mediaRepository.save(media);

                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean
    @JobScope
    public Step uploadToS3(
            @Value("#{jobParameters['filename']}") String filename,
            @Value("#{jobParameters['tmpFile']}") String tmpFile) {
        return this.stepBuilderFactory.get("uploadToS3")
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        mediaProcessingService.saveFile(filename, new File(tmpFile));
                        return RepeatStatus.FINISHED;
                    }
                })
                .build();
    }

    @Bean(name = "asyncJobLauncher")
    public JobLauncher simpleJobLauncher() throws Exception {
        SimpleJobLauncher jobLauncher = new SimpleJobLauncher();
        jobLauncher.setJobRepository(jobRepository);
        // Cette ligne est importante car le ThreadPoolExecutor possède le
        // comportement souhaité :
        //   - asynchrone
        //   - avec un nombre limité de thread (1 par défaut)
        ThreadPoolTaskExecutor threadPoolTaskExecutor = new ThreadPoolTaskExecutor();
        threadPoolTaskExecutor.initialize();
        jobLauncher.setTaskExecutor(threadPoolTaskExecutor);
        jobLauncher.afterPropertiesSet();
        return jobLauncher;
    }
}