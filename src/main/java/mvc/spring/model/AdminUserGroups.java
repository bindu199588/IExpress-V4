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
import javax.persistence.UniqueConstraint;


@Entity
@Table(name = "ADMIN_USERGROUPS",uniqueConstraints = {@UniqueConstraint(columnNames = {"admin_id", "group_id"})})
public class AdminUserGroups {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id ;
	
	@Column(name="admin_id", nullable=false)
	private int admin_id;
	
	@Column(name="group_id")
	private int group_id ;
	
	@Column(name="created_on", nullable=false,insertable = false)
	@Temporal(TemporalType.TIMESTAMP)
	private Calendar created_on;

	public AdminUserGroups() {
		super();
		// TODO Auto-generated constructor stub
	}

	public AdminUserGroups(int id, int admin_id, int group_id, Calendar created_on) {
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

	public Calendar getCreated_on() {
		return created_on;
	}

	public void setCreated_on(Calendar created_on) {
		this.created_on = created_on;
	}
	
	
}
