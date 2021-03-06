package eu.concept.repository.concept.domain;

import java.io.Serializable;
import java.util.Date;
import java.sql.Timestamp;
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
 * @author Gustavo Rovelo <gustavo dot roveloruiz at uhasselt dot be>
 */
@Entity
@Table(name = "FMDecisionCard")
public class FMDecisionCard implements Serializable {

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
    @JoinColumn(nullable = true, name = "fm_id", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private FileManagement fmId;
    @Basic(optional = true)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;

    /**/
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "title")
    private String title;

    @Basic(optional = true)
    @Lob
    @Column(name = "keywords")
    private String keywords;

    @Basic(optional = true)
    @Lob
    @Column(name = "team")
    private String team;

    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "decision")
    private String decision;
    /**/


    public FMDecisionCard(UserCo uid, FileManagement fmId, String title, String keywords, String team, String decision) {
        this.uid = uid;
        this.fmId = fmId;

        this.title = title;
        this.keywords = keywords;
        this.team = team;
        this.decision = decision;


        java.util.Date date= new java.util.Date();
        this.createdDate = new Timestamp(date.getTime());

    }

    public FMDecisionCard() {
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

    public FileManagement getFmId() {
        return fmId;
    }

    public void setFmId(FileManagement fmId) {
        this.fmId = fmId;
    }

    public String getDecision() {
        return decision;
    }

    public void setDecision(String decision) {
        this.decision = decision;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }


    public String getTitle(){return this.title;}

    public void setTitle(String title){ this.title = title; }

    public String getKeywords(){ return this.keywords; }

    public void setKeywords( String keywords ){ this.keywords = keywords; }

    public String getTeam(){ return this.team; }

    public void setTeam(String team){ this.team = team;}
}
