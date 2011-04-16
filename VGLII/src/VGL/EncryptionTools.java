package VGL;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.RSAPrivateKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Enumeration;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

public class EncryptionTools {

	/*
	 * an offset to add to the modulus of student.key and instructor.key
	 * to add a little security - that way, they can't just make
	 * an rsa key pair and have them work
	 */
	private static String OFFSET = "923678";

	/**
	 * key for encrypting work files
	 *   XORed with bytes of work file
	 */
	private final static byte[] KEY = 
		(new String("The Virtual Genetics Lab is Awesome!")).getBytes();

	public static void saveXOREncrypted(Document doc, File outFile) {
		XMLOutputter outputter = 
			new XMLOutputter(Format.getPrettyFormat());
		String xmlString = outputter.outputString(doc);

		//encrypt it with XOR and zip it to prevent cheating
		byte[] xmlBytes = null;
		try {
			xmlBytes = xmlString.getBytes("UTF-8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}

		for (int i = 0; i < xmlBytes.length; i++) {
			xmlBytes[i] = (byte) (xmlBytes[i] ^ KEY[i % (KEY.length - 1)]);
		}

		ZipOutputStream zipWriter = null;
		try {
			zipWriter = 
				new ZipOutputStream(new FileOutputStream(outFile));
			zipWriter.setLevel(Deflater.DEFAULT_COMPRESSION);
			zipWriter.putNextEntry(new ZipEntry("encrypted.txt")); //$NON-NLS-1$
			zipWriter.write(xmlBytes, 0, xmlBytes.length);
		} catch (IOException e) {
			e.printStackTrace();
		}
		finally {
			try {
				if (zipWriter != null) {
					zipWriter.close();
				}
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public static Document readXOREncrypted(File inFile) {
		ZipFile workZip;
		Document doc = null;
		try {
			workZip = new ZipFile(inFile);
			Enumeration zipFileEntries = workZip.entries();
			ZipEntry zipEntry = (ZipEntry)zipFileEntries.nextElement();
			InputStream input = workZip.getInputStream(zipEntry);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int next = input.read();
			while (next > -1) {
				bos.write(next);
				next = input.read();
			}
			bos.flush();
			byte[] bytes = bos.toByteArray();

			if (zipEntry.getName().equals("encrypted.txt")) {
				// decrypt if it was encrypted
				for (int i = 0; i < bytes.length; i++) {
					bytes[i] = (byte) (bytes[i] ^ KEY[i % (KEY.length - 1)]);
				}
			}

			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(new ByteArrayInputStream(bytes));

			// for debugging
//			System.out.println(new String(bytes));

		} catch (Exception e) {
			JOptionPane.showMessageDialog(null,
					Messages.getInstance().getString("VGLII.ErrorOpeningFileLine1") + "\n"
					+ Messages.getInstance().getString("VGLII.ErrorOpeningFileLine2") + "\n"
					+ Messages.getInstance().getString("VGLII.ErrorOpeningFileLine3"), 
					Messages.getInstance().getString("VGLII.ErrorOpeningFileHeadline"),
					JOptionPane.ERROR_MESSAGE);
		} 

		return doc;
	}

	/*
	 * uses keys/private.key "private key" - use only to read grader.key
	 * in VGLII (this key is made public)
	 */
	public static Document readRSAEncrypted(File inFile) {
		Document doc = null;

		BufferedReader in = null;
		StringBuffer b = null;
		String input = null;
		try {
			in = new BufferedReader(new FileReader(inFile));
			b = new StringBuffer();
			String str;
			while ((str = in.readLine()) != null) {
				b.append(str);
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (in != null) {
				try {
					in.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
				input = b.toString();
			}
		}

		byte[] rsaBytes = Base64Coder.decode(input);

		byte[] xmlBytes = null;
		try {
			PrivateKey privKey = readPrivateKeyFromFile("keys/private.key");
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			xmlBytes = cipher.doFinal(rsaBytes);
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(new ByteArrayInputStream(xmlBytes));

			// for debugging
//			System.out.println(new String(xmlBytes));

		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	private static PrivateKey readPrivateKeyFromFile(String keyFileName) throws IOException {
		InputStream in =
			EncryptionTools.class.getResourceAsStream(keyFileName);
		ObjectInputStream oin =
			new ObjectInputStream(new BufferedInputStream(in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			BigInteger e = (BigInteger) oin.readObject();
			RSAPrivateKeySpec keySpec = new RSAPrivateKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PrivateKey privateKey = fact.generatePrivate(keySpec);
			return privateKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}

	/**
	 * reads in public key (student.key) used for encrypted saving
	 * subtracts "offset" - added security
	 * 
	 */
	public static PublicKey readPublicKeyFromFile(String keyFileName) throws IOException {
		ObjectInputStream oin =
			new ObjectInputStream(new BufferedInputStream(new FileInputStream(keyFileName)));
		try {
			BigInteger m = (BigInteger) oin.readObject();
			m = m.subtract(new BigInteger(OFFSET));
			BigInteger e = (BigInteger) oin.readObject();
			RSAPublicKeySpec keySpec = new RSAPublicKeySpec(m, e);
			KeyFactory fact = KeyFactory.getInstance("RSA");
			PublicKey pubKey = fact.generatePublic(keySpec);
			return pubKey;
		} catch (Exception e) {
			throw new RuntimeException("Spurious serialisation error", e);
		} finally {
			oin.close();
		}
	}

}
