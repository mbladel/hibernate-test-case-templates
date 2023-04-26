package org.hibernate.bugs;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;

@Entity
@Table(name="role_scope_permissions")
public class RoleScopePermission implements Serializable {

    @Id
    @GeneratedValue
    private Long id;

    private String roleName;

    private String scopeName;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "roleName", referencedColumnName = "roleName")
    @JoinColumn(name = "scopeName", referencedColumnName = "scopeName")
    private Set<RoleScopeOperationPermission> operationPermissions;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(name = "roleName", referencedColumnName = "roleName")
    @JoinColumn(name = "scopeName", referencedColumnName = "scopeName")
    private Set<RoleScopeOperationObjectPermission> operationObjectPermissions;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getScopeName() {
        return scopeName;
    }

    public void setScopeName(String scopeName) {
        this.scopeName = scopeName;
    }

    public Set<RoleScopeOperationPermission> getOperationPermissions() {
        return operationPermissions;
    }

    public void setOperationPermissions(Set<RoleScopeOperationPermission> operationPermissions) {
        this.operationPermissions = operationPermissions;
    }

    public Set<RoleScopeOperationObjectPermission> getOperationObjectPermissions() {
        return operationObjectPermissions;
    }

    public void setOperationObjectPermissions(Set<RoleScopeOperationObjectPermission> operationObjectPermissions) {
        this.operationObjectPermissions = operationObjectPermissions;
    }
}
