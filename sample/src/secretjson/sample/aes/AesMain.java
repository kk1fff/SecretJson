package secretjson.sample.aes;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

import net.patrickz.onwork.secretjson.AESJsonObject;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;

public class AesMain {

	/**
	 * @param args
	 */
	public static void main(String args[]) {
		// Create AES 256 key
		byte[] rawKey = null;
		try {
	    KeyGenerator keygen = KeyGenerator.getInstance("AES");
	    keygen.init(256);
	    SecretKey key = keygen.generateKey();
	    rawKey = key.getEncoded();
	    Base64 b64 = new Base64();
	    String b64String = b64.encodeToString(rawKey);
	    System.out.println("Key (Base64 encoded): " + b64String);
    } catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
	    return;
    }
		
		// Create an empty secret json
		AESJsonObject aesJson;
		try {
			aesJson = new AESJsonObject(rawKey);
    } catch (InvalidKeyException e) {
	    e.printStackTrace();
	    return;
    }
		
		// Insert data
		try {
	    aesJson.put("testKey", "testValue");
    } catch (JSONException e) {
	    e.printStackTrace();
    }
		
		String encryptedString = aesJson.toString();
    System.out.println("Encoded Result: " + aesJson.toString());
    
		try {
			aesJson = new AESJsonObject(encryptedString, rawKey);
    } catch (InvalidKeyException e) {
	    e.printStackTrace();
	    return;
    } catch (JSONException e) {
	    e.printStackTrace();
	    return;
    }
		
		String originalValue = null;
		
		try {
	    originalValue = aesJson.getString("testKey");
    } catch (JSONException e) {
	    e.printStackTrace();
    }
		
    System.out.println("Value of testKey: " + originalValue);
	}
}
