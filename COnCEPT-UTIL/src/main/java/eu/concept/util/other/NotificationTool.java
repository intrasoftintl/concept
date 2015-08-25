package eu.concept.util.other;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
public enum NotificationTool {

    BA("ICON_BA_NOTIFICATION"),
    SK("ICON_SK_NOTIFICATION"),
    MM("ICON_MM_NOTIFICATION"),
    FM("ICON_FM_NOTIFICATION"),
    SE("ICON_SE_NOTIFICATION"),
    SB("ICON_SB_NOTIFICATION");

    
   public enum NOTIFICATION_OPERATION{
        DELETED,
        EDITED,
        CREATED,
        SHARED,
        UPLOADED;
    }
    
    private final String toolImgLink;

    NotificationTool(String _toolImgLink) {
        toolImgLink = _toolImgLink;

    }

    public String getImageLink() {
        return toolImgLink;
    }
}
