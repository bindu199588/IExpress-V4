package mvc.spring.controllers;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.naming.NamingException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.postgresql.PGConnection;
import org.postgresql.copy.CopyManager;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.ContextLoader;
import org.springframework.web.context.WebApplicationContext;

import com.fasterxml.jackson.core.JsonProcessingException;

import dataObjects.countAtDateObject;
import dataObjects.eventAgendaObject;
import dataObjects.eventObject;
import dataObjects.tagObject;
import mvc.spring.model.Admin;
import mvc.spring.model.Event;
import mvc.spring.model.EventAgenda;
import mvc.spring.model.Tag;
import mvc.spring.model.UserGroups;
import mvc.spring.service.AdminService;
import mvc.spring.service.EventAgendaService;
import mvc.spring.service.EventService;
import mvc.spring.service.TagService;
import mvc.spring.service.UserGroupsService;
import util.AdminPasswordGenerator;

@Controller
public class adminDashBoardController extends indexController{
	
	
	public adminDashBoardController() throws NamingException {
		super();
	}
	
	@RequestMapping(value="/postAllNewTags", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean createTag(@RequestBody List<Tag> tagsList) throws ClassNotFoundException, SQLException{
		
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		TagService tagService = (TagService) context.getBean("tagService");
		try{
			for(Tag tag: tagsList){
				tagService.saveTag(tag);
			}
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/*
	 * NOT IN HIBERNATE
	 */
	
	@RequestMapping(value = "/postEventAgenda", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean addEventAgenda(@RequestBody List<eventAgendaObject> agendaList) throws ClassNotFoundException, SQLException{
		try(Connection con = db.getConnection()){
			if(con!=null){
				StringBuilder sb = new StringBuilder("insert into eventagenda(agenda,start_time,event_id,end_time) values(?,?,?,?)");
				PreparedStatement pst = con.prepareStatement(sb.toString());
				for(eventAgendaObject agendaObject : agendaList){
					pst.setString(1, agendaObject.getAgenda());
					pst.setTimestamp(2, new java.sql.Timestamp(agendaObject.getStart_time()));
					pst.setLong(3, agendaObject.getEvent_id());
					if(agendaObject.getEnd_time()!=null){
						pst.setTimestamp(4, new java.sql.Timestamp(agendaObject.getEnd_time()));	
					}
					else{
						pst.setNull(4, Types.TIMESTAMP);
					}
					pst.executeUpdate();
				}
				return true;
			}
		}
		return false;
	}
	
	@RequestMapping(value = "/loadEventAgenda")
	@ResponseBody
	public List<EventAgenda> getEventAgenda(@RequestBody Event event) throws ClassNotFoundException, SQLException, JsonProcessingException{
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		EventAgendaService eventAgendaService = (EventAgendaService) context.getBean("eventAgendaService");
		List<EventAgenda> agendaList = eventAgendaService.findAgendaFromEvent(event.getId());
		for(EventAgenda agenda: agendaList){
			agenda.setEvent(null);
		}
		return agendaList;
		
	}
	
	@RequestMapping(value="/saveAgenda", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean saveAgenda(@RequestBody EventAgenda agenda){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		EventAgendaService eventAgendaService = (EventAgendaService) context.getBean("eventAgendaService");
		try{
			eventAgendaService.saveAgenda(agenda);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	@RequestMapping(value="/deleteAgenda", produces=MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean deleteAgenda(@RequestBody EventAgenda agenda){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		EventAgendaService eventAgendaService = (EventAgendaService) context.getBean("eventAgendaService");
		try{
			eventAgendaService.deleteAgenda(agenda.getId());
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping("/updateEvent")
	@ResponseBody
	public boolean updateEvent(@RequestBody Event event) throws ClassNotFoundException, SQLException{
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		EventService eventService = (EventService) context.getBean("eventService");
		try{
			if(event.getId()!=0){
				eventService.mergeEvent(event);
				return true;
			}
			else{
				event.setAccess_code(eventService.getNewAccessCode());
				eventService.saveEvent(event);
				return true;
			}
			
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	@RequestMapping("/updateTag")
	@ResponseBody
	public boolean updateTag(@RequestBody Tag tag) throws ClassNotFoundException, SQLException{
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		TagService tagService = (TagService) context.getBean("tagService");
		try{
			tagService.mergeTag(tag);
			return true;
		}
		catch(Exception e){
			e.printStackTrace();
			return false;
		}
	}
	
	/**
	 * NOT IN HIBERNATE
	 */
	

	

	@RequestMapping(value="/getTagsFromEventId", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Tag> getTagsFromEventId(@RequestBody Event event)
			throws ClassNotFoundException, SQLException, JsonProcessingException {
		
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		TagService tagService = (TagService) context.getBean("tagService");
		List<Tag> tagsList = tagService.getTagsFromEventId(event.getId());
		for(Tag tag: tagsList){
			tag.setEvent(event);
		}
		return tagsList;
	}

	
	
	/*
	 * NOT IN HIBERNATE
	 */
	
	@RequestMapping("/getEventGraphData")
	@ResponseBody
	public String getEventGraphData(@RequestParam(value="eventId") Long eventId,@RequestParam(value="tagId",required = false) Integer tagId) throws ClassNotFoundException, SQLException, JsonProcessingException {
		
		ResultSet rs;
		String jsonString ="";		
		try(Connection con = db.getConnection()){
			if(con!=null){		
				StringBuilder sb = new StringBuilder("select count(xprJoin.id),myDate.date");
				sb.append(" from ");
				sb.append(" (select distinct date(created_on) from xpression ");
				sb.append(" join ");
				sb.append(" (select id,event_id from tag where event_id = ? ");
				if(tagId != null){
					sb.append("and id =? ");
				}
				sb.append(") as tagTable on tagTable.id = tag_id");
				sb.append(" order by date) as myDate ");
				sb.append(" left join ");
				sb.append(" (select xpression.id,created_on from xpression join (select id,event_id from tag where event_id =? ");
				if(tagId != null){
					sb.append("and id =? ");
				}
				sb.append(") as tagJoin on tagJoin.id=tag_id where xpression.sentiment = ?) as xprJoin ");
				sb.append(" on myDate.date >= date(xprJoin.created_on) ");
				sb.append(" group by myDate.date;");
				PreparedStatement pst = con.prepareStatement(sb.toString());
				
				int sentiment = 0;
				countAtDateObject cb;
				List<List<countAtDateObject>> totalList= new ArrayList<>();
				List<countAtDateObject> countAtDateListForEachSentiment;
				
				for(sentiment = 0;sentiment<5;sentiment++){
					countAtDateListForEachSentiment = new ArrayList<countAtDateObject>();
					if(tagId!=null){
						pst.setLong(1, eventId);
						pst.setInt(2, tagId);
						pst.setLong(3, eventId);
						pst.setInt(4, tagId);
						pst.setInt(5, sentiment);
					}
					else{
						pst.setLong(1, eventId);						
						pst.setLong(2, eventId);
						pst.setInt(3, sentiment);
					}
					rs = pst.executeQuery();
					while(rs.next()){
						cb = new countAtDateObject(rs.getLong("count"),rs.getDate("date").getTime());
						countAtDateListForEachSentiment.add(cb);
					}
					totalList.add(countAtDateListForEachSentiment);
				}
				return mapper.writeValueAsString(totalList);
			}
		}
		
		return jsonString;
	}
	
	
	/*
	 * NOT IN HIBERNATE
	 */
	
	@RequestMapping(value = "/exportComments")
	public void getTagDataCSV(HttpServletResponse response,@RequestParam(value="eventId") Long eventId,@RequestParam(value="tagId",required = false) Long tagId) throws ClassNotFoundException, IOException, SQLException{
		try(Connection con = db.getConnection()){
			//PgConnection copyOperationConnection = (PgConnection) connection;
			//PGConnection pgCon = con.unwrap(PGConnection.class);
			CopyManager cpm = ((PGConnection) con).getCopyAPI();
			
			ServletOutputStream outStream = response.getOutputStream();
			
			String mimetype = "application/octet-stream";
	        String fileName = "CommentList.csv";
	        response.setContentType(mimetype);	        
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		    
		    StringBuilder sb = new StringBuilder("COPY (select tag.name as tagname,tag.description,regexp_replace(regexp_replace(message,'\\n',' ','g'),'\\|',' ','g') as message,sentiment.sentiment,xpression.created_on ");
		    sb.append(" from tag join xpression on tag.id = xpression.tag_id ");
		    sb.append(String.format(" and tag.event_id = %d", eventId));
		    if(tagId !=null){
		    	sb.append(String.format(" and tag.id = %d",tagId));
		    }
		    sb.append(" join sentiment on xpression.sentiment = sentiment.id");		    
		    sb.append(") TO STDOUT WITH CSV HEADER DELIMITER '|'");
	    	try{
	    		cpm.copyOut(sb.toString(),outStream);
	    	}
	    	catch(Exception er){
	    		System.out.println("HERE IS THE COPYERROR");
	    		er.printStackTrace(System.out);
	    	}
	        outStream.close();
		}
		
	}
	
	/*
	 * NOT IN HIBERNATE
	 */
	
	
	@RequestMapping(value = "/exportQuestions")
	public void getQuestionDataCSV(HttpServletResponse response,@RequestParam(value="eventId") Long eventId) throws ClassNotFoundException, IOException, SQLException{
		try(Connection con = db.getConnection()){
			//PgConnection copyOperationConnection = (PgConnection) connection;
			//PGConnection pgCon = con.unwrap(PGConnection.class);
			CopyManager cpm = ((PGConnection) con).getCopyAPI();
			
			ServletOutputStream outStream = response.getOutputStream();
			
			String mimetype = "application/octet-stream";
	        String fileName = "QuestionList.csv";
	        response.setContentType(mimetype);	        
		    response.setHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
		    
		    String queryString = String.format("select regexp_replace(regexp_replace(question,'\\n',' ','g'),'\\|',' ','g') as question,likecounter,created_on from question where event_id = %d", eventId);
		    StringBuilder sb = new StringBuilder("COPY (");
		    sb.append(queryString);
		    sb.append(") TO STDOUT WITH CSV HEADER DELIMITER '|'");		    
		    
	    	try{
	    		cpm.copyOut(sb.toString(),outStream);
	    	}
	    	catch(Exception er){
	    		System.out.println("HERE IS THE COPYERROR");
	    		er.printStackTrace(System.out);
	    	}
	        outStream.close();
		}
		
	}
	
	
	
	@RequestMapping(value="/createNewAdmin", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean createNewAdmin(@RequestBody Admin admin){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		AdminService service = (AdminService) context.getBean("adminService");
		try{
			if(0 != admin.getId()){
				
				admin = service.findById(admin.getId());
			}
			else{
				AdminPasswordGenerator generator = new AdminPasswordGenerator();
				admin.setPassword(generator.generateToken());
			}
			System.out.println("STOP HERE");
			service.saveAdmin(admin);
			return true;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return false;
		}
		//return false;
		
	}
	

	@RequestMapping(value="/addAdminToGroup", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean addAdminToGroup(@RequestBody UserGroups group){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		UserGroupsService service = (UserGroupsService) context.getBean("userGroupsService");
		try{
			UserGroups dbGroup = service.findGroupById(group.getId());
			Set<Admin> adminListInGroup = (new HashSet<Admin>(0));
			adminListInGroup.addAll(service.findAdminForGroup(group.getId()));		
			for(Admin admin : group.getAdminlist()){
				if(0 != admin.getId()){
					AdminService adminService = (AdminService) context.getBean("adminService");
					admin = adminService.findById(admin.getId());
				}
				else{
					AdminPasswordGenerator generator = new AdminPasswordGenerator();
					admin.setPassword(generator.generateToken());
				}
				adminListInGroup.add(admin);
			}
			dbGroup.setAdminlist(adminListInGroup);
			service.saveGroup(dbGroup);
			return true;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return false;
		}
		//return false;
		
	}
	
	
	@RequestMapping(value="/createNewGroup", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public boolean createNewGroup(@RequestBody UserGroups group){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		UserGroupsService service = (UserGroupsService) context.getBean("userGroupsService");
		try{
			service.saveGroup(group);
			return true;
		}
		catch(Exception e){
			e.printStackTrace(System.out);
			return false;
		}
	}
	
	@RequestMapping(value="/getAdminInGroup", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Admin> getUsersInGroup(@RequestBody UserGroups group){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		UserGroupsService service = (UserGroupsService) context.getBean("userGroupsService");
		List<Admin> result = service.findAdminForGroup(group.getId());
		for(Admin admin : result){
			//admin.setPassword(null);
			admin.setUserGroups(null);
		}
		return result;
		
	}
	
	@RequestMapping(value="/getAllGroups", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<UserGroups> getAllGroups(@RequestBody Admin admin){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		UserGroupsService service = (UserGroupsService) context.getBean("userGroupsService");
		List<UserGroups> result=service.findAllGroups();
		for(UserGroups group:result){
			group.setAdminlist(null);
			group.setEventlist(null);
		}
		return result;
	}
	
	@RequestMapping(value="/getAllAdmin", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Admin> getAllAdmin(){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		AdminService service = (AdminService) context.getBean("adminService");
		List<Admin> result=service.findAllAdmin();
		for(Admin admin:result){
			//admin.setPassword(null);
			admin.setUserGroups(null);
		}
		return result;
	}
	
	@RequestMapping(value="/getAdminNotInGroup", produces = MediaType.APPLICATION_JSON_VALUE)
	@ResponseBody
	public List<Admin> getAdminNotInGroup(@RequestBody UserGroups group){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		UserGroupsService service = (UserGroupsService) context.getBean("userGroupsService");
		List<Admin> result = service.findAdminNotInGroup(group.getId());
		for(Admin admin : result){
			admin.setPassword(null);
			admin.setUserGroups(null);
		}
		return result;
	}
	
	@RequestMapping(value="/getEventsFromAdminId")
	@ResponseBody
	public List<UserGroups> getEventsFromAdminId(@RequestBody Admin admin){
		WebApplicationContext context = ContextLoader.getCurrentWebApplicationContext();
		AdminService adminService = (AdminService) context.getBean("adminService");
		List<UserGroups> groupsList = adminService.findGroupsByUser(admin.getId());
		
		try{
			UserGroupsService userGroupsService = (UserGroupsService) context.getBean("userGroupsService");
			for(UserGroups group: groupsList){
				Set<Event> resultEventList = new HashSet<Event>(0);
				List<Event> eventsInGroup = userGroupsService.findEventsInGroup(group.getId());
				for(Event event: eventsInGroup ){
					event.setUserGroup(null);
					event.setAgendaList(null);
					event.setTagList(null);
				}
				resultEventList.addAll(eventsInGroup);
				group.setAdminlist(null);
				group.setEventlist(resultEventList);
			}
		}
		catch(Exception e){
			e.printStackTrace(System.out);
		}
		
		return groupsList;
			
	}

}
