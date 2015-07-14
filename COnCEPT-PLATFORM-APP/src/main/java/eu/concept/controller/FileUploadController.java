package eu.concept.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Controller
public class FileUploadController {

    @RequestMapping(value = "/upload", method = RequestMethod.GET)
    public @ResponseBody
    String provideUploadInfo() {
        return "You can upload a file by posting to this same URL.";
    }

    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public @ResponseBody  String handleFileUpload(@RequestParam("file") MultipartFile[] files) {
        if (files != null &&  files.length > 0) {
            try {
                for (MultipartFile file : files) {
                    Logger.getLogger(FileUploadController.class.getName()).log(Level.INFO, "Receibed file: {0}", file.getOriginalFilename());
                    byte[] bytes = file.getBytes();
                    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(file.getName())));
                    stream.write(bytes);
                    stream.close();
                }
                return "You successfully uploaded " + files.length + " files!";
            } catch (Exception e) {
                return "You failed to upload   => " + e.getMessage();
            }
        } else {
            return "You failed to upload  because the file was empty.";
        }
    }

}
