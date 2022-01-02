/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package herramientas;

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Luis Monterroso
 */
public class Encriptador {

    private static final String llave = "41288320@Bc";
    private static final String sal = "@#$%sal";

    /**
     * Este metodo crea un bucle de 5 iteraciones y por cada iteracion envia y
     * recibe una clave incriptada
     *
     * @param password
     * @return
     */
    public String encriptarPassword(String password) {
        //repetimos el metodo de incriptacion 5 veces
        for (int x = 0; x < 5; x++) {
            password = getAES(password);
        }
        return password;
    }

    /**
     * Este metodo incripta una palabra en AES
     *
     * @param data
     * @return
     */
    private static String getAES(String data) {
        try {
            byte[] iv = new byte[16];
            IvParameterSpec ivParameterSpec = new IvParameterSpec(iv);
            SecretKeyFactory secretKeyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            KeySpec keySpec = new PBEKeySpec(llave.toCharArray(), sal.getBytes(), 65536, 256);
            SecretKey secretKeyTemp = secretKeyFactory.generateSecret(keySpec);
            SecretKeySpec secretKey = new SecretKeySpec(secretKeyTemp.getEncoded(), "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameterSpec);
            return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException | InvalidAlgorithmParameterException | InvalidKeyException | NoSuchAlgorithmException | InvalidKeySpecException | BadPaddingException | IllegalBlockSizeException | NoSuchPaddingException e) {
            return null;
        }
    }

}
