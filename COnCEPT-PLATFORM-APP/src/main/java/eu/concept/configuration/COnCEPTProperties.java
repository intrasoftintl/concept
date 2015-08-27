package eu.concept.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christos Paraskeva <ch.paraskeva at gmail dot com>
 */
@ConfigurationProperties(prefix = "concept")
@Component
public class COnCEPTProperties {

    private String fmgenericimageurl;
    private String fmuploadgenericimageurl;
    private String mindmapcreateurl;
    private String mindmapediturl;

    public String getmindmapcreateurl() {
        return mindmapcreateurl;
    }

    public void setmindmapcreateurl(String mindmapcreateurl) {
        this.mindmapcreateurl = mindmapcreateurl;
    }

    public String getMindmapEditurl() {
        return mindmapediturl;
    }

    public void setMindmapEditURL(String mindmapediturl) {
        this.mindmapediturl = mindmapediturl;
    }

    public String getFMGenericImageURL() {
        return fmgenericimageurl;
    }

    public void setfmgenericimageurl(String _fmgenericimageurl) {
        this.fmgenericimageurl = _fmgenericimageurl;
    }

    public String getFMUploadGenericImageURL() {
        return fmuploadgenericimageurl;
    }

    public void setfmuploadgenericimageurl(String _fmuploadgenericimageurl) {
        this.fmuploadgenericimageurl = _fmuploadgenericimageurl;
    }

}
