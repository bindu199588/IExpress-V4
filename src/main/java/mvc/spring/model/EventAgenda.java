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
@Table(name = "EVENTAGENDA")
public class EventAgenda {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id ;
	
	@Column(name="agenda", nullable = false)
	private String agenda;
	
	@Column(name="start_time", nullable = false)
	private Calendar start_time;
	
	@Column(name="end_time", nullable = false)
	private Calendar end_time;
	
	@Column(name="created_on", nullable = false,insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created_on;
	
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name="event_id")
	private Event event;

	public EventAgenda() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventAgenda(int id, String agenda, Calendar start_time, Calendar end_time, Calendar created_on,
			Event event) {
		super();
		this.id = id;
		this.agenda = agenda;
		this.start_time = start_time;
		this.end_time = end_time;
		this.created_on = created_on;
		this.event = event;
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

	public Calendar getStart_time() {
		return start_time;
	}

	public void setStart_time(Calendar start_time) {
		this.start_time = start_time;
	}

	public Calendar getEnd_time() {
		return end_time;
	}

	public void setEnd_time(Calendar end_time) {
		this.end_time = end_time;
	}

	public Calendar getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Calendar created_on) {
		this.created_on = created_on;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
}
