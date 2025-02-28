package redhat;

import java.io.Serializable;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;

@Embeddable
public class EntIngotIdPK implements Serializable {
    // default serial version id, required for serializable classes.
    private static final long serialVersionUID = 1L;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "CODE_INGOT_TOKEN")
    public java.util.Date codeIngotToken;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "DATE_TIME_ENTERED")
    public java.util.Date dateTimeEntered;

    @Column(name = "NUMBER_SERIAL")
    public String numberSerial;

    public EntIngotIdPK() {
    }

    public EntIngotIdPK(Date codeIngotToken, Date dateTimeEntered, String numberSerial) {
        this.codeIngotToken = codeIngotToken;
        this.dateTimeEntered = dateTimeEntered;
        this.numberSerial = numberSerial;
    }

    public java.util.Date getCodeIngotToken() {
        return this.codeIngotToken;
    }

    public void setCodeIngotToken(java.util.Date codeIngotToken) {
        this.codeIngotToken = codeIngotToken;
    }

    public java.util.Date getDateTimeEntered() {
        return this.dateTimeEntered;
    }

    public void setDateTimeEntered(java.util.Date dateTimeEntered) {
        this.dateTimeEntered = dateTimeEntered;
    }

    public String getNumberSerial() {
        return this.numberSerial == null ? null : this.numberSerial.trim();
    }

    public void setNumberSerial(String numberSerial) {
        this.numberSerial = numberSerial;
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof EntIngotIdPK)) {
            return false;
        }
        EntIngotIdPK castOther = (EntIngotIdPK) other;
        return this.codeIngotToken.equals(castOther.codeIngotToken) && this.dateTimeEntered.equals(castOther.dateTimeEntered)
                && this.numberSerial.equals(castOther.numberSerial);

    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int hash = 17;
        hash = hash * prime + this.codeIngotToken.hashCode();
        hash = hash * prime + this.dateTimeEntered.hashCode();
        hash = hash * prime + this.numberSerial.hashCode();

        return hash;
    }
}