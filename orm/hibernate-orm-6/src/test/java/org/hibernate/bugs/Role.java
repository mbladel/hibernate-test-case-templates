package org.hibernate.bugs;

import java.util.Objects;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
@Entity
@Table(name = "ROLE")
public class Role extends AbstractSharedEntity {

    // Prefix "x" in property-name causes the problem in combination with FetchMode.SUBSELECT applied to
    // ApprovalStepConfiguration.approvalSteps. See comment on ApprovalStepConfiguration.approvalSteps
    public static final String ATTR_APPLICATION = "xapplication";

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_role")
    @Column(name = "ID")
    private Long id;

    @Column(name = "NAME", nullable = false, unique = true)
    private String name;

    @ManyToOne
    @JoinColumn(name = "APPLICATION_ID", nullable = false)
    private Application xapplication;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "APPROVAL_STEP_CONFIG_ID")
    private ApprovalStepConfiguration approvalStepConfiguration;

    @OneToOne
    @JoinColumn(name = "RESP_APPROVAL_STEP_ID")
    private ApprovalStep responsibleApprovalStep;

    public Role() {
    }

    @Override
    public boolean equals(final Object o) {
        return o == this || o instanceof Role && Objects.equals(this.id, ((Role) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
