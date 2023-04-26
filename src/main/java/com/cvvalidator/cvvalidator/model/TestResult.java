package com.cvvalidator.cvvalidator.model;

import com.cvvalidator.cvvalidator.constants.Level;

public class TestResult {
    private Skill skill;
    private Level level;
    private boolean success;
    private int successRate;
    private int successBoundary;
    private String spentTime;
    private String testTime;

    public Skill getSkill() {
        return skill;
    }

    public void setSkill(Skill skill) {
        this.skill = skill;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
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

    public int getSuccessBoundary() {
        return successBoundary;
    }

    public void setSuccessBoundary(int successBoundary) {
        this.successBoundary = successBoundary;
    }

    public String getSpentTime() {
        return spentTime;
    }

    public void setSpentTime(String spentTime) {
        this.spentTime = spentTime;
    }

    public String getTestTime() {
        return testTime;
    }

    public void setTestTime(String testTime) {
        this.testTime = testTime;
    }
}
