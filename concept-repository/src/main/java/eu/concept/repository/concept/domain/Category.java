package eu.concept.repository.concept.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import org.hibernate.annotations.Cascade;

/**
 *
 * @author smantzouratos
 */
@Entity
@Table(name = "Category", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class Category implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Size(min = 1, max = 250)
    @Column(name = "name")
    private String name;
    @JoinColumn(name = "parent_id", referencedColumnName = "id")
    @OneToOne(optional = true)
    @Cascade(org.hibernate.annotations.CascadeType.SAVE_UPDATE)
    private Category parentID;
    @JoinColumn(name = "pc_id", referencedColumnName = "id")
    @OneToOne(optional = false)
    @Cascade(org.hibernate.annotations.CascadeType.ALL)
    private ProjectCategory projectCategory;
    @Basic(optional = false)
    @Column(name = "last_modified", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private String lastModified;

    public Category() {
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Category getParentID() {
        return parentID;
    }

    public void setParentID(Category parentID) {
        this.parentID = parentID;
    }

    public ProjectCategory getProjectCategory() {
        return projectCategory;
    }

    public void setProjectCategory(ProjectCategory projectCategory) {
        this.projectCategory = projectCategory;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }
    
    public boolean isFather() {
        return (null == this.parentID ? false : this.parentID.getId() == 1);
    }


}
