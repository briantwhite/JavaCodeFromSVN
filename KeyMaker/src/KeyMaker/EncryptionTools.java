package KeyMaker;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.PrintWriter;
import java.io.StringReader;
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

import org.jdom.Document;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;


public class EncryptionTools {

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
			System.out.println(new String(bytes));

		} catch (Exception e) {
			e.printStackTrace();
		} 

		return doc;
	}

	/*
	 * uses keys/public.key "public key" - use only for saving grader.key 
	 * in KeyMaker (this key is not made public)
	 */
	public static void saveRSAEncrypted(Document doc, File outFile) {

		XMLOutputter outputter = 
			new XMLOutputter(Format.getPrettyFormat());
		String xmlString = outputter.outputString(doc);

		//get bytes ready for RSA
		byte[] xmlBytes = null;
		try {
			xmlBytes = xmlString.getBytes("UTF-8"); //$NON-NLS-1$
		} catch (UnsupportedEncodingException e1) {
			e1.printStackTrace();
		}
		
		// RSA encrypt and save
		PrintWriter out = null;
		try {
			out = new PrintWriter(new FileWriter(outFile));
			out.println(rsaEncrypt(xmlBytes));
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			out.close();
		}
	}

	private static String rsaEncrypt(byte[] data) {
		StringBuffer rsaByteBuffer = new StringBuffer();
		try {
			PublicKey pubKey = readPublicKeyFromFile("keys/public.key");
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.ENCRYPT_MODE, pubKey);
			
			// do in blocks of 100 bytes; separated by ";"
			int numBlocks = (data.length + 99)/100;
			for (int i = 0; i < numBlocks; i++) {
				// copy in block of 100 bytes
				byte[] currentData = new byte[100];
				int j = 0;
				for (j = 0; ( (j < 100) && ((j + (100 * i)) < data.length) ); j++) {
					currentData[j] = data[j + (100 * i)];
				}
				
				//pad last block to 100 bytes with spaces
				while (j < 100) {
					currentData[j] = ' ';
					j++;
				}
				rsaByteBuffer.append(Base64Coder.encode(cipher.doFinal(currentData)));
				rsaByteBuffer.append(";"); // separator between blocks
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		String out = rsaByteBuffer.toString();
		return out;
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

		String[] base64RSABlocks = input.split(";");

		StringBuffer xmlBuffer = new StringBuffer();
		try {
			PrivateKey privKey = readPrivateKeyFromFile("keys/private.key");
			Cipher cipher = Cipher.getInstance("RSA/ECB/PKCS1Padding");
			cipher.init(Cipher.DECRYPT_MODE, privKey);
			
			for (int i = 0; i < base64RSABlocks.length; i++) {
				xmlBuffer.append(
						new String(
								cipher.doFinal(
										Base64Coder.decode(base64RSABlocks[i]))));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		String xmlString = xmlBuffer.toString();
		
		// for debugging
		System.out.println(xmlString);
		
		try {
			SAXBuilder builder = new SAXBuilder();
			doc = builder.build(new StringReader(xmlString));


		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	private static PublicKey readPublicKeyFromFile(String keyFileName) throws IOException {
		InputStream in =
			EncryptionTools.class.getResourceAsStream(keyFileName);
		ObjectInputStream oin =
			new ObjectInputStream(new BufferedInputStream(in));
		try {
			BigInteger m = (BigInteger) oin.readObject();
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

}
