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
@Table(name = "EVENTAGENDA")
public class EventAgenda {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", nullable=false)
	private int id ;
	
	@Column(name="agenda", nullable = false)
	private String agenda;
	
	@Column(name="start_time", nullable = false)
	private Timestamp start_time;
	
	@Column(name="end_time", nullable = false)
	private Timestamp end_time;
	
	@Column(name="event_id", nullable = false)
	private int event_id;
	
	@Column(name="created_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp created_on;

	public EventAgenda() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventAgenda(int id, String agenda, Timestamp start_time, Timestamp end_time, int event_id,
			Timestamp created_on) {
		super();
		this.id = id;
		this.agenda = agenda;
		this.start_time = start_time;
		this.end_time = end_time;
		this.event_id = event_id;
		this.created_on = created_on;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAgenda() {
		return agenda;
	}

	public void setAgenda(String agenda) {
		this.agenda = agenda;
	}

	public Timestamp getStart_time() {
		return start_time;
	}

	public void setStart_time(Timestamp start_time) {
		this.start_time = start_time;
	}

	public Timestamp getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Timestamp end_time) {
		this.end_time = end_time;
	}

	public int getEvent_id() {
		return event_id;
	}

	public void setEvent_id(int event_id) {
		this.event_id = event_id;
	}

	public Timestamp getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Timestamp created_on) {
		this.created_on = created_on;
	}
	
	
}
