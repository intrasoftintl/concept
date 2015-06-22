package eu.concept.authentication;

import eu.concept.util.other.Util;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 *
 * @author Christos Paraskeva
 */
public class ConceptPasswordEncoder implements PasswordEncoder {

    @Override
    public String encode(CharSequence cs) {
        return cs.toString();
    }

    @Override
    public boolean matches(CharSequence cs, String string) {
        return Util.createAlgorithm(cs.toString(), "SHA").equals(string);
    }

}
