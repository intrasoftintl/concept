package eu.concept.controller;

import eu.concept.repository.concept.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 *
 * @author Christos Paraskeva
 */
@Controller
public class ApplicationController {
    
    
    @Autowired
    UserService userService;
    

    @RequestMapping("/")
    public String index() {
        return "redirect:/login";
    }

    @RequestMapping("/login")
    public String login() {
        return "login";
    }

    @RequestMapping("/register")
    public String register() {
        return "registration";
    }
    
    
    
    @RequestMapping("/dashboard")
    public String dashboard() {
        return "topnav";
    }

    
    @RequestMapping("/test")
    public String test(){
       userService.findByUsernameAndPassword("admin","!admina!");
       
    
    return "/";
}
    

}
