package org.aenori.SpringBatchJobWorkshop.controller;

import org.aenori.SpringBatchJobWorkshop.entities.Media;
import org.aenori.SpringBatchJobWorkshop.services.MediaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;

@Controller
@RequestMapping("")
public class MediaController {
    @Autowired
    private MediaService mediaService;

    @GetMapping("")
    public String getAll(Model model) {
        model.addAttribute("allElements", mediaService.getMediaList());
        model.addAttribute("categories", Media.Type.class.getEnumConstants());

        return "main";
    }

    @PostMapping("")
    public String create(
            @RequestParam("mediaFile") MultipartFile file,
            @RequestParam("mediaType") String type,
            RedirectAttributes redirectAttributes
    ) {
        String filename = file.getOriginalFilename();

        try {
            mediaService.saveMedia(
                    filename,
                    file.getInputStream(),
                    file.getSize()
            );
        }
        catch(IOException e) {
            redirectAttributes.addAttribute("errorMessage", e.getMessage());
        }

        return "redirect:/";
    }
}
