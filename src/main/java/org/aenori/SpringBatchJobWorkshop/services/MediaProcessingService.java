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
import org.aenori.SpringBatchJobWorkshop.model.HostedMedia;
import org.aenori.SpringBatchJobWorkshop.repositories.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MediaProcessingService {
    @Autowired
    MediaRepository mediaRepository;

    @Value("${amazon.aws.s3.accesskey}")
    private String accessKey;

    @Value("${amazon.aws.s3.secretkey}")
    private String accessSecret;

    @Value("${amazon.aws.s3.bucket}")
    private String bucketName;

    @Value("${amazon.aws.s3.region}")
    private String region;

    @Value("https://${amazon.aws.s3.bucket}.s3.${amazon.aws.s3.region}.amazonaws.com")
    private String urlPrefix;

    AmazonS3 amazonS3;
    public void saveFile(String filename, InputStream inputStream, long size) {
        ObjectMetadata objectMetadata = new ObjectMetadata();
        objectMetadata.setContentLength(size);
        getAmazonS3().putObject(
                new PutObjectRequest(bucketName, filename, inputStream, objectMetadata)
                        .withCannedAcl(CannedAccessControlList.PublicRead));

    }

    public void saveFile(String filename, File file) {
        getAmazonS3().putObject(
                new PutObjectRequest(bucketName, filename, file)
                        .withCannedAcl(CannedAccessControlList.PublicRead));
    }

    public void deleteFile(String key) {
        getAmazonS3().deleteObject(new DeleteObjectRequest(bucketName, key));
    }

    String getUrlPrefix() {
        return urlPrefix;
    }

    private AmazonS3 getAmazonS3() {
        if(amazonS3 == null) {
            initAmazonS3();
        }
        return amazonS3;
    }

    private void initAmazonS3() {
        AWSCredentials credentials = new BasicAWSCredentials(
                accessKey,
                accessSecret
        );

        amazonS3 = AmazonS3ClientBuilder
                .standard()
                .withCredentials(new AWSStaticCredentialsProvider(credentials))
                .withRegion(Regions.valueOf(region.toUpperCase().replace('-', '_')))
                .build();
    }
}
