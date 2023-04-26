package com.cvvalidator.cvvalidator.controller;

import com.cvvalidator.cvvalidator.model.Category;
import com.cvvalidator.cvvalidator.model.Skill;
import com.cvvalidator.cvvalidator.model.TestTemplate;
import com.cvvalidator.cvvalidator.model.TestTemplateCategory;
import com.cvvalidator.cvvalidator.repository.CategoryRepository;
import com.cvvalidator.cvvalidator.repository.SkillRepository;
import com.cvvalidator.cvvalidator.repository.TestTemplateCategoryRepository;
import com.cvvalidator.cvvalidator.repository.TestTemplateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path="/test-templates")
public class TestTemplateController implements IRestController<TestTemplate> {

    //region Init
    @Autowired
    private TestTemplateRepository testTemplateRepository;
    @Autowired
    private TestTemplateCategoryRepository testTemplateCategoryRepository;
    @Autowired
    private CategoryRepository categoryRepository;
    @Autowired
    private SkillRepository skillRepository;
    //endregion

    //region EndPoints
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public @ResponseBody @Override ResponseEntity<Iterable<TestTemplate>> get()
    {
        try {
            Iterable<TestTemplate> testTemplates = testTemplateRepository.findAll();
            return ResponseEntity.ok().body(testTemplates);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<TestTemplate> getById(long id)
    {
        try {
            Optional<TestTemplate> optional = testTemplateRepository.findById(id);
            if (optional.isPresent())
            {
                TestTemplate testTemplate = optional.get();
                testTemplate.setTime(testTemplate.getTestTime().getMinute());
                return ResponseEntity.ok().body(testTemplate);
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
    public ResponseEntity<TestTemplate> create(TestTemplate newTestTemplate)
    {
        try {
            Skill skill = skillRepository.findById(newTestTemplate.getSkill().getId()).get();
            newTestTemplate.setSkill(skill);
            newTestTemplate.setCreationDate(new Date());
            LocalTime localTime = LocalTime.MIN.plusMinutes(newTestTemplate.getTime());
            newTestTemplate.setTestTime(localTime);
            for(TestTemplateCategory category : newTestTemplate.getCategoryList())
            {
                Category category1 = categoryRepository.findById(category.getCategory().getId()).get();
                category.setTestTemplate(newTestTemplate);
                category.setCategory(category1);
            }
            newTestTemplate = testTemplateRepository.save(newTestTemplate);
            for(TestTemplateCategory category : newTestTemplate.getCategoryList())
            {
                category.setTestTemplate(newTestTemplate);
            }

            testTemplateCategoryRepository.saveAll(newTestTemplate.getCategoryList());
            return ResponseEntity.created(new URI("")).body(newTestTemplate);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity delete(@PathVariable long id)
    {
        try{
            Optional<TestTemplate> optional = testTemplateRepository.findById(id);
            if(optional.isPresent())
            {
                TestTemplate testTemplate = optional.get();
                List<TestTemplateCategory> testTemplateCategories = testTemplate.getCategoryList();
                if(testTemplateCategories.size() != 0)
                {
                    testTemplateCategoryRepository.deleteAll(testTemplateCategories);
                }
                testTemplateRepository.delete(testTemplate);
                return ResponseEntity.noContent().build();
            }
            else
            {
                return ResponseEntity.notFound().build();
            }
        }catch (Exception e)
        {
            return ResponseEntity.status(500).build();
        }

    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<TestTemplate> update(long id,TestTemplate newTestTemplate)
    {
        try{
            newTestTemplate.setId(id);
            Optional<TestTemplate> optional = testTemplateRepository.findById(id);
            if(optional.isPresent())
            {
                LocalTime time = newTestTemplate.getTestTime();
                time = time.minusMinutes(time.getMinute());
                newTestTemplate.setTestTime(time.plusMinutes(newTestTemplate.getTime()));
                testTemplateRepository.save(newTestTemplate);
                return ResponseEntity.noContent().build();
            }
            else
            {
                TestTemplate testTemplate = testTemplateRepository.save(newTestTemplate);
                return ResponseEntity.created(new URI("")).body(testTemplate);
            }
        }catch (Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    //endregion
}
