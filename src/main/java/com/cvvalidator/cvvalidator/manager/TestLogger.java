package com.cvvalidator.cvvalidator.manager;

import com.cvvalidator.cvvalidator.constants.EActionType;
import com.cvvalidator.cvvalidator.model.Test;
import com.cvvalidator.cvvalidator.model.TestLog;
import com.cvvalidator.cvvalidator.repository.TestLogRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class TestLogger
{
    TestLogRepository testLogRepository;

    public TestLogger(TestLogRepository testLogRepository) {
        this.testLogRepository = testLogRepository;
    }

    public void Add(EActionType type, String data, Test test) {
        TestLog testLog = new TestLog(type, data, test);
        testLog.setTimeStamp(LocalDateTime.now());
        testLogRepository.save(testLog);
    }

    public void Add(EActionType type, Test test) {
        TestLog testLog = new TestLog(type, test);
        testLog.setTimeStamp(LocalDateTime.now());
        testLogRepository.save(testLog);
    }

    public LocalDateTime getStartTime(Test test) {
        TestLog testLog = testLogRepository.findByTestAndActionType(test, EActionType.start);
        return testLog.getTimeStamp();
    }

}
