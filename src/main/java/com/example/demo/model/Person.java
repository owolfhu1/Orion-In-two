package com.example.demo.model;

import java.util.ArrayList;

/**
 * Created by student on 7/6/17.
 */
public class Person {

    private User user;
    private ArrayList<Edu> edus;
    private ArrayList<Work> works;
    private ArrayList<Skill> skills;

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public ArrayList<Edu> getEdus() {
        return edus;
    }

    public void setEdus(ArrayList<Edu> edus) {
        this.edus = edus;
    }

    public ArrayList<Work> getWorks() {
        return works;
    }

    public void setWorks(ArrayList<Work> works) {
        this.works = works;
    }

    public ArrayList<Skill> getSkills() {
        return skills;
    }

    public void setSkills(ArrayList<Skill> skills) {
        this.skills = skills;
    }
}
