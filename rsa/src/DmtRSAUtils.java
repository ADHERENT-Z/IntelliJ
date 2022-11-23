import org.bouncycastle.asn1.*;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;
import org.bouncycastle.jcajce.provider.asymmetric.elgamal.CipherSpi;

import java.io.*;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPrivateCrtKey;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPrivateCrtKeySpec;
import java.security.spec.RSAPublicKeySpec;
import java.util.Iterator;

public class DmtRSAUtils {

    private static boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
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

    private static int hex2Dec(final char hexChar) {
        if (hexChar >= '0' && hexChar <= '9') {
            return hexChar - '0';
        } else if (hexChar >= 'A' && hexChar <= 'F') {
            return hexChar - 'A' + 10;
        } else {
            throw new IllegalArgumentException();
        }
    }

    public static final String pub_RSA = "3082010A0282010100C6BEADFDA2E899131D7F1F98260FEC3B08E7A8AAB37FCD2602B0" +
            "71D411CC5A4EE0A759F7CC0F1622B87F73C4DD344ECB19375CE1FFBBCCC442C7F725FE347941E1FA4E404B82952C2F9EE064DC" +
            "D9460152709462612E0A3DECE43FC20694CE2EF35748E7C709EBAE7CFFDE87FEB28F7224EBC1F1F3FA350FCB46B58FC06C67EE" +
            "64FC2E392777A0308AAC2413B4D4AF1046C14D216A2E636DDFFD7A648E8BA9DA173978BD557C0DB121C8BA55DF7E8013F40012" +
            "31FFE931E0FDB6487277C65B3695EE0DF5FF5DFA0E783484F6B805E86D8410C6FF1EC3EE57A46B2DDC0FC1262F6503443C80D3" +
            "0112D205BCF3548AB68B5CBC086C7A02D87A412E219970FA8F590203010001";

    public static boolean PKCS1V1_5PADDING_MODE_ENC = true;
    public static boolean PKCS1V1_5PADDING_MODE_SIG = false;

    public static int RSA_N_LEN_1024 = 128;
    public static int RSA_N_LEN_2048 = 256;

    public static byte[] OID_SHA1 = {
            (byte)0x30,(byte)0x21,(byte)0x30,(byte)0x09,(byte)0x06,
            (byte)0x05,(byte)0x2b,(byte)0x0e,(byte)0x03,(byte)0x02,
            (byte)0x1a,(byte)0x05,(byte)0x00,(byte)0x04,(byte)0x14};
    public static byte[] OID_SHA256 = {
            (byte)0x30,(byte)0x31,(byte)0x30,(byte)0x0d,(byte)0x06,
            (byte)0x09,(byte)0x60,(byte)0x86,(byte)0x48,(byte)0x01,
            (byte)0x65,(byte)0x03,(byte)0x04,(byte)0x02,(byte)0x01,
            (byte)0x05,(byte)0x00,(byte)0x04,(byte)0x20};

    private static SecureRandom random = CryptoServicesRegistrar.getSecureRandom();

    private static BigInteger findFactor(BigInteger e, BigInteger d, BigInteger n) {
        BigInteger edMinus1 = e.multiply(d).subtract(BigInteger.ONE);
        int s = edMinus1.getLowestSetBit();
        BigInteger t = edMinus1.shiftRight(s);

        for (int aInt = 2; true; aInt++) {
            BigInteger aPow = BigInteger.valueOf(aInt).modPow(t, n);
            for (int i = 1; i <= s; i++) {
                if (aPow.equals(BigInteger.ONE)) {
                    break;
                }
                if (aPow.equals(n.subtract(BigInteger.ONE))) {
                    break;
                }
                BigInteger aPowSquared = aPow.multiply(aPow).mod(n);
                if (aPowSquared.equals(BigInteger.ONE)) {
                    return aPow.subtract(BigInteger.ONE).gcd(n);
                }
                aPow = aPowSquared;
            }
        }

    }

    public static RSAPrivateCrtKey createCrtKey(RSAPublicKey rsaPub, RSAPrivateKey rsaPriv) throws NoSuchAlgorithmException, InvalidKeySpecException {

        BigInteger e = rsaPub.getPublicExponent();
        BigInteger d = rsaPriv.getPrivateExponent();
        BigInteger n = rsaPub.getModulus();
        BigInteger p = findFactor(e, d, n);
        BigInteger q = n.divide(p);
//        if (p.compareTo(q) > 0) {
//            BigInteger t = p;
//            p = q;
//            q = t;
//        }
        BigInteger exp1 = d.mod(p.subtract(BigInteger.ONE));
        BigInteger exp2 = d.mod(q.subtract(BigInteger.ONE));
        BigInteger coeff = q.modInverse(p);
        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(n, e, d, p, q, exp1, exp2, coeff);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateCrtKey) kf.generatePrivate(keySpec);
    }

    public static RSAPrivateCrtKey createCrtKey(BigInteger e, BigInteger d, BigInteger n) throws NoSuchAlgorithmException, InvalidKeySpecException {

        BigInteger p = findFactor(e, d, n);
        BigInteger q = n.divide(p);
//        if (p.compareTo(q) > 0) {
//            BigInteger t = p;
//            p = q;
//            q = t;
//        }
        BigInteger exp1 = d.mod(p.subtract(BigInteger.ONE));
        BigInteger exp2 = d.mod(q.subtract(BigInteger.ONE));
        BigInteger coeff = q.modInverse(p);
        RSAPrivateCrtKeySpec keySpec = new RSAPrivateCrtKeySpec(n, e, d, p, q, exp1, exp2, coeff);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPrivateCrtKey) kf.generatePrivate(keySpec);
    }

    private static byte charToByte(char c) {
        return (byte) "0123456789ABCDEF".indexOf(c);
    }

    public static byte[] hexStringToByteArray(String hexString) {
        if (hexString == null || hexString.equals("")) {
            return new byte[0];
        }
        if (hexString.length() % 2 != 0) {
            return new byte[0];
        }
        hexString = hexString.toUpperCase();
        int length = hexString.length() / 2;
        char[] hexChars = hexString.toCharArray();
        byte[] d = new byte[length];
        for (int i = 0; i < length; i++) {
            int pos = i * 2;
            d[i] = (byte) (charToByte(hexChars[pos]) << 4 | charToByte(hexChars[pos + 1]));
        }
        return d;
    }

    public static String byteArrayToHexString(byte[] src) {
        return byteArrayToHexString(src, 0, src.length);
    }

    public static String byteArrayToHexString(byte[] src, int offset, int len) {
        if (src == null || src.length <= 0) {
            return null;
        }
        StringBuilder stringBuilder = new StringBuilder("");
        int min = Math.min(offset + len, src.length);
        for (int i = offset; i < min; i++) {
            int v = src[i] & 0xFF;
            String hv = Integer.toHexString(v).toUpperCase();
            if (hv.length() < 2) {
                stringBuilder.append(0);
            }
            stringBuilder.append(hv);
        }
        return stringBuilder.toString();
    }

    public static byte[] dmtRsaPrivateCrtKey(byte[] e, byte[] d, byte[] n) throws Exception {
        RSAPrivateCrtKey rsaPrivateCrtKey = createCrtKey(new BigInteger(e), new BigInteger(d),  new BigInteger(n));
        ASN1EncodableVector v = new ASN1EncodableVector();
//        ASN1Integer version = new ASN1Integer(new BigInteger("00", 16));
        ASN1Integer modules = new ASN1Integer(rsaPrivateCrtKey.getModulus());
        ASN1Integer publicExponent = new ASN1Integer(rsaPrivateCrtKey.getPublicExponent());
//        ASN1Integer privateExponent = new ASN1Integer(rsaPrivateCrtKey.getPrivateExponent());
//        ASN1Integer prime1 = new ASN1Integer(rsaPrivateCrtKey.getPrimeP());
//        ASN1Integer prime2 = new ASN1Integer(rsaPrivateCrtKey.getPrimeQ());
//        ASN1Integer exponent1 = new ASN1Integer(rsaPrivateCrtKey.getPrimeExponentP());
//        ASN1Integer exponent2 = new ASN1Integer(rsaPrivateCrtKey.getPrimeExponentQ());
//        ASN1Integer coefficient = new ASN1Integer(rsaPrivateCrtKey.getCrtCoefficient());
//        v.add(version);
        v.add(modules);
        v.add(publicExponent);
//        v.add(privateExponent);
//        v.add(prime1);
//        v.add(prime2);
//        v.add(exponent1);
//        v.add(exponent2);
//        v.add(coefficient);
        DERSequence out =  new DERSequence(v);
        return out.getEncoded();
    }

    /********************************************************/
    public static byte[] dmtPublicKey( byte[] n,byte[] e) throws Exception {
        RSAPublicKey RSAPublicKey = createPubKey(new BigInteger(n),new BigInteger(e));
        ASN1EncodableVector v = new ASN1EncodableVector();
        //RSAPrivateCrtKey rsaPrivateCrtKey = createCrtKey(new BigInteger(e), new BigInteger(d),  new BigInteger(n));
        // ASN1EncodableVector v = new ASN1EncodableVector();

        ASN1Integer modules = new ASN1Integer(RSAPublicKey.getModulus());
        ASN1Integer publicExponent = new ASN1Integer(RSAPublicKey.getPublicExponent());

        v.add(modules);
        v.add(publicExponent);

        DERSequence out =  new DERSequence(v);
        return out.getEncoded();
    }

    public static RSAPublicKey createPubKey(BigInteger n,BigInteger e) throws NoSuchAlgorithmException, InvalidKeySpecException{

        RSAPublicKeySpec keySpec = new RSAPublicKeySpec(n,e);
        KeyFactory kf = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) kf.generatePublic(keySpec);
        //generatePublic(keySpec);

    }
    /********************************************************/

    public static ASN1Sequence byteArrayToASN1Sequence(byte[] data) throws IOException {
        ByteArrayInputStream bIn = new ByteArrayInputStream(data);
        ASN1InputStream aIn = new ASN1InputStream(bIn);
        return (ASN1Sequence )aIn.readObject();
    }

    /**
     * PKCS5V1_5填充
     * @param input         isenc为true时为加密数据，isenc为false时为签名hash值
     * @param inOff
     * @param rsaModeLen    支持1024和2048
     * @param inLen         input长度
     * @param hashmode      支持SHA1和SHA256
     * @param isenc         加密或签名填充
     * @return
     */
    public static byte[] dmtPKCS1v1_5Padding(byte[] input, int inOff, int rsaModeLen, int inLen, String hashmode, boolean isenc){
        byte[] block;
        if (inLen > 117)
        {
            throw new IllegalArgumentException("input data too large");
        }

        if(rsaModeLen == RSA_N_LEN_1024)
        {
            block = new byte[128];
        }
        else {
            block = new byte[256];
        }
        block[0] = 0x00;

        if (isenc)
            //加解密0x00||0x02||PS||0x00||M :其中PS为填充0xFF，M为有效数据
            //M为用户加密数据
        {
            block[1] = 0x02;                        // type code 1

            for (int i = 2; i < block.length - inLen - 1; i++)
            {
                while (block[i] == 0)
                {
                    block[i] = (byte)random.nextInt();
                }
            }
            block[block.length - inLen - 1] = 0x00;       // mark the end of the padding
        }
        else
            //签名验签 0x00||0x01||PS||0x00||M :其中PS为填充的随机数如可取0xFF，M为有效数据 </b>
            //M为hash的OID ASN.1编码 + 原始数据HASH计算的值
        {
            int tmp = inLen;

            if(hashmode.equals("OID_SHA1"))
            {
                inLen += OID_SHA1.length;
            }
            else
            {
                inLen += OID_SHA256.length;
            }

            block[1] = 0x01;                        // type code 2
            //
            // a zero byte marks the end of the padding, so all
            // the pad bytes must be non-zero.
            //
            for (int i = 2; i < block.length - inLen - 1; i++)
            {
                block[i] = (byte)0xFF;
            }
            block[block.length - inLen - 1] = 0x00;       // mark the end of the padding

            if(hashmode.equals(OID_SHA1))
            {
                System.arraycopy(OID_SHA1, 0, block, block.length - inLen, OID_SHA1.length);
            }
            else
            {
                System.arraycopy(OID_SHA256, 0, block, block.length - inLen, OID_SHA256.length);
            }

            inLen = tmp;
        }

        System.arraycopy(input, inOff, block, block.length - inLen, inLen);
        return block;
    }


    private static byte[] getSHA1(byte[] in, int inOff, int inLen) {
        SHA1Digest digester = new SHA1Digest();
        byte[] retValue = new byte[digester.getDigestSize()];
        System.out.println(retValue.length);
        digester.update(in, inOff, inLen);
        digester.doFinal(retValue, 0);
        return retValue;
    }

    private static byte[] getSHA256(byte[] in, int inOff, int inLen) {
        SHA256Digest digester = new SHA256Digest();
        byte[] retValue = new byte[digester.getDigestSize()];
        System.out.println(retValue.length);
        digester.update(in, inOff, inLen);
        digester.doFinal(retValue, 0);
        return retValue;
    }

    static void test1() throws Exception {
        byte[] outDer = dmtRsaPrivateCrtKey(hexStringToByteArray("010001"),
//                hexStringToByteArray("00754761ffa3a34906b8dc577b89095ca6165f11ce4203a2fc4f7cc29bc5fa24186744a3ee3bd26efb4ff2f4121e7be94625ff91f92dfceb8127d4d7912aecc70393564dca8ff43fa94e4cb4df691012a1419a36792fdd80f9b8b4fc180b5fb338fb0362c694839fa8bec1d04f052ff4d35859e5ffc0ff621b41cc32cbd883d5b1"),
//                hexStringToByteArray("00ed2e75f3a04722819a4e86c4e5e3f40d1b1af8a1d147a8a8e292ebb3a9468fb37e5b7101574c392d31ad055d510532361f9cd782fe63a4ca3282d8d94b85795dd98dc7933c307eaffd2cb861894feefa0f133244158b653609364572618bd934c835e10040d6215f9278b1bb82e6b318d520e4d2b1a77a5adebe27d07795b967"));
                hexStringToByteArray("00A6BF2D1A4DE95F14FC30791FE855C00300018075984F2A3C13173F9EE380A6D212BCED2626BA7BEC8B3A89C532B38E1A9D3B959FCD6744CDE2D53860B4972CA953FDE77BD8A90B2929A6937E8788E69E76B6973E2344766B6438EDAAA1D34426B71C2FA7187A267E09B9BBCFB098B2BB6BC7440AA9C6F2B0D6851E1206B8F62772F6F3410934E3F600B889FE0FE30429F5B7CB32ED9A411979AE9857F924BC40B12210BCA214E6EB723815A34E2E3B79D07D4B566EDD0D18DC83278FECA10962974177061B571D225B321BF0CDB1C7C6159DE192D62C48D9B36E74BF5F2CB8786D59627BC27822789EA0B985A7D8395B3EA3C02E795F4C56205C993391FA2D39"),
                hexStringToByteArray("00CF9C3B2EE4EA8D43C16A82182D139C4F6A929A2E96233D2E7E937528BC6AA1E1219622CEC7F37C63E2536DA7BD610AF44442659F85CA0014A5D32A1F19D7F19F51CD82F7D2B848880E568F8D4880F012787BB3282BB92E2BE6303D97DC97FB7BC8F57C7806E6E0C11F541D80362BBC6F6D9EE907907E64CB49FFFCB3C3248EC24BE977FCA69AEA6E7BA7A024524580B973BDF8DF413CE9273E53AC90F4EC67AEC765DCE6D52527E03A1FA6F159A701BFCFE91FD9C4956E9CFE86270977B71E48AE7AFDF2BD2462289C344ADB6E482C0B5A54C092A393DFD1F538B4212167F91DBDBF034EF69538024D2C3BFEF40493D786E1F00A87DCBDF912C88DC3EA960105"));

        System.out.println(byteArrayToHexString(outDer));
        OutputStream os = new FileOutputStream("F:\\KX\\Encryption\\RSA\\rsakey.der");
        os.write(outDer);
        os.close();
    }

    static void test2() throws Exception {
//        int len;
//        byte arr[] = new byte[1024 * 8];
//        InputStream is = new FileInputStream("F:\\KX\\Encryption\\RSA\\rsakey.der");
//        len = is.read(arr);
        ASN1Sequence  as = byteArrayToASN1Sequence(hexString2Bytes(pub_RSA));

        for (Iterator<ASN1Encodable> it = as.iterator(); it.hasNext(); ) {
            ASN1Encodable a = it.next();
//            System.out.println( ((ASN1Integer)a).getValue().toString(16));
//            System.out.println("********************");
            System.out.println(byteArrayToHexString(a.toASN1Primitive().getEncoded()));
            System.out.println("********************");
        }
    }

    static void test3()throws Exception {

        //加密
        byte[] inputmsg = hexStringToByteArray("112233445566");
        byte[] outencpad = dmtPKCS1v1_5Padding(inputmsg, 0, RSA_N_LEN_1024, inputmsg.length, "OID_SHA1", PKCS1V1_5PADDING_MODE_ENC);
        System.out.println(byteArrayToHexString(outencpad));

        //签名
        byte[] inputhash = hexStringToByteArray("1122334455667788112233445566778811223344556677881122334455667788");
        byte[] outsigpad = dmtPKCS1v1_5Padding(inputhash, 0, RSA_N_LEN_1024, inputhash.length, "OID_SHA256", PKCS1V1_5PADDING_MODE_SIG);
        System.out.println(byteArrayToHexString(outsigpad));
    }

    public static void test4() throws Exception {
        byte[] outDer1 = dmtPublicKey(
//                hexStringToByteArray("00ed2e75f3a04722819a4e86c4e5e3f40d1b1af8a1d147a8a8e292ebb3a9468fb37e5b7101574c392d31ad055d510532361f9cd782fe63a4ca3282d8d94b85795dd98dc7933c307eaffd2cb861894feefa0f133244158b653609364572618bd934c835e10040d6215f9278b1bb82e6b318d520e4d2b1a77a5adebe27d07795b967"),
                hexStringToByteArray("00C1CA34D9881A1C28A6AFD6FA76A3E1D72DEDE508869857561CE101C15117DDCC6B24C7FCE3392FB99665529D1EF53BB5E5BB3BF5D1EF8288F0616461F43074D752380C6D96223A35C2CFED1C4122DFC6277970EEE647E4034D2179B481608B67CE059CC05929EC82F1E035576C2919E5B24EE7685C98BC473D8530C320D6C1B97AA84797B191F91D201BB8B08AF8CA194AF0B22A1B734B4244C1848C5C80EC176FD1A35BC97D62F5995BD93D74C8898282C665A0DFF22A16D002220BE4ECF207C58C3EEDAA3791EFC6C636D05BC79E076443C1750F1E54604812456D78E1AA639925AED5FEC884130ABA6A92B5EC4DA79641AF3CFC570CF17562458BE0DB6335"),
                hexStringToByteArray("010001"));

        System.out.println(byteArrayToHexString(outDer1));
    }

    //dmtRsaPrivateCrtKey
    //dmtPKCS1v1_5Padding填充，解包填充需要自己实现
    //getSHA1
    //getSHA256

    public static void main(String[] args) throws Exception {
//        byte[] out = getSHA256(hexStringToByteArray("1122334455667788112233445566778811223344556677881122334455667788"),0,32);
//        byte[] out2 = getSHA1(hexStringToByteArray("1122334455667788112233445566778811223344556677881122334455667788"),0,32);
//        System.out.println(byteArrayToHexString(out));
//        System.out.println(byteArrayToHexString(out2));
        test4();
    }
}
