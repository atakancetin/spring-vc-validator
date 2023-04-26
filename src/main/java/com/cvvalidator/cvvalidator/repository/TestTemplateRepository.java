package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.constants.Level;
import com.cvvalidator.cvvalidator.model.TestTemplate;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestTemplateRepository extends CrudRepository<TestTemplate, Long> {

    TestTemplate findBySkill_IdAndLevel(long skillId, Level level);
    List<TestTemplate> findBySkill_Id(long skillId);
}
