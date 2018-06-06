package mvc.spring.service;

import java.util.List;

import mvc.spring.model.Admin;
import mvc.spring.model.Event;
import mvc.spring.model.UserGroups;

public interface UserGroupsService {
	void saveGroup(UserGroups group);
//	void deactivateGroup(int groupId);
	void updateGroup(UserGroups group);
	void deleteGroup(int groupId);
	List<UserGroups> findAllGroups();
	UserGroups findGroupById(int groupId);
	List<Admin> findAdminForGroup(int groupId);
	List<Admin> findAdminNotInGroup(int groupId);
	
	List<Event> findEventsInGroup(int groupId);
}
