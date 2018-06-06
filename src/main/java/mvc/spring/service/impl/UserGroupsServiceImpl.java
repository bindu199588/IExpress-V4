package mvc.spring.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mvc.spring.dao.UserGroupsDao;
import mvc.spring.model.Admin;
import mvc.spring.model.Event;
import mvc.spring.model.UserGroups;
import mvc.spring.service.UserGroupsService;

@Service("userGroupsService")
@Transactional
public class UserGroupsServiceImpl implements UserGroupsService {
	
	@Autowired
    private UserGroupsDao dao;
	
	public void saveGroup(UserGroups group) {
		dao.saveGroup(group);
		
	}

	public void deleteGroup(int groupId) {
		dao.deleteGroup(groupId);
	}

	public List<UserGroups> findAllGroups() {
		return dao.findAllGroups();
	}

	public UserGroups findGroupById(int groupId) {
		return dao.findGroupById(groupId);
	}

	public List<Admin> findAdminForGroup(int groupId) {
		return dao.findAdminForGroup(groupId);
	}

	@Override
	public void updateGroup(UserGroups group) {
		dao.updateGroup(group);
	}

	@Override
	public List<Admin> findAdminNotInGroup(int groupId) {
		return dao.findAdminNotInGroup(groupId);
	}

	@Override
	public List<Event> findEventsInGroup(int groupId) {
		return dao.findEventsInGroup(groupId);
	}

}
