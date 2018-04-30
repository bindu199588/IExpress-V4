package mvc.model;

import java.sql.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;



@Entity
@Table(name = "EVENT")
public class Event {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", nullable=false)
	private int id ;	
	
	@Column(name="name", nullable=false)
	private String name ;
	
	@Column(name="created_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_on ;
	
	@Column(name="description", nullable=false)
	private String description ;
	
	@Column(name="owner", nullable=false)
	private String owner;
	
	@Column(name="is_active", nullable=false)
	private boolean is_active ;
	
	@Column(name="access_code", nullable=false)
	private String access_code;
	
	public Event(int id, String name, Date created_on, String description, String owner, boolean is_active,
			String access_code) {
		super();
		this.id = id;
		this.name = name;
		this.created_on = created_on;
		this.description = description;
		this.owner = owner;
		this.is_active = is_active;
		this.access_code = access_code;
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

	public Date getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
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

}
