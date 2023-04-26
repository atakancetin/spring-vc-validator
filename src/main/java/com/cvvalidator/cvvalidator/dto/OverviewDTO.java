package com.cvvalidator.cvvalidator.dto;

public class OverviewDTO {

    private int completedTest;
    private int activeTest;
    private double successRate;
    private String mostTestedSkill;
    private String mostVerifiedSkill;

    public OverviewDTO(int completedTest, int activeTest, double successRate, String mostTestedSkill, String mostVerifiedSkill) {
        this.completedTest = completedTest;
        this.activeTest = activeTest;
        this.successRate = successRate;
        this.mostTestedSkill = mostTestedSkill;
        this.mostVerifiedSkill = mostVerifiedSkill;
    }

    public OverviewDTO() {
    }

    public int getCompletedTest() {
        return completedTest;
    }

    public void setCompletedTest(int completedTest) {
        this.completedTest = completedTest;
    }

    public int getActiveTest() {
        return activeTest;
    }

    public void setActiveTest(int activeTest) {
        this.activeTest = activeTest;
    }

    public double getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(double successRate) {
        this.successRate = successRate;
    }

    public String getMostTestedSkill() {
        return mostTestedSkill;
    }

    public void setMostTestedSkill(String mostTestedSkill) {
        this.mostTestedSkill = mostTestedSkill;
    }

    public String getMostVerifiedSkill() {
        return mostVerifiedSkill;
    }

    public void setMostVerifiedSkill(String mostVerifiedSkill) {
        this.mostVerifiedSkill = mostVerifiedSkill;
    }
}
