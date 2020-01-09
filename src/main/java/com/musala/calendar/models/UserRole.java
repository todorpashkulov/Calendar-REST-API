package com.musala.calendar.models;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@Entity
@Table(name = "USER_ROLES")
public class UserRole {
    private Integer id;
    private String name;
    private List<User> userList;

    public UserRole() {
    }

    public UserRole(String name) {
        this.name = name;
    }

    public UserRole(Integer id, String name, List<User> userList) {
        this.id = id;
        this.name = name;
        this.userList = userList;
    }

    public UserRole(Integer id, String name) {
        this.id = id;
        this.name = name;
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

    @OneToMany(mappedBy = "userRole", fetch = FetchType.EAGER)
    @JsonIgnoreProperties(value = "userRole", allowSetters = true)
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    public String roleString() {
        return name;
    }

    @Override
    public String toString() {
        return "UserRole{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", userList=" + userList +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserRole userRole = (UserRole) o;
        return Objects.equals(id, userRole.id) &&
                Objects.equals(name, userRole.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, userList);
    }
}
