package mvc.spring.service;

import java.util.List;
import java.util.Set;

import org.springframework.transaction.annotation.Transactional;

import mvc.spring.model.Admin;
import mvc.spring.model.UserGroups;

public interface AdminService {
	
	void saveAdmin(Admin admin);
	
    List<Admin> findAllAdmin();
     
    void deleteAdminById(int adminId);
    
    Admin findById(int adminId);
     
    void updateAdmin(Admin admin);
    
    List<UserGroups> findGroupsByUser(int adminId);

    Admin authenticateAdmin(int adminId,String username);
    Admin authenticateAdmin(String password,String username);
}
