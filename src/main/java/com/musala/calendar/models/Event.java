package com.musala.calendar.models;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "EVENTS")
public class Event {

    private Integer id;
    private String name;
    private String description;
    private String location;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private EventType eventType;
    private User user;

    public Event() {
    }

    public Event(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Event(String name, EventType eventType, User user) {
        this.name = name;
        this.eventType = eventType;
        this.user = user;
    }

    public Event(Integer id, String name, EventType eventType, User user) {
        this.id = id;
        this.name = name;
        this.eventType = eventType;
        this.user = user;
    }

    public Event(String name) {
        this.name = name;
    }

    public Event(Integer id,
                 String name,
                 String description,
                 String location,
                 LocalDateTime startDate,
                 LocalDateTime endDate, EventType eventType, User user) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.location = location;
        this.startDate = startDate;
        this.endDate = endDate;
        this.eventType = eventType;
        this.user = user;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "NAME")
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Column(name = "DESCRIPTION")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Column(name = "LOCATION")
    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Column(name = "START_DATE")
    public LocalDateTime getStartDate() {
        return startDate;
    }

    public void setStartDate(LocalDateTime startDate) {
        this.startDate = startDate;
    }

    @Column(name = "END_DATE")
    public LocalDateTime getEndDate() {
        return endDate;
    }

    public void setEndDate(LocalDateTime endDate) {
        this.endDate = endDate;
    }

    @ManyToOne
    @JoinColumn(name = "EVENT_TYPE_ID")
    @JsonIgnoreProperties("eventList")
    public EventType getEventType() {
        return eventType;
    }

    public void setEventType(EventType eventType) {
        this.eventType = eventType;
    }

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    @JsonIgnoreProperties("eventList")
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", location='" + location + '\'' +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Event event = (Event) o;
        return Objects.equals(id, event.id) &&
                Objects.equals(name, event.name) &&
                Objects.equals(description, event.description) &&
                Objects.equals(location, event.location) &&
                Objects.equals(startDate, event.startDate) &&
                Objects.equals(endDate, event.endDate) &&
                Objects.equals(eventType, event.eventType) &&
                Objects.equals(user, event.user);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, location, startDate, endDate, eventType, user);
    }
}
