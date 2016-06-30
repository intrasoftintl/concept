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

    public static String BASE64 = "data:image/png;base64,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";

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
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName() + " (Start Date: " + wp.getStartDate() + ")", wp.getSubject(), BASE64, wp.getComponent(), wp.getStartDate(), wp.getUser()));
                }

                if (wp.getComponent().getId().equals("TK") && wp.getStartDate() != null && wp.getDueDate() != null) {
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName() + " (Due Date: " + wp.getDueDate() + ")", wp.getSubject(), BASE64, wp.getComponent(), wp.getDueDate(), wp.getUser()));
                }

                if (wp.getComponent().getId().equals("TK") && wp.getStartDate() == null && wp.getDueDate() != null) {
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName() + " (Due Date: " + wp.getDueDate() + ")", wp.getSubject(), BASE64, wp.getComponent(), wp.getDueDate(), wp.getUser()));
                }

                if (wp.getComponent().getId().equals("TK") && wp.getStartDate() == null && wp.getDueDate() == null) {
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName() + " (Start/Due Date: N/A )", wp.getSubject(), BASE64, wp.getComponent(), wp.getStartDate(), wp.getUser()));
                }

                if (wp.getComponent().getId().equals("ML") && wp.getStartDate() == null && wp.getDueDate() == null) {
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName(), wp.getSubject(), BASE64, wp.getComponent(), wp.getStartDate(), wp.getUser()));
                }

                if (wp.getComponent().getId().equals("ML") && wp.getStartDate() != null && wp.getDueDate() != null) {
                    newTimelines.add(new Timeline(wp.getProjectId(), wp.getId(),  wp.getComponent().getName() + " (Due Date: " + wp.getDueDate() + ")", wp.getSubject(), BASE64, wp.getComponent(), wp.getStartDate(), wp.getUser()));
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
