package org.hibernate.bugs;

import java.util.List;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

@Entity
@Table(name = "CLASSROOM")
public class Classroom {


    /**
     *
     */
    private static final long serialVersionUID = -2220681662831144716L;


    @Id
    @Column(name = "OID", unique = true, nullable = false, precision = 9, scale = 0)
    private Long oid;


    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "OID_BUILDING")
    @NotFound(action = NotFoundAction.EXCEPTION)
    private Building building;

    @OneToMany(fetch = FetchType.EAGER)
    @NotFound(action = NotFoundAction.EXCEPTION)
    private List<Building> adiacentBuildings;

    public Classroom() {
    }

    public Classroom(Long oid, String description, Building building, List<Building> adiacentBuildings) {
        this.oid = oid;
        this.description = description;
        this.building = building;
        this.adiacentBuildings = adiacentBuildings;
    }

    public Long getOid() {
        return this.oid;
    }

    public void setOid(Long oid) {
        this.oid = oid;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDesCompletaAula() {
        return description + " - " + building.getDescription();
    }

    public Building getBuilding() {
        return building;
    }


    public void setBuilding(Building oidBuilding) {
        this.building = oidBuilding;
    }

    public List<Building> getAdiacentBuildings() {
        return adiacentBuildings;
    }
}
