package doryanbessiere.procopy.fr.commons;

import java.io.File;
import java.net.URISyntaxException;

public class Commons {

    public static File localDirectory(){
        try {
            return new File(Commons.class.getProtectionDomain().getCodeSource().getLocation()
                    .toURI()).getParentFile();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        return null;
    }
}
