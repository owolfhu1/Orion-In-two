package com.example.demo.Controller;

import com.example.demo.model.*;
import com.example.demo.repositories.*;
import com.example.demo.services.UserService;
import com.example.demo.services.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.security.core.Authentication;

import javax.validation.Valid;
import java.util.ArrayList;

@Controller
public class HomeController {

    private UserValidator userValidator;
    private UserService userService;
    private UserRepository userRepository;
    private EduRepository eduRepository;
    private WorkRepository workRepository;
    private SkillRepository skillRepository;
    private JobRepository jobRepository;
    private NotificationRepository notificationRepository;

    @Autowired
    public HomeController(UserValidator userValidator, UserService userService, UserRepository userRepository, NotificationRepository notificationRepository
            , EduRepository eduRepository, WorkRepository workRepository, SkillRepository skillRepository, JobRepository jobRepository) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.userRepository = userRepository;
        this.eduRepository = eduRepository;
        this.workRepository = workRepository;
        this.skillRepository = skillRepository;
        this.jobRepository = jobRepository;
        this.notificationRepository = notificationRepository;
    }

    @RequestMapping("/")
    public String index() {
        return "index";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String showRegistrationPage(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String processRegistrationPage(@Valid @ModelAttribute("user") User user, BindingResult result, Model model) {
        model.addAttribute("user", user);
        userValidator.validate(user, result);
        if (result.hasErrors()) {
            return "registration";
        } else {
            userService.saveUser(user);
            model.addAttribute("message", "User Account Successfully Created");
        }
        return "index";
    }

    @RequestMapping("/builder")
    public String builder(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

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

    @RequestMapping("/delete/job/{id}")
    public String delJob(@PathVariable("id") Integer id) {
        jobRepository.delete(id);
        return "redirect:/post_job";
    }

    @RequestMapping("/delete/notification/{id}")
    public String delNotification(@PathVariable("id") Integer id) {
        notificationRepository.delete(id);
        return "index";
    }

    @RequestMapping("/job_search")
    public String jobSearch(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        model.addAttribute("job", new Job());
        model.addAttribute("person", user);
        model.addAttribute("jobs", new ArrayList<Job>());
        return "job_search";
    }

    @RequestMapping("/title_job_search")
    public String titleSearch(Job job, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        model.addAttribute("job", new Job());
        model.addAttribute("person", user);
        model.addAttribute("jobs", jobRepository.findAllByTitle(job.getTitle()));
        return "job_search";
    }

    @RequestMapping("/skill_job_search")
    public String skillSearch(Job job, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        model.addAttribute("job", new Job());
        model.addAttribute("person", user);
        model.addAttribute("jobs", jobRepository.findAllByRequirementsContaining(job.getRequirements()));
        return "job_search";
    }

    @RequestMapping("/post_job")
    public String postJob(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

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
        return "index";
    }

    @RequestMapping("/notifications")
    public String notifications(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        model.addAttribute("msgs", notificationRepository.findAllByUsername(user.getUsername()));
        model.addAttribute("person", user);

        return "notifications";
    }


    //for testing
    private void console(String format, Object... args) {
        format = "\n" + format + "\n";
        System.out.printf(format, args);
    }

    private void alertSeekers(Job job) {
        String[] skills = job.getRequirements().split(",");
        for(String skill : skills) {
            ArrayList<Skill> users = skillRepository.findAllByArea(skill);
            for (Skill s : users) {
                Notification notification = new Notification();
                notification.setMessage("A new job has been posted which requires the skill: " + skill);
                notification.setUsername(s.getUsername());
                notificationRepository.save(notification);
            }
        }
    }

    public UserValidator getUserValidator() {
        return userValidator;
    }
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }
}

/*

Recruiters can now post jobs - jobs have skill requirements as part of the posting
Users can search for jobs in addition to people, companies and schools
Users receive notifications when jobs that match their skills are posted

The requirements from the last challenge must still be met.



A job should contain:

=============================================================

A title,
an employer,
a salary range,
a description and
a list of skills.

 */