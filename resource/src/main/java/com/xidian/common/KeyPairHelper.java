/**
 * 
 */
package com.xidian.common;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.security.Key;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;

public class KeyPairHelper {
	private String ALGORITHM = "RSA";

	public String PADDING = "RSA/NONE/NoPadding";

	private int KEYSIZE = 1024;

	public KeyPairHelper() {
	}

	public long generateKeyPair() throws NoSuchAlgorithmException,
			NoSuchProviderException, FileNotFoundException, IOException {
		SecureRandom sr = new SecureRandom();
		KeyPairGenerator kpg = KeyPairGenerator.getInstance(ALGORITHM);
		kpg.initialize(KEYSIZE, sr);
		KeyPair kp = kpg.generateKeyPair();
		Key publicKey = kp.getPublic();
		Key privateKey = kp.getPrivate();

		long currentTime = System.currentTimeMillis();

		ObjectOutputStream oos1 = new ObjectOutputStream(new FileOutputStream(
				String.format("publickey-%d.keystore", currentTime)));
		ObjectOutputStream oos2 = new ObjectOutputStream(new FileOutputStream(
				String.format("privatekey-%d.keystore", currentTime)));
		oos1.writeObject(publicKey);
		oos2.writeObject(privateKey);
		oos1.close();
		oos2.close();

		return currentTime;
	}
}
