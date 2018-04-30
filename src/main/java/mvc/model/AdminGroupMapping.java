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
@Table(name = "ADMIN_GROUP_MAPPING")
public class AdminGroupMapping {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="id", nullable=false)
	private int id ;
	
	@Column(name="admin_id", nullable=false)
	private int admin_id;
	
	@Column(name="group_id", nullable=false)
	private int group_id ;
	
	@Column(name="created_on", nullable=false)
	@Temporal(TemporalType.TIMESTAMP)
	private Timestamp created_on;

	public AdminGroupMapping() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminGroupMapping(int id, int admin_id, int group_id, Timestamp created_on) {
		super();
		this.id = id;
		this.admin_id = admin_id;
		this.group_id = group_id;
		this.created_on = created_on;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getAdmin_id() {
		return admin_id;
	}

	public void setAdmin_id(int admin_id) {
		this.admin_id = admin_id;
	}

	public int getGroup_id() {
		return group_id;
	}

	public void setGroup_id(int group_id) {
		this.group_id = group_id;
	}

	public Timestamp getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Timestamp created_on) {
		this.created_on = created_on;
	}
	
	
}
