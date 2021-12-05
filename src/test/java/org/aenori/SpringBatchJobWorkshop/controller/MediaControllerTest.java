package org.aenori.SpringBatchJobWorkshop.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.ui.ExtendedModelMap;
import org.springframework.ui.Model;

import javax.transaction.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class MediaControllerTest {
    @Autowired
    MediaController mediaController;

    @Test
    void getAll() {
        Model model = new ExtendedModelMap();
        mediaController.getAll(model);
        assertTrue(model.containsAttribute("allElements"));
    }

    @Test
    void create() {
    }
}