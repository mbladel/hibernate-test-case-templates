package org.hibernate.bugs;

import java.io.Serializable;
import java.util.Collection;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinColumns;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;

@Entity
public class ApplicationLinks implements Serializable {

    @EmbeddedId
    private ApplicationLinksPK applicationLinksPK;

    // Simplified bidirectional One-to-Many relationship to self (parent-child links)
    @OneToMany(mappedBy = "parentLink")
    private Collection<ApplicationLinks> childLinks;

    // Many-to-One relationship with self (parent link), using insertable=false, updatable=false
    @JoinColumns({
        @JoinColumn(name = "AppId", referencedColumnName = "AppId", insertable = false, updatable = false),
        @JoinColumn(name = "ParentId", referencedColumnName = "Id", insertable = false, updatable = false)
    })
    @ManyToOne(optional = false)
    private ApplicationLinks parentLink;

    // Many-to-One relationship to Applications with insertable=false, updatable=false
    @ManyToOne(optional = false)
    @JoinColumn(name = "AppId", referencedColumnName = "Id", insertable = false, updatable = false)
    private Applications applications;

    // Getters and setters
    public ApplicationLinksPK getApplicationLinksPK() {
        return applicationLinksPK;
    }

    public void setApplicationLinksPK(ApplicationLinksPK applicationLinksPK) {
        this.applicationLinksPK = applicationLinksPK;
    }

    public Collection<ApplicationLinks> getChildLinks() {
        return childLinks;
    }

    public void setChildLinks(Collection<ApplicationLinks> childLinks) {
        this.childLinks = childLinks;
    }

    public ApplicationLinks getParentLink() {
        return parentLink;
    }

    public void setParentLink(ApplicationLinks parentLink) {
        this.parentLink = parentLink;
    }

    public Applications getApplications() {
        return applications;
    }

    public void setApplications(Applications applications) {
        this.applications = applications;
    }
}
