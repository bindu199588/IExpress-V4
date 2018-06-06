package mvc.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mvc.spring.dao.TagDao;
import mvc.spring.model.Tag;
import mvc.spring.service.TagService;

@Service("tagService")
@Transactional
public class TagServiceImpl implements TagService{

	@Autowired 
	private TagDao dao;
	
	@Override
	public void saveTag(Tag tag) {
		dao.saveTag(tag);
	}

	@Override
	public void updateTag(Tag tag) {
		dao.updateTag(tag);
	}

	@Override
	public void deleteTagById(int id) {
		dao.deleteTagById(id);
	}

	@Override
	public Tag findById(int id) {
		return dao.findById(id);
	}

	@Override
	public List<Tag> findAllTag() {
		return dao.findAllTag();
	}

	@Override
	public void mergeTag(Tag tag) {
		dao.mergeTag(tag);
	}

	@Override
	public List<Tag> getTagsFromEventId(int eventId) {
		return dao.getTagsFromEventId(eventId);
	}

}
