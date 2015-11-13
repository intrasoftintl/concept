package eu.concept.authentication;

import eu.concept.repository.openproject.domain.MemberRoleOp;

/**
 *
 * @author Christos Paraskeva
 */
public enum COnCEPTRole {

    MANAGER(6),
    DESIGNER(7),
    CLIENT(8),
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
            case 6:
                return MANAGER;

            case 7:
                return DESIGNER;

            case 8:
                return CLIENT;

            default:
                return NON_MEMBER;

        }

    }
}
