package org.aenori.SpringBatchJobWorkshop.controller;

import org.aenori.SpringBatchJobWorkshop.services.MediaService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.transaction.Transactional;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class MediaControllerTest {
    @InjectMocks
    MediaController mediaController;

    @Mock
    MediaService mediaService;

    @Test
    void getAll() {
        Model model = new ExtendedModelMap();
        when(mediaService.getMediaList()).thenReturn(new ArrayList<>());
        mediaController.getAll(model);
        assertTrue(model.containsAttribute("allElements"));
        verify(mediaService).getMediaList();
    }

    @Test
    void create() {
    }
}