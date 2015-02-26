#boot-calendar

##Description

This is a demonstration of the spring-boot framework. This example shows how to build a spring-boot calendar web application with the fullcalendar.js and jQuery libraries. The web application persists Event data with JPA and hibernate and supplies a rest interface for retrieving Event objects from the web server and displaying those Events in a browser calendar.  

##Prerequists

This project uses Spring Tool Suite for development with spring-boot installed. 

All methods have been added inline to the Application class. (/src/com/sanoy/CalendarApplication)

##Instructions

1. Create a Spring boot application in Spring Tool Suite. Select the Web, Thymeleaf, JPA, HSQLDB dependancies. 


1. Add Support for web mvc and thymeleaf to the pom.xml file (if they don't already exist from the step above)

``` xml
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-web</artifactId>
	</dependency>
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-starter-thymeleaf</artifactId>
	</dependency>
```

1. Turn off thymeleaf caching so that changes to html files will reload 

add the following line to the application.properties file. (/src/main/application.properties)

```
spring.thymeleaf.cache=false
	
```
1. And a thymeleaf index.html to thymeleaf templates  (/src/main/resources/templates)

```html
<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-spring4-4.dtd">
<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
	<meta charset='utf-8' content="Cory Sanoy" name="Author" />
</head>
<body>
Hello World!
</body>
</html>

```

1. Add a controller for the index.html file

``` java
@Controller	
class CalendarController {
	@RequestMapping(value="/", method=RequestMethod.GET) 
	public ModelAndView index() {
		ModelAndView modelAndView = new ModelAndView("index");
		return modelAndView;
	}
}
```

1.	Create JPA Entity for an event

``` java
@Entity
class Event {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	private Long id;
	
	private String title;
	private String description; 
	private Date start;
	private Date end;
}
```
1.	Build the repository

``` java
interface EventRepository extends  CrudRepository<Event, Long> {
    List<Event> findAll();
}
```	
1. Add Hibernate libraries to pom.xml

``` xml
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-entitymanager</artifactId>
		</dependency>
		<dependency>
			<groupId>org.hsqldb</groupId>
			<artifactId>hsqldb</artifactId>
			<scope>runtime</scope>
		</dependency>
		<dependency>
			<groupId>org.springframework</groupId>
			<artifactId>spring-orm</artifactId>
		</dependency>
		<dependency>
			<groupId>org.springframework.boot</groupId>
			<artifactId>spring-boot-starter-data-jpa</artifactId>
		</dependency>
		<dependency>
			<groupId>org.hibernate</groupId>
			<artifactId>hibernate-validator</artifactId>
		</dependency>
```	
1.	Add import.sql with events to the /src/main/resources

``` javascript
	
insert into event(id, title, start, end, description) values (1, 'event1', '2015-01-01 1:00:00', '2015-01-01 2:00:00', 'description1')
insert into event(id, title, start, end, description) values (2, 'event2', '2015-01-02 2:00:00', '2015-01-02 3:00:00', 'description1')
insert into event(id, title, start, end, description) values (3, 'event3', '2015-01-03 1:00:00', '2015-01-03 2:00:00', 'description1')
insert into event(id, title, start, end, description) values (4, 'event4', '2015-01-04 1:00:00', '2015-01-04 2:00:00', 'description1')

```
	
1.	Build the Rest controller to provide a list of Events

@RestController
class EventController {
	
	@Autowired
	private EventRepository eventRespository;
	
	@RequestMapping(value="/events", method=RequestMethod.GET)
	public List<Event> events() {
		return eventRespository.findAll();
	}
}

1. 	Add the webjars for fullcalendar, moment, jquery to the pom.xml file. 

``` xml
		<dependency>
    		<groupId>org.webjars</groupId>
    		<artifactId>fullcalendar</artifactId>
    		<version>2.2.5</version>
		</dependency>
		<dependency>
		    <groupId>org.webjars</groupId>
		    <artifactId>momentjs</artifactId>
		    <version>2.9.0</version>
		</dependency>
		<dependency>
		    <groupId>org.webjars</groupId>
		    <artifactId>jquery-ui</artifactId>
		    <version>1.11.2</version>
		</dependency>
		<dependency>
    		<groupId>org.webjars</groupId>
		    <artifactId>jquery</artifactId>
		    <version>2.1.3</version>
		</dependency>
```

9.	Add the calendar.html file.

``` html

<!DOCTYPE html SYSTEM "http://www.thymeleaf.org/dtd/xhtml1-strict-thymeleaf-spring4-4.dtd">

<html xmlns="http://www.w3.org/1999/xhtml"
      xmlns:th="http://www.thymeleaf.org">
<head>
	<meta charset='utf-8' content="Cory Sanoy" name="Author" />

	<link href="http://fullcalendar.io/js/fullcalendar-2.2.5/fullcalendar.css" 
			th:href="@{/webjars/fullcalendar/2.2.5/fullcalendar.css}" rel="stylesheet"></link>
	<link href="http://fullcalendar.io/js/fullcalendar-2.2.5/fullcalendar.print.css" 
			th:href="@{/webjars/fullcalendar/2.2.5/fullcalendar.print.css}" rel="stylesheet" media="print"></link>
	<script src="http://cdnjs.cloudflare.com/ajax/libs/moment.js/2.9.0/moment.min.js"
	        th:src="@{/webjars/momentjs/2.9.0/min/moment.min.js}" type="text/javascript"></script>
	<script src="http://cdn.jsdelivr.net/webjars/jquery/2.1.3/jquery.min.js"
	        th:src="@{/webjars/jquery/2.1.3/jquery.min.js}" type="text/javascript"></script>
	<script src="http://fullcalendar.io/js/fullcalendar-2.2.5/fullcalendar.min.js"
			th:src="@{/webjars/fullcalendar/2.2.5/fullcalendar.min.js}" type="text/javascript"></script>

<script>
<!--
$(document).ready(function() {

	$('#calendar').fullCalendar({
		defaultDate: '2014-11-01',
		editable: true,
		eventLimit: true, // allow "more" link when too many events
		events: [
			{
				title: 'All Day Event',
				start: '2014-11-01'
			},
			{
				title: 'Long Event',
				start: '2014-11-07',
				end: '2014-11-10'
			},
			{
				id: 999,
				title: 'Repeating Event',
				start: '2014-11-09T16:00:00'
			},
			{
				id: 999,
				title: 'Repeating Event',
				start: '2014-11-16T16:00:00'
			},
			{
				title: 'Conference',
				start: '2014-11-11',
				end: '2014-11-13'
			},
			{
				title: 'Meeting',
				start: '2014-11-12T10:30:00',
				end: '2014-11-12T12:30:00'
			},
			{
				title: 'Lunch',
				start: '2014-11-12T12:00:00'
			},
			{
				title: 'Meeting',
				start: '2014-11-12T14:30:00'
			},
			{
				title: 'Happy Hour',
				start: '2014-11-12T17:30:00'
			},
			{
				title: 'Dinner',
				start: '2014-11-12T20:00:00'
			},
			{
				title: 'Birthday Party',
				start: '2014-11-13T07:00:00'
			},
			{
				title: 'Click for Google',
				url: 'http://google.com/',
				start: '2014-11-28'
			}
		]
	});
	
});
-->
</script>

<style>

	body {
		margin: 40px 10px;
		padding: 0;
		font-family: "Lucida Grande",Helvetica,Arial,Verdana,sans-serif;
		font-size: 14px;
	}

	#calendar {
		max-width: 900px;
		margin: 0 auto;
	}

</style>			
</head>
<body>
	<div id='calendar' th:id="calendar"></div>
</body>
</html>
```

11. Change the js code within the calendar.html file to load the Events from the rest interface. 

``` javascript
	$('#calendar').fullCalendar({
		header: {
			left: 'prev,next today',
			center: 'title',
			right: 'month,agendaWeek,agendaDay'
		},
		defaultDate: '2015-01-01',
		editable: true,
		eventLimit: true, // allow "more" link when too many events
		events: {
	        url: '/events',
	        type: 'GET',
	        error: function() {
	            alert('there was an error while fetching events!');
	        },
	        //color: 'blue',   // a non-ajax option
	        //textColor: 'white' // a non-ajax option
	    }
	});
```

