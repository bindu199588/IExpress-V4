package mvc.spring.model;

import java.util.Calendar;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "XPRESSION")
public class Xpression {
	
	public enum Category{
		COMMENT,RATING
	}
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id ;
	
	@Column(name="tag_id", nullable=false)
	private int tag_id;
	
	@Column(name="message", nullable=false)
	private String message;
	
	@Column(name="sentiment", nullable=false)
	private int sentiment ;
	
	@Column(name="created_on", nullable = false,insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created_on ;
	
	@Column(name="category",nullable = false)
	@Enumerated(EnumType.STRING)
	private Category category;

	public Xpression() {
		super();
		// TODO Auto-generated constructor stub
	}

	

	public Xpression(int id, int tag_id, String message, int sentiment, Calendar created_on, Category category) {
		super();
		this.id = id;
		this.tag_id = tag_id;
		this.message = message;
		this.sentiment = sentiment;
		this.created_on = created_on;
		this.category = category;
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

	public Calendar getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Calendar created_on) {
		this.created_on = created_on;
	}

	public Category getCategory() {
		return category;
	}

	public void setCategory(Category category) {
		this.category = category;
	}
	
	
	
}
