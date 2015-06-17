package eu.concept.controller;


import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * Restful API for integration with Openproject
 *
 * @author Christos Paraskeva
 */
@RestController
@RequestMapping("/openproject/rest")
public class OPRestController {

    @Autowired
    ProjectService service;

//    @RequestMapping("/project")
//    public Project greeting(@RequestParam(value = "id") int id) {
//
//        return service.findProjectByID(id);
//}
    @RequestMapping(value = "/project/{id}", method = RequestMethod.GET)
    public ProjectOp fetchProjectByID(@PathVariable int id) {
        return service.findProjectByID(id);
    }

}