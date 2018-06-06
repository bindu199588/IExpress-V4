package mvc.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mvc.spring.dao.QuestionDao;
import mvc.spring.model.Question;
import mvc.spring.service.QuestionService;

@Service("questionService")
@Transactional
public class QuestionServiceImpl implements QuestionService {

	@Autowired 
	private QuestionDao dao;
	
	@Override
	public Question increaseLikeCounter(int questionId) {
		return dao.increaseLikeCounter(questionId);
	}

	@Override
	public List<Question> findPopularQuestions(int eventId) {
		return dao.findPopularQuestions(eventId);
	}

}
