package com.cvvalidator.cvvalidator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.DynamicUpdate;

import javax.persistence.*;
import java.util.List;

@Entity
@DynamicUpdate
public class Category {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "name" ,columnDefinition = "ntext")
    private String name;
    @Basic
    @Column(name = "description" ,length = 255)
    private String description;

    @OneToMany(mappedBy = "testTemplate")
    private List<TestTemplateCategory> testTemplateList;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @JsonIgnore
    public List<TestTemplateCategory> getTestTemplateList() {
        return testTemplateList;
    }

    public void setTestTemplateList(List<TestTemplateCategory> testTemplateList) {
        this.testTemplateList = testTemplateList;
    }
}
