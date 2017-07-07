package com.example.demo.controllers;

import com.example.demo.models.Job;
import com.example.demo.models.User;
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
public class JobSearchController {
    private UserRepository userRepository;
    private JobRepository jobRepository;

    @Autowired
    public JobSearchController(UserRepository userRepository, JobRepository jobRepository) {
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    @RequestMapping("/job_search")
    public String jobSearch(Model model, Authentication authentication) {
        User user = getUser(authentication);
        model.addAttribute("job", new Job());
        model.addAttribute("person", user);
        model.addAttribute("jobs", new ArrayList<Job>());
        return "job_search";
    }

    @RequestMapping("/title_job_search")
    public String titleSearch(Job job, Model model, Authentication authentication) {
        User user = getUser(authentication);
        model.addAttribute("job", new Job());
        model.addAttribute("person", user);
        model.addAttribute("jobs", jobRepository.findAllByTitle(job.getTitle()));
        return "job_search";
    }

    @RequestMapping("/skill_job_search")
    public String skillSearch(Job job, Model model, Authentication authentication) {
        User user = getUser(authentication);

        model.addAttribute("job", new Job());
        model.addAttribute("person", user);
        model.addAttribute("jobs", jobRepository.findAllByRequirementsContaining(job.getRequirements()));
        return "job_search";
    }

    @RequestMapping("/description_job_search")
    public String descriptionSearch(Job job, Model model, Authentication authentication) {
        User user = getUser(authentication);
        ArrayList<Job> jobs = new ArrayList<>();
        String[] keyWords = job.getDescription().split(",");
        for (String key : keyWords)
            for (Job j : jobRepository.findAllByDescriptionContaining(key))
                if(!hasJob(jobs, j.getId()))
                    jobs.add(j);
        model.addAttribute("job", new Job());
        model.addAttribute("person", user);
        model.addAttribute("jobs", jobs);
        return "job_search";
    }

    @RequestMapping("/show_all_jobs")
    public String showAllJobs(Model model, Authentication authentication) {
        User user = getUser(authentication);
        ArrayList<Job> jobs = (ArrayList<Job>) jobRepository.findAll();
        model.addAttribute("job", new Job());
        model.addAttribute("person", user);
        model.addAttribute("jobs", jobs);
        return "job_search";
    }

    private boolean hasJob(ArrayList<Job> jobs, int id) {
        for (Job j : jobs)
            if (j.getId() == id)
                return true;
        return false;
    }

    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }

}
