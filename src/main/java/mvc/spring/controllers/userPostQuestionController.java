package mvc.spring.controllers;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.dreambig.swearwords.SwearWordsFilter;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;

import dataObjects.questionObject;
import mvc.spring.model.Question;
import mvc.spring.service.QuestionService;
import mvc.spring.service.UserGroupsService;

@Controller
public class userPostQuestionController extends indexController{

	public userPostQuestionController() {
		super();
	}

	
	@RequestMapping(value="/postQuestion", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean postUserQuestion(@RequestBody questionObject questionObject) throws SQLException, ClassNotFoundException{
		try(Connection con = db.getConnection()){
			if(con!=null){
				StringBuilder sb = new StringBuilder("INSERT INTO QUESTION(question,event_id) VALUES(?,?)");
				PreparedStatement pst = con.prepareStatement(sb.toString());
				pst.setString(1, questionObject.getQuestion());
				pst.setLong(2, questionObject.getEvent_id());
				pst.executeUpdate();
				return true;
			}
		}		
		return false;
	}
	
	@RequestMapping("/loadQuestions")
	@ResponseBody
	public String getUserQuestions(@RequestBody questionObject questionObject) throws SQLException, ClassNotFoundException, JsonProcessingException{
		String jsonString="";
		ResultSet rs;
		List<questionObject> qObjList = new ArrayList<questionObject>();
		try(Connection con = db.getConnection()){
			if(con!=null){
				StringBuilder sb= new StringBuilder("select id,question,likeCounter,event_id,floor(extract(epoch from created_on)*1000) as created_on");
				sb.append(" from question where date(question.created_on) >= date(now()) and floor(extract(epoch from question.created_on)*1000) > ? and event_id=? order by question.created_on");
				PreparedStatement pst = con.prepareStatement(sb.toString());
				pst.setLong(1, questionObject.getCreated_on());
				pst.setLong(2, questionObject.getEvent_id());
				rs = pst.executeQuery();
				while(rs.next()){
					qObjList.add(new questionObject(rs.getString("id"),rs.getString("question"),rs.getLong("event_id"),rs.getLong("created_on"),rs.getLong("likeCounter")));
	
				}
				return mapper.writeValueAsString(qObjList);
			}
			
		}
		return jsonString;
	}
	
	@RequestMapping(value="/loadPopularQuestions", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Question> getPopularQuestions(@RequestBody Question question){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		QuestionService service = (QuestionService) context.getBean("questionService");
		return service.findPopularQuestions(question.getEvent_id());
	}
	
	@RequestMapping(value="/likeQuestion", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public Question updateQuestionLikeCounter(@RequestBody Question question){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		QuestionService service = (QuestionService) context.getBean("questionService");
		return service.increaseLikeCounter(question.getId());
		
	}
	
	@RequestMapping(value="/filterSwearWords", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean filterSwearWords(@RequestBody Question question){
		SwearWordsFilter filter = SwearWordsFilter.getInstance();	
		// return true if it has swear words
		if(filter.haveSwearWords(question.getQuestion())){
			return true;
		}
		return false;
	}
	
}
