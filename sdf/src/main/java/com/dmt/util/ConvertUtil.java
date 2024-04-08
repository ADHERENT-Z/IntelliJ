package com.dmt.util;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class ConvertUtil {

    private static final char[] HEX_DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] HEX_DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 判断奇数还是偶数
     *
     * @param num
     * @return
     */
    public static int isOdd(int num) {
        return num & 0x01;
    }

    /**
     * Int to 16bits or 32bits byteArray
     *
     * @param num
     * @param is16 true:16 bits byteArray false:32 bits byteArray
     * @return 数组高位在前，低位在后
     */
    public static byte[] int2byteArray(final int num, boolean is16){
        if (is16){
            byte[] result = new byte[2];
            result[0] = (byte)((num >> 8) & 0xFF);
            result[1] = (byte)((num >> 0) & 0xFF);
            return result;
        }else{
            byte[] result = new byte[4];
            result[0] = (byte)((num >> 24) & 0xFF);
            result[1] = (byte)((num >> 16) & 0xFF);
            result[2] = (byte)((num >> 8) & 0xFF);
            result[3] = (byte)((num >> 0) & 0xFF);
            return result;
        }
    }

    /**
     * long to byteArray
     *
     * @param num
     * @return 数组高位在前，低位在后
     */
    public static byte[] long2byteArray(final long num){
        byte[] result = new byte[8];
        result[0] = (byte)((num >> 56) & 0xFF);
        result[1] = (byte)((num >> 48) & 0xFF);
        result[2] = (byte)((num >> 40) & 0xFF);
        result[3] = (byte)((num >> 32) & 0xFF);
        result[4] = (byte)((num >> 24) & 0xFF);
        result[5] = (byte)((num >> 16) & 0xFF);
        result[6] = (byte)((num >> 8) & 0xFF);
        result[7] = (byte)((num >> 0) & 0xFF);
        return result;
    }

    /**
     * byte数组中取int数值，用于(高位在前,低位在后)的顺序
     * @param bytes
     * @param offset
     * @return
     */
    public static int byteArray2int(final byte[] bytes, int offset, boolean is16){
        int value = 0;
        if (is16){
            value = (int) ((bytes[offset] & 0xFF) << 8)
                    | (bytes[offset + 1] & 0xFF);
            return value;
        }else {
            value = (int) (((bytes[offset] & 0xFF) << 24)
                    | ((bytes[offset + 1] & 0xFF) << 16)
                    | ((bytes[offset + 2] & 0xFF) << 8)
                    | (bytes[offset + 3] & 0xFF));
            return value;
        }
    }

    /**
     * byte数组中取 long数值
     * 用于(高位在前,低位在后)的顺序,如 0x0100对应的 byteArray{01, 00}
     *
     * @param bytes
     * @param offset
     * @return
     */
    public static long byteArray2long(final byte[] bytes, int offset){
        long value = 0;
        byte[] tmp = new byte[64];
        if (bytes.length < 8) {
            Arrays.fill(tmp, (byte) 0x00);
            System.arraycopy(bytes, 0, tmp, (8 - bytes.length), bytes.length);
        }

        value = (long) (((tmp[offset] & 0xFF) << 56)
                | ((tmp[offset + 1] & 0xFF) << 48)
                | ((tmp[offset + 2] & 0xFF) << 40)
                | ((tmp[offset + 3] & 0xFF) << 32)
                | ((tmp[offset + 4] & 0xFF) << 24)
                | ((tmp[offset + 5] & 0xFF) << 16)
                | ((tmp[offset + 6] & 0xFF) << 8)
                | (tmp[offset + 7] & 0xFF));
        return value;
    }

    /**
     * 低位在前，高位在后
     * @param num
     * @param is16
     * @return
     */
    public static byte[] intTobyteArray(final int num, boolean is16)
    {
        if (is16){
            byte[] src = new byte[2];
            src[1] = (byte) ((num >> 8) & 0xFF);
            src[0] = (byte) (num & 0xFF);
            return src;
        }else {
            byte[] src = new byte[4];
            src[3] = (byte) ((num >> 24) & 0xFF);
            src[2] = (byte) ((num >> 16) & 0xFF);
            src[1] = (byte) ((num >> 8) & 0xFF);
            src[0] = (byte) (num & 0xFF);
            return src;
        }
    }

    /**
     * byte数组中取int数值，用于(低位在前，高位在后)的顺序
     * @param bytes
     * @param offset
     * @return
     */
    public static int byteArrayToint(byte[] bytes, int offset, boolean is16) {
        int value = 0;
        if (is16){
            value = (int) ((bytes[offset] & 0xFF)
                    | ((bytes[offset + 1] & 0xFF) << 8));
            return value;
        }else {
            value = (int) ((bytes[offset] & 0xFF)
                    | ((bytes[offset + 1] & 0xFF) << 8)
                    | ((bytes[offset + 2] & 0xFF) << 16)
                    | ((bytes[offset + 3] & 0xFF) << 24));
            return value;
        }
    }

    /**
     * Int to hex string.
     *
     * @param num The int number.
     * @return the hex string
     */
    public static String int2HexString(int num) {
        return Integer.toHexString(num);
    }


    /**
     * Hex string to int.
     *
     * @param hexString The hex string.
     * @return the int
     */
    public static int hexString2Int(String hexString) {
        return Integer.parseInt(hexString, 16);
    }

    /**
     * Bytes to bits.
     *
     * @param bytes The bytes.
     * @return bits
     */
    public static String bytes2Bits(final byte[] bytes) {
        if (bytes == null || bytes.length == 0) return "";
        StringBuilder sb = new StringBuilder();
        for (byte aByte : bytes) {
            for (int j = 7; j >= 0; --j) {
                sb.append(((aByte >> j) & 0x01) == 0 ? '0' : '1');
            }
        }
        return sb.toString();
    }

    /**
     * Bits to bytes.
     *
     * @param bits The bits.
     * @return bytes
     */
    public static byte[] bits2Bytes(String bits) {
        int lenMod = bits.length() % 8;
        int byteLen = bits.length() / 8;
        // add "0" until length to 8 times
        if (lenMod != 0) {
            for (int i = lenMod; i < 8; i++) {
                bits = "0" + bits;
            }
            byteLen++;
        }
        byte[] bytes = new byte[byteLen];
        for (int i = 0; i < byteLen; ++i) {
            for (int j = 0; j < 8; ++j) {
                bytes[i] <<= 1;
                bytes[i] |= bits.charAt(i * 8 + j) - '0';
            }
        }
        return bytes;
    }

    /**
     * Bytes to chars.
     *
     * @param bytes The bytes.
     * @return chars
     */
    public static char[] bytes2Chars(final byte[] bytes) {
        if (bytes == null) return null;
        int len = bytes.length;
        if (len <= 0) return null;
        char[] chars = new char[len];
        for (int i = 0; i < len; i++) {
            chars[i] = (char) (bytes[i] & 0xff);
        }
        return chars;
    }

    /**
     * Chars to bytes.
     *
     * @param chars The chars.
     * @return bytes
     */
    public static byte[] chars2Bytes(final char[] chars) {
        if (chars == null || chars.length <= 0) return null;
        int len = chars.length;
        byte[] bytes = new byte[len];
        for (int i = 0; i < len; i++) {
            bytes[i] = (byte) (chars[i]);
        }
        return bytes;
    }

    /**
     * Bytes to hex string.
     * <p>e.g. bytes2HexString(new byte[] { 0, (byte) 0xa8 }) returns "00A8"</p>
     *
     * @param bytes The bytes.
     * @return hex string
     */
    public static String bytes2HexString(final byte[] bytes) {
        return bytes2HexString(bytes, true);
    }

    /**
     * Bytes to hex string.
     * <p>e.g. bytes2HexString(new byte[] { 0, (byte) 0xa8 }, true) returns "00A8"</p>
     *
     * @param bytes       The bytes.
     * @param isUpperCase True to use upper case, false otherwise.
     * @return hex string
     */
    public static String bytes2HexString(final byte[] bytes, boolean isUpperCase) {
        if (bytes == null) return "";
        char[] hexDigits = isUpperCase ? HEX_DIGITS_UPPER : HEX_DIGITS_LOWER;
        int len = bytes.length;
        if (len <= 0) return "";
        char[] ret = new char[len << 1];
        for (int i = 0, j = 0; i < len; i++) {
            ret[j++] = hexDigits[bytes[i] >> 4 & 0x0f];
            ret[j++] = hexDigits[bytes[i] & 0x0f];
        }
        return new String(ret);
    }

    /**
     * Hex string to bytes.
     * <p>e.g. hexString2Bytes("00A8") returns { 0, (byte) 0xA8 }</p>
     *
     * @param hexString The hex string.
     * @return the bytes
     */
    public static byte[] hexString2Bytes(String hexString) {
        if (isSpace(hexString)) return new byte[0];
        int len = hexString.length();
        if (len % 2 != 0) {
            hexString = "0" + hexString;
            len = len + 1;
        }
        char[] hexBytes = hexString.toUpperCase().toCharArray();
        byte[] ret = new byte[len >> 1];
        for (int i = 0; i < len; i += 2) {
            ret[i >> 1] = (byte) (hex2Dec(hexBytes[i]) << 4 | hex2Dec(hexBytes[i + 1]));
        }
        return ret;
    }

    public static int hex2Dec(final char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    /**
     * Bytes to string.
     */
    public static String bytes2String(final byte[] bytes) {
        return bytes2String(bytes, "");
    }

    /**
     * String to bytes.
     */
    public static byte[] string2Bytes(final String string) {
        return string2Bytes(string, "");
    }

    /**
     * Bytes to string.
     */
    public static String bytes2String(final byte[] bytes, final String charsetName) {
        if (bytes == null) return null;
        try {
            return new String(bytes, getSafeCharset(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new String(bytes);
        }
    }

    /**
     * String to bytes.
     */
    public static byte[] string2Bytes(final String string, final String charsetName) {
        if (string == null) return null;
        try {
            return string.getBytes(getSafeCharset(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string.getBytes();
        }
    }

    private static String getSafeCharset(String charsetName) {
        String cn = charsetName;
        if (isSpace(charsetName) || !Charset.isSupported(charsetName)) {
            cn = "UTF-8";
        }
        return cn;
    }


    /**
     * Return whether the string is null or white space.
     *
     * @param s The string.
     * @return {@code true}: yes<br> {@code false}: no
     */
    public static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }


    public static void main(String[] args){

//        byte[] outBuf = {(byte) 0x00, (byte) 0x00};
        byte[] outBuf = new byte[2];
//        long a = byteArray2long(outBuf, 0);
        if (outBuf == null) {
            System.out.println("null");
        } else {
            System.out.println("non empty");
        }

//        byte[] result = int2byteArray(0x10000000, false);
//        System.out.println("null");
    }
}
