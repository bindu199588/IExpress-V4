package mvc.spring.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import mvc.spring.dao.AbstractDao;
import mvc.spring.dao.EventDao;
import mvc.spring.model.Event;

@Repository("eventDao")
public class EventDaoImpl extends AbstractDao implements EventDao{

	public void saveEvent(Event event) {
		getSession().merge(event);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Event> findAllEvent() {
		Query query = getSession().createQuery("from Event");        
        return (List<Event>) query.list();
	}

	@SuppressWarnings({ "rawtypes" })
	public void deleteEventById(int id) {
		Query query = getSession().createQuery("delete from Event where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
	}

	@SuppressWarnings({ "rawtypes" })
	public Event findById(int id) {
		Query query = getSession().createQuery("from Event a where a.id = :id ");
        query.setParameter("id", id);
        try{
        	return (Event) query.getSingleResult();        	
        }
        catch(NoResultException E){
        	return null;
        }
	}

	@Override
	public void updateEvent(Event event) {
		getSession().update(event);
	}
	
	public void mergeEvent(Event event) {
		getSession().merge(event);
	}

	@Override
	public String getNewAccessCode() {
		Query<?> query = getSession().createNativeQuery("select access_code()");
		try {
			return (String) query.getSingleResult();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
	}

}
