package com.example.demo.controllers;

import com.example.demo.models.Job;
import com.example.demo.models.Notification;
import com.example.demo.models.Skill;
import com.example.demo.models.User;
import com.example.demo.repositories.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;

/**
 * Created by student on 7/7/17.
 */
@Controller
public class JobController {


    private UserRepository userRepository;
    private SkillRepository skillRepository;
    private JobRepository jobRepository;
    private NotificationRepository notificationRepository;

    @Autowired
    public JobController(UserRepository userRepository, NotificationRepository notificationRepository
            , SkillRepository skillRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.notificationRepository = notificationRepository;
    }

    @RequestMapping("/apply/{employer}/{jobId}")
    public String apply(@PathVariable("employer") String employer, @PathVariable("jobId") int jobId, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        Job job = jobRepository.findOne(jobId);
        Notification notification = new Notification();
        notification.setUsername(employer);
        notification.setMessage(String.format("User %s applied to job listing '%s'", user.getUsername(), job.getTitle()));
        notificationRepository.save(notification);
        return "redirect:/job_search";
    }//TODO make this not return anything if possible

    @RequestMapping("/post_job")
    public String postJob(Model model, Authentication authentication) {
        User user = getUser(authentication);
        model.addAttribute("job", new Job());
        model.addAttribute("person", user);
        model.addAttribute("jobs", jobRepository.findAllByEmployer(user.getUsername()));
        return "post_job";
    }

    @RequestMapping("/new_job")
    public String newJob(Job job, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
        job.setEmployer(user.getUsername());
        alertSeekers(job);
        jobRepository.save(job);
        return "redirect:/post_job";
    }

    @RequestMapping("/delete/job/{id}")
    public String delJob(@PathVariable("id") Integer id) {
        jobRepository.delete(id);
        return "redirect:/post_job";
    }

    private void alertSeekers(Job job) {
        String[] skills = job.getRequirements().split(",");
        for(String skill : skills) {
            ArrayList<Skill> users = skillRepository.findAllByArea(skill);
            for (Skill s : users) {
                Notification notification = new Notification();
                notification.setMessage(String.format("A new job has been posted by %s requiring the skill: %s", job.getEmployer(), skill));
                notification.setUsername(s.getUsername());
                notificationRepository.save(notification);
            }
        }
    }

    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }

}
