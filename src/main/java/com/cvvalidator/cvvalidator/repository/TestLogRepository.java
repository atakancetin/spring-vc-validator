package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.constants.EActionType;
import com.cvvalidator.cvvalidator.model.Test;
import com.cvvalidator.cvvalidator.model.TestLog;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestLogRepository extends CrudRepository<TestLog, Long> {

    TestLog findByTestAndActionType(Test test, EActionType eActionType);
    List<TestLog> findByTest_Id(long id);
}
