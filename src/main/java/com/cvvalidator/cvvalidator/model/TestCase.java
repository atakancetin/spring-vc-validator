package com.cvvalidator.cvvalidator.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
public class TestCase {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @Column
    private String input;

    @Column
    private String output;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "codeQuestionId")
    private CodeQuestion codeQuestion;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getOutput() {
        return output;
    }

    public void setOutput(String output) {
        this.output = output;
    }

    public CodeQuestion getCodeQuestion() {
        return codeQuestion;
    }

    public void setCodeQuestion(CodeQuestion codeQuestion) {
        this.codeQuestion = codeQuestion;
    }
}
