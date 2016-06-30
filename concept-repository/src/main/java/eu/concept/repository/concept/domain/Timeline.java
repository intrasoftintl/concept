package eu.concept.repository.concept.domain;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Entity
@Table(name = "Timeline")
public class Timeline implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Long id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pid")
    private int pid;
    @Basic(optional = false)
    @NotNull
    @Column(name = "cid")
    private int cid;
    @JoinColumn(name = "component", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Component component;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 1024)
    @Column(name = "operation")
    private String operation;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "message")
    private String message;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "thumbnail")
    private String thumbnail;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JoinColumn(name = "uid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserCo uid;

    public Timeline() {
    }

    public Timeline(Long id) {
        this.id = id;
    }

    public Timeline(Long id, int pid, int cid, String operation, String message, String thumbnail, Component component, Date createdDate) {
        this.id = id;
        this.pid = pid;
        this.cid = cid;
        this.operation = operation;
        this.message = message;
        this.thumbnail = thumbnail;
        this.createdDate = createdDate;
        this.component = component;
    }

    public Timeline(int pid, int cid, String operation, String message, String thumbnail, Component component, Date createdDate, UserCo user) {
        this.pid = pid;
        this.cid = cid;
        this.operation = operation;
        this.message = message;
        this.thumbnail = thumbnail;
        this.createdDate = createdDate;
        this.component = component;
        this.uid = user;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public int getCid() {
        return cid;
    }

    public void setCid(int cid) {
        this.cid = cid;
    }

    public Component getComponent() {
        return component;
    }

    public void setComponent(Component component) {
        this.component = component;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public UserCo getUid() {
        return uid;
    }

    public void setUid(UserCo uid) {
        this.uid = uid;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Timeline)) {
            return false;
        }
        Timeline other = (Timeline) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eu.concept.repository.concept.domain.Notification[ id=" + id + " ]";
    }

}
