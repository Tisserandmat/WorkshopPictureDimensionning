package org.aenori.SpringBatchJobWorkshop.services;

import com.amazonaws.auth.AWSCredentials;
import com.amazonaws.auth.AWSStaticCredentialsProvider;
import com.amazonaws.auth.BasicAWSCredentials;
import com.amazonaws.regions.Regions;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.AmazonS3ClientBuilder;
import com.amazonaws.services.s3.model.CannedAccessControlList;
import com.amazonaws.services.s3.model.DeleteObjectRequest;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import net.bytebuddy.utility.RandomString;
import org.aenori.SpringBatchJobWorkshop.entities.Media;
import org.aenori.SpringBatchJobWorkshop.model.HostedMedia;
import org.aenori.SpringBatchJobWorkshop.repositories.MediaRepository;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.launch.JobOperator;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class MediaService {
    @Autowired
    MediaRepository mediaRepository;

    @Autowired
    @Qualifier("asyncJobLauncher")
    JobLauncher jobLauncher;

    @Autowired
    JobOperator jobOperator;

    @Autowired
    Job uploadToS3Job;

    @Autowired
    MediaProcessingService mediaProcessingService;

    public void saveMedia(String filename, InputStream inputStream, Long size) {
        String tmpFile = "/tmp/" + RandomString.make(32);
        try {
            Files.copy(inputStream, new File(tmpFile).toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Media savedMedia = mediaRepository.save(
                new Media(
                    filename,
                    Media.Type.SimpleMedia,
                    Media.Status.Waiting
                )
        );

        try {
            Map<String, JobParameter> confMap = new HashMap<String, JobParameter>();
            confMap.put("mediaId", new JobParameter(String.valueOf(savedMedia.getId())));
            confMap.put("filename", new JobParameter(filename));
            confMap.put("tmpFile", new JobParameter(tmpFile));

            JobParameters jobParameters = new JobParameters(confMap);
            jobLauncher.run(uploadToS3Job, jobParameters);
        } catch (JobExecutionAlreadyRunningException e) {
            e.printStackTrace();
        } catch (JobRestartException e) {
            e.printStackTrace();
        } catch (JobInstanceAlreadyCompleteException e) {
            e.printStackTrace();
        } catch (JobParametersInvalidException e) {
            e.printStackTrace();
        }
    }

    public List<HostedMedia> getMediaList() {
        return mediaRepository
                .findAll()
                .stream()
                .map(cm -> new HostedMedia(
                        cm,
                        mediaProcessingService.getUrlPrefix()
                ))
                .collect(Collectors.toList());
    }
}
