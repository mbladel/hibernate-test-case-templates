package org.hibernate.bugs;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import jakarta.persistence.Cacheable;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
@Cacheable
@Entity
@Table(name = "APPROVAL_STEP_CONFIG")
public class ApprovalStepConfiguration {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_approval_step_config")
    @Column(name = "ID")
    private Long id;

    // @Fetch(FetchMode.SUBSELECT) causes the problem in combination with Role.application renamed to Role.xapplication.
    @Fetch(FetchMode.SUBSELECT)
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @OneToMany(mappedBy = ApprovalStep.ATTR_APPROVALSTEPCONFIGURATION, fetch = FetchType.EAGER, cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<ApprovalStep> approvalSteps = new HashSet<>();

    @Override
    public boolean equals(final Object o) {
        return o == this || o instanceof ApprovalStepConfiguration && Objects.equals(this.id, ((ApprovalStepConfiguration) o).id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

}
