package mvc.spring.service;

import java.util.List;

import mvc.spring.model.Question;

public interface QuestionService {
	Question increaseLikeCounter(int questionId);
	List<Question> findPopularQuestions(int eventId);
}
