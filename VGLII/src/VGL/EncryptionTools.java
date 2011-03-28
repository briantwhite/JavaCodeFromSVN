package VGL;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Enumeration;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipException;
import java.util.zip.ZipFile;
import java.util.zip.ZipOutputStream;

import javax.swing.JOptionPane;

import org.jdom.Document;
import org.jdom.JDOMException;
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

}
