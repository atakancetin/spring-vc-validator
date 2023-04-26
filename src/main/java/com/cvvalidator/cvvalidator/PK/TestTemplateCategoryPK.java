package com.cvvalidator.cvvalidator.PK;

import javax.persistence.Embeddable;
import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class TestTemplateCategoryPK implements Serializable {

    private long categoryId;
    private long testTemplateId;

    public TestTemplateCategoryPK() {
    }
    public TestTemplateCategoryPK(long categoryId,long testTemplateId) {
        super();
        this.categoryId = categoryId;
        this.testTemplateId = testTemplateId;
    }

    public Long getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(Long categoryId) {
        this.categoryId = categoryId;
    }

    public Long getTestTemplateId() {
        return testTemplateId;
    }

    public void setTestTemplateId(Long testTemplateId) {
        this.testTemplateId = testTemplateId;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TestTemplateCategoryPK that = (TestTemplateCategoryPK) o;
        return categoryId == that.categoryId &&
                testTemplateId == that.testTemplateId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(categoryId, testTemplateId);
    }
}
