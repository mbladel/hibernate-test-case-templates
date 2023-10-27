package org.hibernate.bugs;

import java.util.Objects;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Cacheable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
@Entity
@Table(name = "APPROVAL_STEP")
public class ApprovalStep extends AbstractSharedEntity {

    public static final String ATTR_APPROVALSTEPCONFIGURATION = "approvalStepConfiguration";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_approval_step")
    @Column(name = "ID")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "APPROVAL_STEP_CONFIG_ID", nullable = false)
    private ApprovalStepConfiguration approvalStepConfiguration;

    @ManyToOne
    @JoinColumn(name = "SIGNER_ROLE_ID")
    private Role signerRole;

    public ApprovalStep() {
    }

    @Override
    public boolean equals(final Object o) {
        return o == this || o instanceof ApprovalStep && Objects.equals(this.id, ((ApprovalStep) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

}
