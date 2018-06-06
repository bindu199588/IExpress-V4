package mvc.spring.model;


import java.util.Calendar;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@Entity
@Table(name = "EVENT")
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id ;	
	
	@Column(name="name", nullable=false)
	private String name ;
	
	@Column(name="created_on", nullable = false,insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created_on ;
	
	@Column(name="description", nullable=false)
	private String description ;
	
	@Column(name="is_active", nullable=false)
	private boolean is_active ;
	
	
	@Column(name="access_code", nullable=false)
	private String access_code;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="owner_group")
	private UserGroups userGroup;
	
	@OneToMany(mappedBy ="event")
	private Set<EventAgenda> agendaList = new HashSet<EventAgenda>(0);

	@OneToMany(mappedBy="event")
	private Set<Tag> tagList = new HashSet<Tag>(0);


	

	public Event(int id, String name, Calendar created_on, String description, boolean is_active, String access_code,
			UserGroups userGroup, Set<EventAgenda> agendaList, Set<Tag> tagList) {
		super();
		this.id = id;
		this.name = name;
		this.created_on = created_on;
		this.description = description;
		this.is_active = is_active;
		this.access_code = access_code;
		this.userGroup = userGroup;
		this.agendaList = agendaList;
		this.tagList = tagList;
	}

	public Event() {
		super();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Calendar getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Calendar created_on) {
		this.created_on = created_on;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}



	public boolean isIs_active() {
		return is_active;
	}

	public void setIs_active(boolean is_active) {
		this.is_active = is_active;
	}

	public String getAccess_code() {
		return access_code;
	}

	public void setAccess_code(String access_code) {
		this.access_code = access_code;
	}

	public UserGroups getUserGroup() {
		return userGroup;
	}

	public void setUserGroup(UserGroups userGroup) {
		this.userGroup = userGroup;
	}
	public Set<EventAgenda> getAgendaList() {
		return agendaList;
	}


	public void setAgendaList(Set<EventAgenda> agendaList) {
		this.agendaList = agendaList;
	}


	public Set<Tag> getTagList() {
		return tagList;
	}


	public void setTagList(Set<Tag> tagList) {
		this.tagList = tagList;
	}
	
	

}
