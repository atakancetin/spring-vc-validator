package com.cvvalidator.cvvalidator.model;

import com.cvvalidator.cvvalidator.constants.EQuestionType;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;

@Entity
@Table(name = "test_template_category", schema = "cvvalidator", catalog = "")
public class TestTemplateCategory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private long id;

    @ManyToOne
    @JoinColumn(name = "categoryId")
    private Category category;

    @JsonIgnore
    @ManyToOne
    @JoinColumn(name = "testTemplateId")
    private TestTemplate testTemplate;

    @Basic
    @Column(name = "questionType", nullable = true)
    private EQuestionType questionType;

    @Basic
    @Column(name = "amount", nullable = true)
    private int amount;

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public TestTemplate getTestTemplate() {
        return testTemplate;
    }

    public void setTestTemplate(TestTemplate testTemplate) {
        this.testTemplate = testTemplate;
    }

    public EQuestionType getQuestionType() {
        return questionType;
    }

    public void setQuestionType(EQuestionType questionType) {
        this.questionType = questionType;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

}
