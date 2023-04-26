package com.cvvalidator.cvvalidator.controller;

import com.cvvalidator.cvvalidator.constants.EQuestionType;
import com.cvvalidator.cvvalidator.model.MultipleChoiceQuestion;
import com.cvvalidator.cvvalidator.repository.MultipleChoiceQuestionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Optional;

@RestController
@RequestMapping(path="/questions/multiple-choice")
public class MultipleChoiceQuestionController implements IRestController<MultipleChoiceQuestion> {

    //region Init
    @Autowired
    private MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;
    //endregion

    //region EndPoints
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<Iterable<MultipleChoiceQuestion>> get() {
        try {
            Iterable<MultipleChoiceQuestion> multipleChoiceQuestions = multipleChoiceQuestionRepository.findAll();
            return ResponseEntity.ok().body(multipleChoiceQuestions);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<MultipleChoiceQuestion> getById(long id) {
        try {
            Optional<MultipleChoiceQuestion> optional = multipleChoiceQuestionRepository.findById(id);
            if (optional.isPresent())
            {
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
    public ResponseEntity<MultipleChoiceQuestion> create(MultipleChoiceQuestion newEntity) {
        try {
            newEntity.setQuestionType(EQuestionType.MultipleChoice);
            MultipleChoiceQuestion entity = multipleChoiceQuestionRepository.save(newEntity);
            return ResponseEntity.created(new URI("")).body(entity);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity delete(long id) {
        try
        {
            Optional<MultipleChoiceQuestion> optional = multipleChoiceQuestionRepository.findById(id);
            if (optional.isPresent())
            {
                multipleChoiceQuestionRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            }
            else
            {
                return ResponseEntity.notFound().build();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<MultipleChoiceQuestion> update(long id ,MultipleChoiceQuestion newEntity) {
        try{
            newEntity.setId(id);
            Optional<MultipleChoiceQuestion> optional = multipleChoiceQuestionRepository.findById(id);
            if (optional.isPresent())
            {
                multipleChoiceQuestionRepository.save(newEntity);
                return ResponseEntity.noContent().build();
            }
            else
            {
                newEntity = multipleChoiceQuestionRepository.save(newEntity);
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
