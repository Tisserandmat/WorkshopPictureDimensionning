package org.aenori.SpringBatchJobWorkshop.model;

import org.aenori.SpringBatchJobWorkshop.entities.Media;

public class HostedMedia {
    private final String urlPrefix;
    private final Media media;

    public HostedMedia(Media media, String urlPrefix) {
        this.urlPrefix = urlPrefix;
        this.media = media;
    }

    public String getUrl() {
        return urlPrefix + "/" + media.getName();
    }

    public String getUrlPrefix() {
        return urlPrefix;
    }

    public Media getMedia() {
        return media;
    }
}
