package eu.concept.authentication;

import eu.concept.repository.openproject.domain.MemberRoleOp;

/**
 *
 * @author Christos Paraskeva
 */
public enum COnCEPTRole {

    MANAGER(12),
    DESIGNER(13),
    CLIENT(14),
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
            case 12:
                return MANAGER;

            case 13:
                return DESIGNER;

            case 14:
                return CLIENT;

            default:
                return NON_MEMBER;

        }

    }
}
