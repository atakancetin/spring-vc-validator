package com.cvvalidator.cvvalidator.controller;

import com.cvvalidator.cvvalidator.constants.RoleName;
import com.cvvalidator.cvvalidator.manager.ApiAuthenticationManager;
import com.cvvalidator.cvvalidator.model.*;
import com.cvvalidator.cvvalidator.payload.LoginRequest;
import com.cvvalidator.cvvalidator.repository.*;
import com.cvvalidator.cvvalidator.security.JwtTokenProvider;
import com.cvvalidator.cvvalidator.security.UserPrincipal;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.net.URI;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(path = "/users")
public class UserController implements IRestController<User> {

    // region Init
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ApiAuthenticationManager apiAuthenticationManager;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private SkillRepository skillRepository;
    @Autowired
    private UserSkillRepository userSkillRepository;
    @Autowired
    private JwtTokenProvider tokenProvider;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private ExperienceRepository experienceRepository;
    @Autowired
    private UniversityRepository universityRepository;
    @Autowired
    private TestRepository testRepository;
    // endregion

    // region Login
    @Override
    @PostMapping("/sign-up")
    public ResponseEntity<?> create(@Valid User newEntity) {
        try {
            Profile profile = new Profile();
            profile.setAbout("Fresh blood :)");
            profileRepository.save(profile);

            Role userRole = roleRepository.findByName(RoleName.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("StatusMessages.UserRoleNotSet"));

            newEntity.setRoles(Collections.singleton(userRole));
            newEntity.setPassword(passwordEncoder.encode(newEntity.getPassword()));
            Date recordDate = new Date();

            newEntity.setRecordDate(recordDate);
            newEntity.setProfile(profile);
            User user = userRepository.save(newEntity);

            return ResponseEntity.ok("User has registered successfully.");
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }
    }

    @PostMapping("/sign-in")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        try {
            Authentication authentication = apiAuthenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            String jwt = tokenProvider.JwtGenerateToken(authentication);
            return ResponseEntity.ok(jwt);
        }catch(Exception e)
        {
            e.printStackTrace();
            return ResponseEntity.status(500).body(e.getMessage());
        }

    }
    // endregion

    // region Admin-User
    @Override
    @PostMapping
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public ResponseEntity delete(long id) {
        try {
            Optional<User> optional = userRepository.findById(id);
            if(optional.isPresent())
            {
                userRepository.deleteById(id);
                return ResponseEntity.ok().build();
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
    public ResponseEntity<Iterable<User>> get() {
        try {
            Iterable<User> users = userRepository.findAll();
            return ResponseEntity.ok().body(users);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @Override
    public ResponseEntity<User> getById(long id) {
        try {
            Optional<User> optional = userRepository.findById(id);
            if(optional.isPresent())
            {
                return ResponseEntity.ok().body(optional.get());
            }
            else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @Override
    public ResponseEntity<User> update(long id, User newEntity) {
        return ResponseEntity.notFound().build();
    }
    // endregion

    // region Profile
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/me")
    public ResponseEntity<Optional<User>> getAuthenticated(@AuthenticationPrincipal UserPrincipal u) {
        try {
            Optional<User> user = userRepository.findById(u.getId());
            return ResponseEntity.ok().body(user);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    @PreAuthorize("hasRole('ADMIN') or hasRole('USER')")
    @PutMapping(value="/me", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<User> updateAuthenticated(@RequestPart(name = "file", required = false) MultipartFile file,
                                                    User newUser,@AuthenticationPrincipal UserPrincipal u) {
        try {
            //User
            User user = userRepository.findById(u.getId()).get();
            user.setFirstName(newUser.getFirstName());
            user.setLastName(newUser.getLastName());
            // user.setPassword(newUser.getPassword());
            user.setEmail(newUser.getEmail());

            //Profile update
            Profile profile = user.getProfile();
            if(file != null)
            {
                profile.setImage(file.getBytes());
            }
            profile.setAbout(newUser.getProfile().getAbout());
            profile.setCity(newUser.getProfile().getCity());
            profile.setCountry(newUser.getProfile().getCountry());
            profile.setLinkedinUrl(newUser.getProfile().getLinkedinUrl());
            profile.setGithubUrl(newUser.getProfile().getGithubUrl());

            profileRepository.save(profile);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    //endregion

    //region University
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/me/university")
    public @ResponseBody ResponseEntity getUniversities(@NonNull @AuthenticationPrincipal UserPrincipal u)
    {
        try
        {
            User user = userRepository.findById(u.getId()).get();
            return ResponseEntity.ok(user.getProfile().getUniversityList());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).build();
        }

    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/me/university")
    public @ResponseBody ResponseEntity addUniversity(@RequestBody University university,@AuthenticationPrincipal UserPrincipal u)
    {
        try
        {
            User user = userRepository.findById(u.getId()).get();
            university.setProfile(user.getProfile());
            university = universityRepository.save(university);
            return ResponseEntity.created(new URI("")).body(university);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).build();
        }

    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/me/university/{id}")
    public ResponseEntity deleteUniversity(@PathVariable long id,@AuthenticationPrincipal UserPrincipal u)
    {
        try
        {
            User user = userRepository.findById(u.getId()).get();
            University university = universityRepository.findByIdAndProfile(id,user.getProfile());
            universityRepository.delete(university);
            return ResponseEntity.noContent().build();
        }catch (Exception e)
        {
            return ResponseEntity.status(500).build();
        }

    }

    //endregion

    //region Experience
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/me/experience")
    public @ResponseBody ResponseEntity getExperiences(@NonNull @AuthenticationPrincipal UserPrincipal u)
    {
        try
        {
            User user = userRepository.findById(u.getId()).get();
            return ResponseEntity.ok(user.getProfile().getExperienceList());
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).build();
        }

    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/me/experience")
    public @ResponseBody ResponseEntity addExperience(@NonNull @RequestBody Experience experience, @NonNull @AuthenticationPrincipal UserPrincipal u)
    {
        try
        {
            User user = userRepository.findById(u.getId()).get();
            experience.setProfile(user.getProfile());
            experience = experienceRepository.save(experience);
            return ResponseEntity.created(new URI("")).body(experience);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).build();
        }

    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/me/experience/{id}")
    public ResponseEntity deleteExperience(@PathVariable long id,@AuthenticationPrincipal UserPrincipal u)
    {
        try
        {
            User user = userRepository.findById(u.getId()).get();
            experienceRepository.deleteById(id);
            return ResponseEntity.noContent().build();
        }catch (Exception e)
        {
            return ResponseEntity.status(500).build();
        }

    }
    //endregion

    //region Skill
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/me/skills")
    public @ResponseBody ResponseEntity addSkillToUser(@RequestBody UserSkill userSkill,@AuthenticationPrincipal UserPrincipal u)
    {
        try
        {
            User user = userRepository.findById(u.getId()).get();
            if(userSkillRepository.findByUser_IdAndSkill_IdAndLevel(u.getId(),userSkill.getSkill().getId(),userSkill.getLevel()) != null)
            {
                return ResponseEntity.status(404).body("This skill already exists");
            }
            Skill skillNew = skillRepository.findById(userSkill.getSkill().getId()).get();
            userSkill.setSkill(skillNew);
            userSkill.setUser(user);
            UserSkill userSkill1 = userSkillRepository.save(userSkill);
            return ResponseEntity.created(new URI("")).body(userSkill1);
        }
        catch (Exception e)
        {
            return ResponseEntity.status(500).build();
        }

    }
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @DeleteMapping("/me/skills/{id}")
    public ResponseEntity deleteSkillFromUser(@PathVariable long id,@AuthenticationPrincipal UserPrincipal u)
    {
        try
        {
            Optional<UserSkill> userSkill = userSkillRepository.findById(id);
            if(userSkill.isPresent())
            {
                if(userSkill.get().getUser().getId() == u.getId())
                {
                    userSkillRepository.deleteById(id);
                    return ResponseEntity.noContent().build();
                }
                return ResponseEntity.status(403).build();
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
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/me/skills")
    public ResponseEntity<List<UserSkill>> getMySkills(@AuthenticationPrincipal UserPrincipal u) {
        try {
            User user = userRepository.findById(u.getId()).get();
            return ResponseEntity.ok().body(user.getSkillList());
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    //endregion

    //region Test
    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/me/tests")
    public ResponseEntity<List<Test>> getMyTests(@AuthenticationPrincipal UserPrincipal u) {
        try {
            List<Test> tests = testRepository.findAllByUser_Id(u.getId());
            return ResponseEntity.ok().body(tests);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    //endregion

}
