package mvc.spring.model;


import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;


@Entity
@Table(name = "SENTIMENT_AGG")
public class SentimentAgg {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id ;
	
	@Column(name="tag_id", nullable=false)
	private int tag_id;
	
	@Column(name="upset_agg")
	private int upset_agg;
	
	@Column(name="sad_agg")
	private int sad_agg;
	
	@Column(name="neutral_agg")
	private int neutral_agg;
	
	@Column(name="happy_agg")
	private int happy_agg;
	
	@Column(name="glad_agg")
	private int glad_agg;
	
	@Column(name="updated_on", nullable=false,insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar updated_on;

	public SentimentAgg() {
		super();
		// TODO Auto-generated constructor stub
	}

	public SentimentAgg(int id, int tag_id, int upset_agg, int sad_agg, int neutral_agg, int happy_agg, int glad_agg,
			Calendar updated_on) {
		super();
		this.id = id;
		this.tag_id = tag_id;
		this.upset_agg = upset_agg;
		this.sad_agg = sad_agg;
		this.neutral_agg = neutral_agg;
		this.happy_agg = happy_agg;
		this.glad_agg = glad_agg;
		this.updated_on = updated_on;
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

	public int getUpset_agg() {
		return upset_agg;
	}

	public void setUpset_agg(int upset_agg) {
		this.upset_agg = upset_agg;
	}

	public int getSad_agg() {
		return sad_agg;
	}

	public void setSad_agg(int sad_agg) {
		this.sad_agg = sad_agg;
	}

	public int getNeutral_agg() {
		return neutral_agg;
	}

	public void setNeutral_agg(int neutral_agg) {
		this.neutral_agg = neutral_agg;
	}

	public int getHappy_agg() {
		return happy_agg;
	}

	public void setHappy_agg(int happy_agg) {
		this.happy_agg = happy_agg;
	}

	public int getGlad_agg() {
		return glad_agg;
	}

	public void setGlad_agg(int glad_agg) {
		this.glad_agg = glad_agg;
	}

	public Calendar getUpdated_on() {
		return updated_on;
	}

	public void setUpdated_on(Calendar updated_on) {
		this.updated_on = updated_on;
	}
	
	
}
