package convert;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Objects;

public class Base64Util {
    public static final String DEFAULT_CHARSET = "UTF-8";

    /**
     * Decodes a Base64 encoded String into a string.
     *
     * @param src the string to decode
     * @return The decoded string. Returns null for null input.
     */
    public static String decode(String src) {
        return decode(src, DEFAULT_CHARSET);
    }

    /**
     * Decodes a Base64 encoded String into a string.
     *
     * @param src
     *          the string to decode
     * @param charsetName
     *          charset name
     * @return The decoded string. Returns null for null input.
     */
    public static String decode(String src, String charsetName) {
        if (src == null) {
            return null;
        }
        byte[] decoded = Base64.getDecoder().decode(src);
        String str = null;
        try {
            str = new String(decoded, charsetName);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return str;
    }

    /**
     * Decodes a Base64 encoded String into a newly-allocated byte array.
     *
     * @param src
     *          the string to decode
     * @return A newly-allocated byte array containing the decoded bytes. Returns
     *         null for null input.
     */
    public static byte[] decodeB(String src) {
        if (src == null) {
            return null;
        }
        byte[] decoded = Base64.getDecoder().decode(src);
        return decoded;
    }

    /**
     * Encodes the specified byte array into a String using the Base64 encoding
     * scheme.
     *
     * @param src
     *          the string to encode
     * @return A String containing the resulting Base64 encoded characters. Returns
     *         null for null input.
     */
    public static String encode(String src) {
        return encode(src, DEFAULT_CHARSET);
    }

    /**
     * Encodes the specified byte array into a String using the Base64 encoding
     * scheme.
     *
     * @param src
     *          the string to encode
     * @param charsetName
     *          charset name
     * @return A String containing the resulting Base64 encoded characters. Returns
     *         null for null input.
     */
    public static String encode(String src, String charsetName) {
        if (src == null) {
            return null;
        }
        String encoded = null;
        try {
            byte[] srcBytes = src.getBytes(charsetName);
            encoded = Base64.getEncoder().encodeToString(srcBytes);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        return encoded;
    }

    /**
     * Encodes the specified byte array into a String using the Base64 encoding
     * scheme.
     *
     * @param src
     *          the byte array to encode
     * @return A String containing the resulting Base64 encoded characters. Returns
     *         null for null input.
     */
    public static String encode(byte[] src) {
        if (src == null) {
            return null;
        }
        String encoded = Base64.getEncoder().encodeToString(src);
        return encoded;
    }

}
