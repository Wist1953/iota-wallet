package eukaryote.iotawallet;

import java.awt.BorderLayout;
import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;

import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import org.apache.commons.io.FileUtils;
import org.jasypt.exceptions.EncryptionInitializationException;
import org.jasypt.exceptions.EncryptionOperationNotPossibleException;
import org.jasypt.util.text.BasicTextEncryptor;
import org.jasypt.util.text.StrongTextEncryptor;

import lombok.Getter;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SeedStore {

	private File seedfile;

	@Getter
	private String seed;

	public SeedStore(File seedfile) {
		this.seedfile = seedfile;
		if (seedfile.exists()) {
			// decrypte seed from file

			char[] pass = passwordDialog("Enter password to decrypt wallet:");
			do {
				try {
					seed = getSeed(pass);

					// if no errors but still null, that means user cancelled. leave seed null.
					if (seed == null)
						break;
				} catch (EncryptionInitializationException | EncryptionOperationNotPossibleException e) {
					JOptionPane.showMessageDialog(null, "Password was invalid!", "Decryption Error",
							JOptionPane.ERROR_MESSAGE);
				} catch (IOException e) {
					JOptionPane.showMessageDialog(null, e, "Decryption Error", JOptionPane.ERROR_MESSAGE);
				}
			} while (seed != null);
		} else {
			log.info("Generating seed");
			String seed = generateSeed();
			seedConfirmDialog(seed);
		}
		log.info("SeedStore initialized.");
	}

	public static char[] passwordDialog(String message) {
		JPanel panel = new JPanel();
		JLabel label = new JLabel(message);
		JPasswordField pass = new JPasswordField();
		panel.setLayout(new BorderLayout());
		panel.add(label, BorderLayout.NORTH);
		panel.add(pass, BorderLayout.CENTER);
		String[] options = new String[] { "OK" };
		int option = JOptionPane.showOptionDialog(null, panel, "Wallet", JOptionPane.NO_OPTION,
				JOptionPane.PLAIN_MESSAGE, null, options, options[0]);
		if (option == 0) {
			return pass.getPassword();
		} else {
			return null;
		}
	}

	public void seedConfirmDialog(String seed) {
		while (true) {
			JPanel panel = new JPanel();
			JLabel label = new JLabel(
					"Write your seed down somewhere safe! It can be used to restore access to your funds.\n"
							+ "Do NOT show anyone your seed. Do NOT store your seed electronically. \nSeed:\n");
			JTextField pass = new JTextField();
			pass.setText(seed);
			pass.setEditable(false);
			pass.setEnabled(true);
			panel.setLayout(new BorderLayout());
			panel.add(label, BorderLayout.NORTH);
			panel.add(pass, BorderLayout.CENTER);
			JOptionPane.showMessageDialog(null, panel);

			char[] seedver = passwordDialog("Type your seed to confirm that you have your seed written down properly:");
			
			if (seed.equals(new String(seedver)))
				break;
		}
	}

	private void saveSeed(String seed, char[] pass) throws IOException {
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPasswordCharArray(pass);
		String encrypted = textEncryptor.encrypt(seed);
		FileUtils.write(seedfile, encrypted, StandardCharsets.UTF_8);
	}

	private String getSeed(char[] pass) throws IOException {
		StrongTextEncryptor textEncryptor = new StrongTextEncryptor();
		textEncryptor.setPasswordCharArray(pass);
		return textEncryptor.decrypt(FileUtils.readFileToString(seedfile, StandardCharsets.UTF_8));
	}

	static final String TRYTE_ALPHABET = "9ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	static final int SEED_LEN = 81;

	public static String generateSeed() {
		// our secure randomness source
		SecureRandom sr = new SecureRandom();

		// the resulting seed
		StringBuilder sb = new StringBuilder(SEED_LEN);

		for (int i = 0; i < SEED_LEN; i++) {
			int n = sr.nextInt(27);
			char c = TRYTE_ALPHABET.charAt(n);

			sb.append(c);
		}

		return sb.toString();
	}
}
