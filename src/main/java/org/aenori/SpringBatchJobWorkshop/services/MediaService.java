package org.aenori.SpringBatchJobWorkshop.services;

import org.aenori.SpringBatchJobWorkshop.entities.Media;
import org.aenori.SpringBatchJobWorkshop.model.HostedMedia;
import org.aenori.SpringBatchJobWorkshop.repositories.MediaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MediaService {
    private static final String destDir = "uploads";

    @Autowired
    MediaRepository mediaRepository;

    public void saveMedia(String filename, InputStream inputStream, long size) {
        saveFile(filename, inputStream, size);
        mediaRepository.save(
                new Media(
                    filename,
                    Media.Type.SimpleMedia
                )
        );
    }

    public List<HostedMedia> getMediaList() {
        return mediaRepository
                .findAll()
                .stream()
                .map(cm -> new HostedMedia(
                        cm,
                        getUrlPrefix()
                ))
                .collect(Collectors.toList());
    }

    private String getUrlPrefix() {
        return destDir;
    }

    private void saveFile(String filename, InputStream inputStream, long size) {
        Path fullPath = Path.of(String.format("src/main/resources/static/%s/%s", destDir, filename));
        try {
            Files.createDirectories(fullPath);
            Files.copy(inputStream, fullPath, StandardCopyOption.REPLACE_EXISTING);
        }
        catch(IOException io) {
            System.err.println("Cannot create directories - " + io);
        }
    }
}
