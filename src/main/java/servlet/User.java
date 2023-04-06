package servlet;

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

public class User {
    private Integer id;
    private String name;
    private String city;
    private String work;
    private String university;
    private String school;
    private String type;
    private List<Group> groups = new ArrayList<>();
    private List<User> friends = new ArrayList<>();

    private static User ourInstance = new User();
    public static User getInstance() {
        return ourInstance;
    }

    private User() {
    }

    public User(String name, Integer id) {
        this.name = name;
        this.id = id;
        this.city = "не указано";
        this.work = "не указано";
        this.school = "не указано";
        this.university = "не указано";
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getSchool() {
        return school;
    }

    public void setSchool(String school) {
        this.school = school;
    }

    public String getUniversity() {
        return university;
    }

    public void setUniversity(String university) {
        this.university = university;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setFriends(List<User> friends) {
        this.friends = friends;
    }
}