package mvc.spring.model;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

@Entity
@Table(name = "ADMIN")
public class Admin {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="id", nullable=false)
	private int id;
	
	@Column(name="username", nullable=false, unique=true)
	private String username;
	
	@Column(name="password", nullable=false)
	private String password;

	@Column(name="is_super",nullable=false)
	private boolean is_super;
	
	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "adminlist")
	private Set<UserGroups> userGroups = new HashSet<UserGroups>(0);
	
	
	
	public Admin(int id, String username, String password, boolean is_super, Set<UserGroups> userGroups) {
		super();
		this.id = id;
		this.username = username;
		this.password = password;
		this.is_super = is_super;
		this.userGroups = userGroups;
	}

	public Admin(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}
	
	public Admin() {
		super();
	}
	
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public String getPassword() {
		return password;
	}
	

	public boolean isIs_super() {
		return is_super;
	}

	public void setIs_super(boolean is_super) {
		this.is_super = is_super;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public Set<UserGroups> getUserGroups() {
		return userGroups;
	}

	public void setUserGroups(Set<UserGroups> userGroups) {
		this.userGroups = userGroups;
	}

}
