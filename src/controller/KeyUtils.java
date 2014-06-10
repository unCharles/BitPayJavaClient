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
	
	/*
	 * Accepts a hex encoded string representation of the compressed
	 * private key.
	 * 
	 * @param	String privateKey	Hex Encoded String representation of
	 * a bitcoin private key.
	 * 
	 * @return	ECKey
	 */
	public static ECKey loadKey(String privateKey) {
		BigInteger privKey = new BigInteger(privateKey, 16);
		ECKey key = new ECKey(privKey, null, true);
		return key;
	}
	
	/*
	 * Read a compressed hex encoded bitcoin private key from file.
	 * 
	 * @param	filename	the filename of the hex encoded private key
	 */
	public static String readCompressedHexKey(String filename) {
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
	
	/*
	 * Sign a string with the bitcoin private key
	 * 
	 * @param	key	ECKey- bitcoin private key
	 * @param	input	the string you want to sign
	 * 
	 * @return	the signature
	 */
	public static String signString(ECKey key, String input) {
		byte[] data = input.getBytes();
        Sha256Hash hash = Sha256Hash.create(data);
        ECDSASignature sig = key.sign(hash, null);
        byte[] bytes = sig.encodeToDER();
        return bytesToHex(bytes);
	}
	
	/*
	 * Convert a byte array to a hex encoded string
	 * 
	 * @param	bytes	the bytes to encode
	 * @return	a hex encoded string
	 */
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
