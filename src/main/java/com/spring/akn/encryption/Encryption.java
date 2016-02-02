package com.spring.akn.encryption;

import javax.xml.bind.DatatypeConverter;

public class Encryption {
	
	public int decode(String code){
		int decodedId = Integer.parseInt(new String(DatatypeConverter.parseBase64Binary(code)));
		return decodedId;
	}
	
	public String encode(String code){
		String decodedId = DatatypeConverter.printBase64Binary(code.getBytes());
		return decodedId;
	}
	
	public static void main(String[] args) {
		System.out.println(new Encryption().encode("1"));
		System.out.println(new Encryption().decode("MQ=="));
	}
}
