package eu.concept.repository.openproject.service;


import eu.concept.repository.openproject.dao.ProjectRepository;
import eu.concept.repository.openproject.domain.Project;
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
public class ProjectService {

    @Autowired
    ProjectRepository projectRepo;

    /**
     *
     * @return
     */
    public List<Project> findAllProjects() {
        return projectRepo.findAll();
    }

    /**
     *
     * @param projectID
     * @return
     */
    public Project findProjectByID(int projectID) {
        return projectRepo.findOne(projectID);
    }

}
