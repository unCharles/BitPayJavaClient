package controller;

import java.io.BufferedOutputStream;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.math.BigInteger;
import java.util.Arrays;

import com.google.bitcoin.core.*;
import com.google.bitcoin.core.ECKey;
import com.google.bitcoin.core.ECKey.ECDSASignature;
import com.google.bitcoin.core.Sha256Hash;

public class KeyUtils {
	
	final protected static char[] hexArray = "0123456789abcdef".toCharArray();
	final protected static String PRIV_KEY_FILENAME = "bitpay_private.key";
	
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
	
	/*
	 * Derive a SIN from a public key
	 * 
	 * @param	key	your private ECKey
	 * 
	 * @return SIN. a string starting with the letter T that is derived from your
	 * public key
	 */
	public static String deriveSIN(ECKey key) {
		//Get sha256 hash of the public key.
		byte[] pubKeyHash = key.getPubKeyHash(); //Gets the hash160 form of the public key
		//Convert binary pubKeyHash, SINtype and version to Hex
		String SINtype = "02";
		String version = "0F";
		String pubKeyHashHex = bytesToHex(pubKeyHash);

		//Append all 3
		String preSIN = version + SINtype + pubKeyHashHex;
		
		//Convert the hex string back to binary and double sha256 hash it leaving in binary both times
		byte[] preSINbyte = preSIN.getBytes();
		Sha256Hash hash = Sha256Hash.create(preSINbyte);
		byte[] hashBytes = hash.getBytes();
		Sha256Hash hash2 = Sha256Hash.create(hashBytes);
		byte[] hash2Bytes = hash2.getBytes();
		
		//Convert back to hex and take first 4 bytes
		String first4Bytes = bytesToHex(Arrays.copyOfRange(hash2Bytes, 0, 4));
		
		//Append first 4 bytes to fully appended sin string
		String unencoded = preSIN + first4Bytes;
		byte[] unencodedBytes = new BigInteger(unencoded,16).toByteArray();

		//String encoded = Base58.encode(unencodedBytes);
		String SIN = Base58.encode(unencodedBytes);
		System.out.println(SIN);
		return SIN;
	}

	public static ECKey readKeyFromASN1() throws IOException {
		RandomAccessFile f = new RandomAccessFile(PRIV_KEY_FILENAME, "r");
		byte[] bytes = new byte[(int)f.length()];
		f.read(bytes);
		f.close();
		ECKey key = ECKey.fromASN1(bytes);

		return key;
	}

	public static void saveECKey(ECKey key) throws IOException{
		byte[] bytes = key.toASN1();
		BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(PRIV_KEY_FILENAME));
		bos.write(bytes);
		bos.flush();
		bos.close();
	}
	
	public static boolean privateKeyExists() {
		File f = new File(PRIV_KEY_FILENAME);
		return (f.exists() && !f.isDirectory());
	}

	public static ECKey generateNewECKey() {
		//Default constructor uses SecureRandom numbers.
		return new ECKey();
	}

}
