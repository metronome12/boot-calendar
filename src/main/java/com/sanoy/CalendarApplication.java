package com.sanoy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

@SpringBootApplication
public class CalendarApplication {

    public static void main(String[] args) {
        SpringApplication.run(CalendarApplication.class, args);
    }
}

@Controller
class CalendarController {
	
	@Autowired
	private EventJpaRepository eventRepository; 
	
	@RequestMapping(value="/", method=RequestMethod.GET) 
	public ModelAndView index() {
		return new ModelAndView("index");
	}
	
	@RequestMapping(value="/staticcalendar", method=RequestMethod.GET) 
	public ModelAndView staticcalendar() {
		return new ModelAndView("staticcalendar");
	}
	
	@RequestMapping(value="/calendar", method=RequestMethod.GET) 
	public ModelAndView calendar() {
		return new ModelAndView("calendar");
	}
	
	@RequestMapping(value="/jsoncalendar", method=RequestMethod.GET) 
	public ModelAndView jsoncalendar() {
		return new ModelAndView("jsoncalendar");
	}
	
	@RequestMapping(value="/eventlist", method=RequestMethod.GET) 
	public String events(HttpServletRequest request, Model model) {
		model.addAttribute("events", eventRepository.findAll());
		return "events";
	}
}

@Entity
@Table(name="event")
class Event {
	
	@Id 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	private String title; 
	private String description;
	private LocalDateTime start; 
	private LocalDateTime finish;
	
	public Event(Long id, String title, String description, LocalDateTime start, LocalDateTime finish) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.start = start;
		this.finish = finish;
	}
	
	public Event() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public LocalDateTime getStart() {
		return start;
	}

	public void setStart(LocalDateTime start) {
		this.start = start;
	}

	public LocalDateTime getFinish() {
		return finish;
	}

	public void setFinish(LocalDateTime finish) {
		this.finish = finish;
	}

	@Override
	public String toString() {
		return "Event [id=" + id + ", title=" + title + ", description=" + description + ", start=" + start
				+ ", finish=" + finish + "]";
	} 	
}


@Repository
interface EventJpaRepository extends JpaRepository<Event, Long> {
	
	/* Note these two methods do the same thing.  The @Repository annotation handles both*/
	
	
	/* This one uses a JPQL */
	public List<Event> findByStartGreaterThanEqualAndFinishLessThanEqual(LocalDateTime start, LocalDateTime end);
	
	
	/* This one uses an @Query */
	@Query("select b from Event b where b.start >= ?1 and b.finish <= ?2")
	public List<Event> findByDateBetween(LocalDateTime start, LocalDateTime end);
	
}

@RestController 
class EventController {
	
	@Autowired
	private EventJpaRepository eventRepository;
	
	@RequestMapping(value="/allevents", method=RequestMethod.GET)
	public List<Event> allEvents() {
		return eventRepository.findAll();
	}
	
	@RequestMapping(value="/event", method=RequestMethod.POST)
	public ResponseEntity<Event> addEvent(@RequestBody Event event) {
		return ResponseEntity.ok(eventRepository.save(event));
	}

	@RequestMapping(value="/event", method=RequestMethod.PATCH)
	public ResponseEntity<Event> updateEvent(@RequestBody Event event) {
		return ResponseEntity.ok(eventRepository.save(event));
	}

	@RequestMapping(value="/event", method=RequestMethod.DELETE)
	public ResponseEntity<Void> removeEvent(@RequestBody Event event) {
		eventRepository.delete(event);
		return ResponseEntity.noContent().build();
	}
	
	@RequestMapping(value="/events", method=RequestMethod.GET)
	public List<Event> getEventsInRange(@RequestParam(value = "start", required = true) String start, 
										@RequestParam(value = "end", required = true) String end) {
		Date startDate = null;
		Date endDate = null;
		SimpleDateFormat inputDateFormat=new SimpleDateFormat("yyyy-MM-dd");
		
		try {
			startDate = inputDateFormat.parse(start);
		} catch (ParseException e) {
			throw new BadDateFormatException("bad start date: " + start);
		}
		
		try {
			endDate = inputDateFormat.parse(end);
		} catch (ParseException e) {
			throw new BadDateFormatException("bad end date: " + end);
		}
		
		LocalDateTime startDateTime = LocalDateTime.ofInstant(startDate.toInstant(),
                ZoneId.systemDefault());
		
		LocalDateTime endDateTime = LocalDateTime.ofInstant(endDate.toInstant(),
                ZoneId.systemDefault());
		
		return eventRepository.findByDateBetween(startDateTime, endDateTime); 
	}
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadDateFormatException extends RuntimeException {
  private static final long serialVersionUID = 1L;

	public BadDateFormatException(String dateString) {
        super(dateString);
    }
}

