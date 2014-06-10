package controller;
import com.google.bitcoin.core.*;

public class Encoder extends VersionedChecksummedBytes{
	
	public Encoder(int version, byte[] bytes){
		super(version, bytes);
	}
	
}
