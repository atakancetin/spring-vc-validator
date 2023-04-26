package com.cvvalidator.cvvalidator.model;

import com.cvvalidator.cvvalidator.constants.EQuestionType;
import com.cvvalidator.cvvalidator.constants.Level;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.util.List;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;
    @Basic
    @Column(name = "questionText", nullable = true, length = 255)
    private String questionText;
    @ManyToOne
    @JoinColumn(name = "categoryId", referencedColumnName = "id")
    private Category category;
    @ManyToOne
    @JoinColumn(name = "skillId", referencedColumnName = "id")
    private Skill skill;
    @Basic
    @Column(name = "level", nullable = true)
    private Level level;
    @JsonIgnore
    @OneToMany(mappedBy = "question",fetch = FetchType.LAZY)
    private List<TestQuestion> testQuestions;
    @Basic
    @Column(name = "questionType", nullable = true)
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

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

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

    public List<TestQuestion> getTestQuestions() {
        return testQuestions;
    }

    public void setTestQuestions(List<TestQuestion> testQuestions) {
        this.testQuestions = testQuestions;
    }

    public EQuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EQuestionType questionType) {
        this.questionType = questionType;
    }
}
