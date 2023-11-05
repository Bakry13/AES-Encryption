import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Crypto {
    private String key;
    private String saltValue;
    private File inputFileName;
    private File outputFileName;

    public Crypto setKey(String key) {
        this.key = key;
        return this;
    }

    public Crypto setSaltValue(String saltValue) {
        this.saltValue = saltValue;
        return this;
    }

    public Crypto setInputFileName(File inputFileName) {
        this.inputFileName = inputFileName;
        return this;
    }
    public Crypto setOutputFileName(File outputFileName) {
        this.outputFileName = outputFileName;
        return this;
    }
    public void encrypt() {
        fileProcessor(Cipher.ENCRYPT_MODE);
    }

    public void decrypt() {
        fileProcessor(Cipher.DECRYPT_MODE);
    }

     void fileProcessor(int cipherMode){
        try {
            /* Declare a byte array. */
            byte[] iv = {0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0};
            IvParameterSpec ivspec = new IvParameterSpec(iv);
            /* Create factory for secret keys. */
            SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            /* PBEKeySpec class implements KeySpec interface. */
            KeySpec spec = new PBEKeySpec(key.toCharArray(), saltValue.getBytes(), 65536, 256);
            SecretKey tmp = factory.generateSecret(spec);
            SecretKeySpec secretKey = new SecretKeySpec(tmp.getEncoded(), "AES");
//            System.out.println(Base64.getEncoder().encodeToString(secretKey.getEncoded()));

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(cipherMode, secretKey, ivspec);

            FileInputStream inputStream = new FileInputStream(inputFileName);
            byte[] inputBytes = new byte[(int) inputFileName.length()];
            inputStream.read(inputBytes);

            byte[] outputBytes = cipher.doFinal(inputBytes);

            FileOutputStream outputStream = new FileOutputStream(outputFileName);
            outputStream.write(outputBytes);

            inputStream.close();
            outputStream.close();

        } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeySpecException
                | InvalidKeyException | BadPaddingException | InvalidAlgorithmParameterException
                | IllegalBlockSizeException | IOException e) {
            e.printStackTrace();
        }
     }

}
