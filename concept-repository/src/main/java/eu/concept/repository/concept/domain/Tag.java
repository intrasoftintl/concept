/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.concept.repository.concept.domain;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@Entity
public class Tag implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private Integer id;
    @Basic(optional = false)
    @NotNull
    @Column(name = "pid")
    private int pid;
    @Null
    private String tagOne;
    @Null
    private String tagTwo;
    @Null
    private String tagThree;
    @Null
    private String tagFour;
    @Null
    private String tagFive;

    private boolean isSet;

    public Tag(int pid, String tagOne, String tagTwo, String tagThree, String tagFour, String tagFive) {
        this.pid = pid;
        this.tagOne = tagOne;
        this.tagTwo = tagTwo;
        this.tagThree = tagThree;
        this.tagFour = tagFour;
        this.tagFive = tagFive;
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

    public String getTagOne() {
        return tagOne;
    }

    public void setTagOne(String tagOne) {
        this.tagOne = tagOne;
    }

    public String getTagTwo() {
        return tagTwo;
    }

    public void setTagTwo(String tagTwo) {
        this.tagTwo = tagTwo;
    }

    public String getTagThree() {
        return tagThree;
    }

    public void setTagThree(String tagThree) {
        this.tagThree = tagThree;
    }

    public String getTagFour() {
        return tagFour;
    }

    public void setTagFour(String tagFour) {
        this.tagFour = tagFour;
    }

    public String getTagFive() {
        return tagFive;
    }

    public void setTagFive(String tagFive) {
        this.tagFive = tagFive;
    }

    public boolean isSet() {
        return isSet;
    }

    public void setIsSet(boolean isSet) {
        this.isSet = isSet;
    }
}
