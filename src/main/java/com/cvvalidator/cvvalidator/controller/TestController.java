package com.cvvalidator.cvvalidator.controller;

import com.cvValidatorApi.APIHelper;
import com.cvValidatorApi.ProgrammingLanguage;
import com.cvValidatorApi.SourceCode;
import com.cvvalidator.cvvalidator.constants.EActionType;
import com.cvvalidator.cvvalidator.constants.EQuestionType;
import com.cvvalidator.cvvalidator.dto.ModelMapper;
import com.cvvalidator.cvvalidator.manager.TestLogger;
import com.cvvalidator.cvvalidator.model.*;
import com.cvvalidator.cvvalidator.repository.*;
import com.cvvalidator.cvvalidator.security.UserPrincipal;
import org.apache.commons.lang.StringEscapeUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.sql.Blob;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@RestController
@RequestMapping(path = "/tests")
public class TestController {

    //region Init
    @Autowired
    private TestTemplateRepository testTemplateRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private TestRepository testRepository;
    @Autowired
    private QuestionRepository questionRepository;
    @Autowired
    private MultipleChoiceQuestionRepository multipleChoiceQuestionRepository;
    @Autowired
    private CodeQuestionRepository codeQuestionRepository;
    @Autowired
    private AnswerRepository answerRepository;
    @Autowired
    private TestQuestionRepository testQuestionRepository;
    @Autowired
    private TestCaseRepository testCaseRepository;
    @Autowired
    private TestLogRepository testLogRepository;
    @Autowired
    private CertificateRepository certificateRepository;
    @Autowired
    private UserSkillRepository userSkillRepository;

    private TestLogger testLogger;

    final double thresholdRate = 0.7;

    // #endregion

    // region HelperMethods

    private List<Question> prepareQuestion(final TestTemplate testTemplate) {
        final List<Question> questions = new ArrayList<>();
        final List<TestTemplateCategory> categoryList = testTemplate.getCategoryList();
        for (final TestTemplateCategory category : categoryList) {
            final List<Question> questionList = questionRepository.findQuestion(category.getCategory().getId(),
                    testTemplate.getSkill().getId(), testTemplate.getLevel(), category.getQuestionType());
            questions.addAll(questionList.subList(0, category.getAmount()));
        }
        return questions;
    }

    private Iterable<TestQuestion> createTestQuestions(final List<Question> questions, final Test test) {
        final List<TestQuestion> testQuestions = new ArrayList<>();
        for (final Question question : questions) {
            final TestQuestion testQuestion = new TestQuestion();
            testQuestion.setQuestion(question);
            testQuestion.setTest(test);
            testQuestions.add(testQuestion);
        }
        return testQuestionRepository.saveAll(testQuestions);
    }

    private boolean testIsValid(final Test test) {
        // LocalTime testTime = test.getTestTemplate().getTestTime();
        // if(test.getEndDate() != null)
        // {
        // LocalDateTime dateTime = LocalDateTime.of(test.getStartDate().toLocalDate(),
        // testTime);
        // LocalDateTime finalTime = dateTime.plusMinutes(testTime.getMinute());
        // if(LocalDateTime.now().isAfter(finalTime))
        // {
        // return false;
        // }
        // }
        // else
        // {
        // return false;
        // }
        return true;
    }

    private boolean isTestSuccess(Test test, double thresholdRate) {
        //int threshold = (int) (test.getTestQuestions().size() * thresholdRate);
        int total = 0;
        List<TestQuestion> submittedQuestion = test.getTestQuestions().parallelStream()
                .filter(a -> a.getAnswer() != null).collect(Collectors.toList());
        total += submittedQuestion.parallelStream().mapToInt(x -> x.getAnswer().getScore()).sum();
        // for(TestQuestion testQuestion:test.getTestQuestions())
        // {
        // total += testQuestion.getAnswer().getScore();
        // }
        float divide = (float) total / test.getTestQuestions().size();
        float successRate = (divide * 100);
        int rate = (int) successRate;
        test.setSuccessRate(rate);
        return rate >= (int)(thresholdRate * 100);
    }

    private LocalTime longToLocalTime(long time) {

        final long hours = TimeUnit.SECONDS.toHours(time);
        time -= TimeUnit.HOURS.toSeconds(hours);

        final long minutes = TimeUnit.SECONDS.toMinutes(time);
        time -= TimeUnit.MINUTES.toSeconds(minutes);

        final long seconds = time;

        return LocalTime.of((int) hours, (int) minutes, (int) seconds);
    }
    // #endregion

    //region EndPoints

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("")
    public @ResponseBody
    ResponseEntity<Iterable<Test>> get() {
        try {
            Iterable<Test> tests = testRepository.findAll();
            return ResponseEntity.ok().body(tests);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/logs")
    public @ResponseBody
    ResponseEntity<List<TestLog>> getDetails(@PathVariable final long id) {
        try {
            List<TestLog> logs = testLogRepository.findByTest_Id(id);
            return ResponseEntity.ok().body(logs);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("")
    public @ResponseBody
    ResponseEntity createTest(@RequestBody UserSkill userSkill,
                                    @AuthenticationPrincipal final UserPrincipal u) {
        try {
            testLogger = new TestLogger(testLogRepository);

            final User user = userRepository.findById(u.getId()).get();

            //Old test handler
            Test oldTest = testRepository.findByUser_IdAndActive(user.getId(),true);
            if(oldTest != null)
            {
                oldTest.setSuccess(false);
                oldTest.setSuccessRate(0);
                oldTest.setActive(false);
                testRepository.save(oldTest);
                testLogger.Add(EActionType.violation,"New test created while the user already has a test.",oldTest);
            }

            final TestTemplate testTemplate = testTemplateRepository
                    .findBySkill_IdAndLevel(userSkill.getSkill().getId(), userSkill.getLevel());

            final List<Question> questions = prepareQuestion(testTemplate);

            Test test = new Test();
            test.setTestTemplate(testTemplate);
            test.setUser(user);
            test.setSuccessRate(0);
            test.setSpentTime(LocalTime.of(0, 0, 0));
            test.setActive(true);
            test = testRepository.save(test);

            final List<TestQuestion> testQuestions = (List<TestQuestion>) createTestQuestions(questions, test);
            test.setTestQuestions(testQuestions);

            return ResponseEntity.ok().build();
        } catch (final Exception ex) {
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/me")
    public @ResponseBody
    ResponseEntity getTest(@AuthenticationPrincipal final UserPrincipal u) {
        try {
            Test test = testRepository.findByUser_IdAndActive(u.getId(),true);
            if(test != null)
            {
                if(test.getStartDate() == null)
                {
                    test.setStartDate(LocalDateTime.now());
                    testLogger.Add(EActionType.start, test);

                    long mil = test.getTestTemplate().getTestTime().getMinute() * 60000;
                    test.getTestTemplate().setTime(mil);
                    return ResponseEntity.ok().body(ModelMapper.testToTestDTO(test));
                }
                // accessed two or more times.
                else
                {
                    LocalDateTime dateTime = test.getStartDate();
                    long difference = Duration.between(dateTime,LocalDateTime.now()).toMillis();
                    long mil = test.getTestTemplate().getTestTime().getMinute() * 60000;
                    long newTime = mil-difference;
                    // available
                    if(newTime > 0)
                    {
                        test.getTestTemplate().setTime(newTime);
                        return ResponseEntity.ok().body(ModelMapper.testToTestDTO(test));
                    }
                    // expired
                    else
                    {
                        test.setActive(false);
                        testRepository.save(test);
                        return ResponseEntity.status(404).body("test expired");
                    }
                }

            }
                return ResponseEntity.notFound().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @GetMapping("/summary/{id}")
    public ResponseEntity summary(@PathVariable final long id) {
        try {
            Optional<Test> optional = testRepository.findById(id);
            if (optional.isPresent()) {
                Test test = optional.get();
                TestTemplate testTemplate = test.getTestTemplate();
                TestResult testResult = new TestResult();
                testResult.setSkill(testTemplate.getSkill());
                testResult.setLevel(testTemplate.getLevel());
                testResult.setSuccess(test.isSuccess());
                testResult.setSuccessRate(test.getSuccessRate());
                testResult.setSuccessBoundary((int) (thresholdRate * 100));
                testResult.setSpentTime(test.getSpentTime().toString());
                testResult.setTestTime(testTemplate.getTestTime().toString());
                return ResponseEntity.ok().body(testResult);
            }
            return ResponseEntity.badRequest().build();
        } catch (final Exception exp) {
            return ResponseEntity.status(500).build();
        }

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/complete")
    public ResponseEntity completeTest(@RequestBody Test test, @AuthenticationPrincipal UserPrincipal u) {
        try {
            testLogger = new TestLogger(testLogRepository);
            Optional<Test> optionalTest = testRepository.findById(test.getId());
            if (optionalTest.isPresent()) {
                Test dbTest = optionalTest.get();
                //LocalDateTime startTime = testLogger.getStartTime(test);
                //long difference = Duration.between(startTime, LocalDateTime.now()).toSeconds();
                long difference = ChronoUnit.SECONDS.between(dbTest.getStartDate(),LocalDateTime.now());
                dbTest.setSpentTime(longToLocalTime(difference));
                dbTest.setActive(false);
                dbTest.setSuccess(isTestSuccess(dbTest, thresholdRate));

                // create certificate

                if (dbTest.isSuccess()) {
                    UserSkill userSkill =
                            userSkillRepository.findByUser_IdAndSkill_IdAndLevel(u.getId(),
                                    dbTest.getTestTemplate().getSkill().getId(),dbTest.getTestTemplate().getLevel());
                    Certificate certificate = new Certificate(userSkill);
                    userSkill.setApproved(true);
                    userSkillRepository.save(userSkill);
                    certificateRepository.save(certificate);
                }

                dbTest = testRepository.save(dbTest);
                testLogger.Add(EActionType.complete_test, dbTest);

                return ResponseEntity.ok().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (final Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/video")
    public ResponseEntity saveQuizVideo(@RequestParam final MultipartFile video) {
        try {
            String[] fileName = video.getOriginalFilename().split("\\.");
            Long id = Long.parseLong(fileName[0]);
            Test test = testRepository.findById(id).get();

            byte[] bytes = video.getBytes();
            Blob blob = new javax.sql.rowset.serial.SerialBlob(bytes);
            test.setVideo(blob);
            testRepository.save(test);
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @GetMapping("/{id}/video")
    public ResponseEntity<Resource> getVideo(@PathVariable final long id) {
        try {
            Optional<Test> optionalTest = testRepository.findById(id);
            if (optionalTest.isPresent()) {
                Blob quizVideoBlob = testRepository.findById(id).get().getVideo();
                int length = (int) quizVideoBlob.length();
                return ResponseEntity.ok().contentType(MediaType.parseMediaType("video/webm"))
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + "video.webm" + "\"")
                        .body(new ByteArrayResource(quizVideoBlob.getBytes(1, length)));
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/question/complete")
    public ResponseEntity submitQuestion(@RequestBody Answer answer) {
        try {
            testLogger = new TestLogger(testLogRepository);
            TestQuestion testQuestion = null;
            Optional<TestQuestion> optional = testQuestionRepository.findById(answer.getTestQuestion().getId());
            if (optional.isPresent()) {
                testQuestion = optional.get();
                int result;
                if (testQuestion.getQuestion().getQuestionType() == EQuestionType.Code) {
                    Skill skill = testQuestion.getTest().getTestTemplate().getSkill();
                    List<TestCaseResult> results = runForAllTestCases(answer,
                            ((CodeQuestion) testQuestion.getQuestion()).getTestCases(), skill.getName());
                    result = evaluateCodeQuestion(results);
                } else {
                    result = evaluateMultipleChoiceQuestion(answer.getAnswerText(),
                            ((MultipleChoiceQuestion) testQuestion.getQuestion()).getTrueChoice());
                }
                //new answer
                Answer newAnswer = testQuestion.getAnswer();
                if(testQuestion.getAnswer() == null)
                {
                    newAnswer = new Answer();
                    newAnswer.setAnswerText(answer.getAnswerText());
                    newAnswer.setTestQuestion(testQuestion);
                    newAnswer.setScore(result);

                    Answer a = answerRepository.save(newAnswer);
                    testQuestion.setAnswer(a);

                    testQuestionRepository.save(testQuestion);
                }
                //if answer saved before
                else
                {
                    newAnswer.setAnswerText(answer.getAnswerText());
                    newAnswer.setScore(result);
                    answerRepository.save(newAnswer);
                }

                String data = "question id : " + testQuestion.getQuestion().getId()+" ; "+testQuestion.getAnswer().getAnswerText() +" score : "+answer.getScore();
                testLogger.Add(EActionType.complete_question, data, testQuestion.getTest());
                return ResponseEntity.noContent().build();
            } else {
                return ResponseEntity.badRequest().build();
            }
        } catch (final Exception exception) {
            // TODO must be logged
            return ResponseEntity.status(500).build();
        }

    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/question/run")
    public ResponseEntity<List<TestCaseResult>> runCode(@RequestBody final Answer answer, @AuthenticationPrincipal final UserPrincipal u) {
        try {
            testLogger = new TestLogger(testLogRepository);
            final TestQuestion testQuestion = testQuestionRepository.findById(answer.getTestQuestion().getId()).get();
            final TestTemplate testTemplate = testQuestion.getTest().getTestTemplate();
            final CodeQuestion codeQuestion = (CodeQuestion) testQuestion.getQuestion();
            final List<TestCaseResult> results = runForAllTestCases(answer, codeQuestion.getTestCases(),
                    testTemplate.getSkill().getName());

            final JSONArray resultsJson = new JSONArray(results);
            testLogger.Add(EActionType.run, "question id :"+codeQuestion.getId()+", "+ resultsJson.toString(), testQuestion.getTest());

            return ResponseEntity.ok().body(results);
        } catch (final Exception exception) {
            return ResponseEntity.status(500).build();
        }
    }

    @PreAuthorize("hasRole('ROLE_USER') or hasRole('ROLE_ADMIN')")
    @PostMapping("/violation")
    public ResponseEntity violation(@AuthenticationPrincipal final UserPrincipal u) {
        try {
            testLogger = new TestLogger(testLogRepository);
            Test test = testRepository.findByUser_IdAndActive(u.getId(), true);
            if (test != null) {
                testLogger.Add(EActionType.violation, test);
                test.setActive(false);
                test.setSuccess(false);
                test.setSuccessRate(0);

                LocalDateTime startTime = testLogger.getStartTime(test);
                long difference = ChronoUnit.SECONDS.between(startTime,LocalDateTime.now());
                //long difference = Duration.between(startTime, LocalDateTime.now()).toSeconds();
                test.setSpentTime(longToLocalTime(difference));

                testRepository.save(test);

                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.notFound().build();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).build();
        }
    }
    //endregion

    //region CodeCompileHelperMethods
    private List<TestCaseResult> runForAllTestCases(final Answer answer, final List<TestCase> testCases,
                                                    final String pl) {
        final List<TestCaseResult> results = new ArrayList<>();
        for (final TestCase testCase : testCases) {
            final JSONObject response = run(answer.getAnswerText(), pl.toLowerCase(), testCase.getInput());
            TestCaseResult result;
            if (response.getInt("statusCode") == 200) {
                String output = testCase.getOutput();
                String apiOutput = response.getString("output");
                String apiOutputWithEscapeString = apiOutput.replace("\n","\\n");

                if (output.equals(apiOutputWithEscapeString))
                    result = new TestCaseResult(true, "");
                else
                    result = new TestCaseResult(false, "");
            } else {
                final String apiOutput = response.getString("output");
                result = new TestCaseResult(false, apiOutput);
            }

            results.add(result);
        }
        return results;
    }

    private JSONObject run(final String code, final String pl, final String input) {

        final ArrayList<String> tokens = new ArrayList<String>();
        String escapedStr = null;

        final APIHelper apiManager = APIHelper.getInstance();

        escapedStr = StringEscapeUtils.escapeJava(code);
        tokens.add(escapedStr);

        final ProgrammingLanguage language = new ProgrammingLanguage(pl, "0");
        final SourceCode sourceCode = new SourceCode(tokens, language);

        final JSONObject response = apiManager.request(sourceCode, input);

        return response;
    }

    private int evaluateCodeQuestion(final List<TestCaseResult> results) {
        if (results.parallelStream().anyMatch(x -> !x.isResult())) {
            return 0;
        }
        return 1;
    }

    private int evaluateMultipleChoiceQuestion(final String answer, final String trueAnswer) {
        if (answer.equals(trueAnswer))
            return 1;
        return 0;
    }
    //endregion


}
