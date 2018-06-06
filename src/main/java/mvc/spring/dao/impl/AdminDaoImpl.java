package mvc.spring.dao.impl;

import java.util.List;
import java.util.Set;

import javax.persistence.NoResultException;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;
import org.hibernate.query.Query;
import org.springframework.stereotype.Repository;

import mvc.spring.dao.AbstractDao;
import mvc.spring.dao.AdminDao;
import mvc.spring.model.Admin;
import mvc.spring.model.UserGroups;


@Repository("adminDao")
public class AdminDaoImpl extends AbstractDao implements AdminDao{
	
    @SuppressWarnings({ "unchecked", "rawtypes" })
    public List<Admin> findAllAdmin() {
    	Query query = getSession().createQuery("from Admin");        
        return (List<Admin>) query.list();
    }
    
    @Override
	public void saveAdmin(Admin admin) {
		getSession().save(admin);
		
	}

	@Override
	public void deleteAdminById(int id) {
		Query query = getSession().createQuery("delete from Admin where id = :id");
        query.setParameter("id", id);
        query.executeUpdate();
		
	}

	@SuppressWarnings("rawtypes")
	@Override
	public Admin findById(int adminId) {
		Query query = getSession().createQuery("from Admin a where a.id = :id ");
        query.setParameter("id", adminId);
        try{
        	return (Admin) query.getSingleResult();        	
        }
        catch(NoResultException E){
        	return null;
        }
	}
	

    
    public void updateAdmin(Admin admin){
        getSession().update(admin);
    }
    
    @SuppressWarnings({ "rawtypes", "unchecked" })
	public List<UserGroups> findGroupsByUser(int adminId) {  		
		Query query = getSession().createQuery("select a.userGroups from Admin a where a.id = :id ");
        query.setParameter("id", adminId);
        return (List<UserGroups>) query.list();
		
	}

	@SuppressWarnings("rawtypes")
	public Admin authenticateAdmin(int adminId,String username) {
		Query query = getSession().createQuery("select a from Admin a where a.id = :id and a.username = :username");
        query.setParameter("id", adminId);
        query.setParameter("username", username);
        try{
        	return (Admin) query.getSingleResult();        	
        }
        catch(NoResultException E){
        	return null;
        }
	}

	@SuppressWarnings("rawtypes")
	public Admin authenticateAdmin(String password, String username) {
		Query query = getSession().createQuery("select a from Admin a where a.password = :password and a.username = :username");
		query.setParameter("password", password);
        query.setParameter("username", username);
        try{
        	return (Admin) query.getSingleResult();        	
        }
        catch(NoResultException E){
        	return null;
        }
        
	}


	
}
