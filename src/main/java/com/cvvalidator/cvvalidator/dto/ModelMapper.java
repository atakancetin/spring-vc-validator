package com.cvvalidator.cvvalidator.dto;

import com.cvvalidator.cvvalidator.constants.EQuestionType;
import com.cvvalidator.cvvalidator.model.CodeQuestion;
import com.cvvalidator.cvvalidator.model.MultipleChoiceQuestion;
import com.cvvalidator.cvvalidator.model.Test;
import com.cvvalidator.cvvalidator.model.TestQuestion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ModelMapper {

    public static TestDTO testToTestDTO(Test test)
    {
        TestDTO testDTO = new TestDTO();

        testDTO.setId(test.getId());
        testDTO.setSkill(test.getTestTemplate().getSkill().getName());
        testDTO.setSpentTime(test.getSpentTime());
        testDTO.setStartDate(test.getStartDate());
        testDTO.setTestTime(test.getTestTemplate().getTime());

        ArrayList<TestQuestionDTO> testQuestionDTOList = new ArrayList<>();
        List<TestQuestion> testQuestionList = test.getTestQuestions();
        for(TestQuestion testQuestion:testQuestionList)
        {
            if(testQuestion.getQuestion().getQuestionType() == EQuestionType.Code)
            {
                CodeQuestion codeQuestion = (CodeQuestion) testQuestion.getQuestion();

                CodeQuestionDTO codeQuestionDTO = new CodeQuestionDTO();
                codeQuestionDTO.setId(testQuestion.getId());
                codeQuestionDTO.setConstraints(codeQuestion.getConstraints());
                codeQuestionDTO.setInputFormat(codeQuestion.getInputFormat());
                codeQuestionDTO.setOutputFormat(codeQuestion.getOutputFormat());
                codeQuestionDTO.setQuestionTitle(codeQuestion.getQuestionTitle());
                codeQuestionDTO.setTask(codeQuestion.getTask());
                codeQuestionDTO.setTemplateCode(codeQuestion.getTemplateCode());
                codeQuestionDTO.setQuestionText(codeQuestion.getQuestionText());
                codeQuestionDTO.setQuestionType(codeQuestion.getQuestionType());
                testQuestionDTOList.add(codeQuestionDTO);
            }
            else
            {
                MultipleChoiceQuestion multipleChoiceQuestion = (MultipleChoiceQuestion) testQuestion.getQuestion();

                MultipleChoiceQuestionDTO multipleChoiceQuestionDTO = new MultipleChoiceQuestionDTO();
                multipleChoiceQuestionDTO.setId(testQuestion.getId());
                multipleChoiceQuestionDTO.setQuestionText(multipleChoiceQuestion.getQuestionText());
                multipleChoiceQuestionDTO.setQuestionType(multipleChoiceQuestion.getQuestionType());

                ArrayList<String> choices = new ArrayList();
                choices.add(multipleChoiceQuestion.getTrueChoice());
                choices.add(multipleChoiceQuestion.getChoice1());
                choices.add(multipleChoiceQuestion.getChoice2());
                choices.add(multipleChoiceQuestion.getChoice3());

                Collections.shuffle(choices);

                multipleChoiceQuestionDTO.setChoices(choices);
                testQuestionDTOList.add(multipleChoiceQuestionDTO);
            }
        }
        testDTO.setTestQuestions(testQuestionDTOList);
        return testDTO;

    }
}
