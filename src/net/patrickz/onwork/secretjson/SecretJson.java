package net.patrickz.onwork.secretjson;

import java.io.UnsupportedEncodingException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class SecretJson extends JSONObject {
	
	/**
	 * Base64 decode enc_string and decrypt it with user cipher.
	 * @param enc_string 
	 * @param decodeCipher
	 * @return
	 */
	protected static String getJsonStringFromBase64Secret(
			String enc_string, Cipher decodeCipher) {
		// Base64 decode the encoded byte array.
		Base64 base64 = new Base64();
		byte secret_byte[] = base64.decode(enc_string);
		byte original[];
		
		try {
			original = decodeCipher.doFinal(secret_byte);
    } catch (IllegalBlockSizeException e) {
	    e.printStackTrace();
	    return null;
    } catch (BadPaddingException e) {
	    e.printStackTrace();
	    return null;
    }
		
		String jsonString;
    try {
	    jsonString = new String(original, "UTF-8");
    } catch (UnsupportedEncodingException e) {
	    jsonString = new String(original); // Use default charset
    }
		
		return jsonString;
	}
	
	/**
	 * Function that use a cipher to encrypt the string which is
	 * passed in argument.
	 * @param jsonString String that is going to be encrypted.
	 * @param encodeCipher cipher that used to encrypt string. It should
	 * be initialized before it is passed to this function.
	 * @return String that is form by encoding byte array which is encrypted
	 * with the cipher. 
	 */
	protected static String getBase64SecretFromJsonString(
			String jsonString, Cipher encodeCipher) {
		// Transfer string to byte array.
		byte original[];
    try {
	    original = jsonString.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e1) {
    	original = jsonString.getBytes(); // Use default charset
    }
		byte secret[] = null;
		
		// Encrypt string.
		try {
			secret = encodeCipher.doFinal(original);
    } catch (IllegalBlockSizeException e) {
	    e.printStackTrace();
	    return null;
    } catch (BadPaddingException e) {
	    e.printStackTrace();
	    return null;
    }
		
		// Base64 encode the encrypted info.
		Base64 base64 = new Base64();
		String sec_base64 = base64.encodeToString(secret);
		
		return sec_base64;
	}
	
	/**
	 * Implement this to provide cipher that is used to encrypt.
	 * @return
	 */
	protected abstract Cipher getEncodeCipher();
	
	/**
	 * Implement this to provide cipher that is used to decrypt.
	 * @return
	 */
	protected abstract Cipher getDecodeCipher();
	
	/**
	 * Construct an empty SecretJson.
	 */
	public SecretJson() {
		
	}
	
	/**
	 * Decrypt encoded string and form a JSON object.
	 * @param enc_string Encrypted secret string.
	 * @param decodeCipher cipher that used to decrypt, must be initialized.
	 * @throws JSONException
	 */
	public SecretJson(String enc_string, Cipher decodeCipher)
			throws JSONException {
		super(getJsonStringFromBase64Secret(enc_string, decodeCipher));
	}
	
	/**
	 * Get a base64-encoded byte array. That byte array is formed by encoding
	 * this json object with cipher.
	 * @return
	 */
	@Override
	public String toString() {
		return getBase64SecretFromJsonString(super.toString(), getEncodeCipher());
	}
}
