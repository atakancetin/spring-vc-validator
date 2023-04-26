package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.Answer;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AnswerRepository extends CrudRepository<Answer, Long> {
}
