package mvc.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import util.SessionTokenGenerator;

@Controller
public class SessionTokenUtil {

	@RequestMapping(value="/generateSessionToken")
	@ResponseBody
	public String getSessionToken(){
		SessionTokenGenerator sessionGenerator = new SessionTokenGenerator();
		return sessionGenerator.generateToken();
	}
}
