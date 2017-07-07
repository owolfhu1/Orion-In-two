package com.example.demo.model.no.database;

import com.example.demo.model.Edu;
import com.example.demo.model.Skill;
import com.example.demo.model.User;
import com.example.demo.model.Work;

import java.util.ArrayList;

/**
 * Created by student on 7/6/17.
 */

public class Person {

    private User user;
    private ArrayList<Edu> edus;
    private ArrayList<Work> works;
    private ArrayList<Skill> skills;

    public Person(User user, ArrayList<Edu> edus, ArrayList<Work> works, ArrayList<Skill> skills) {
        this.user = user;
        this.edus = edus;
        this.works = works;
        this.skills = skills;
    }
    public Person() {
    }

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
