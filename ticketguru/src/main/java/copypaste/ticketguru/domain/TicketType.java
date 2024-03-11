package copypaste.ticketguru.domain;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

@Entity
@Table(name = "ticket_types")
public class TicketType {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@NotBlank(message = "Name must not be blank")
	private String name;

	@DecimalMin(value = "0.0", inclusive = false, message = "Price must be greater than zero")
	private double price;

    @ManyToOne
    @JoinColumn(name = "event_id", nullable = false)
    @JsonIgnore // Ignorataan vakio Getter kun JSON vastaus luodaan
    private Event event;

	public TicketType() {
		super();
	}

	public TicketType(String name, double price, Event event) {
		super();
		this.name = name;
		this.price = price;
		this.event = event;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public Event getEvent() {
		return event;
	}

	public void setEvent(Event event) {
		this.event = event;
	}
	
	@JsonGetter("event") // palautetaan vain ID
    public Long getEventId() {
        return this.event != null ? this.event.getId() : null;
    }

	@Override
	public String toString() {
		return "TicketType [id=" + id + ", name=" + name + ", price=" + price + ", event=" + event + "]";
	}
    
    

}
