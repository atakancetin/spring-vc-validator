package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.MultipleChoiceQuestion;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MultipleChoiceQuestionRepository extends CrudRepository<MultipleChoiceQuestion, Long> {

}
