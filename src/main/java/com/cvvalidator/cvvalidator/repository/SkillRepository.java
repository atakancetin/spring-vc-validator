package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.Skill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SkillRepository extends CrudRepository<Skill, Long> {

}
