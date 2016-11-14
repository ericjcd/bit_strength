package com.bit.strength.util;

import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;
import java.util.Base64;

public class ByteHex {
	// checked
	public static String toHexStr(String str) {
		return toHexStr(str.getBytes());
	}

	// checked
	public static String toHexStr(byte[] bs) {
		char[] chars = "0123456789ABCDEF".toCharArray();
		StringBuilder sb = new StringBuilder("");
		int bit;
		for (int i = 0; i < bs.length; i++) {
			bit = (bs[i] & 0x0f0) >> 4;
			sb.append(chars[bit]);
			bit = bs[i] & 0x0f;
			sb.append(chars[bit]);
		}
		return sb.toString();
	}

	// checked
	public static byte[] hexStr2(byte[] hexs) throws Exception {
		return hexStr2(new String(hexs));
	}

	// checked
	public static byte[] hexStr2(String src) throws Exception {
		src = src.replace("\r|\n|\t", "").toUpperCase();
		byte[] hexs = src.getBytes();
		Exception exception = new Exception("Invalid Hex String");
		if ((hexs.length % 2) == 1)
			throw exception;
		String str = "0123456789ABCDEF";
		byte[] bytes = new byte[hexs.length / 2];
		int n;
		for (int i = 0; i < bytes.length; i++) {
			if ((str.indexOf(hexs[2 * i]) == -1)
					|| (str.indexOf(hexs[2 * i + 1]) == -1))
				throw exception;
			n = str.indexOf(hexs[2 * i]) * 16;
			n += str.indexOf(hexs[2 * i + 1]);
			bytes[i] = (byte) (n & 0xff);
		}
		return bytes;
	}

	public static byte[] char2byte(char[] charstr) {
		Charset cs = Charset.forName("UTF-8");
		CharBuffer cb = CharBuffer.allocate(charstr.length);
		cb.put(charstr);
		cb.flip();
		ByteBuffer bb = cs.encode(cb);
		return bb.array();
	}

	// checked
	public static byte[] Base64Decode(byte[] bytes) {
		return Base64.getDecoder().decode(bytes);
	}

	// checked
	public static byte[] Base64Encode(byte[] bytes) {
		return Base64.getEncoder().encode(bytes);
	}
}
