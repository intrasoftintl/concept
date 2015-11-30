package eu.concept.authentication;

import eu.concept.repository.openproject.domain.MemberRoleOp;

/**
 *
 * @author Christos Paraskeva
 */
public enum COnCEPTRole {

    MANAGER(3),
    DESIGNER(4),
    CLIENT(5),
    NON_MEMBER(0);

    private final int ID;

    COnCEPTRole(int id) {
        ID = id;
    }

    public int getID() {
        return ID;
    }

    public static COnCEPTRole getCOnCEPTRole(MemberRoleOp role) {
        switch ((null == role ? 0 : role.getRoleId())) {
            case 3:
                return MANAGER;

            case 4:
                return DESIGNER;

            case 5:
                return CLIENT;

            default:
                return NON_MEMBER;

        }

    }
}
