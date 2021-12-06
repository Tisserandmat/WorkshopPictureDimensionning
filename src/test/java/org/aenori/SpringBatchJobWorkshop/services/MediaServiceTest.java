package org.aenori.SpringBatchJobWorkshop.services;

import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.Assumptions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@ActiveProfiles("test")
@Transactional
class MediaServiceTest {
    @Autowired
    MediaService mediaService;

    @Value("${amazon.aws.s3.accesskey}")
    private String s3_access_key;

    @Test
    public void uploadAndDelete() {
        Assumptions.assumeTrue(!s3_access_key.isEmpty(), "S3 env variables are not set");

        File testFile = new File("src/test/resources/image_for_test.png");
        String generatedString = RandomStringUtils.random(32);
        String objectKey = generatedString + "_image_for_test.png";
        try (InputStream inputStream = new FileInputStream(
                testFile
        )) {
            mediaService.saveMedia(
                    objectKey,
                    inputStream,
                    testFile.length()
            );
        }
        catch (IOException io) {
            fail("IOException caught");
        }

        mediaService.deleteFile(objectKey);
    }
}