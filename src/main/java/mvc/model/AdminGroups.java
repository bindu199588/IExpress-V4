package mvc.model;

import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

@Entity
@Table(name = "ADMIN_GROUPS")
public class AdminGroups {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", nullable=false)
	private int id ;
	
	@Column(name="group_name", nullable = false)
	private String group_name;
	
	@Column(name="description", nullable = false)
	private String description;
	
	@Column(name="created_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp created_on ;

	public AdminGroups() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminGroups(int id, String group_name, String description, Timestamp created_on) {
		super();
		this.id = id;
		this.group_name = group_name;
		this.description = description;
		this.created_on = created_on;
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

	public Timestamp getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Timestamp created_on) {
		this.created_on = created_on;
	}
	
	
	
}
