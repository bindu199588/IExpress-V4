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
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;

@Entity
@Table(name = "USERGROUPS")
public class UserGroups {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id ;
	
	@Column(name="group_name", nullable = false)
	private String group_name;
	
	@Column(name="description", nullable = false)
	private String description;
	
	@Column(name="created_on", nullable = false,insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created_on ;

	@ManyToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
	@JoinTable(name = "ADMIN_USERGROUPS", joinColumns = { @JoinColumn(name = "group_id") }, inverseJoinColumns = { @JoinColumn(name = "admin_id") })
	private Set<Admin> adminlist = new HashSet<Admin>(0);
	
	@OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy="userGroup")
	private Set<Event> eventlist = new HashSet<Event>(0);
	
	public UserGroups() {
		super();
	}

	
	
	
	public UserGroups(int id, String group_name, String description, Calendar created_on, Set<Admin> adminlist,
			Set<Event> eventlist) {
		super();
		this.id = id;
		this.group_name = group_name;
		this.description = description;
		this.created_on = created_on;
		this.adminlist = adminlist;
		this.eventlist = eventlist;
	}




	public UserGroups(String group_name, String description) {
		super();
		this.group_name = group_name;
		this.description = description;
	}



	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGroup_name() {
		return group_name;
	}

	public void setGroup_name(String group_name) {
		this.group_name = group_name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Calendar getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Calendar created_on) {
		this.created_on = created_on;
	}
	public Set<Admin> getAdminlist() {
		return adminlist;
	}
	public void setAdminlist(Set<Admin> adminlist) {
		this.adminlist = adminlist;
	}

	public Set<Event> getEventlist() {
		return eventlist;
	}

	public void setEventlist(Set<Event> eventlist) {
		this.eventlist = eventlist;
	}
	
	
}
