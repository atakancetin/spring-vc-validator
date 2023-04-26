package com.cvvalidator.cvvalidator.model;

import javax.persistence.*;
import java.util.List;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class CodeQuestion extends Question {
    @Basic
    @Column(name = "questionTitle", nullable = true, length = 255)
    private String questionTitle;
    @OneToMany(mappedBy = "codeQuestion" ,cascade = CascadeType.ALL, orphanRemoval=true)
    private List<TestCase> testCases;
    @Basic
    @Column(columnDefinition = "text")
    private String task;
    @Basic
    @Column(columnDefinition = "text")
    private String inputFormat;
    @Basic
    @Column(columnDefinition = "text")
    private String outputFormat;
    @Basic
    @Column(columnDefinition = "text")
    private String constraints;
    @Basic
    @Column(columnDefinition = "text")
    private String templateCode;



    public String getQuestionTitle() {
        return questionTitle;
    }

    public void setQuestionTitle(String questionTitle) {
        this.questionTitle = questionTitle;
    }

    public List<TestCase> getTestCases() {
        return testCases;
    }

    public void setTestCases(List<TestCase> testCases) {
        this.testCases = testCases;
    }

    public String getTask() {
        return task;
    }

    public void setTask(String task) {
        this.task = task;
    }

    public String getInputFormat() {
        return inputFormat;
    }

    public void setInputFormat(String inputFormat) {
        this.inputFormat = inputFormat;
    }

    public String getOutputFormat() {
        return outputFormat;
    }

    public void setOutputFormat(String outputFormat) {
        this.outputFormat = outputFormat;
    }

    public String getConstraints() {
        return constraints;
    }

    public void setConstraints(String constraints) {
        this.constraints = constraints;
    }

    public String getTemplateCode() {
        return templateCode;
    }

    public void setTemplateCode(String templateCode) {
        this.templateCode = templateCode;
    }
}
