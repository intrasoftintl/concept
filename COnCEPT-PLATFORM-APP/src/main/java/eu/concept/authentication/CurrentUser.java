package eu.concept.authentication;

import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.userdetails.User;

/**
 *
 * @author Christos Paraskeva
 */
public class CurrentUser extends User {

    private final int userID;
    private final int roleID;
    private final String firstName;
    private final String lastName;

    public CurrentUser(int userID, String username, String password, COnCEPTRole role,String firstName,String lastName) {
        super(username, password, AuthorityUtils.createAuthorityList(role.toString()));
        this.userID = userID;
        this.roleID = role.getID();
        this.firstName=firstName;
        this.lastName=lastName;
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

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
    
    

}
