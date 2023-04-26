package org.hibernate.bugs;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import java.util.Set;

@Entity
@Table(name="role_scope_permissions")
public class RoleScopePermission {

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
