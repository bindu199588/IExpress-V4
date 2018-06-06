package mvc.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mvc.spring.dao.EventAgendaDao;
import mvc.spring.model.EventAgenda;
import mvc.spring.service.EventAgendaService;

@Service("eventAgendaService")
@Transactional
public class EventAgendaServiceImpl implements EventAgendaService{
	
	@Autowired
    private EventAgendaDao dao;
	@Override
	public void saveAgenda(EventAgenda eventagenda) {
		dao.saveAgenda(eventagenda);
	}

	@Override
	public List<EventAgenda> findAgendaFromEvent(int eventId) {
		return dao.findAgendaFromEvent(eventId);
	}

	@Override
	public void deleteAgenda(int agendaId) {
		dao.deleteAgenda(agendaId);
	}

	@Override
	public EventAgenda findAgendaById(int agendaId) {
		return dao.findAgendaById(agendaId);
	}

}
