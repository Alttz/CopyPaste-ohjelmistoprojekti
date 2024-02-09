package copypaste.ticketguru.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Permission {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long permission_id;

    private String permission_name;
    
	public Permission() {
	}

	public Permission(Long id, String permission_name) {
		this.permission_name = permission_name;
	}

	public Long getId() {
		return permission_id;
	}

	public void setId(Long id) {
		this.permission_id = id;
	}

	public String getPermission_name() {
		return permission_name;
	}

	public void setPermission_name(String permission_name) {
		this.permission_name = permission_name;
	}

	@Override
	public String toString() {
		return "Permission [id=" + permission_id + ", permission_name=" + permission_name + "]";
	}
}
