package mvc.spring.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.databind.ObjectMapper;
import database.DbBean;
import org.aed.bigdata.kafka.XpressionProducer;

@Controller
public class indexController {	
	protected static final ObjectMapper mapper = new ObjectMapper();
	protected static final XpressionProducer producer = new XpressionProducer("xys");;
	protected static final DbBean db = new DbBean();
	
}