package com.example.demo.Controller;

import com.example.demo.repositories.*;
import com.example.demo.services.UserService;
import com.example.demo.services.UserValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 * Created by student on 7/7/17.
 */

@Controller
public class JobSearchController {

    private UserValidator userValidator;
    private UserService userService;
    private UserRepository userRepository;
    private EduRepository eduRepository;
    private WorkRepository workRepository;
    private SkillRepository skillRepository;
    private JobRepository jobRepository;
    private NotificationRepository notificationRepository;

    @Autowired
    public JobSearchController(UserValidator userValidator, UserService userService, UserRepository userRepository, NotificationRepository notificationRepository
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








}
