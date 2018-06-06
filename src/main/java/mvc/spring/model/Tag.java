package mvc.spring.model;

import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "TAG")
public class Tag {

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
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="event_id")
	private Event event;
	
	public Tag() {
		super();
	}

	public Tag(int id, String name, Calendar created_on, String description, boolean is_active, Event event) {
		super();
		this.id = id;
		this.name = name;
		this.created_on = created_on;
		this.description = description;
		this.is_active = is_active;
		this.event = event;
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

	public Event getEvent() {
		return event;
	}


	public void setEvent(Event event) {
		this.event = event;
	}


}
