package mvc.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "XPRESSION")
public class Xpression {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", nullable=false)
	private int id ;
	
	@Column(name="tag_id", nullable=false)
	private int tag_id;
	
	@Column(name="message", nullable=false)
	private String message;
	
	@Column(name="sentiment", nullable=false)
	private int sentiment ;
	
	@Column(name="created_on", nullable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Date created_on ;
	

	public Xpression() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Xpression(int id, int tag_id, String message, int sentiment, Date created_on) {
		super();
		this.id = id;
		this.tag_id = tag_id;
		this.message = message;
		this.sentiment = sentiment;
		this.created_on = created_on;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getTag_id() {
		return tag_id;
	}

	public void setTag_id(int tag_id) {
		this.tag_id = tag_id;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public int getSentiment() {
		return sentiment;
	}

	public void setSentiment(int sentiment) {
		this.sentiment = sentiment;
	}

	public Date getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Date created_on) {
		this.created_on = created_on;
	}
	
	
}
