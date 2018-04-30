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
@Table(name = "QUESTION")
public class Question {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", nullable=false)
	private int id ;
	
	@Column(name="question", nullable = false)
	private String question;
	
	@Column(name="likecounter", nullable = false)
	private int likecounter;
	
	@Column(name="event_id", nullable = false)
	private int event_id;

	@Column(name="created_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp created_on;

	public Question() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Question(int id, String question, int likecounter, int event_id, Timestamp created_on) {
		super();
		this.id = id;
		this.question = question;
		this.likecounter = likecounter;
		this.event_id = event_id;
		this.created_on = created_on;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getQuestion() {
		return question;
	}

	public void setQuestion(String question) {
		this.question = question;
	}

	public int getLikecounter() {
		return likecounter;
	}

	public void setLikecounter(int likecounter) {
		this.likecounter = likecounter;
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
