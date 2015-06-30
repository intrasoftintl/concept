package eu.concept.controller;

import eu.concept.repository.concept.domain.UserCo;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public class RepositoryController {

    /*
     *  POST Methods 
     */
    @RequestMapping(value = "/API/members", method = RequestMethod.POST)
    public String loginSubmit(@ModelAttribute UserCo user, Model model) {
        model.addAttribute(model);
        return "login";
    }

}
