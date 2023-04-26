package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.PK.TestTemplateCategoryPK;
import com.cvvalidator.cvvalidator.model.TestTemplateCategory;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestTemplateCategoryRepository extends CrudRepository<TestTemplateCategory, TestTemplateCategoryPK> {

}
