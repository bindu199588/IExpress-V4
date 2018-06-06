package mvc.spring.dao;

import java.util.List;

import mvc.spring.model.Admin;
import mvc.spring.model.UserGroups;

public interface AdminDao {
	
	void saveAdmin(Admin admin);

	List<Admin> findAllAdmin();
     
	void deleteAdminById(int adminId);
     
    Admin findById(int adminId);
     
    void updateAdmin(Admin admin);
    
    List<UserGroups> findGroupsByUser(int adminId);
    
    Admin authenticateAdmin(int adminId,String username);
    Admin authenticateAdmin(String password,String username);
}
