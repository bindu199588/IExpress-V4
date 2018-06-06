package mvc.spring.controllers;

import java.sql.SQLException;

import javax.naming.NamingException;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;

import mvc.spring.model.Admin;
import mvc.spring.service.AdminService;

@Controller
public class adminLoginController  extends indexController {
	public adminLoginController() throws NamingException {
		super();
	}
	
	@RequestMapping(value= "/loginAdmin", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public String loginAdmin(@RequestBody Admin admin) throws ClassNotFoundException, SQLException, JsonProcessingException{
		
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		AdminService service = (AdminService) context.getBean("adminService");
		Admin result = service.authenticateAdmin(admin.getPassword(), admin.getUsername());		
		
		if(null != result ){
			Admin serializedAdmin = new Admin(result.getId(),result.getUsername(),null,result.isIs_super(),null);
			return mapper.writeValueAsString(serializedAdmin);
		}
		else{
			return "";
		}
	}
		
}
