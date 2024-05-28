package org.hibernate.bugs;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.DiscriminatorFormula;
import org.hibernate.annotations.DiscriminatorOptions;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;

@Entity(name = "Grantee")
@Table(name = "M_EDM_GRANTEES")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@DiscriminatorFormula("CONCAT(GRANTEE_TYPE,AUTH_TYPE)")
@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING, length = 9)
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorOptions(force = true)
public abstract class Grantee {
    @Id
    @Column(name = "GRANTEE_ID")
    public String id;

    @Column(name = "GRANTEE_TYPE", nullable = false)
    private String granteeType;

    @Column(name = "AUTH_TYPE", nullable = false)
    private String authTypeInternal;

    public Grantee() {

    }

    public Grantee(String id) {
        super();
        this.id = id;
    }

    protected void setGranteeType(String theGranteeType) {
        this.granteeType = theGranteeType;
    }

    public void setAuthType(String authType) {
        this.authTypeInternal = authType;
    }
}
