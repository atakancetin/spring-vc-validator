package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.CodeQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CodeQuestionRepository extends CrudRepository<CodeQuestion, Long> {

}
