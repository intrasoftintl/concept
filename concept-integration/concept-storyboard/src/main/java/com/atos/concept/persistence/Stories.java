package com.atos.concept.persistence;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;

import static javax.persistence.GenerationType.IDENTITY;

import javax.persistence.CascadeType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.apache.commons.lang3.builder.ToStringBuilder;

@Entity
@Table(name = "stories")
public class Stories implements java.io.Serializable {

    private static final long serialVersionUID = 1L;
    private Integer id;
    private Integer id_project;
    private String id_user;
    private String storyName;
    private String uuid;
    private Date creation;
    private Set<StoriesSlides> storiesSlideses = new HashSet<StoriesSlides>(0);

    public Stories() {
    }

    public Stories(int id_project, String storyName) {
        this.id_project = id_project;
        this.storyName = storyName;
    }

    public Stories(int id_project, String id_user, String storyName, Date creation, Set<StoriesSlides> storiesSlideses) {
        this.id_project = id_project;
        this.storyName = storyName;
        this.creation = creation;
        this.storiesSlideses = storiesSlideses;
        this.id_user = id_user;
    }

    public void setProjectId(int id_project) {
        this.id_project = id_project;
    }

    @Column(name = "id_project", nullable = false)
    public int getProjectId() {
        return this.id_project;
    }

    public void setUserId(String id_user) {
        this.id_user = id_user;
    }

    @Column(name = "id_user", nullable = false)
    public String getUserId() {
        return this.id_user;
    }

    @Id
    @GeneratedValue(strategy = IDENTITY)
    @Column(name = "id", unique = true, nullable = false)
    public Integer getId() {
        return this.id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    @Column(name = "story_name", nullable = false, length = 30)
    public String getStoryName() {
        return this.storyName;
    }

    public void setStoryName(String storyName) {
        this.storyName = storyName;
    }

    @Column(name = "uuid", nullable = false, length = 36)
    public String getUuid() {
        return this.uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "creation", length = 19)
    public Date getCreation() {
        return this.creation;
    }

    public void setCreation(Date creation) {
        this.creation = creation;
    }

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "stories", cascade = {CascadeType.ALL}, orphanRemoval = true)
    public Set<StoriesSlides> getStoriesSlideses() {
        return this.storiesSlideses;
    }

    public void setStoriesSlideses(Set<StoriesSlides> storiesSlideses) {
        this.storiesSlideses = storiesSlideses;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

}
