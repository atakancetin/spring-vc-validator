package com.cvvalidator.cvvalidator.controller;

import com.cvvalidator.cvvalidator.constants.EQuestionType;
import com.cvvalidator.cvvalidator.model.CodeQuestion;
import com.cvvalidator.cvvalidator.model.TestCase;
import com.cvvalidator.cvvalidator.repository.CodeQuestionRepository;
import com.cvvalidator.cvvalidator.repository.TestCaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/questions/code")
public class CodeQuestionController implements IRestController<CodeQuestion> {

    //region Init
    @Autowired
    private CodeQuestionRepository codeQuestionRepository;
    @Autowired
    private TestCaseRepository testCaseRepository;
    //endregion

    //region EndPoints
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Iterable<CodeQuestion>> get() {
        try {
            Iterable<CodeQuestion> codeQuestions = codeQuestionRepository.findAll();
            return ResponseEntity.ok().body(codeQuestions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<CodeQuestion> getById(long id) {
        try {
            Optional<CodeQuestion> optional = codeQuestionRepository.findById(id);
            if (optional.isPresent()) {
                return ResponseEntity.ok().body(optional.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<CodeQuestion> create(CodeQuestion newEntity) {
        try {
            newEntity.setQuestionType(EQuestionType.Code);
            CodeQuestion codeQuestion = codeQuestionRepository.save(newEntity);
            for(TestCase testCase :newEntity.getTestCases())
            {
                testCase.setCodeQuestion(codeQuestion);
            }
            Iterable<TestCase> testCaseList = testCaseRepository.saveAll(newEntity.getTestCases());
            codeQuestion.setTestCases((List<TestCase>) testCaseList);
            return ResponseEntity.created(new URI("")).body(codeQuestion);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity delete(long id) {
        try {
            Optional<CodeQuestion> optional = codeQuestionRepository.findById(id);
            if (optional.isPresent())
            {
                CodeQuestion codeQuestion = optional.get();
                testCaseRepository.deleteAll(codeQuestion.getTestCases());
                codeQuestionRepository.deleteById(codeQuestion.getId());
                return ResponseEntity.noContent().build();
            }
            else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<CodeQuestion> update(long id,CodeQuestion newEntity) {
        try{
            newEntity.setId(id);
            Optional<CodeQuestion> optional = codeQuestionRepository.findById(id);
            if (optional.isPresent())
            {
                for (TestCase testCase:newEntity.getTestCases()) {
                    testCase.setCodeQuestion(newEntity);
                }
                codeQuestionRepository.save(newEntity);
                return ResponseEntity.noContent().build();
            }
            else
            {
                newEntity = codeQuestionRepository.save(newEntity);
                return ResponseEntity.created(new URI("")).body(newEntity);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    //endregion
}
