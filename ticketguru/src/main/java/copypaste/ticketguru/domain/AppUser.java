package copypaste.ticketguru.domain;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "app_user")
@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "user_id")
public class AppUser {

	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "user_id")
	private Long user_id;

	@Column(name = "username")
	private String username;

	@Column(name = "password")
	@JsonIgnore
	private String password;

	@OneToMany(mappedBy = "appUser")
	@JsonIgnore
	private List<Purchase> purchases;

	public AppUser(String username, String password) {
		super();
		this.username = username;
		this.password = password;
	}

	public AppUser() {
		super();
	}

	public Long getUser_id() {
		return user_id;
	}

	public void setUser_id(Long user_id) {
		this.user_id = user_id;
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

	public void setPassword(String password) {
		this.password = password;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	@Override
	public String toString() {
		return "AppUser [id=" + user_id + ", username=" + username + ", purchases=" + purchases + "]";
	}

}
