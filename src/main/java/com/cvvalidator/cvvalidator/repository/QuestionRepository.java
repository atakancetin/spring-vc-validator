package com.cvvalidator.cvvalidator.repository;

import com.cvvalidator.cvvalidator.model.Category;
import com.cvvalidator.cvvalidator.constants.EQuestionType;
import com.cvvalidator.cvvalidator.constants.Level;
import com.cvvalidator.cvvalidator.model.Question;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface QuestionRepository extends CrudRepository<Question,Long> {

    //TODO Must be refactor
    @Query(value="SELECT q FROM Question q  WHERE q.category.id =:categoryId " +
            "and q.skill.id = :skillId and q.level =:level and q.questionType =:questionType" +
            " order by function('RAND')")
    List<Question> findQuestion(@Param("categoryId") long categoryId,
                                @Param("skillId") long skillId,@Param("level") Level level,
                                @Param("questionType") EQuestionType questionType);

    List<Question> findByCategoryAndQuestionType(Category category,EQuestionType type);
}
