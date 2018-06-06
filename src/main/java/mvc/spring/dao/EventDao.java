package mvc.spring.dao;

import java.util.List;

import mvc.spring.model.Event;
import mvc.spring.model.Tag;

public interface EventDao {
	void saveEvent(Event event);
    
    List<Event> findAllEvent();
     
    void deleteEventById(int id);
     
    Event findById(int id);
     
    void updateEvent(Event event);
    
    String getNewAccessCode();
    
    void mergeEvent(Event event);
   
    
}
