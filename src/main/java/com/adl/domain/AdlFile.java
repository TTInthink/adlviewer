package com.adl.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

/**
 * A AdlFile.
 */

@Document(collection = "adl_file")
public class AdlFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("filename")
    private String filename;

    @Field("size")
    private Long size;

    @Field("date_upload")
    private LocalDate dateUpload;

    @Field("comment")
    private String comment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

    public LocalDate getDateUpload() {
        return dateUpload;
    }

    public void setDateUpload(LocalDate dateUpload) {
        this.dateUpload = dateUpload;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdlFile adlFile = (AdlFile) o;
        if(adlFile.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, adlFile.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AdlFile{" +
            "id=" + id +
            ", filename='" + filename + "'" +
            ", size='" + size + "'" +
            ", dateUpload='" + dateUpload + "'" +
            ", comment='" + comment + "'" +
            '}';
    }
}
