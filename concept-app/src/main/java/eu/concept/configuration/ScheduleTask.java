package eu.concept.configuration;

import eu.concept.main.DSHandler;
import eu.concept.repository.concept.domain.Component;
import eu.concept.repository.concept.domain.Timeline;
import eu.concept.repository.concept.domain.UserCo;
import eu.concept.repository.concept.service.TimelineService;
import eu.concept.repository.concept.service.UserCoService;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.Scheduled;

@Configuration
public class ScheduleTask {

    static final Logger LOGGER = Logger.getLogger(ScheduleTask.class.getName());

    @Autowired
    TimelineService timelineService;

    @Autowired
    UserCoService userService;

    public static String TK = "data:image/png;base64,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";
    public static String ML = "data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAKcAAACnCAYAAAB0FkzsAAAACXBIWXMAABcSAAAXEgFnn9JSAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAA7lJREFUeNrs3c9NG0EUwGFY5R46gA7IiUMu2RLSQUwHlGA6MRXEdMCJQ06mA0qACpxZZQ+5BIEF2TfvfT9ptBIYy8x8mvUfxB7v9/sjKWKDKRCcEpyCU4JTcEpwSnAKTglOwSnBKcEpOCU4BacEpwSn4JTgFJwSnBKcglOCU3BKcEpwKnCfTMHhXdyPX9phnMfJX9/azWP76+vdk5k6rGP/Ze4glN/bYd3G+StufjPdtiF9NHNwfiTKaXfctvHtgB+/bkDXZhHOjzqFb165W/5zF21AV2YTzveGedfG53e4O0C9Wg8Jc+pHu8+NmYUzGkxA4QwNE1A4Q8MEFM7QMAGFMzRMQOEMDRNQOEPDBBTO0DABhTM0TEDhDA0T0Mo4O4AJaEWcHcEsD3QAE1A4wQQUzpQwSwIdwAQUTjABhTM9zDJABzABhRNMQCvjLAgzNdABTEDhBBPQajjBzAt0ABNQOMEEtAJOMGsAHcAEFE4wAc2KE8x6QAcwAYUTTEAz4QSzNtABTEDhBBPQ3nGCCWhInGACGhInmICGxAkmoCFxggloSJzztSQ3YAIaceecLnJ6zkJYoKslH8Bi176cLwv9k4HQPbdxttQ145fcOdfWPnzT062rUqf1+UWQ03kfrUrhbI3WvJtO22ZyBqeiVgrnifXuqrESTglOvUul3kraWe+u2sEpOAPg3Frvbrot9QnR/MveWPcuWmwj8fGlXuqhbSSbcjjbL/3YDtfWP2zTH32slnwAi76V1ICund7DdtXWZ1cW5wx0BWi4Lpc8nYfBCSiYoXECCmZonICCGRonoGCGxgkomKFxAgpmaJyA1oYZHiegdWF2gRPQmjC7wQloPZhd4QS0FszucAJaB2aXOAGtAbNbnIDmh9k1TkBzw+weJ6B5YabACWhOmGlwApoPZiqcgOaCmQ5nYaDpYKbEWRBoSphpcRYCmhZmapwFgKaGmR5nYqDpYZbAmRBoCZhlcCYCWgZmKZwJgJaCWQ5nx0DLwSyJs0OgJWGWxdkR0LIwS+PsAGhpmOVxBgZaHiacMYGCCWdIoGDCGRIomHCGBAomnCGBgglnSKBgwhkSKJhwhgQKJpwhgYIJZzigz2DC+dFAL2dob+mhjRHMt3W83+/Nwhu7uB9P2uHq6M9VdU9fuOltG1so4VwK6lk7TGOcv/TUxnS13V1D+WSG4JTnnBKcEpyCU4JTcEpwCk4JTglOwSnBKTglOCU4BacEp+CU4JTgFJwSnIJTglOCU3BKcApO6b/2W4ABABwQHw7rAEInAAAAAElFTkSuQmCC";

    @Scheduled(fixedRate = 10000)
    public void trigger() {
        LOGGER.info("Synchronizing Openproject database with COnCEPT Platform...");

        Connection connection = DSHandler.INSTANCE.getConnection();
        ResultSet rs = null;
        Map<Integer, List<WorkPackage>> workpackageMap = new HashMap<>();

        try {
            rs = connection.prepareStatement("SELECT * from work_packages").executeQuery();
            while (rs.next()) {
                UserCo user = userService.findById(rs.getInt("author_id"));
                Component component = (rs.getInt("type_id") <= 2 ? new Component(rs.getInt("type_id") == 1 ? "TK" : "ML", rs.getInt("type_id") == 1 ? "Task" : "Milestone") : null);

                if (null != component && null != user) {

                    WorkPackage tmpWorkpackage = new WorkPackage(rs.getInt("id"), rs.getInt("project_id"), component, user, rs.getDate("start_date"), rs.getDate("due_date"), rs.getString("subject"));
     
                    if (workpackageMap.containsKey(tmpWorkpackage.getProjectId())) {
                        workpackageMap.get(tmpWorkpackage.getProjectId()).add(tmpWorkpackage);
                    } else {
                        List<WorkPackage> list = new ArrayList<>();
                        list.add(tmpWorkpackage);
                        workpackageMap.put(tmpWorkpackage.getProjectId(), list);
                    }
                }
            }
        } catch (SQLException ex) {
            Logger.getLogger(ScheduleTask.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            DSHandler.INSTANCE.closeDBStreams(connection, null, rs);
        }

        List<Timeline> newTimelines = new ArrayList<>();

        workpackageMap.keySet().forEach(projectId -> {

            //Delete all Tasks
            timelineService.deleteByPidAndComponent(projectId, new Component("TK"));
            //Delete all Milestones
            timelineService.deleteByPidAndComponent(projectId, new Component("ML"));

            workpackageMap.get(projectId).forEach(wp -> {

                if (wp.getComponent().getId().equals("TK") && wp.getStartDate() != null) {
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName() + " (Start Date: " + wp.getStartDate() + ")", wp.getSubject(), TK, wp.getComponent(), wp.getStartDate(), wp.getUser()));
                }

                if (wp.getComponent().getId().equals("TK") && wp.getStartDate() != null && wp.getDueDate() != null) {
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName() + " (Due Date: " + wp.getDueDate() + ")", wp.getSubject(), TK, wp.getComponent(), wp.getDueDate(), wp.getUser()));
                }

                if (wp.getComponent().getId().equals("TK") && wp.getStartDate() == null && wp.getDueDate() != null) {
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName() + " (Due Date: " + wp.getDueDate() + ")", wp.getSubject(), TK, wp.getComponent(), wp.getDueDate(), wp.getUser()));
                }

                if (wp.getComponent().getId().equals("TK") && wp.getStartDate() == null && wp.getDueDate() == null) {
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName() + " (Start/Due Date: N/A )", wp.getSubject(), TK, wp.getComponent(), wp.getStartDate(), wp.getUser()));
                }

                if (wp.getComponent().getId().equals("ML") && wp.getStartDate() == null && wp.getDueDate() == null) {
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName(), wp.getSubject(), ML, wp.getComponent(), wp.getStartDate(), wp.getUser()));
                }

                if (wp.getComponent().getId().equals("ML") && wp.getStartDate() != null && wp.getDueDate() != null) {
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName() + " (Due Date: " + wp.getDueDate() + ")", wp.getSubject(), ML, wp.getComponent(), wp.getStartDate(), wp.getUser()));
                }

    
            });

        });

        //Store new timelines
        timelineService.store(newTimelines);

    }

}

class WorkPackage {

    int id;
    int projectId;
    Component component;
    UserCo user;
    Date startDate;
    Date dueDate;
    String subject;

    public WorkPackage(int id, int projectId, Component component, UserCo user, Date startDate, Date dueDate, String subject) {
        this.id = id;
        this.projectId = projectId;
        this.component = component;
        this.user = user;
        this.startDate = startDate;
        this.dueDate = dueDate;
        this.subject = subject;
    }

    public String getSubject() {
        return subject;
    }

    public int getId() {
        return id;
    }

    public int getProjectId() {
        return projectId;
    }

    public Component getComponent() {
        return component;
    }

    public UserCo getUser() {
        return user;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getDueDate() {
        return dueDate;
    }

}
