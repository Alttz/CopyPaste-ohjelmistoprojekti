package copypaste.ticketguru.domain;

import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

@Entity
@Table(name= "app_user")
public class AppUser {
	
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id", nullable = false, updatable = false)
    private Long user_id;
	
	@NotNull(message = "Käyttäjätunnus ei saa olla tyhjä")
    @NotEmpty(message = "Käyttäjätunnus ei saa olla tyhjä")
	@Column(name = "username", nullable = false, unique = true)
    private String username;
	
    @Column(name = "password_hash")
    private String passwordHash;
	
	@ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "user_roles",
      joinColumns = @JoinColumn(name = "user_id"),
      inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<Role> roles;
	
	@OneToMany(mappedBy = "user")
	@JsonManagedReference
	private List<Purchase> purchases;

	public AppUser() {
		super();
	}

	public AppUser(
			@NotNull(message = "Käyttäjätunnus ei saa olla tyhjä") @NotEmpty(message = "Käyttäjätunnus ei saa olla tyhjä") String username,
			String passwordHash, Set<Role> roles) {
		super();
		this.username = username;
		this.passwordHash = passwordHash;
		this.roles = roles;
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

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

	public List<Purchase> getPurchases() {
		return purchases;
	}

	public void setPurchases(List<Purchase> purchases) {
		this.purchases = purchases;
	}

	public Set<Role> getRoles() {
		return roles;
	}

	public void setRoles(Set<Role> roles) {
		this.roles = roles;
	}

	@Override
	public String toString() {
		return "AppUser [user_id=" + user_id + ", username=" + username + ", passwordHash=" + passwordHash
				+ ", purchases=" + purchases + ", roles=" + roles + "]";
	}

	
}
