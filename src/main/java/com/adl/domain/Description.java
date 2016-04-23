package com.adl.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A Description.
 */

@Document(collection = "description")
public class Description implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("concept")
    private String concept;

    @Field("long_concept")
    private String longConcept;

    @Field("author")
    private String author;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConcept() {
        return concept;
    }

    public void setConcept(String concept) {
        this.concept = concept;
    }

    public String getLongConcept() {
        return longConcept;
    }

    public void setLongConcept(String longConcept) {
        this.longConcept = longConcept;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Description description = (Description) o;
        if(description.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, description.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Description{" +
            "id=" + id +
            ", concept='" + concept + "'" +
            ", longConcept='" + longConcept + "'" +
            ", author='" + author + "'" +
            '}';
    }
}
