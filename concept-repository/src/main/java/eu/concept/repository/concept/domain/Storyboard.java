package eu.concept.repository.concept.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Entity
@Table(name = "Storyboard")
public class Storyboard implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pid", updatable = false)
    private int pid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "title")
    private String title;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "content")
    private String content;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Column(name = "content_thumbnail")
    private String contentThumbnail;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isPublic")
    private short isPublic;
    @JoinColumn(name = "uid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserCo uid;
    //Non Domain field
    @OneToMany(mappedBy = "sbId", orphanRemoval = false)
    public Collection<Likes> likes;
    //Non Domain field
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sbId", orphanRemoval = false)
    private Collection<SBComment> comments;

    
    /*Decision cards*/
    //Non Domain field
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "sbId", orphanRemoval = false)
    private Collection<SBDecisionCard> decisions;
    /**/
    
    
    //Transient field
    @Transient
    private boolean pinned = false;

    public Collection<SBComment> getComments() {
        return comments;
    }

    public void setComments(Collection<SBComment> comments) {
        this.comments = comments;
    }

    
    /*Decision cards*/
    public Collection<SBDecisionCard> getDecisions(){ return this.decisions; }

    public void setDecisions(Collection<SBDecisionCard> decisions){ this.decisions = decisions; }
    /**/
    
    
    /**
     *
     * @return A collection of Likes object
     */
    public Collection<Likes> getLikes() {
        return likes;
    }

    /**
     *
     * @param userID
     * @return True if the user has liked the current Sketch otherwise false
     */
    public boolean hasLike(int userID) {
        return likes.stream().filter(like -> like.getUid().getId().equals(userID)).count() > 0;
    }

    public Storyboard() {
    }

    public Storyboard(Integer id) {
        this.id = id;
    }

    public Storyboard(Integer id, int pid, String title, String content, String contentThumbnail, Date createdDate, short isPublic) {
        this.id = id;
        this.pid = pid;
        this.title = title;
        this.content = content;
        this.contentThumbnail = contentThumbnail;
        this.createdDate = createdDate;
        this.isPublic = isPublic;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getContentThumbnail() {
        return contentThumbnail;
    }

    public void setContentThumbnail(String contentThumbnail) {
        this.contentThumbnail = contentThumbnail;
    }

    @JsonIgnore
    public Date getCreatedDate() {
        return createdDate;
    }

    @JsonIgnore
    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public short getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(short isPublic) {
        this.isPublic = isPublic;
    }

    public UserCo getUid() {
        return uid;
    }

    public void setUid(UserCo userCo) {
        this.uid = userCo;
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
        if (!(object instanceof Storyboard)) {
            return false;
        }
        Storyboard other = (Storyboard) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eu.concept.repository.concept.domain.Storyboard[ id=" + id + " ]";
    }

    public void setPinned(boolean pinStatus) {
        this.pinned = pinStatus;
    }

    public boolean isPinned() {
        return this.pinned;
    }
}
