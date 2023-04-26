package com.cvvalidator.cvvalidator.model;

import javax.persistence.*;

@Entity
@PrimaryKeyJoinColumn(name = "id")
public class MultipleChoiceQuestion extends Question {
    @Basic
    @Column(name = "choice1", nullable = true, length = 255)
    private String choice1;
    @Basic
    @Column(name = "choice2", nullable = true, length = 255)
    private String choice2;
    @Basic
    @Column(name = "choice3", nullable = true, length = 255)
    private String choice3;
    @Basic
    @Column(name = "trueChoice", nullable = true, length = 255)
    private String trueChoice;

    public String getChoice1() {
        return choice1;
    }

    public void setChoice1(String choice1) {
        this.choice1 = choice1;
    }

    public String getChoice2() {
        return choice2;
    }

    public void setChoice2(String choice2) {
        this.choice2 = choice2;
    }

    public String getChoice3() {
        return choice3;
    }

    public void setChoice3(String choice3) {
        this.choice3 = choice3;
    }

    public String getTrueChoice() {
        return trueChoice;
    }

    public void setTrueChoice(String trueChoice) {
        this.trueChoice = trueChoice;
    }



}
