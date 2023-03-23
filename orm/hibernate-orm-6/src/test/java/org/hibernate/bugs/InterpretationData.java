package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;

@Entity
@Table(name = "interpretation_data")
public class InterpretationData {

    @EmbeddedId
    public InterpretationVersion interpretationVersion;

    @Column(updatable = false)
    public String name;
}
