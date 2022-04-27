package edu.berziet.houserental;

import java.util.regex.Pattern;

public class PasswordValidator {
	private final static String passwordPolicy = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%!{}])(?=\\S+$).{8,15}$";
	
	public static boolean isValidPassword(String plainPassword) {
		Pattern passRegex = Pattern.compile(passwordPolicy);
		return passRegex.matcher(plainPassword).matches();
	}
}
