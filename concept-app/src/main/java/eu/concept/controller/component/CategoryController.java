package eu.concept.controller.component;

import static eu.concept.controller.WebController.getCurrentUser;
import eu.concept.repository.concept.domain.Category;
import eu.concept.repository.concept.domain.ProjectCategory;
import eu.concept.repository.concept.service.CategoryService;
import eu.concept.repository.concept.service.ProjectCategoryService;
import eu.concept.repository.openproject.domain.ProjectOp;
import eu.concept.repository.openproject.service.ProjectServiceOp;
import java.util.List;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class CategoryController {

    private static final Logger logger = Logger.getLogger(CategoryController.class.getName());

    @Autowired
    ProjectServiceOp projectServiceOp;

    @Autowired
    CategoryService categoryService;

    @Autowired
    ProjectCategoryService projectCategoryService;

    /*
     *  GET Methods 
     */
 /*
     *  POST Methods 
     */
    @RequestMapping(value = "/category_app/{projectID}", method = RequestMethod.GET)
    public String category_app(Model model, @PathVariable(value = "projectID") String projectID) {
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());
        model.addAttribute("projectID", projectID);
        return "category_app";
    }

    @RequestMapping(value = "/category/fragment/{projectID}", method = RequestMethod.GET)
    public String loadFragment(Model model, @PathVariable(value = "projectID") String projectID, @RequestParam(value = "action", defaultValue = "", required = false) String action, @RequestParam(value = "id", defaultValue = "", required = false) String categoryID) {

        ProjectCategory projectCategory = projectCategoryService.findByPid(Integer.valueOf(projectID));

        if (null != projectCategory) {

            if (action.isEmpty()) {
                model.addAttribute("treegrid", categoryService.constructTreeGrid(projectCategory.getCategories()));
                return "category :: category-list";
            } else if (action.equalsIgnoreCase("add")) {
                model.addAttribute("projectCategoryID", projectCategory.getId());
                model.addAttribute("isEdit", 0);
                return "category :: category-add";
            } else if (action.equalsIgnoreCase("edit")) {
                model.addAttribute("projectCategoryID", projectCategory.getId());
                model.addAttribute("isEdit", 1);

                Category category = categoryService.fetchCategoryById(Integer.valueOf(categoryID));
                model.addAttribute("hasFather", null == category.getParentID().getParentID() ? 0 : 1);
                model.addAttribute("category", category);
                return "category :: category-add";
            } else {
                return "category :: category-init";
            }

        } else {
            return "category :: category-init";
        }
    }

    @RequestMapping(value = "/category/add", method = RequestMethod.POST)
    public String addCategory(Model model, @RequestParam(value = "projectID", defaultValue = "", required = true) String projectID, @RequestParam(value = "categoryName", defaultValue = "", required = true) String categoryName, @RequestParam(value = "parentCategoryID", defaultValue = "", required = false) String parentCategoryID, @RequestParam(value = "projectCategoryID", defaultValue = "", required = true) String projectCategoryID, final RedirectAttributes redirectAttributes) {

        Category newCategory;

        if (null != categoryName && !categoryName.isEmpty()) {

            if (!parentCategoryID.isEmpty()) {

                Category parentCategory = categoryService.fetchCategoryById(Integer.valueOf(parentCategoryID));
                newCategory = new Category(categoryName, parentCategory, projectCategoryService.fetchProjectCategoryById(Integer.valueOf(projectCategoryID)));

            } else {
                ProjectCategory projectCategory = projectCategoryService.fetchProjectCategoryById(Integer.valueOf(projectCategoryID));
                Category parentCategory = categoryService.findRootCategoryByProjectCategory(projectCategory);
                newCategory = new Category(categoryName, parentCategory, projectCategoryService.fetchProjectCategoryById(Integer.valueOf(projectCategoryID)));
            }

            if (categoryService.storeCategory(newCategory)) {
                redirectAttributes.addFlashAttribute("success", "Category has been added successfully!");
                return "redirect:/category_app/" + projectID;

            } else {
                redirectAttributes.addFlashAttribute("error", "Problem occured while adding Category to Model!");
                return "redirect:/category_app/" + projectID;
            }

        } else {
            redirectAttributes.addFlashAttribute("error", "Please fill all the fields!");
            return "redirect:/category_app/" + projectID;
        }
    }

    @RequestMapping(value = "/category/update", method = RequestMethod.POST)
    public String updateCategory(Model model, @RequestParam(value = "projectID", defaultValue = "", required = true) String projectID, @RequestParam(value = "categoryName", defaultValue = "", required = true) String categoryName, @RequestParam(value = "parentCategoryID", defaultValue = "", required = false) String parentCategoryID, @RequestParam(value = "projectCategoryID", defaultValue = "", required = true) String projectCategoryID, @RequestParam(value = "categoryID", defaultValue = "", required = true) String categoryID,  final RedirectAttributes redirectAttributes) {

        Category existingCategory = categoryService.fetchCategoryById(Integer.valueOf(categoryID));

        if (!parentCategoryID.isEmpty()) {

            Category parentCategory = categoryService.fetchCategoryById(Integer.valueOf(parentCategoryID));
            existingCategory.setName(categoryName);
            existingCategory.setParentID(parentCategory);
            existingCategory.setProjectCategory(projectCategoryService.fetchProjectCategoryById(Integer.valueOf(projectCategoryID)));

        } else {

            ProjectCategory projectCategory = projectCategoryService.fetchProjectCategoryById(Integer.valueOf(projectCategoryID));

            Category parentCategory = categoryService.findRootCategoryByProjectCategory(projectCategory);

            existingCategory.setName(categoryName);
            existingCategory.setParentID(parentCategory);
            existingCategory.setProjectCategory(projectCategoryService.fetchProjectCategoryById(Integer.valueOf(projectCategoryID)));
        }

        if (categoryService.storeCategory(existingCategory)) {
            redirectAttributes.addFlashAttribute("success", "Category has been updated successfully!");
            return "redirect:/category_app/" + projectID;

        } else {
            redirectAttributes.addFlashAttribute("error", "Problem occured while updating Category!");
            return "redirect:/category_app/" + projectID;
        }
    }

    @RequestMapping(value = "/category/delete", method = RequestMethod.GET)
    public String deleteCategory(Model model, @RequestParam(value = "pid", defaultValue = "", required = true) String projectID, @RequestParam(value = "id", defaultValue = "", required = true) Integer categoryID) {

        if (categoryService.deleteCategoryCustom(categoryID)) {
            model.addAttribute("success", "Category has been deleted successfully!");
            
        } else {
             model.addAttribute("error", "Problem occured while deleting Category!");
        }

        return loadFragment(model, projectID, "", "");
    }

    @RequestMapping(value = "/category/init", method = RequestMethod.POST)
    public String assignCategory(Model model,
            @RequestParam(value = "projectID", defaultValue = "", required = true) String projectID,
            @RequestParam(value = "name", defaultValue = "", required = true) String name,
            final RedirectAttributes redirectAttributes
    ) {
        if (!model.containsAttribute("projectID")) {
            model.addAttribute("projectID", "0");
        }
        List<ProjectOp> projects = projectServiceOp.findProjectsByUserId(getCurrentUser().getId());
        model.addAttribute("projects", projects);
        model.addAttribute("currentUser", getCurrentUser());

        ProjectCategory projectCategory = new ProjectCategory(name, null, Integer.valueOf(projectID));

        if (projectCategoryService.storeProjectCategoryWithRootCategory(projectCategory)) {
            redirectAttributes.addFlashAttribute("success", "Model has been created successfully!");
            return "redirect:/category_app/" + projectID;

        } else {
            redirectAttributes.addFlashAttribute("error", "Problem occured while adding model!");
            return "redirect:/category_app/" + projectID;
        }

    }

}
