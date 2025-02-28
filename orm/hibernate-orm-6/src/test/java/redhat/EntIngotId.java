package redhat;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "EntIngotId")
public class EntIngotId implements Serializable {
    private static final long serialVersionUID = 1L;

    @EmbeddedId
    public EntIngotIdPK id;

    @ManyToOne
    @JoinColumn(name = "CODE_INGOT_TOKEN", referencedColumnName = "CODE_INGOT_TOKEN", insertable = false, updatable = false)
    public EntIngotNoRelationship entIngot;

    public EntIngotId() {
    }

    public EntIngotIdPK getId() {
        return this.id;
    }

    public void setId(EntIngotIdPK id) {
        this.id = id;
    }

    public EntIngotNoRelationship getEntIngot() {
        return this.entIngot;
    }

    public void setEntIngot(EntIngotNoRelationship entIngot) {
        this.entIngot = entIngot;
    }

}