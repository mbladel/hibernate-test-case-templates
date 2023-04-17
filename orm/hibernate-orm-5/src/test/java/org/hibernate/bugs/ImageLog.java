package org.hibernate.bugs;

import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "image_logs")
public class ImageLog {

    @Id
    public UUID uuid;

    @Column(name = "id")
    public Long id;

    @ManyToOne
    @JoinColumn(name = "lateral_id", nullable = false)
    public LateralEntity lateralEntity;

}
