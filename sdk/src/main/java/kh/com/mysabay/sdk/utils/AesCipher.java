package kh.com.mysabay.sdk.utils;

import android.media.MediaCodec;
import android.util.Base64;

import com.google.gson.Gson;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;
import java.security.GeneralSecurityException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * AesCipher
 * <p>Encode/Decode text by password using AES-128-CBC algorithm</p>
 */
public class AesCipher {
    public static final int INIT_VECTOR_LENGTH = 16;
    private static final String TAG = AesCipher.class.getSimpleName();
    /**
     * @see <a href="https://stackoverflow.com/questions/9655181/how-to-convert-a-byte-array-to-a-hex-string-in-java">how-to-convert-a-byte-array-to-a-hex-string</a>
     */
    private final static char[] hexArray = "0123456789ABCDEF".toCharArray();

    private AesCipher() {
        super();
    }

    /**
     * Encrypt input text by AES-128-CBC algorithm
     *
     * @param secretKey 16/24/32 -characters secret password
     * @param plainText Text for encryption
     * @return Encoded string or NULL if error
     */
    public static String encrypt(String secretKey, String plainText) {
        try {
            // Check secret length
            if (!isKeyLengthValid(secretKey))
                throw new Exception("Secret key's length must be 128, 192 or 256 bits");

            // Get random initialization vector
            SecureRandom secureRandom = new SecureRandom();
            byte[] initVectorBytes = new byte[INIT_VECTOR_LENGTH / 2];
            secureRandom.nextBytes(initVectorBytes);
            String initVector = bytesToHex(initVectorBytes);
            initVectorBytes = initVector.getBytes(StandardCharsets.UTF_8);

            IvParameterSpec ivParameterSpec = new IvParameterSpec(initVectorBytes);
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Encrypt input text
            byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));

            ByteBuffer byteBuffer = ByteBuffer.allocate(initVectorBytes.length + encrypted.length);
            byteBuffer.put(initVectorBytes);
            byteBuffer.put(encrypted);

            // Result is base64-encoded string: initVector + encrypted result
            return Base64.encodeToString(byteBuffer.array(), Base64.DEFAULT);
        } catch (Throwable t) {
            t.printStackTrace();
            LogUtil.error(TAG, t.getMessage());
            return "";
        }
    }

    /**
     * Decrypt encoded text by AES-128-CBC algorithm
     *
     * @param secretKey  16/24/32 -characters secret password
     * @param cipherText Encrypted text
     * @return Self object instance with data or error message
     */
    public static String decrypt(String secretKey, String cipherText) {
        try {
            // Check secret length
            if (!isKeyLengthValid(secretKey)) {
                throw new Exception("Secret key's length must be 128, 192 or 256 bits");
            }

            // Get raw encoded data
            byte[] encrypted = Base64.decode(cipherText, Base64.DEFAULT);

            // Slice initialization vector
            IvParameterSpec ivParameterSpec = new IvParameterSpec(encrypted, 0, INIT_VECTOR_LENGTH);
            // Set secret password
            SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivParameterSpec);

            // Trying to get decrypted text
            return new String(cipher.doFinal(encrypted, INIT_VECTOR_LENGTH, encrypted.length - INIT_VECTOR_LENGTH));
        } catch (Throwable t) {
            t.printStackTrace();
            LogUtil.error(TAG, t.getMessage());
            return "";
        }
    }

    /**
     * Check that secret password length is valid
     *
     * @param key 16/24/32 -characters secret password
     * @return TRUE if valid, FALSE otherwise
     */
    public static boolean isKeyLengthValid(String key) {
        return key.length() == 16 || key.length() == 24 || key.length() == 32;
    }

    /**
     * Convert Bytes to HEX
     *
     * @param bytes Bytes array
     * @return String with bytes values
     */
    public static String bytesToHex(byte[] bytes) {
        char[] hexChars = new char[bytes.length * 2];
        for (int j = 0; j < bytes.length; j++) {
            int v = bytes[j] & 0xFF;
            hexChars[j * 2] = hexArray[v >>> 4];
            hexChars[j * 2 + 1] = hexArray[v & 0x0F];
        }
        return new String(hexChars);
    }

    //old

    public static final String PROVIDER = "BC";
    public static final int SALT_LENGTH = 8;
    public static final int IV_LENGTH = 16;
    public static final int PBE_ITERATION_COUNT = 100;
    public static final String CIPHER_ALGORITHM = "AES/CBC/PKCS5Padding";
    public static final byte[] ivBytes = {0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00};

    byte[] _key = new byte[32]; //256 bit key space
    byte[] _salt = new byte[8];

    public static String encryptFirst(SecretKey secret, byte[] iv, String cleartext) throws
            NoSuchAlgorithmException,
            NoSuchPaddingException,
            InvalidKeyException,
            InvalidAlgorithmParameterException,
            IllegalBlockSizeException,
            BadPaddingException {

        IvParameterSpec ivspec = new IvParameterSpec(iv);
        Cipher encryptionCipher = Cipher.getInstance(CIPHER_ALGORITHM);
        encryptionCipher.init(Cipher.ENCRYPT_MODE, secret, ivspec);
        byte[] encryptedText = encryptionCipher.doFinal(cleartext.getBytes());
        return Base64.encodeToString(encryptedText, Base64.DEFAULT);


    }

    public static String encryptSecond(SecretKey secret, @NotNull String encrypted64First) throws GeneralSecurityException {

        IvParameterSpec ivspec = new IvParameterSpec(ivBytes);
        Cipher encryptionCipher2 = Cipher.getInstance(CIPHER_ALGORITHM);
        encryptionCipher2.init(Cipher.ENCRYPT_MODE, secret, ivspec);
        byte[] encryptTextSecond = encryptionCipher2.doFinal(encrypted64First.getBytes());
        return Base64.encodeToString(encryptTextSecond, Base64.DEFAULT);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static String decryptFirst(SecretKey secret, String decryptFirst, byte[] bytes) throws GeneralSecurityException {
        IvParameterSpec ivspec;
        if (bytes == null) {
            ivspec = new IvParameterSpec(ivBytes);
        } else
            ivspec = new IvParameterSpec(bytes);

        Cipher decryptionCipherFirst = Cipher.getInstance(CIPHER_ALGORITHM);
        byte[] byteDecrypted = Base64.decode(decryptFirst.getBytes(), Base64.DEFAULT);

        decryptionCipherFirst.init(Cipher.DECRYPT_MODE, secret, ivspec);
        byte[] byteDecryptedTextFirst = decryptionCipherFirst.doFinal(byteDecrypted);
        return new String(byteDecryptedTextFirst);
    }

    @NotNull
    public static String decryptFirst(SecretKey secret, String decryptFirst) throws GeneralSecurityException {
        return decryptFirst(secret, decryptFirst, null);
    }

    @NotNull
    @Contract("_, _, _ -> new")
    public static String decryptSecond(SecretKey secret, byte[] iv, String decryptedSecond) throws MediaCodec.CryptoException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IllegalBlockSizeException, BadPaddingException {

        IvParameterSpec ivspec = new IvParameterSpec(iv);
        Cipher decryptionCipherSecond = Cipher.getInstance(CIPHER_ALGORITHM);
        decryptionCipherSecond.init(Cipher.DECRYPT_MODE, secret, ivspec);
        byte[] byteDecrypted = Base64.decode(decryptedSecond, Base64.DEFAULT);
        byte[] byteDecryptedTextSecond = decryptionCipherSecond.doFinal(byteDecrypted);
        return new String(byteDecryptedTextSecond, StandardCharsets.UTF_8);

    }

    @NotNull
    public static SecretKey getSecretKey(@NotNull String password, byte[] salt) throws GeneralSecurityException, NoSuchProviderException {
        KeySpec keySpec = new PBEKeySpec(password.toCharArray(), salt, PBE_ITERATION_COUNT, 256);
        SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
        byte[] keyBytes = keyFactory.generateSecret(keySpec).getEncoded();
        SecretKey key = new SecretKeySpec(keyBytes, "AES");
        return key;

    }

    public static String encodeSecretKeyToString(@NotNull SecretKey secretKey) {
        return Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
    }

    public static String encodeSecretKeyToString() throws NoSuchAlgorithmException {
        SecretKey secretKey = KeyGenerator.getInstance("AES").generateKey();
        return Base64.encodeToString(secretKey.getEncoded(), Base64.DEFAULT);
    }

    @NotNull
    public static SecretKey decodeStringToSecretKey(String stringKey) {
        byte[] bytes = Base64.decode(stringKey, Base64.DEFAULT);
        return new SecretKeySpec(bytes, "AES");
    }

    @NotNull
    @Contract("_ -> new")
    public static SecretKey getSecretKey2(@NotNull String password2) {
        byte[] keyBytes = new byte[32];
        // explicitly fill with zeros
        Arrays.fill(keyBytes, (byte) 0x0);
        // if password is shorter then key length, it will be zero-padded
        // to key length
        byte[] passwordBytes = password2.getBytes(StandardCharsets.UTF_8);
        System.arraycopy(passwordBytes, 0, keyBytes, 0, passwordBytes.length);

        LogUtil.debug("secretKey bytes ; " + new Gson().toJson(keyBytes));

        //LogUtil.debug("secretKey bytes to String ; " + new String(keyBytes, StandardCharsets.UTF_8));

        return new SecretKeySpec(keyBytes, "AES");
    }

    @NotNull
    public static byte[] generateSalt() throws GeneralSecurityException {

        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[SALT_LENGTH];
        random.nextBytes(salt);
        return salt;
    }

    @NotNull
    public static byte[] generateIv() throws NoSuchAlgorithmException, NoSuchProviderException {
        SecureRandom random = new SecureRandom();
        byte[] iv = new byte[IV_LENGTH];
        random.nextBytes(iv);
        return iv;
    }

    //convert String jsonobject to hashmap
    @NotNull
    public static HashMap<String, String> convertToHashMap(String jsonString) {
        HashMap<String, String> myHashMap = new HashMap<String, String>();
        try {
            //JSONArray jArray = new JSONArray(jsonString);
            JSONObject jObject = new JSONObject(jsonString);
            String keyString = null;
            for (int i = 0; i < jObject.length(); i++) {
                // beacuse you have only one key-value pair in each object so I have used index 0
                keyString = (String) jObject.names().get(i);
                myHashMap.put(keyString, jObject.getString(keyString));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return myHashMap;
    }

    //convert arrayjson to jsonobject
    @NotNull
    public static Map<String, Object> convertArrayJsonToMap(@NotNull JSONObject object) throws JSONException {
        Map<String, Object> map = new HashMap<String, Object>();
        Iterator<?> keys = object.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            map.put(key, fromJson(object.get(key)));
        }
        return map;
    }

    @Nullable
    private static Object fromJson(Object json) throws JSONException {
        if (json == JSONObject.NULL) {
            return null;
        } else if (json instanceof JSONObject) {
            return convertArrayJsonToMap((JSONObject) json);
        } else {
            return json;
        }
    }
}