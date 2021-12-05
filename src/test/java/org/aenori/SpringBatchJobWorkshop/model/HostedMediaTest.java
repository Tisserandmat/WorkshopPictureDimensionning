package org.aenori.SpringBatchJobWorkshop.model;

import org.aenori.SpringBatchJobWorkshop.entities.Media;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ActiveProfiles("test")
@SpringBootTest
class HostedMediaTest {

    @Test
    void getUrl() {
        Media media = mock(Media.class);
        HostedMedia hostedMedia = new HostedMedia(media, "test");
        when(media.getName()).thenReturn("testFile");
        assertEquals("test/testFile", hostedMedia.getUrl());
        verify(media).getName();
    }
}