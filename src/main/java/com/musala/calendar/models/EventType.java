package com.musala.calendar.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "EVENT_TYPE")
public class EventType {

    private Integer id;
    private String name;
    private String info;
    private List<Event> eventList;

    public EventType() {
    }

    public EventType(String name) {
        this.name = name;
    }

    public EventType(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public EventType(Integer id, String name, String info, List<Event> eventList) {
        this.id = id;
        this.name = name;
        this.info = info;
        this.eventList = eventList;
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

    @Column(name = "INFO")
    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    @OneToMany(mappedBy = "eventType")
    @JsonIgnoreProperties(value = "event", allowSetters = true)
    public List<Event> getEventList() {
        return eventList;
    }

    public void setEventList(List<Event> eventList) {
        this.eventList = eventList;
    }

    @Override
    public String toString() {
        return "EventType{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", info='" + info + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        EventType eventType = (EventType) o;
        return Objects.equals(id, eventType.id) &&
                Objects.equals(name, eventType.name) &&
                Objects.equals(info, eventType.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, info, eventList);
    }
}
