package model;

import java.security.GeneralSecurityException;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.util.Arrays;
import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
import com.sun.org.apache.xml.internal.security.utils.Base64;

public class KeyUtils {
	
	public KeyUtils() {
		com.sun.org.apache.xml.internal.security.Init.init();	
	}
	
	public static PrivateKey loadPrivateKey(String key64) throws GeneralSecurityException {
	    byte[] clear;
		try {
			clear = Base64.decode(key64);
			PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(clear);
		    KeyFactory fact = KeyFactory.getInstance("DSA");
		    PrivateKey priv = fact.generatePrivate(keySpec);
		    Arrays.fill(clear, (byte) 0);
		    return priv;
		} catch (Base64DecodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	    return null;
	}
	
}
