package eu.concept.repository.concept.domain;

import java.io.Serializable;
import javax.persistence.Basic;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import org.hibernate.annotations.Cascade;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Entity
@Table(name = "Likes")
public class Likes implements Serializable {

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
    @JoinColumn(nullable = true, name = "ba_id", referencedColumnName = "id")
    @ManyToOne
    private BriefAnalysis baId;
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(nullable = true, name = "sk_id", referencedColumnName = "id")
    @ManyToOne
    private Sketch skId;
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(nullable = true, name = "fm_id", referencedColumnName = "id")
    @ManyToOne
    private FileManagement fmId;
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(nullable = true, name = "mm_id", referencedColumnName = "id")
    @ManyToOne
    private MindMap mmId;
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(nullable = true, name = "sb_id", referencedColumnName = "id")
    @ManyToOne
    private Storyboard sbId;
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    @JoinColumn(nullable = true, name = "mb_id", referencedColumnName = "id")
    @ManyToOne
    private Storyboard mbId;

    public Likes() {
    }

    public Likes(Integer id) {
        this.id = id;
    }

    public Likes(Integer id, UserCo user) {
        this.id = id;
        this.uid = user;
    }

    public Integer getId() {
        return id;
    }

    public Storyboard getMbId() {
        return mbId;
    }

    public void setMbId(Storyboard mbId) {
        this.mbId = mbId;
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

    public BriefAnalysis getBaId() {
        return baId;
    }

    public void setBaId(BriefAnalysis baId) {
        this.baId = baId;
    }

    public Sketch getSkId() {
        return skId;
    }

    public void setSkId(Sketch skId) {
        this.skId = skId;
    }

    public FileManagement getFmId() {
        return fmId;
    }

    public void setFmId(FileManagement fmId) {
        this.fmId = fmId;
    }

    public MindMap getMmId() {
        return mmId;
    }

    public void setMmId(MindMap mmId) {
        this.mmId = mmId;
    }

    public Storyboard getSbId() {
        return sbId;
    }

    public void setSbId(Storyboard sbId) {
        this.sbId = sbId;
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
        if (!(object instanceof Likes)) {
            return false;
        }
        Likes other = (Likes) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eu.concept.repository.concept.domain.Likes[ id=" + id + " ]";
    }

}
