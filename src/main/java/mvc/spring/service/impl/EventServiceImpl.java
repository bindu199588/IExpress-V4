package mvc.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mvc.spring.dao.EventDao;
import mvc.spring.model.Event;
import mvc.spring.service.EventService;

@Service("eventService")
@Transactional
public class EventServiceImpl implements EventService{

	@Autowired
	private EventDao dao;
	
	@Override
	public void saveEvent(Event event) {
		dao.saveEvent(event);
	}

	@Override
	public List<Event> findAllEvent() {
		return dao.findAllEvent();
	}

	@Override
	public void deleteEventById(int id) {
		dao.deleteEventById(id);
	}

	@Override
	public Event findById(int id) {
		return dao.findById(id);
	}

	@Override
	public void updateEvent(Event event) {
		dao.updateEvent(event);
	}

	@Override
	public String getNewAccessCode() {
		return dao.getNewAccessCode();
	}

	@Override
	public void mergeEvent(Event event) {
		dao.mergeEvent(event);
	}
	
	

}
