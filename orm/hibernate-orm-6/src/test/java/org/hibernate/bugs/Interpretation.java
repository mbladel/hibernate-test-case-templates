package org.hibernate.bugs;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;

import org.hibernate.annotations.Generated;
import org.hibernate.annotations.GenerationTime;

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
