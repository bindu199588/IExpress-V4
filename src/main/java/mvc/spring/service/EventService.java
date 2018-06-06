package mvc.spring.service;

import java.util.List;

import mvc.spring.model.Event;

public interface EventService {
	void saveEvent(Event event);
    
    List<Event> findAllEvent();
     
    void deleteEventById(int id);
     
    Event findById(int id);
     
    void updateEvent(Event event);
    String getNewAccessCode();
    
    void mergeEvent(Event event);
}
