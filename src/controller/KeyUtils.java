package controller;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigInteger;

import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.ECKey.ECDSASignature;
import com.google.bitcoin.core.Sha256Hash;

public class KeyUtils {
	
	final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	
	public KeyUtils() {
		
	}
	
	public static ECKey loadKeys(String privateKey, String publicKey) {
		BigInteger privKey = new BigInteger(privateKey, 16);
		
		ECKey key = new ECKey(privKey, null, true);

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
		byte[] data = input.getBytes();
        Sha256Hash hash = Sha256Hash.create(data);
        ECDSASignature sig = key.sign(hash, null);
        byte[] bytes = sig.encodeToDER();
        return bytesToHex(bytes);
	}
	
	public static String bytesToHex(byte[] bytes) {
	    char[] hexChars = new char[bytes.length * 2];
	    for ( int j = 0; j < bytes.length; j++ ) {
	        int v = bytes[j] & 0xFF;
	        hexChars[j * 2] = hexArray[v >>> 4];
	        hexChars[j * 2 + 1] = hexArray[v & 0x0F];
	    }
	    return new String(hexChars);
	}

}
