package com.sanoy;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
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
	
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ModelAndView index() {
		return new ModelAndView("jsoncalendar");
	}
	
}

@Entity
@Table(name = "Event")
class Event {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String title;
	private String description; 
	
	@Column(name="start")
	private Date start;
	
	@Column(name="end")
	private Date end;
	
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
	public Date getStart() {
		return start;
	}
	public void setStart(Date start) {
		this.start = start;
	}
	public Date getEnd() {
		return end;
	}
	public void setEnd(Date end) {
		this.end = end;
	}
	public Event(Long id, String title, String description, Date start, Date end) {
		super();
		this.id = id;
		this.title = title;
		this.description = description;
		this.start = start;
		this.end = end;
	}
	public Event() {
		super();
	}
	@Override
	public String toString() {
		return "Event [id=" + id + ", title=" + title + ", description="
				+ description + ", start=" + start + ", end=" + end + "]";
	}
}

interface EventRepository extends CrudRepository<Event, Long> {
	List<Event> findAll();
	Event save(Event event);
	void delete(Event event);

	@Query("select b from Event b " +
	         "where b.start between ?1 and ?2 and b.end between ?1 and ?2")
	 List<Event> findByDatesBetween(Date start, Date end);
}

@RestController 
class EventController {
	
	@Autowired
	EventRepository eventRepository;
	
	@RequestMapping(value="/allevents", method=RequestMethod.GET)
	public List<Event> allEvents() {
		return eventRepository.findAll();
	}
	
	@RequestMapping(value="/event", method=RequestMethod.POST)
	public Event addEvent(@RequestBody Event event) {
		Event created = eventRepository.save(event);
		return created; 
	}
	
	@RequestMapping(value="/event", method=RequestMethod.PATCH)
	public Event updateEvent(@RequestBody Event event) {
		return eventRepository.save(event);
	}
	
	@RequestMapping(value="/event", method=RequestMethod.DELETE)
	public void removeEvent(@RequestBody Event event) {
		eventRepository.delete(event);
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
		
		return eventRepository.findByDatesBetween(startDate, endDate); 
	}
	
}

@ResponseStatus(HttpStatus.BAD_REQUEST)
class BadDateFormatException extends RuntimeException {
  private static final long serialVersionUID = 1L;

	public BadDateFormatException(String dateString) {
        super(dateString);
    }
}

