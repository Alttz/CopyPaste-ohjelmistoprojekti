package copypaste.ticketguru.domain;

import java.util.Set;

import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinTable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.Table;

@Entity
@Table(name= "role")
public class Role {
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long role_id;

    private String role_name;

    @ManyToMany(mappedBy = "roles")
    private Set<AppUser> users;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
      name = "role_permissions",
      joinColumns = @JoinColumn(name = "role_id"),
      inverseJoinColumns = @JoinColumn(name = "permission_id"))
    
    private Set<Permission> permissions;

	public Role() {
	}

	public Role(Long role_id, String role_name, Set<AppUser> users, Set<Permission> permissions) {
		this.role_id = role_id;
		this.role_name = role_name;
		this.users = users;
		this.permissions = permissions;
	}

	public Long getRole_id() {
		return role_id;
	}

	public void setRole_id(Long role_id) {
		this.role_id = role_id;
	}

	public String getRole_name() {
		return role_name;
	}

	public void setRole_name(String role_name) {
		this.role_name = role_name;
	}

	public Set<AppUser> getUsers() {
		return users;
	}

	public void setUsers(Set<AppUser> users) {
		this.users = users;
	}

	public Set<Permission> getPermissions() {
		return permissions;
	}

	public void setPermissions(Set<Permission> permissions) {
		this.permissions = permissions;
	}

	@Override
	public String toString() {
		return "Role [role_id=" + role_id + ", role_name=" + role_name + ", users=" + users + ", permissions="
				+ permissions + "]";
	}
}
