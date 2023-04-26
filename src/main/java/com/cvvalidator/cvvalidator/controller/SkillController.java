package com.cvvalidator.cvvalidator.controller;

import com.cvvalidator.cvvalidator.constants.Level;
import com.cvvalidator.cvvalidator.model.Skill;
import com.cvvalidator.cvvalidator.model.TestTemplate;
import com.cvvalidator.cvvalidator.model.UserSkill;
import com.cvvalidator.cvvalidator.repository.SkillRepository;
import com.cvvalidator.cvvalidator.repository.TestTemplateRepository;
import com.cvvalidator.cvvalidator.repository.UserSkillRepository;
import com.cvvalidator.cvvalidator.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/skills")
public class SkillController implements IRestController<Skill> {

    //region Init
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private TestTemplateRepository testTemplateRepository;
    @Autowired
    private UserSkillRepository userSkillRepository;
    //endregion

    //region Skill
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public @ResponseBody
    ResponseEntity<Iterable<Skill>> get() {
        try {
            Iterable<Skill> skillList = skillRepository.findAll();
            return ResponseEntity.ok().body(skillList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Skill> getById(@PathVariable long id) {
        try {
            Optional<Skill> skill = skillRepository.findById(id);
            if (skill.isPresent()) {
                return ResponseEntity.ok().body(skill.get());
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Skill> create(Skill newSkill) {
        try {
            newSkill = skillRepository.save(newSkill);
            return ResponseEntity.created(new URI("")).body(newSkill);
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity delete(@PathVariable long id) {
        try {
            Optional<Skill> optional = skillRepository.findById(id);
            if (optional.isPresent()) {
                skillRepository.deleteById(id);
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity<Skill> update(long id, Skill newSkill) {
        try {
            newSkill.setId(id);
            Optional<Skill> optional = skillRepository.findById(id);
            if (optional.isPresent()) {
                skillRepository.save(newSkill);
                return ResponseEntity.noContent().build();
            } else {
                Skill skill = skillRepository.save(newSkill);
                return ResponseEntity.created(new URI("")).body(skill);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }
    //endregion

    //region Skill-UserSide
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/testable")
    public ResponseEntity<List<Skill>> getTestableSkills(@AuthenticationPrincipal UserPrincipal u) {
        try {
            List<TestTemplate> testTemplateList = (List<TestTemplate>) testTemplateRepository.findAll();

            List<UserSkill> userSkillList = userSkillRepository.findByUser_Id(u.getId());
            List<TestTemplate> deleteList = new ArrayList<>();
            for (UserSkill userSkill : userSkillList) {
                for (TestTemplate testTemplate : testTemplateList) {
                    if (testTemplate.getSkill().getId() == userSkill.getSkill().getId() && testTemplate.getLevel() == userSkill.getLevel())
                        deleteList.add(testTemplate);
                }
            }
            testTemplateList.removeAll(deleteList);

            List<Skill> skillList = testTemplateList.parallelStream().
                    map(TestTemplate::getSkill).distinct().
                    collect(Collectors.toList());

            return ResponseEntity.ok().body(skillList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/testable/{id}")
    public ResponseEntity<List<Level>> getTestableSkillLevels(@PathVariable long id, @AuthenticationPrincipal UserPrincipal u) {
        try {
            List<TestTemplate> testTemplateList = testTemplateRepository.findBySkill_Id(id);
            List<UserSkill> userSkillList = userSkillRepository.findByUser_IdAndSkill_Id(u.getId(), id);

            List<TestTemplate> deleteList = new ArrayList<>();

            for (UserSkill userSkill : userSkillList) {
                for (TestTemplate testTemplate : testTemplateList) {
                    if (testTemplate.getLevel() == userSkill.getLevel())
                        deleteList.add(testTemplate);
                }
            }
            testTemplateList.removeAll(deleteList);

            List<Level> levelList = testTemplateList.parallelStream().
                    map(TestTemplate::getLevel).
                    collect(Collectors.toList());

            return ResponseEntity.ok().body(levelList);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    //endregion
}
