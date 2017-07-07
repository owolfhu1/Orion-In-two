package com.example.demo.controllers;

import com.example.demo.models.Edu;
import com.example.demo.models.Skill;
import com.example.demo.models.User;
import com.example.demo.models.Work;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * Created by student on 7/7/17.
 */

@Controller
public class ResumeController {
    private UserRepository userRepository;
    private EduRepository eduRepository;
    private WorkRepository workRepository;
    private SkillRepository skillRepository;

    @Autowired
    public ResumeController(UserRepository userRepository, EduRepository eduRepository
            , WorkRepository workRepository, SkillRepository skillRepository) {
        this.userRepository = userRepository;
        this.eduRepository = eduRepository;
        this.workRepository = workRepository;
        this.skillRepository = skillRepository;
    }

    @RequestMapping("/builder")
    public String builder(Model model, Authentication authentication) {
        User user = getUser(authentication);
        model.addAttribute("edu", new Edu());
        model.addAttribute("work", new Work());
        model.addAttribute("skill", new Skill());
        model.addAttribute("person", user);
        model.addAttribute("edus", eduRepository.findAllByUsername(user.getUsername()));
        model.addAttribute("works", workRepository.findAllByUsername(user.getUsername()));
        model.addAttribute("skills", skillRepository.findAllByUsername(user.getUsername()));
        return "builder";
    }

    @RequestMapping("/new_edu")
    public String addEdu(Edu edu, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        edu.setUsername(user.getUsername());
        eduRepository.save(edu);
        return "redirect:/builder";
    }

    @RequestMapping("/new_work")
    public String addWork(Work work, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        work.setUsername(user.getUsername());
        workRepository.save(work);
        return "redirect:/builder";
    }

    @RequestMapping("/new_skill")
    public String addSkill(Skill skill, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        skill.setUsername(user.getUsername());
        skillRepository.save(skill);
        return "redirect:/builder";
    }

    @RequestMapping("/delete/work/{id}")
    public String delWork(@PathVariable("id") Integer id) {
        workRepository.delete(id);
        return "redirect:/builder";
    }

    @RequestMapping("/delete/edu/{id}")
    public String delEdu(@PathVariable("id") Integer id) {
        eduRepository.delete(id);
        return "redirect:/builder";
    }

    @RequestMapping("/delete/skill/{id}")
    public String delSkill(@PathVariable("id") Integer id) {
        skillRepository.delete(id);
        return "redirect:/builder";
    }

    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }

}
