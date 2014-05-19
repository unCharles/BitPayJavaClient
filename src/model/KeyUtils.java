package model;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import com.google.bitcoin.core.ECKey;

public class KeyUtils {
	
	public KeyUtils() {
		
	}
	
	public static ECKey loadKeys(String privateKey, String publicKey) {
		publicKey = convertHexToString(publicKey);
		privateKey = convertHexToString(privateKey);
		
		ECKey key = null;
		key = new ECKey(privateKey.getBytes(), publicKey.getBytes());

		return key;
	}
	
	public static String readKeyFromFile(String filename) {
		BufferedReader br;
	    try {
	    	br = new BufferedReader(new FileReader(filename));
	        String line = br.readLine();
	        br.close();
	        return line;
	    } catch (IOException e) {
			e.printStackTrace();
		}
	    return "";
	}
	
	public static String signString(ECKey key, String input) {
		return key.signMessage(input);
	}
	
	private static String convertHexToString(String hex) {
	    StringBuilder output = new StringBuilder();
	    for (int i = 0; i < hex.length(); i+=2) {
	        String str = hex.substring(i, i+2);
	        output.append((char)Integer.parseInt(str, 16));
	    }
	    return output.toString();
	}
}
