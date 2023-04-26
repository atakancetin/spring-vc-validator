package com.cvvalidator.cvvalidator.scheduled;

import com.cvvalidator.cvvalidator.model.Test;
import com.cvvalidator.cvvalidator.repository.TestLogRepository;
import com.cvvalidator.cvvalidator.repository.TestRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Component
public class ScheduledTasks {

    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("HH:mm:ss");

    final TestRepository testRepository;
    final TestLogRepository testLogRepository;
    public ScheduledTasks(TestRepository testRepository, TestLogRepository testLogRepository) {
        this.testRepository = testRepository;
        this.testLogRepository = testLogRepository;
    }
    //TODO optimize to maximum efficiency (min sql query , min search etc.)
    @Scheduled(fixedRate = 60000)
    public void killExpiredTests() {
        List<Test> activeTests = testRepository.findAllByActive(true);
        System.out.println(String.format("------------------ Expired tests have been killing !!! "));
        for(Test activeTest : activeTests)
        {
            //LocalDateTime startTime = activeTest.getTestLogs().parallelStream().filter(y->y.getActionType() == EActionType.start).findFirst().orElse(null).getTimeStamp();
            long spentMinutes = ChronoUnit.MINUTES.between(activeTest.getStartDate(),LocalDateTime.now());
            //long spentMinutes = Duration.between(activeTest.getStartDate(),LocalDateTime.now()).toMinutes();
            System.out.println(String.format("------------------ Difference %d", spentMinutes));

            // Calculates test time, unit: minute
            long testTime = 0;
            testTime += activeTest.getTestTemplate().getTestTime().getHour() * 60;
            testTime += activeTest.getTestTemplate().getTestTime().getMinute();

            System.out.println(String.format("------------------ Test time  %d", testTime));

            // if expired kill it
            if(spentMinutes > testTime)
            {
                activeTest.setActive(false);
                activeTest.setSuccess(false);
                activeTest.setSuccessRate(0);
                //calculateSuccessOfTest(); TODO
                testRepository.save(activeTest);
            }
        }
    }
    private void calculateSuccessOfTest()
    {

    }
}
