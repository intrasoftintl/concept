package eu.concept.repository.concept.domain;

import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.UniqueConstraint;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 *
 * @author smantzouratos
 */
@Entity
@Table(name = "ProjectCategory", uniqueConstraints = @UniqueConstraint(columnNames = {"id"}))
public class ProjectCategory implements Serializable {

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
    @Basic(optional = true)
    @Size(min = 1, max = 4096)
    @Column(name = "current_structure", columnDefinition = "TEXT")
    private String currentStructure;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pid", updatable = false)
    private int pid;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL, mappedBy = "projectCategory")
    private List<Category> categories;
    @Basic(optional = false)
    @Column(name = "last_modified", columnDefinition = "TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP")
    private String lastModified;

    public ProjectCategory() {
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

    public String getCurrentStructure() {
        return currentStructure;
    }

    public void setCurrentStructure(String currentStructure) {
        this.currentStructure = currentStructure;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }

    public String getLastModified() {
        return lastModified;
    }

    public void setLastModified(String lastModified) {
        this.lastModified = lastModified;
    }

    public ProjectCategory(String name, String currentStructure, int pid) {
        this.name = name;
        this.currentStructure = currentStructure;
        this.pid = pid;
    }

}
