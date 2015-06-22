package eu.concept.repository.openproject.service;

import eu.concept.repository.openproject.dao.ProjectOpRepository;
import eu.concept.repository.openproject.domain.ProjectOp;
import java.util.List;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author Christos Paraskeva
 */
@Service
@Transactional
public class ProjectServiceOp {


    @Autowired
    ProjectOpRepository projectRepo;

    /**
     *
     * @return
     */
    public List<ProjectOp> findAllProjects() {
        return projectRepo.findAll();
    }

    /**
     *
     * @param projectID
     * @return
     */
    public ProjectOp findProjectByID(int projectID) {
        return projectRepo.findOne(projectID);
    }

    /**
     * Fetch all the projects that a user has access to
     *
     * @param userId The id of the user to search for projects
     * @return A List of ProjectOp objects
     */
    public List<ProjectOp> findProjectsByUserId(int userId) {
        return projectRepo.findProjectsByUserId(userId);
    }
}
