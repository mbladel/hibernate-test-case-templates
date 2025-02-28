package redhat;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.NamedQueries;
import jakarta.persistence.NamedQuery;
import jakarta.persistence.Query;
import jakarta.persistence.Table;

@Entity
@Table(name = "EntIngot")
@NamedQueries({
        @NamedQuery(name = "EntIngotNoRelationship.findBySerial", query = "select distinct object(i) from EntIngotNoRelationship i where i.numberSerial = ?1"), })
public class EntIngotNoRelationship implements Serializable {
    private static final long serialVersionUID = 1L;

    public static EntIngotNoRelationship findBySerial(EntityManager em, String serial) {
        if (serial == null) {
            return null;
        }

        serial = serial.toUpperCase();
        Query qry = em.createNamedQuery("EntIngotNoRelationship.findBySerial");
        qry.setParameter(1, serial);
        try {
            return (EntIngotNoRelationship) qry.getSingleResult();
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

    public EntIngotNoRelationship() {
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

    public Date getCodeIngotToken() {
        return codeIngotToken;
    }

    public void setCodeIngotToken(Date codeIngotToken) {
        this.codeIngotToken = codeIngotToken;
    }
}