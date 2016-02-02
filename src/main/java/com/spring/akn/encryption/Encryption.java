package com.spring.akn.encryption;

import javax.xml.bind.DatatypeConverter;

public class Encryption {
	
	public int decode(String code){
		int decodedId = Integer.parseInt(new String(DatatypeConverter.parseBase64Binary(code)));
		return decodedId;
	}
	
	public static void main(String[] args) {
		int id = new Encryption().decode("MQ==");
		System.out.println("ID : " + id);
	}
}
