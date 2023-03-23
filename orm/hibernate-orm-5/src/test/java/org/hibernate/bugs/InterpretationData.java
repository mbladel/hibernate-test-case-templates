package org.hibernate.bugs;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "interpretation_data")
public class InterpretationData {

    @EmbeddedId
    public InterpretationVersion interpretationVersion;

    @Column(updatable = false)
    public String name;
}
