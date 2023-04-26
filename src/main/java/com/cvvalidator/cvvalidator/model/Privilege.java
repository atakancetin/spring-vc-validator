package com.cvvalidator.cvvalidator.model;

import java.util.Collection;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import com.cvvalidator.cvvalidator.constants.PrivilegeName;
import com.cvvalidator.cvvalidator.constants.RoleName;

import org.hibernate.annotations.NaturalId;



@Entity
@Table(name = "nyx_privilege")
public class Privilege {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @NaturalId 
    private PrivilegeName name;

    @ManyToMany(mappedBy = "privileges")
    private Collection<Role> roles;

	public PrivilegeName getName() {
		return this.name;
	}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(PrivilegeName name) {
        this.name = name;
    }

    public Collection<Role> getRoles() {
        return roles;
    }

    public void setRoles(Collection<Role> roles) {
        this.roles = roles;
    }
}