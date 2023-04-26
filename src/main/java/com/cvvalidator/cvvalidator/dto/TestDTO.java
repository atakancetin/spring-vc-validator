package com.cvvalidator.cvvalidator.dto;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

public class TestDTO {

    private long id;
    private LocalTime spentTime;
    private LocalDateTime startDate;
    private long testTime;
    private String skill;
    private List<TestQuestionDTO> testQuestions;

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

    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    public long getTestTime() {
        return testTime;
    }

    public void setTestTime(long testTime) {
        this.testTime = testTime;
    }

    public String getSkill() {
        return skill;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public List<TestQuestionDTO> getTestQuestions() {
        return testQuestions;
    }

    public void setTestQuestions(List<TestQuestionDTO> testQuestions) {
        this.testQuestions = testQuestions;
    }
}
