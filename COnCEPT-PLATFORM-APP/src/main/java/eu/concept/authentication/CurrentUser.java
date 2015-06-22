/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package eu.concept.authentication;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author ermis
 */
public class CurrentUser extends User {

    private final int userID;
    private final int roleID;

    public CurrentUser(int userID, String username, String password, COnCEPTRole role) {
        super(username, password, AuthorityUtils.createAuthorityList(role.toString()));
        this.userID = userID;
        this.roleID = role.getID();
    }

    public int getId() {
        return userID;
    }

    public String getRole() {
        return super.getAuthorities().toArray()[0].toString();
    }

    public int getRoleId() {
        return roleID;
    }

}
