package eu.concept.controller.component;

import eu.concept.repository.concept.service.FileManagementService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class FileManagementController {

    @Autowired
    FileManagementService fmService;

}
