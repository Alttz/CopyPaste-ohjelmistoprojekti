package copypaste.ticketguru;

import static org.mockito.Mockito.*;

import java.sql.Date;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import copypaste.ticketguru.domain.Event;
import copypaste.ticketguru.domain.EventRepository;

@ExtendWith(MockitoExtension.class)
public class EventUnitTest {

    @Mock
    private EventRepository eventRepository;

    @Test
    public void testFindEventById() {
        // Arrange
        Event mockEvent = new Event(LocalDateTime.of(LocalDate.of(2023, 9, 28), LocalTime.now()), "Hartwallareena", "Helsinki", "Lordi", 1000);
        when(eventRepository.findById(1L)).thenReturn(Optional.of(mockEvent));

        // Act
        Optional<Event> result = eventRepository.findById(1L);

        // Assert
        assertTrue(result.isPresent(), "Event should be found");
        assertEquals("Lordi", result.get().getName(), "Event name should match");
    }
}
