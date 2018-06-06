package mvc.spring.dao.impl;

import java.util.List;

import javax.persistence.NoResultException;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import mvc.spring.dao.AbstractDao;
import mvc.spring.dao.TagDao;
import mvc.spring.model.EventAgenda;
import mvc.spring.model.Tag;

@Repository("tagDao")
public class TagDaoImpl  extends AbstractDao implements TagDao{

	@Override
	public void saveTag(Tag tag) {
		getSession().merge(tag);
	}

	@Override
	public void updateTag(Tag tag) {
		getSession().update(tag);
	}

	@SuppressWarnings("rawtypes")
	@Override
	public void deleteTagById(int id) {
		Query query = getSession().createQuery("delete from Tag where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Tag findById(int id) {
		Query query = getSession().createQuery("from Tag a where a.id = :id ");
        query.setParameter("id", id);
        try{
        	return (Tag) query.getSingleResult();        	
        }
        catch(NoResultException E){
        	return null;
        }
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Tag> findAllTag() {
		Query query = getSession().createQuery("from Tag");        
        return (List<Tag>) query.list();
	}

	@Override
	public void mergeTag(Tag tag) {
		getSession().merge(tag);
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	@Override
	public List<Tag> getTagsFromEventId(int eventId) {
		Query query = getSession().createQuery("select e.tagList from Event e where e.id = :id ");
        query.setParameter("id", eventId);
        return (List<Tag>) query.list();
	}

}
