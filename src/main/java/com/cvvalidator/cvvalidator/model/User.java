package com.cvvalidator.cvvalidator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "firstName", nullable = true, length = 250)
    private String firstName;
    @Basic
    @Column(name = "lastName", nullable = true, length = 250)
    private String lastName;
    @Basic
    @NotBlank
    @Column(name = "password", nullable = true, length = 255)
    private String password;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Basic
    @Column(name = "recordDate", nullable = true)
    private Date recordDate;
    @Basic
    @NotBlank
    @Column(name = "email", nullable = true, length = 250)
    private String email;
 
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "user_roles_relation", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
	private Set<Role> roles = new HashSet<>();

    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<UserSkill> skillList;

    @JsonIgnore	
	@OneToOne(mappedBy = "user")
	private UserResetPasswordToken userResetPasswordToken;

    @OneToOne
    @JoinColumn(name="profileId")
    private Profile profile;

    @JsonIgnore
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Test> testList;


    public Set<Role> getRoles()
    {
        return this.roles;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }
    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getRecordDate() {
        return recordDate;
    }

    public void setRecordDate(Date recordDate) {
        this.recordDate = recordDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<UserSkill> getSkillList() {
        return skillList;
    }

    public void setSkillList(List<UserSkill> skillList) {
        this.skillList = skillList;
    }

    public UserResetPasswordToken getUserResetPasswordToken() {
        return userResetPasswordToken;
    }

    public void setUserResetPasswordToken(UserResetPasswordToken userResetPasswordToken) {
        this.userResetPasswordToken = userResetPasswordToken;
    }
    public void setRoles(Set<Role> roles) {
        this.roles = roles;
    }

    public Profile getProfile() {
        return profile;
    }

    public void setProfile(Profile profile) {
        this.profile = profile;
    }

    public List<Test> getTestList() {
        return testList;
    }

    public void setTestList(List<Test> testList) {
        this.testList = testList;
    }
}
