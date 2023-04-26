package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.TestCase;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface TestCaseRepository extends CrudRepository<TestCase, Long> {

    List<TestCase> findAllByCodeQuestion_Id(long id);
}
