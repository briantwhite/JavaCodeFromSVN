package KeyMaker;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.math.BigInteger;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;

public class MakeRSAPair {
	
	/*
	 * an offset to add to the modulus of student.key and instructor.key
	 * to add a little security - that way, they can't just make
	 * an rsa key pair and have them work
	 */
	private static String OFFSET = "923678";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(2048);
			System.out.println("Generating Public & Private Keys");
			KeyPair kp = kpg.genKeyPair();
			Key publicKey = kp.getPublic();
			Key privateKey = kp.getPrivate();

			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pub = fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
			saveToFile("src/keys/public.key", pub.getModulus(), pub.getPublicExponent());
			
			RSAPrivateKeySpec priv = fact.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			saveToFile("src/keys/private.key", priv.getModulus(), priv.getPrivateExponent());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public static void saveStudentInstructorKeyPair(File directory) {
		try {
			KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
			kpg.initialize(1024);
			KeyPair kp = kpg.genKeyPair();
			Key publicKey = kp.getPublic();
			Key privateKey = kp.getPrivate();

			KeyFactory fact = KeyFactory.getInstance("RSA");
			RSAPublicKeySpec pub = fact.getKeySpec(publicKey, RSAPublicKeySpec.class);
			saveToFile(
					directory.getAbsolutePath() + System.getProperty("file.separator") + "student.key", 
					pub.getModulus().add(new BigInteger(OFFSET)), 
					pub.getPublicExponent());
			
			RSAPrivateKeySpec priv = fact.getKeySpec(privateKey, RSAPrivateKeySpec.class);
			saveToFile(
					directory.getAbsolutePath() + System.getProperty("file.separator") + "instructor.key", 
					priv.getModulus().add(new BigInteger(OFFSET)), 
					priv.getPrivateExponent());

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
	}

	private static void saveToFile(String fileName,
			BigInteger mod, BigInteger exp) throws IOException {
		ObjectOutputStream oout = new ObjectOutputStream(
				new BufferedOutputStream(new FileOutputStream(fileName)));
		try {
			oout.writeObject(mod);
			oout.writeObject(exp);
		} catch (Exception e) {
			throw new IOException("Unexpected error", e);
		} finally {
			oout.close();
		}
	}
}
