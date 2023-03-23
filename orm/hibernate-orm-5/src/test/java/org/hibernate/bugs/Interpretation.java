package org.hibernate.bugs;

import java.util.UUID;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinColumns;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "interpretations",
        uniqueConstraints = @UniqueConstraint(
                name = "interpretations_id_unique",
                columnNames = {"id"}))
public class Interpretation {
    @Id
    public Long uuid;

    @Column(name = "interpretation_version")
    public Long interpretationVersion;

    @Column(name = "id", insertable = false, updatable = false, nullable = false, columnDefinition = "serial")
    @Generated(GenerationTime.INSERT)
    public Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumns({
            @JoinColumn(name = "uuid", referencedColumnName = "interpretation_uuid", insertable = false, updatable = false),
            @JoinColumn(name = "interpretation_version", referencedColumnName = "interpretation_version", insertable = false, updatable = false)
    })
    public InterpretationData interpretationData;
}
