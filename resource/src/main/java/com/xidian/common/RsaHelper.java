/**
 * 
 */
package com.xidian.common;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public class RsaHelper {

	public RsaHelper() {
	}

	private static long generateKeyPair() throws NoSuchAlgorithmException,
			NoSuchProviderException, FileNotFoundException, IOException {
		KeyPairHelper keyPairHelper = new KeyPairHelper();
		return keyPairHelper.generateKeyPair();
	}

	public static String encrypt(String data, Key publicKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.ENCRYPT_MODE, publicKey);
		byte[] b = data.getBytes();
		byte[] b1 = cipher.doFinal(b);
		BASE64Encoder encoder = new BASE64Encoder();
		return HexHelper.bytes2Hex(encoder.encode(b1).getBytes());
	}

	public static String decrypt(String cryptData, Key privateKey)
			throws NoSuchAlgorithmException, NoSuchPaddingException,
			InvalidKeyException, IOException, IllegalBlockSizeException,
			BadPaddingException {
		Cipher cipher = Cipher.getInstance("RSA");
		cipher.init(Cipher.DECRYPT_MODE, privateKey);
		BASE64Decoder decoder = new BASE64Decoder();
		byte[] bytes = HexHelper.hex2Bytes(cryptData);
		byte[] b1 = decoder.decodeBuffer(new String(bytes));
		byte[] b = cipher.doFinal(b1);
		return new String(b);
	}

	public static void main(String[] args) throws FileNotFoundException,
			IOException, ClassNotFoundException, InvalidKeyException,
			NoSuchAlgorithmException, NoSuchPaddingException,
			IllegalBlockSizeException, BadPaddingException,
			NoSuchProviderException {
		long currentTime = RsaHelper.generateKeyPair();

		String source = "v军军v";

		ObjectInputStream publicKeyOis = new ObjectInputStream(
				new FileInputStream(String.format("publickey-%d.keystore",
						currentTime)));
		Key publicKey = (Key) publicKeyOis.readObject();
		publicKeyOis.close();

		ObjectInputStream privateKeyOis = new ObjectInputStream(
				new FileInputStream(String.format("privatekey-%d.keystore",
						currentTime)));
		Key privateKey = (Key) privateKeyOis.readObject();
		privateKeyOis.close();

		String cryptData = RsaHelper.encrypt(source, publicKey);
		System.out.println("-----------密文-----------");
		System.out.println(cryptData);
		System.out.println();

		String target = RsaHelper.decrypt(cryptData, privateKey);
		System.out.println("-----------原文-----------");
		System.out.println(target);
	}

}
