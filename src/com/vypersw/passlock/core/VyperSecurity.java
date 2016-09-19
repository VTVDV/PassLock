package com.vypersw.passlock.core;

import java.security.*;
import java.math.*;
import java.security.spec.InvalidKeySpecException;
import java.util.Base64;

import javax.crypto.*;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.PBEParameterSpec;

public class VyperSecurity 
{
	private static VyperSecurity instance = null;
	
	private VyperSecurity()
	{
		
	}
	
	public static VyperSecurity getInstance()
	{
		if(instance == null)
		{
			instance = new VyperSecurity();
		}
		return instance;
	}
	
	public String encrypt(String pass)
	{
		String keyString = "ITcr7253";
		try 
		{
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(keyString.toCharArray()));
			byte[] randomSalt = new byte[8];
			new SecureRandom().nextBytes(randomSalt);
			String salt = Base64.getEncoder().encodeToString(randomSalt);
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");
			pbeCipher.init(Cipher.ENCRYPT_MODE, key, new PBEParameterSpec(randomSalt, 20));
			String encrypted = new String(Base64.getEncoder().encode(pbeCipher.doFinal(pass.getBytes())));
			return salt + encrypted;			
		} 
		catch (NoSuchAlgorithmException exception) 
		{			
			exception.printStackTrace();
		}
		catch (NoSuchPaddingException exception) 
		{
			exception.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return "";
	}
	
	public String decrypt(String pass)
	{
		String keyString = "ITcr7253";
		try
		{
			SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBEWithMD5AndDES");
			SecretKey key = keyFactory.generateSecret(new PBEKeySpec(keyString.toCharArray()));
			Cipher pbeCipher = Cipher.getInstance("PBEWithMD5AndDES");			
			String salt = pass.substring(0, 12);
			String unsalted = pass.substring(12, pass.length());
			byte[] saltBytes = Base64.getDecoder().decode(salt.getBytes());
			pbeCipher.init(Cipher.DECRYPT_MODE, key, new PBEParameterSpec(saltBytes, 20));
			return new String(pbeCipher.doFinal(Base64.getDecoder().decode(unsalted.getBytes())));
		}
		catch (NoSuchAlgorithmException exception) 
		{			
			exception.printStackTrace();
		}
		catch (NoSuchPaddingException exception) 
		{
			exception.printStackTrace();
		}
		catch (Exception exception)
		{
			exception.printStackTrace();
		}
		return "";
	}
	
	public String generatePass()
	{
		SecureRandom random = new SecureRandom();
		return new BigInteger(130,random).toString(32);
	}
}
