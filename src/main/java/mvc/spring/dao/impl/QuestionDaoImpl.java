package mvc.spring.dao.impl;

import java.util.List;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import mvc.spring.dao.AbstractDao;
import mvc.spring.dao.QuestionDao;
import mvc.spring.model.Admin;
import mvc.spring.model.Question;

@Repository("questionDao")
public class QuestionDaoImpl extends AbstractDao implements QuestionDao{

	@SuppressWarnings("rawtypes")
	@Override
	public Question increaseLikeCounter(int qId) {
		Query query = getSession().createQuery("update Question set likeCounter=likecounter+1 where id = :id");
		query.setParameter("id", qId);
		query.executeUpdate();
		
		Query query1 = getSession().createQuery("from Question q where q.id = :id ");
        query1.setParameter("id", qId);
        return (Question) query1.getSingleResult();
		
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Question> findPopularQuestions(int eventId) {
		Query query = getSession().createQuery("from Question q where q.event_id = :id order by q.likecounter desc ");
        query.setParameter("id", eventId);
        query.setMaxResults(10);
        return (List<Question>) query.list();
	}

}
