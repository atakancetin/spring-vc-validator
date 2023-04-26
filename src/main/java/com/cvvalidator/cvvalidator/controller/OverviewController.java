package com.cvvalidator.cvvalidator.controller;

import com.cvvalidator.cvvalidator.dto.OverviewDTO;
import com.cvvalidator.cvvalidator.repository.TestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.math.RoundingMode;

@RestController
@RequestMapping(path = "/overview")
public class OverviewController {

    @Autowired
    TestRepository testRepository;

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public @ResponseBody ResponseEntity<OverviewDTO> get()
    {
        try {
            OverviewDTO overviewDTO = new OverviewDTO();
            overviewDTO.setCompletedTest(testRepository.countAllByActive(false));
            overviewDTO.setActiveTest(testRepository.countAllByActive(true));
            overviewDTO.setMostTestedSkill(testRepository.findMostTestedSkill());
            overviewDTO.setMostVerifiedSkill(testRepository.findMostVerifiedSkill());
            double rate = calculateSuccessRate(overviewDTO.getCompletedTest(),testRepository.countAllBySuccess(true));
            overviewDTO.setSuccessRate(rate);
            return ResponseEntity.ok(overviewDTO);
        }catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    private double calculateSuccessRate(long completed,long success)
    {
        if(completed != 0)
        {
            double rate = ((double)success / (double)completed * 100);
            BigDecimal bd = new BigDecimal(rate).setScale(2, RoundingMode.HALF_UP);
            return bd.doubleValue();
        }
        return 0;
    }
}
