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
import org.hibernate.annotations.Cascade;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Entity
@Table(name = "MBComment")
public class MBComment implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "uid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserCo uid;
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(nullable = true, name = "mb_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private Moodboard mbId;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "comment")
    private String comment;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    public MBComment(UserCo uid, Moodboard mbId, String comment) {
        this.uid = uid;
        this.mbId = mbId;
        this.comment = comment;
    }

    public MBComment() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public UserCo getUid() {
        return uid;
    }

    public void setUid(UserCo uid) {
        this.uid = uid;
    }

    public Moodboard getMbId() {
        return mbId;
    }

    public void setMbId(Moodboard mbId) {
        this.mbId = mbId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

}
