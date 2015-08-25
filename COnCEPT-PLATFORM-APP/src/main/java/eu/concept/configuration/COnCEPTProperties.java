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

    public String getFMGenericImageURL() {
        return fmgenericimageurl;
    }

    public void setfmgenericimageurl(String _fmgenericimageurl) {
        this.fmgenericimageurl = _fmgenericimageurl;
    }

}
