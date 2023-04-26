package com.cvvalidator.cvvalidator.model;

import com.cvvalidator.cvvalidator.constants.Level;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "test_template", schema = "cvvalidator", catalog = "")
public class TestTemplate {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Basic
    @Column(name = "creationDate", nullable = true)
    private Date creationDate;
    @ManyToOne
    @JoinColumn(name = "skillId")
    private Skill skill;
    @Basic
    @Column(name = "level", nullable = true)
    private Level level;
    @OneToMany(mappedBy = "testTemplate")
    private List<TestTemplateCategory> categoryList;
    @OneToMany(cascade=CascadeType.ALL,mappedBy = "testTemplate",fetch = FetchType.LAZY)
    private List<Test> testList;
    @Basic
    @Column(name = "time")
    private LocalTime testTime;
    @Transient
    private long time;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(Date creationDate) {
        this.creationDate = creationDate;
    }

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public List<TestTemplateCategory> getCategoryList() {
        return categoryList;
    }

    public void setCategoryList(List<TestTemplateCategory> categoryList) {
        this.categoryList = categoryList;
    }
    @JsonIgnore
    public List<Test> getTestList() {
        return testList;
    }

    public void setTestList(List<Test> testList) {
        this.testList = testList;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public LocalTime getTestTime() {
        return testTime;
    }

    public void setTestTime(LocalTime testTime) {
        this.testTime = testTime;
    }

    public long getTime() {
        return time;
    }

    public void setTime(long time) {
        this.time = time;
    }
}
