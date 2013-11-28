package model;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.Cipher;

public class ChaveSeguranca {
	private PrivateKey priv;
	private PublicKey pub;

	private final static String ALGORITMO = "DSA";

	public ChaveSeguranca() {
		try {

			final KeyPairGenerator keyGen = KeyPairGenerator.getInstance(
					ALGORITMO, "SUN");
			final SecureRandom random = SecureRandom.getInstance("SHA1PRNG",
					"SUN");
			keyGen.initialize(1024, random);
			KeyPair pair = keyGen.generateKeyPair();
			this.priv = pair.getPrivate();
			this.pub = pair.getPublic();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public static byte[] encriptarComChavePrivada(String mensagem, PrivateKey privada) {
		byte[] cipherData = null;
		try {
			Cipher cipher = Cipher.getInstance(ALGORITMO);
			cipher.init(Cipher.ENCRYPT_MODE, privada);
			cipherData = cipher.doFinal(mensagem.getBytes());
		} catch (Exception ex) {
			Logger.getLogger(Criptografia.class.getName()).log(Level.SEVERE,
					null, ex);
		}
		return cipherData;
	}

	public PrivateKey getPriv() {
		return priv;
	}

	public PublicKey getPub() {
		return pub;
	}

}
