package eu.concept.controller;

import eu.concept.repository.openproject.domain.MemberOp;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.MemberOpService;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import java.util.List;
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
@RequestMapping("/concept/rest")
public class RestAPIController {

    @Autowired
    ProjectServiceOp service;

    @Autowired
    MemberOpService members;

//    @RequestMapping("/project")
//    public Project greeting(@RequestParam(value = "id") int id) {
//
//        return service.findProjectByID(id);
//}
    @RequestMapping(value = "/memberships/{project_id}", method = RequestMethod.GET)
    public List<MemberOp> fetchProjectByID(@PathVariable int project_id) {
        return members.fetchMemberhipsByProjectId(project_id);
    }

}
