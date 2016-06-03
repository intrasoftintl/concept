package eu.concept.repository.concept.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.Transient;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Entity
@Table(name = "FileManagement")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "FileManagement.findAll", query = "SELECT f FROM FileManagement f")})
public class FileManagement implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pid")
    private int pid;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 255)
    @Column(name = "filename")
    private String filename;
    @Basic(optional = false)
    @NotNull
    @Lob
    @Size(min = 1, max = 2147483647)
    @Column(name = "content")
    private String content;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 128)
    @Column(name = "type")
    private String type;
    @Basic(optional = false)
    @NotNull
    @Column(name = "isPublic")
    private short isPublic;
    @Basic(optional = false)
    @Column(name = "created_date")
    @Temporal(TemporalType.TIMESTAMP)
    private Date createdDate;
    @JoinColumn(name = "uid", referencedColumnName = "id")
    @ManyToOne(optional = false)
    private UserCo uid;
    //Non Domain field
    @OneToMany(mappedBy = "fmId", orphanRemoval = false)
    private Collection<Likes> likes;
    //Non Domain field
    @OneToMany(fetch = FetchType.LAZY, mappedBy = "fmId", orphanRemoval = false)
    private Collection<FMComment> comments;

    //Transient Field
    @Transient
    private String title = "N/A";

    //Transient field
    @Transient
    private boolean pinned = false;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Collection<FMComment> getComments() {
        return comments;
    }

    public void setComments(Collection<FMComment> comments) {
        this.comments = comments;
    }

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

    public FileManagement() {
    }

    public FileManagement(Integer id) {
        this.id = id;
    }

    public FileManagement(Integer id, int pid, String filename, String content, String type, short isPublic, Date createdDate) {
        this.id = id;
        this.pid = pid;
        this.filename = filename;
        this.content = content;
        this.type = type;
        this.isPublic = isPublic;
        this.createdDate = createdDate;
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

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public short getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(short isPublic) {
        this.isPublic = isPublic;
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
        if (!(object instanceof FileManagement)) {
            return false;
        }
        FileManagement other = (FileManagement) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "eu.concept.repository.concept.domain.FileManagement[ id=" + id + " ]";
    }

    public void setPinned(boolean pinStatus) {
        this.pinned = pinStatus;
    }

    public boolean isPinned() {
        return this.pinned;
    }

}
