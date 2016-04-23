package com.adl.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Adlparser.
 */

@Document(collection = "adlparser")
public class Adlparser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("filename")
    private String filename;

    @Field("content")
    private String content;

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

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Adlparser adlparser = (Adlparser) o;
        if(adlparser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, adlparser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Adlparser{" +
            "id=" + id +
            ", filename='" + filename + "'" +
            ", content='" + content + "'" +
            '}';
    }
}
