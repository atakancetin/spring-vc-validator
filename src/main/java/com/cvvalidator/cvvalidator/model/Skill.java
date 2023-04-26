package com.cvvalidator.cvvalidator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
public class Skill {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "name", nullable = true, length = 255)
    private String name;
    @OneToMany(mappedBy = "skill")
    private List<UserSkill> userList;
    @OneToMany(mappedBy = "skill")
    private List<TestTemplate> testTemplateList;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String skillName) {
        this.name = skillName;
    }

    public void setUserList(List<UserSkill> userList) {
        this.userList = userList;
    }
    @JsonIgnore
    public List<UserSkill> getUserList() {
        return userList;
    }
    @JsonIgnore
    public List<TestTemplate> getTestTemplateList() {
        return testTemplateList;
    }

    public void setTestTemplateList(List<TestTemplate> testTemplateList) {
        this.testTemplateList = testTemplateList;
    }
}
