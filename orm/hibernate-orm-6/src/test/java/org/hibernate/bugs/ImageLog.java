package org.hibernate.bugs;

import java.util.UUID;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "image_logs")
public class ImageLog {

    @Id
    public UUID uuid;

    @Column(name = "id")
    public Long id;

    @ManyToOne
    @JoinColumn(name = "lateral_id", nullable = false)
    public Lateral lateralEntity;

}
