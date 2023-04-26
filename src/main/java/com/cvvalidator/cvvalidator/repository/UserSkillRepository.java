package com.cvvalidator.cvvalidator.repository;


import com.cvvalidator.cvvalidator.constants.Level;
import com.cvvalidator.cvvalidator.model.Skill;
import com.cvvalidator.cvvalidator.model.User;
import com.cvvalidator.cvvalidator.model.UserSkill;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSkillRepository extends CrudRepository<UserSkill, Long> {

    UserSkill findByUserAndSkill(User user,Skill skill);
    UserSkill findByUser_IdAndSkill_IdAndLevel(long id, long skill_id, Level level);
    List<UserSkill> findByUser_IdAndSkill_Id(long id, long skill_id);
    List<UserSkill> findByUser_Id(long user_id);
    Iterable<UserSkill> findAllByUser(User user);
}
