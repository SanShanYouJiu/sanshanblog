package com.sanshan.util;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoderGenerator {

	public static void main(String[] args) {
		BCryptPasswordEncoder passwordEncoder;

		int i = 0;
        while (i < 10) {
			String password = "123456";
			passwordEncoder = new BCryptPasswordEncoder();

			String hashedPassword = passwordEncoder.encode(password);

			System.out.println(hashedPassword);
			i++;
		}


	}
}