package mvc.spring.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import mvc.spring.dao.AbstractDao;
import mvc.spring.dao.EventAgendaDao;
import mvc.spring.model.EventAgenda;

@Repository("eventAgendaDao")
public class EventAgendaDaoImpl extends AbstractDao implements EventAgendaDao {

	public void saveAgenda(EventAgenda eventagenda) {
		getSession().merge(eventagenda);
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<EventAgenda> findAgendaFromEvent(int eventId) {
		Query query = getSession().createQuery("select e.agendaList from Event e where e.id = :id ");
        query.setParameter("id", eventId);
        return (List<EventAgenda>) query.list();
	}

	@SuppressWarnings("rawtypes")
	public void deleteAgenda(int agendaId) {
		Query query = getSession().createQuery("delete from EventAgenda where id = :id");
        query.setParameter("id", agendaId);
        query.executeUpdate();
	}

	@SuppressWarnings("rawtypes")
	public EventAgenda findAgendaById(int agendaId) {
		Query query = getSession().createQuery("from EventAgenda a where a.id = :id ");
        query.setParameter("id", agendaId);
        try{
        	return (EventAgenda) query.getSingleResult();
        }
        catch(NoResultException e){
        	return null;
        }
        
	}

}
