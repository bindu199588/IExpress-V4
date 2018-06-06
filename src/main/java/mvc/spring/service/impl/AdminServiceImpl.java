package mvc.spring.service.impl;

import java.util.List;
import java.util.Set;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import mvc.spring.dao.AdminDao;
import mvc.spring.model.Admin;
import mvc.spring.model.UserGroups;
import mvc.spring.service.AdminService;


@Service("adminService")
@Transactional
public class AdminServiceImpl implements AdminService {

	 	@Autowired
	    private AdminDao dao;
	     
	    public void saveAdmin(Admin admin) {
	        dao.saveAdmin(admin);
	    }
	    
	    public List<Admin> findAllAdmin() {
	        return dao.findAllAdmin();
	    }
	 
	    public void deleteAdminById(int adminId) {
	        dao.deleteAdminById(adminId);
	    }
	    
	    public Admin findById(int adminId) {
	        return dao.findById(adminId);
	    }
	 
	    public void updateAdmin(Admin admin){
	        dao.updateAdmin(admin);
	    }

		public List<UserGroups> findGroupsByUser(int adminId) {
			return dao.findGroupsByUser(adminId);
		}

		@Override
		public Admin authenticateAdmin(int adminId, String username) {
			return dao.authenticateAdmin(adminId, username);
		}

		@Override
		public Admin authenticateAdmin(String password, String username) {
			return dao.authenticateAdmin(password, username);
		}

		
}
