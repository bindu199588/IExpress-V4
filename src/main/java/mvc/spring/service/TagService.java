package mvc.spring.service;

import java.util.List;

import mvc.spring.model.Tag;

public interface TagService {
	void saveTag(Tag tag);
    
	void updateTag(Tag tag);
     
    void deleteTagById(int id);
     
    Tag findById(int id);
    
    List<Tag> findAllTag();
    
    void mergeTag(Tag tag);
    
    List<Tag> getTagsFromEventId(int eventId);
}
