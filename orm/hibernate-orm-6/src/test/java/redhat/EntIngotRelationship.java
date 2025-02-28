package redhat;

import java.io.Serializable;
import java.util.Date;
import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Query;
import jakarta.persistence.Table;

/**
 * The persistent class for the ET3203 database table.
 *
 */
@Entity
@Table(name = "EntIngot")
@NamedQueries({
        @NamedQuery(name = "EntIngotRelationship.findBySerial", query = "select distinct object(i) from EntIngotRelationship i, IN (i.entIdList) as ingotIdList where ingotIdList.id.numberSerial = ?1"), })
public class EntIngotRelationship implements Serializable {
    private static final long serialVersionUID = 1L;

    public static EntIngotRelationship findBySerial(EntityManager em, String serial) {
        if (serial == null) {
            return null;
        }

        serial = serial.toUpperCase();
        Query qry = em.createNamedQuery("EntIngotRelationship.findBySerial");
        qry.setParameter(1, serial);
        try {
            return (EntIngotRelationship) qry.getSingleResult();
        } catch (Exception e) {
            return null;
        }
    }

    @Id
    @Column(name = "INGOT_KEY")
    public long ingotKey;

    @Column(name = "NUMBER_SERIAL")
    public String numberSerial;

    @Column(name = "CODE_INGOT_TOKEN")
    public Date codeIngotToken;

    @OneToMany(mappedBy = "entIngot", fetch = FetchType.EAGER)
    public Set<EntIngotId> entIdList;

    public EntIngotRelationship() {
    }

    public long getIngotKey() {
        return ingotKey;
    }

    public void setIngotKey(long ingotKey) {
        this.ingotKey = ingotKey;
    }

    public String getNumberSerial() {
        return this.numberSerial == null ? null : this.numberSerial.trim();
    }

    public void setNumberSerial(String numberSerial) {
        this.numberSerial = numberSerial;
    }

    public Set<EntIngotId> getEntIdList() {
        return this.entIdList;
    }

    public void setEntIdList(Set<EntIngotId> entIdList) {
        this.entIdList = entIdList;
    }

    public Date getCodeIngotToken() {
        return codeIngotToken;
    }

    public void setCodeIngotToken(Date codeIngotToken) {
        this.codeIngotToken = codeIngotToken;
    }
}