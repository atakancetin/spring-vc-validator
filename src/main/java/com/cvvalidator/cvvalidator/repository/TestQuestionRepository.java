package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.*;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TestQuestionRepository extends CrudRepository<TestQuestion,Long> {
    TestQuestion findByTestAndQuestion(Test test, Question question);
}
