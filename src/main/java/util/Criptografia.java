package util;

import java.security.PrivateKey;
import java.security.PublicKey;

import javax.crypto.Cipher;

public class Criptografia {

    private final static String ALGORITMO = "DSA";

    /**
     * Encripta array de bytes utilizando chave privada
     * @param mensagem
     * @param privateKey
     * @return byte[] contendo mensagem encriptada
     */
    public static byte[] encriptarComChavePrivada(byte[] mensagem, PrivateKey privateKey) {
        byte[] cipherData = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, privateKey);
            cipherData = cipher.doFinal(mensagem);
        } catch (Exception ex) {
            System.out.println("Erro Encrypt com chave privada: " + ex.getLocalizedMessage());
        }
        return cipherData;
    }
    
    /**
     * Encripta array de bytes utilizando chave publica
     * @param mensagem
     * @param publicKey
     * @return byte[] contendo mensagem encriptada
     */
    public static byte[] encriptarComChavePublica(byte[] mensagem, PublicKey publicKey) {
        byte[] cipherData = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.ENCRYPT_MODE, publicKey);
            cipherData = cipher.doFinal(mensagem);
        } catch (Exception ex) {
            System.out.println("Erro Encrypt com chave privada: " + ex.getLocalizedMessage());
        }
        return cipherData;
    }

    public static byte[] decriptarComChavePrivada(byte[] msgEncriptada, PrivateKey privateKey) {
        byte[] cipherData = null;
        try {
            Cipher cipher = Cipher.getInstance(ALGORITMO);
            cipher.init(Cipher.DECRYPT_MODE, privateKey);
            cipherData = cipher.doFinal(msgEncriptada);
        } catch (Exception ex) {
            System.out.println("Erro Encrypt com chave privada: " + ex.getLocalizedMessage());
        }
        return cipherData;
    }
    
}
