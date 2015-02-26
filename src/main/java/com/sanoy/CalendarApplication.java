package com.sanoy;

import java.util.Date;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
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
		return new ModelAndView("index");
	}
	@RequestMapping(value="/calendar", method=RequestMethod.GET)
	public ModelAndView calendar() {
		return new ModelAndView("calendar");
	}
}

@Entity
class Event {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String title;
	private String description; 
	private Date start;
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
		// TODO Auto-generated constructor stub
	}
	@Override
	public String toString() {
		return "Event [id=" + id + ", title=" + title + ", description="
				+ description + ", start=" + start + ", end=" + end + "]";
	}
}

@Repository
interface EventRepository extends CrudRepository<Event, Long> {
	List<Event> findAll();
}

@RestController 
class EventController {
	
	@Autowired
	EventRepository eventRepository;
	
	@RequestMapping(value="/events", method=RequestMethod.GET)
	public List<Event> allEvents() {
		return eventRepository.findAll();
	}
	
}

