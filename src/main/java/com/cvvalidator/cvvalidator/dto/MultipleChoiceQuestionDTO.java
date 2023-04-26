package com.cvvalidator.cvvalidator.dto;

import java.util.List;

public class MultipleChoiceQuestionDTO extends TestQuestionDTO {


    private List<String> choices;

    public List<String> getChoices() {
        return choices;
    }

    public void setChoices(List<String> choices) {
        this.choices = choices;
    }
}
