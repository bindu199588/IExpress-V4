package mvc.spring.dao;

import java.util.List;

import mvc.spring.model.EventAgenda;

public interface EventAgendaDao {
	
	void saveAgenda(EventAgenda eventagenda);
	
	List<EventAgenda> findAgendaFromEvent(int eventId);
	
	void deleteAgenda(int agendaId);
	
	EventAgenda findAgendaById(int agendaId);
	
	
}
