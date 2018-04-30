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
@Table(name = "TAG")
public class Tag {

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
	
	@Column(name="event_id", nullable=false)
	private int event_id;
	
	public Tag() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Tag(int id, String name, Date created_on, String description, int event_id) {
		super();
		this.id = id;
		this.name = name;
		this.created_on = created_on;
		this.description = description;
		this.event_id = event_id;
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

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

}
