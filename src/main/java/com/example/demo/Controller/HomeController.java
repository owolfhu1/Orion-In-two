package com.example.demo.Controller;

import com.example.demo.model.*;
import com.example.demo.model.no.database.Person;
import com.example.demo.repositories.*;
import com.example.demo.services.UserService;
import com.example.demo.services.UserValidator;
import jdk.nashorn.internal.scripts.JO;
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
    public String login(Model modle) {
        modle.addAttribute("message", "Welcome to Job Hunter");
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
        if (result.hasErrors())
            return "registration";
        else {
            if (user.getRole().equals("target")) {
                userService.saveTarget(user);
                model.addAttribute("message", "Welcome new target, good luck!");
            } else {
                userService.saveHunter(user);
                model.addAttribute("message", "Welcome new hunter, happy hunting!");
            }
            return "login";
        }

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
        return "redirect:/notifications";
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

    @RequestMapping("/description_job_search")
    public String descriptionSearch(Job job, Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());
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
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        ArrayList<Job> jobs = (ArrayList<Job>) jobRepository.findAll();

        model.addAttribute("job", new Job());
        model.addAttribute("person", user);
        model.addAttribute("jobs", jobs);
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
        return "redirect:/post_job";
    }

    @RequestMapping("/notifications")
    public String notifications(Model model, Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        User user = userRepository.findByUsername(userDetails.getUsername());

        model.addAttribute("msgs", notificationRepository.findAllByUsername(user.getUsername()));
        model.addAttribute("person", user);

        return "notifications";
    }

    @RequestMapping("/person_search")
    public String personSearch(Model model) {

        model.addAttribute("people", new ArrayList<Person>());
        model.addAttribute("user", new User());

        return "person_search";
    }

    @RequestMapping("/person_person_search")
    public String personPersonSearch(User user, Model model) {

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

        model.addAttribute("people", people);
        model.addAttribute("user", new User());

        return "person_search";
    }

    @RequestMapping("/company_person_search")
    public String companyPersonSearch(User user, Model model) {
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

        model.addAttribute("people", people);
        model.addAttribute("user", new User());

        return "person_search";
    }

    @RequestMapping("/school_person_search")
    public String schoolPersonSearch(User user, Model model) {
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

        model.addAttribute("people", people);
        model.addAttribute("user", new User());

        return "person_search";
    }

    @RequestMapping("/skill_person_search")
    public String skillPersonSearch(User user, Model model) {
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

        model.addAttribute("people", people);
        model.addAttribute("user", new User());

        return "person_search";
    }

    @RequestMapping("/show_all_persons")
    public String showAllPeople(Model model) {

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

        model.addAttribute("people", people);
        model.addAttribute("user", new User());

        return "person_search";

    }

    private void console(String format, Object... args) {
        format = "\n" + format + "\n";
        System.out.printf(format, args);
    }

    private boolean hasJob(ArrayList<Job> jobs, int id) {
        for (Job j : jobs)
            if (j.getId() == id)
                return true;
        return false;
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

    public UserValidator getUserValidator() {
        return userValidator;
    }
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }
}