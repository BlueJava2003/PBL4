package SERVER.HELPER;

import org.mindrot.jbcrypt.BCrypt;

public class BCryptHelper {
    public static boolean check(String text, String hashed) {
        return BCrypt.checkpw(text, hashed);
    }

    public static String encode(String text) {
        return BCrypt.hashpw(text, BCrypt.gensalt(9));
    }
}

