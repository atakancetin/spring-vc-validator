package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.Test;
import com.cvvalidator.cvvalidator.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TestRepository extends CrudRepository<Test,Long> {

    Test findByUserAndActive(User user,boolean active);

    Test findByUser_IdAndActive(long user_id,boolean active);

    List<Test> findAllByActive(boolean active);

    int countAllByActive(boolean active);

    int countAllBySuccess(boolean success);

    @Query(value = "SELECT skill.name FROM test test , test_template template , skill WHERE test.template_id = template.id " +
            "AND template.skill_id = skill.id\n" +
            "AND test.active = FALSE GROUP BY test.template_id ORDER BY COUNT(test.template_id) DESC LIMIT 1",
            nativeQuery = true)
    String findMostTestedSkill();
    @Query(value = "SELECT skill.name FROM test test , test_template template , skill WHERE test.template_id = template.id " +
            "AND template.skill_id = skill.id\n" +
            "AND test.success = TRUE GROUP BY test.template_id ORDER BY COUNT(test.template_id) DESC LIMIT 1",
            nativeQuery = true)
    String findMostVerifiedSkill();

    List<Test> findAllByUser_Id(long user_id);
    Test findByUser_Id(long user_id);
}
