package org.hibernate.bugs;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "laterals")
public class Lateral {

    @Id
    public UUID uuid;

    @Column(name = "id")
    public Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "project_uuid", nullable = false, updatable = false)
    public Project project;
}
