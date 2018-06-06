package mvc.spring.dao;

import java.util.List;

import mvc.spring.model.Question;

public interface QuestionDao {
	Question increaseLikeCounter(int questionId);
	List<Question> findPopularQuestions(int eventId);
}
