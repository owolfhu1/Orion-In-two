package com.example.demo.controllers;

import com.example.demo.models.*;
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

@Controller
public class HomeController {

    private UserValidator userValidator;
    private UserService userService;
    private UserRepository userRepository;
    private NotificationRepository notificationRepository;

    @Autowired
    public HomeController(UserValidator userValidator, UserService userService, UserRepository userRepository
            , NotificationRepository notificationRepository) {
        this.userValidator = userValidator;
        this.userService = userService;
        this.userRepository = userRepository;
        this.notificationRepository = notificationRepository;
    }

    @RequestMapping("/")
    public String index(Authentication authentication, Model model) {
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

    /* =======================
     =======Notifications=====
     =========================*/

    @RequestMapping("/delete/notification/{id}")
    public String delNotification(@PathVariable("id") Integer id) {
        notificationRepository.delete(id);
        return "redirect:/notifications";
    }

    @RequestMapping("/notifications")
    public String notifications(Model model, Authentication authentication) {
        User user = getUser(authentication);
        model.addAttribute("msgs", notificationRepository.findAllByUsername(user.getUsername()));
        model.addAttribute("person", user);
        return "notifications";
    }



    /* =======================
     ========Validators=======
     =========================*/

    public UserValidator getUserValidator() {
        return userValidator;
    }
    public void setUserValidator(UserValidator userValidator) {
        this.userValidator = userValidator;
    }

    private User getUser(Authentication authentication) {
        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return userRepository.findByUsername(userDetails.getUsername());
    }


}