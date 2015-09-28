/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.concept.repository.openproject.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

/**
 *
 * @author ermis
 */
@Entity
@Table(name = "member_roles")
public class MemberRoleOp implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "member_id")
    private int memberId;
    @Basic(optional = false)
    @NotNull
    @Column(name = "role_id")
    private int roleId;
    @Column(name = "inherited_from")
    private Integer inheritedFrom;

    public MemberRoleOp() {
    }

    public MemberRoleOp(Integer id) {
        this.id = id;
    }

    public MemberRoleOp(Integer id, int memberId, int roleId) {
        this.id = id;
        this.memberId = memberId;
        this.roleId = roleId;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getMemberId() {
        return memberId;
    }

    public void setMemberId(int memberId) {
        this.memberId = memberId;
    }

    public int getRoleId() {
        return roleId;
    }

    public void setRoleId(int roleId) {
        this.roleId = roleId;
    }

    public Integer getInheritedFrom() {
        return inheritedFrom;
    }

    public void setInheritedFrom(Integer inheritedFrom) {
        this.inheritedFrom = inheritedFrom;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof MemberRoleOp)) {
            return false;
        }
        MemberRoleOp other = (MemberRoleOp) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eu.concept.repository.openproject.domain.MemberRoles[ id=" + id + " ]";
    }
    
}
