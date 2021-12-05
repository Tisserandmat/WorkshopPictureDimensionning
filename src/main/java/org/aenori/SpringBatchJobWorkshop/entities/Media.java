package org.aenori.SpringBatchJobWorkshop.entities;

import javax.persistence.*;

@Entity
public class Media {
    public static enum Type {
        SimpleMedia
    }

    // <editor-fold desc="Fields">
    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;
    private String name;
    private Type mediaType;
    private boolean amazonS3Hosted;
    // </editor-fold>

    // <editor-fold desc="Constructor">
    public Media() {}
    public Media(String objectKey, Type mediaType) {
        this.mediaType = mediaType;
        this.name = objectKey;
    }
    public Media(String objectKey, String mediaType) {
        this(objectKey, Type.valueOf(mediaType));
    }
    public Media(String objectKey, Type mediaType, boolean amazonS3Hosted) {
        this.mediaType = mediaType;
        this.name = objectKey;
    }
    // </editor-fold>

    // <editor-fold desc="Getter-Setter">
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Type getMediaType() {
        return mediaType;
    }

    public void setMediaType(Type mediaType) {
        this.mediaType = mediaType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    // </editor-fold>
}
