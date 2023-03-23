package org.hibernate.bugs;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
@Embeddable
public class InterpretationVersion implements Serializable {
    @Column(name = "interpretation_version", nullable = false, updatable = false)
    public Long version;

    @Column(name = "interpretation_uuid", nullable = false, updatable = false)
    public Long uuid;

    public InterpretationVersion() {
    }

    public InterpretationVersion(Long version, Long uuid) {
        this.version = version;
        this.uuid = uuid;
    }
}
