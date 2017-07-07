package com.example.demo.controllers;

import com.example.demo.models.Edu;
import com.example.demo.models.Skill;
import com.example.demo.models.User;
import com.example.demo.models.Work;
import com.example.demo.models.no.database.Person;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

/**
 * Created by student on 7/7/17.
 */

@Controller
public class PeopleSearchController {
    private UserRepository userRepository;
    private EduRepository eduRepository;
    private WorkRepository workRepository;
    private SkillRepository skillRepository;

    @Autowired
    public PeopleSearchController(UserRepository userRepository, EduRepository eduRepository
            , WorkRepository workRepository, SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.eduRepository = eduRepository;
        this.workRepository = workRepository;
        this.skillRepository = skillRepository;
    }

    @RequestMapping("/person_search")
    public String personSearch(Model model, Authentication authentication) {

        model.addAttribute("people", new ArrayList<Person>());
        model.addAttribute("user", new User());
        model.addAttribute("person", getUser(authentication));
        return "person_search";
    }

    @RequestMapping("/person_person_search")
    public String personPersonSearch(User user, Model model, Authentication authentication) {

        ArrayList<Person> people = new ArrayList<>();

        if (userRepository.existsByUsername(user.getUsername())) {
            Person person = new Person(
                    userRepository.findByUsername(user.getUsername()),
                    eduRepository.findAllByUsername(user.getUsername()),
                    workRepository.findAllByUsername(user.getUsername()),
                    skillRepository.findAllByUsername(user.getUsername())
            );
            people.add(person);
        }
        model.addAttribute("person", getUser(authentication));
        model.addAttribute("people", people);
        model.addAttribute("user", new User());

        return "person_search";
    }

    @RequestMapping("/company_person_search")
    public String companyPersonSearch(User user, Model model, Authentication authentication) {
        ArrayList<Person> people = new ArrayList<>();

        for (Work work : workRepository.findAllByCompany(user.getUsername())) {
            Person person = new Person(
                    userRepository.findByUsername(work.getUsername()),
                    eduRepository.findAllByUsername(work.getUsername()),
                    workRepository.findAllByUsername(work.getUsername()),
                    skillRepository.findAllByUsername(work.getUsername())
            );
            people.add(person);
        }
        model.addAttribute("person", getUser(authentication));
        model.addAttribute("people", people);
        model.addAttribute("user", new User());
        return "person_search";
    }

    @RequestMapping("/school_person_search")
    public String schoolPersonSearch(User user, Model model, Authentication authentication) {
        ArrayList<Person> people = new ArrayList<>();
        for (Edu edu : eduRepository.findAllBySchool(user.getUsername())) {
            Person person = new Person(
                    userRepository.findByUsername(edu.getUsername()),
                    eduRepository.findAllByUsername(edu.getUsername()),
                    workRepository.findAllByUsername(edu.getUsername()),
                    skillRepository.findAllByUsername(edu.getUsername())
            );
            people.add(person);
        }
        model.addAttribute("person", getUser(authentication));
        model.addAttribute("people", people);
        model.addAttribute("user", new User());
        return "person_search";
    }

    @RequestMapping("/skill_person_search")
    public String skillPersonSearch(User user, Model model, Authentication authentication) {
        ArrayList<Person> people = new ArrayList<>();

        for (Skill skill : skillRepository.findAllByArea(user.getUsername())) {
            Person person = new Person(
                    userRepository.findByUsername(skill.getUsername()),
                    eduRepository.findAllByUsername(skill.getUsername()),
                    workRepository.findAllByUsername(skill.getUsername()),
                    skillRepository.findAllByUsername(skill.getUsername())
            );
            people.add(person);
        }
        model.addAttribute("person", getUser(authentication));
        model.addAttribute("people", people);
        model.addAttribute("user", new User());

        return "person_search";
    }

    @RequestMapping("/show_all_persons")
    public String showAllPeople(Model model, Authentication authentication) {
        ArrayList<Person> people = new ArrayList<>();
        for (User u : userRepository.findAllByRole("target")) {
            Person person = new Person(
                    userRepository.findByUsername(u.getUsername()),
                    eduRepository.findAllByUsername(u.getUsername()),
                    workRepository.findAllByUsername(u.getUsername()),
                    skillRepository.findAllByUsername(u.getUsername())
            );
            people.add(person);
        }
        model.addAttribute("person", getUser(authentication));
        model.addAttribute("people", people);
        model.addAttribute("user", new User());
        return "person_search";

    }

    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }

}
