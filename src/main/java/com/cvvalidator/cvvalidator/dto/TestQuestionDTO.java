package com.cvvalidator.cvvalidator.dto;

import com.cvvalidator.cvvalidator.constants.EQuestionType;

public class TestQuestionDTO {
    private long id;
    private String questionText;
    private EQuestionType questionType;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestionText() {
        return questionText;
    }

    public void setQuestionText(String questionText) {
        this.questionText = questionText;
    }

    public EQuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EQuestionType questionType) {
        this.questionType = questionType;
    }
}
