package mvc.spring.dao.impl;

import java.util.List;

import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import mvc.spring.dao.AbstractDao;
import mvc.spring.dao.UserGroupsDao;
import mvc.spring.model.Admin;
import mvc.spring.model.Event;
import mvc.spring.model.UserGroups;

@Repository("userGroupsDao")
public class UserGroupsDaoImpl  extends AbstractDao implements UserGroupsDao{

	public void saveGroup(UserGroups group) {
		
		getSession().merge(group);
	}


	@SuppressWarnings("rawtypes")
	public void deleteGroup(int groupId) {
		Query query = getSession().createQuery("delete from usergroups where id = :id");
        query.setParameter("id", groupId);
        query.executeUpdate();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<UserGroups> findAllGroups() {
		Query query = getSession().createQuery("from UserGroups");        
        return (List<UserGroups>) query.list();
	}

	@SuppressWarnings("rawtypes")
	public UserGroups findGroupById(int groupId) {
		Query query = getSession().createQuery("from UserGroups g where g.id = :id ");
        query.setParameter("id", groupId);
        return (UserGroups) query.getSingleResult();
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<Admin> findAdminForGroup(int groupId) {
		Query query = getSession().createQuery("select g.adminlist from UserGroups g where g.id = :id ");
        query.setParameter("id", groupId);
        return (List<Admin>) query.list();
	}

	@Override
	public void updateGroup(UserGroups group) {
		getSession().update(group);
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Admin> findAdminNotInGroup(int groupId) {
		Query query = getSession().createQuery("select a from Admin a where a.id not in (select admin_id from AdminUserGroups where group_id =:id )");
        query.setParameter("id", groupId);
        return (List<Admin>) query.list();
        
	}


	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public List<Event> findEventsInGroup(int groupId) {
		Query query = getSession().createQuery("select g.eventlist from UserGroups g where g.id = :id ");
        query.setParameter("id", groupId);
        return (List<Event>) query.list();
	}

}
