package util;

import java.security.SecureRandom;

public class AdminPasswordGenerator {
	protected static SecureRandom random = new SecureRandom();    
    public synchronized String generateToken() {
            long longToken = Math.abs( random.nextLong() );
            String random = Long.toString( longToken, 32 );
            return random;
    }
}
