package com.cvvalidator.cvvalidator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.sql.Blob;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Entity
public class Test {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "spentTime")
    private LocalTime spentTime;
    @Basic
    @Column(name = "startDate")
    private LocalDateTime startDate;
    @ManyToOne
    @JoinColumn(name = "templateId")
    private TestTemplate testTemplate;

    @OneToMany(mappedBy = "test",fetch = FetchType.LAZY)
    private List<TestQuestion> testQuestions;

    @OneToMany(mappedBy = "test",fetch = FetchType.LAZY)
    private List<TestLog> testLogs;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "userId")
    private User user;
    @Basic
    @Column(name = "active")
    private boolean active;

    @Basic
    @Column(name = "success")
    private boolean success;

    @Basic
    @Column(name = "successRate")
    private int successRate;
    
    @JsonIgnore
    @Lob 
    @Column(name = "video")
    private Blob video;     

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalTime getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(LocalTime spentTime) {
        this.spentTime = spentTime;
    }

    public TestTemplate getTestTemplate() {
        return testTemplate;
    }

    public void setTestTemplate(TestTemplate testTemplate) {
        this.testTemplate = testTemplate;
    }

    public List<TestQuestion> getTestQuestions() {
        return testQuestions;
    }

    public void setTestQuestions(List<TestQuestion> testQuestions) {
        this.testQuestions = testQuestions;
    }
    @JsonIgnore
    public List<TestLog> getTestLogs() {
        return testLogs;
    }

    public void setTestLogs(List<TestLog> testLogs) {
        this.testLogs = testLogs;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public int getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(int successRate) {
        this.successRate = successRate;
    }
    public Blob getVideo() {
        return video;
    }

    public void setVideo(Blob video) {
        this.video = video;
    }

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }
}
