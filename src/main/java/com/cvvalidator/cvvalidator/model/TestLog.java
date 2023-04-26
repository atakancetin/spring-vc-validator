package com.cvvalidator.cvvalidator.model;

import com.cvvalidator.cvvalidator.constants.EActionType;

import javax.persistence.*;
import java.time.LocalDateTime;
@Entity
public class TestLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Column(name = "timeStamp")
    private LocalDateTime timeStamp;
    @Enumerated(EnumType.ORDINAL)
    @Column(name = "actionType")
    private EActionType actionType;
    @Column(name = "data")
    private String data;
    @ManyToOne
    @JoinColumn(name = "testId")
    private Test test;

    public TestLog() {
    }
    public TestLog(EActionType type,Test test) {
        this.actionType = type;
        this.test = test;
    }
    public TestLog(EActionType type,String data,Test test) {
        this.actionType = type;
        this.data = data;
        this.test = test;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public LocalDateTime getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(LocalDateTime timeStamp) {
        this.timeStamp = timeStamp;
    }

    public EActionType getActionType() {
        return actionType;
    }

    public void setActionType(EActionType actionType) {
        this.actionType = actionType;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }
}
