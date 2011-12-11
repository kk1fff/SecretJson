package net.patrickz.onwork.secretjson;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Base64;
import org.json.JSONException;

public class AESJsonObject extends SecretJson {
	
	byte[] mRawKey;
	
	Cipher mEncodeCipher, mDecodeCipher;
	
	protected static Cipher createEncodeCipher(byte[] rawKey)
			throws InvalidKeyException {
    SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = null;
    try {
	    cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.ENCRYPT_MODE, skeySpec);
    } catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
    } catch (NoSuchPaddingException e) {
	    e.printStackTrace();
    }
    return cipher;
	}
	
	protected static Cipher createDecodeCipher(byte[] rawKey)
			throws InvalidKeyException {
    SecretKeySpec skeySpec = new SecretKeySpec(rawKey, "AES");
    Cipher cipher = null;
    try {
	    cipher = Cipher.getInstance("AES");
	    cipher.init(Cipher.DECRYPT_MODE, skeySpec);
    } catch (NoSuchAlgorithmException e) {
	    e.printStackTrace();
    } catch (NoSuchPaddingException e) {
	    e.printStackTrace();
    }
    return cipher;
	}
	
	@Override
  protected Cipher getEncodeCipher() {
	  return mEncodeCipher;
  }

	@Override
  protected Cipher getDecodeCipher() {
	  return mDecodeCipher;
  }
	
	protected byte[] getRawKey() {
		return mRawKey;
	}
	
	protected void initializeCiphers() throws InvalidKeyException {
		mEncodeCipher = createEncodeCipher(mRawKey);
		mDecodeCipher = createDecodeCipher(mRawKey);
	}
	
	public AESJsonObject(byte[] rawKey) throws InvalidKeyException {
		mRawKey = rawKey;
		initializeCiphers();
	}
	
	public AESJsonObject(String base64Secret, byte[] rawKey)
			throws InvalidKeyException, JSONException {
		super(base64Secret, createDecodeCipher(rawKey));
		mRawKey = rawKey;
		initializeCiphers();
	}
}
