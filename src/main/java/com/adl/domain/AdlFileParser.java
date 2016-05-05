package com.adl.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.io.Serializable;
import java.util.Objects;

/**
 * A AdlFileParser.
 */

@Document(collection = "adl_file_parser")
public class AdlFileParser implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("archetype_id")
    private String archetypeID;
    
    private AdlFileParserDescription adlFileParserDescription;
    
    private AdlFileParserIdentification adlFileParserIdentification;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getArchetypeID() {
        return archetypeID;
    }

    public void setArchetypeID(String archetypeID) {
        this.archetypeID = archetypeID;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        AdlFileParser adlFileParser = (AdlFileParser) o;
        if(adlFileParser.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, adlFileParser.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "AdlFileParser{" +
            "id=" + id +
            ", archetypeID='" + archetypeID + "'" +
            '}';
    }

	public AdlFileParserDescription getAdlFileParserDescription() {
		return adlFileParserDescription;
	}

	public void setAdlFileParserDescription(
			AdlFileParserDescription adlFileParserDescription) {
		this.adlFileParserDescription = adlFileParserDescription;
	}

	public AdlFileParserIdentification getAdlFileParserIdentification() {
		return adlFileParserIdentification;
	}

	public void setAdlFileParserIdentification(
			AdlFileParserIdentification adlFileParserIdentification) {
		this.adlFileParserIdentification = adlFileParserIdentification;
	}
 
}
