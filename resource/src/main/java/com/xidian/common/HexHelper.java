package com.xidian.common;

public class HexHelper {

	public HexHelper() {
	}

	private static byte charToByte(char c) {
		return (byte) "0123456789ABCDEF".indexOf(c);
	}

	public static String bytes2Hex(byte[] bytes) {
		StringBuffer sb = new StringBuffer(bytes.length * 2);
		if (bytes == null || bytes.length <= 0) {
			return null;
		}
		for (int i = 0; i < bytes.length; i++) {
			int v = bytes[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				sb.append(0);
			}
			sb.append(hv);
		}
		return sb.toString();
	}

	public static byte[] hex2Bytes(String hex) {
		if (hex == null || hex.equals("")) {
			return null;
		}
		hex = hex.toUpperCase();
		int length = hex.length() / 2;
		char[] hexChars = hex.toCharArray();
		byte[] d = new byte[length];
		for (int i = 0; i < length; i++) {
			int pos = i * 2;
			d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
		}
		return d;
	}
}
