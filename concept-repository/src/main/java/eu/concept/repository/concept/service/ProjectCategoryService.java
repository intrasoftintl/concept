package eu.concept.repository.concept.service;

import eu.concept.repository.concept.dao.ProjectCategoryRepository;
import eu.concept.repository.concept.domain.Category;
import eu.concept.repository.concept.domain.ProjectCategory;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 *
 * @author smantzouratos
 */
@Service
public class ProjectCategoryService {

    @Autowired
    private ProjectCategoryRepository projectCategoryRepository;

    private static final Logger logger = Logger.getLogger(ProjectCategoryService.class.getName());

    public boolean storeProjectCategory(ProjectCategory projectCategory) {
        try {
            projectCategoryRepository.save(projectCategory);
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            return false;
        }
        return projectCategory.getId() > 0;
    }

    public boolean deleteProjectCategory(Integer id) {
        try {
            projectCategoryRepository.delete(id);
        } catch (Exception ex) {
            Logger.getLogger(ProjectCategoryService.class.getName()).severe(ex.getMessage());
            return false;
        }
        return true;
    }

    public ProjectCategory fetchProjectCategoryById(Integer id) {
        return projectCategoryRepository.findOne(id);
    }

    /**
     * Fetch a project category from database , given a name
     *
     * @param name The name
     * @return A ProjectCategory object (null if no projct category is found)
     */
    public ProjectCategory findByName(String name) {
        return projectCategoryRepository.findByName(name);
    }

    /**
     * Find a project category from its project ID
     *
     * @param projectID
     * @return
     */
    public ProjectCategory findByPid(Integer projectID) {
        return projectCategoryRepository.findByPid(projectID);
    }

    /**
     * Fetch all project categories from database
     *
     * @return A List of Project Category objects
     */
    public List<ProjectCategory> fetchAllProjectCategories() {
        return projectCategoryRepository.findAll();
    }
    
    public boolean storeProjectCategoryWithRootCategory(ProjectCategory projectCategory) {
        try {
            
            List<Category> defaultCategory = new ArrayList<>();
            
            defaultCategory.add(new Category("RootCategory", null, projectCategory));

            projectCategory.setCategories(defaultCategory);
            
            projectCategoryRepository.save(projectCategory);
            
            return true;
            
        } catch (Exception ex) {
            logger.severe(ex.getMessage());
            return false;
        }
    }
    
    

}
