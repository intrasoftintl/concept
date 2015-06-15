package eu.concept.controller;

import eu.concept.repository.concept.domain.User;
import eu.concept.repository.concept.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Christos Paraskeva
 */
@Controller
public class ApplicationController {

    @Autowired
    UserService userService;

    /*
     *  GET Methods 
     */
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public String index() {
        return "redirect:/login";
    }

    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.GET)
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "registration";
    }

    @RequestMapping(value = "/dashboard", method = RequestMethod.GET)
    public String dashboard() {
        return "topnav";
    }

    @RequestMapping("/test")
    public String test() {
        userService.findByUsernameAndPassword("admin", "!admina!");

        return "/";
    }

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/login", method = RequestMethod.POST)
    public String loginSubmit(@ModelAttribute User user, Model model) {
        model.addAttribute(model);
        return "login";
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String registerSubmit(@ModelAttribute User user, Model model) {
        model.addAttribute("user", user);
        return "result";
    }

}
