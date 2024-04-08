package com.dmt;

import com.dmt.config.CertConfigInfo;
import com.dmt.exception.CertificateException;
import com.sdt.jni.SdfJniApi;
import com.sdt.util.AlgrithmID;
import org.bouncycastle.asn1.gm.GMObjectIdentifiers;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.X509v3CertificateBuilder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.crypto.params.ECDomainParameters;
import org.bouncycastle.crypto.params.ECPublicKeyParameters;
import org.bouncycastle.jce.ECNamedCurveTable;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.bouncycastle.jce.spec.ECNamedCurveParameterSpec;
import org.bouncycastle.jce.spec.ECPublicKeySpec;
import org.bouncycastle.operator.ContentSigner;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.X509Certificate;
import java.util.*;

public class CryptoAdaptor implements CryptoAdaptorInterface {


//    private static String IP_ADDRESS;
//    private static int PORT;

    private static byte[] phDeviceHandle = new byte[8];
    private static byte[] phSessionHandle = new byte[8];

    // 构造函数为 private,防止被外部实例化
//    private User(Builder builder){
//        this.IP_ADDRESS = builder.IP_ADDRESS;
//        this.PORT = builder.PORT;
//    }

    private CryptoAdaptor(){}

    private static SdfJniApi sja = null;
    // 装载类时初始化
    static {
        Security.addProvider(new BouncyCastleProvider());
        sja = new SdfJniApi();
    }


    // 创建类的一个实例对象
    private volatile static CryptoAdaptor instance = null;
    // 获取可用的对象
//    private static User getInstance(Builder builder) throws Exception {
//
//        if (instance == null) {
//            synchronized (User.class) {
//                if (instance == null) {
//
//                    instance = new User(builder);
//
//                    if (sja.SDF_OpenDevice_ex(phDeviceHandle, IP_ADDRESS.getBytes(), PORT) != 0) {
//                        throw new CertificateException("open device exception");
//                    }
//
//                    if (sja.SDF_OpenSession(phDeviceHandle, phSessionHandle) != 0) {
//                        throw new CertificateException("open session exception");
//                    }
//
//                    if (sja.SDF_GetPrivateKeyAccessRight(phSessionHandle, 110,
//                            "asdf1234".getBytes(), 8) != 0) {
//                        throw new CertificateException("open device exception");
//                    }
//                }
//            }
//        }
//
//        return instance;
//    }

    public static CryptoAdaptor getInstance(String IP_ADDRESS, int PORT) throws Exception {

        if (instance == null) {
            synchronized (CryptoAdaptor.class) {
                if (instance == null) {

                    instance = new CryptoAdaptor();

//                    if (sja.SDF_OpenDevice_ex(phDeviceHandle, IP_ADDRESS.getBytes(), PORT) != 0) {
//                        throw new CertificateException("open device exception");
//                    }

                    if (sja.SDF_OpenDevice_ex(phDeviceHandle, IP_ADDRESS.getBytes(), PORT) != 0) {
                        throw new CertificateException("open device exception");
                    }

                    if (sja.SDF_OpenSession(phDeviceHandle, phSessionHandle) != 0) {
                        throw new CertificateException("open session exception");
                    }

                    // 每个接口都需要吗
                    if (sja.SDF_GetPrivateKeyAccessRight(phSessionHandle, 110,
                            "asdf1234".getBytes(), 8) != 0) {
                        throw new CertificateException("open device exception");
                    }
                }
            }
        }

        return instance;
    }


    @Override
    public int genMasterKeyGroup(ArrayList<byte[]> keyGroup, int keyGroupSize) {

        int iRet = 0;

        byte[] ecc_enData_1 = new byte[8356];
        byte[] keyHandle = new byte[8];
        int sessionKeyBits = 128;

        for (int i = 0; i < 6; i++) {
            iRet = sja.SDF_GenerateKeyWithIPK_ECC(phSessionHandle, 110,
                    sessionKeyBits, ecc_enData_1, keyHandle);

            if (iRet != 0) {
                break;
            } else {
                byte[] key = new byte[180];
                Arrays.fill(key, (byte) 0x00);
                System.arraycopy(ecc_enData_1, 0, key, 0, 180);
                keyGroup.add(key);
            }
        }

        return iRet;
    }


    @Override
    public byte[] genMasterCert(CertConfigInfo config) throws Exception {

        int iRet = 0;
        int uiKeyIndex = 110;
        String alg_id = "31323334353637383132333435363738";
        String password = "asdf1234";
        byte[] rawPubKey = new byte[132];

//        iRet = sja.SDF_GetPrivateKeyAccessRight(phSessionHandle, uiKeyIndex, password.getBytes(), password.length());
//        if (iRet != 0) {
//            throw new CertificateException("SDF_GetPrivateKeyAccessRight exception");
//        }

        iRet = sja.SDF_ExportSignPublicKey_ECC(phSessionHandle, uiKeyIndex, rawPubKey);
        if (iRet != 0) {
            throw new CertificateException("SDF_ExportSignPublicKey_ECC exception");
        }

        X509Certificate masterCert = genCertificate(phSessionHandle, uiKeyIndex, rawPubKey, password, alg_id, config);

        return masterCert.getEncoded();
    }


    @Override
    public int genTerminalKeyAndCert(Map<String, byte[]> terKeyCertAndPriKey, ArrayList keyGroup, byte[] TESAMID, CertConfigInfo config) throws Exception {

        if (TESAMID.length != 0x08) {
            throw new IllegalArgumentException("TESAMID length must be 16");
        }

        int iRet = 0;
        int uiKeyIndex = 110;
        String alg_id = "31323334353637383132333435363738";
        String password = "asdf1234";
        byte[] keyHandle = new byte[8];

        byte[] ks1 = (byte[]) keyGroup.get(0);
        byte[] ks2 = (byte[]) keyGroup.get(1);
        byte[] agr = (byte[]) keyGroup.get(2);
        byte[] mac = (byte[]) keyGroup.get(3);
        byte[] cp = (byte[]) keyGroup.get(4);
        byte[] m = (byte[]) keyGroup.get(5);


        int[] length = {0};

//        iRet = sja.SDF_GetPrivateKeyAccessRight(phSessionHandle, uiKeyIndex, password.getBytes(), password.length());
//        if (iRet != 0) {
//            throw new CertificateException("SDF_GetPrivateKeyAccessRight exception");
//        }

        byte[] newTESAMID = new byte[16];
        System.arraycopy(TESAMID, 0, newTESAMID, 0, 8);
        for (int i = 0; i < 8; i++) {
            newTESAMID[i + 8] = (byte) ~TESAMID[i];
        }

        byte[] ks1Data = new byte[16];
        Arrays.fill(ks1Data, (byte) 0x00);
        iRet = sja.SDF_ImportKeyWithISK_ECC(phSessionHandle, uiKeyIndex, ks1, keyHandle);
        iRet = sja.SDF_Encrypt(phSessionHandle, keyHandle, AlgrithmID.SGD_SM4_ECB,
                null, newTESAMID, newTESAMID.length, ks1Data, length);
        if (iRet != 0) {
            return iRet;
        }
        terKeyCertAndPriKey.put("ks1_s", ks1Data); // 存入 ks1_s

        byte[] ks2Data = new byte[16];
        Arrays.fill(ks2Data, (byte) 0x00);
        iRet = sja.SDF_ImportKeyWithISK_ECC(phSessionHandle, uiKeyIndex, ks2, keyHandle);
        iRet = sja.SDF_Encrypt(phSessionHandle, keyHandle, AlgrithmID.SGD_SM4_ECB,
                null, newTESAMID, newTESAMID.length, ks2Data, length);
        if (iRet != 0) {
            return iRet;
        }
        terKeyCertAndPriKey.put("ks2_s", ks2Data); // 存入 ks2_s

        byte[] agrData = new byte[16];
        Arrays.fill(agrData, (byte) 0x00);
        iRet = sja.SDF_ImportKeyWithISK_ECC(phSessionHandle, uiKeyIndex, agr, keyHandle);
        iRet = sja.SDF_Encrypt(phSessionHandle, keyHandle, AlgrithmID.SGD_SM4_ECB,
                null, newTESAMID, newTESAMID.length, agrData, length);
        if (iRet != 0) {
            return iRet;
        }
        terKeyCertAndPriKey.put("agr_s", agrData); // 存入 agr_s

        byte[] macData = new byte[16];
        Arrays.fill(macData, (byte) 0x00);
        iRet = sja.SDF_ImportKeyWithISK_ECC(phSessionHandle, uiKeyIndex, mac, keyHandle);
        iRet = sja.SDF_Encrypt(phSessionHandle, keyHandle, AlgrithmID.SGD_SM4_ECB,
                null, newTESAMID, newTESAMID.length, macData, length);
        if (iRet != 0) {
            return iRet;
        }
        terKeyCertAndPriKey.put("mac_s", macData); // 存入 mac_s

        byte[] cpData = new byte[16];
        Arrays.fill(cpData, (byte) 0x00);
        iRet = sja.SDF_ImportKeyWithISK_ECC(phSessionHandle, uiKeyIndex, cp, keyHandle);
        iRet = sja.SDF_Encrypt(phSessionHandle, keyHandle, AlgrithmID.SGD_SM4_ECB,
                null, newTESAMID, newTESAMID.length, cpData, length);
        if (iRet != 0) {
            return iRet;
        }
        terKeyCertAndPriKey.put("cp_s", cpData); // 存入 cp_s

        byte[] mData = new byte[16];
        Arrays.fill(mData, (byte) 0x00);
        iRet = sja.SDF_ImportKeyWithISK_ECC(phSessionHandle, uiKeyIndex, m, keyHandle);
        iRet = sja.SDF_Encrypt(phSessionHandle, keyHandle, AlgrithmID.SGD_SM4_ECB,
                null, newTESAMID, newTESAMID.length, mData, length);
        if (iRet != 0) {
            return iRet;
        }
        terKeyCertAndPriKey.put("m_s", mData); // 存入 m_s

        byte[] pucPublicKey = new byte[132];
        byte[] pucPrivateKey = new byte[68];

        // 拿到公私钥
        iRet = sja.SDF_GenerateKeyPair_ECC(phSessionHandle, AlgrithmID.SGD_SM2,
                256, pucPublicKey, pucPrivateKey);
        if (iRet != 0) {
            return iRet;
        }
        terKeyCertAndPriKey.put("pri", pucPrivateKey); // 存入私钥

        X509Certificate terminalCert = genCertificate(phSessionHandle, uiKeyIndex, pucPublicKey, password, alg_id, config);
        terKeyCertAndPriKey.put("cert", terminalCert.getEncoded()); // 存入终端证书

        return iRet;
    }


    private X509Certificate genCertificate(final byte[] phSessionHandle, final int uiKeyIndex, final byte[] pubKey,
                                  String password, final String alg_id, CertConfigInfo config) throws Exception {
        int iRet = -1;

        byte[] publicKeyBytes = new byte[64];

//        iRet = sja.SDF_GetPrivateKeyAccessRight(phSessionHandle, uiKeyIndex, password.getBytes(), password.length());
//        if (iRet != 0) {
//            throw new CertificateException("SDF_GetPrivateKeyAccessRight exception");
//        }

        System.arraycopy(pubKey, 36, publicKeyBytes, 0, 32);
        System.arraycopy(pubKey, 100, publicKeyBytes, 32, 32);

        PublicKey publicKey = convert(publicKeyBytes);
        // 构建证书生成器
        X509v3CertificateBuilder certificateBuilder = new X509v3CertificateBuilder(
                config.getIssuer(),
                config.getSerialNumber(),
                config.getStartDate(),
                config.getEndDate(),
                config.getSubject(),
                SubjectPublicKeyInfo.getInstance(publicKey.getEncoded())
        );

        // 构建证书签名器
        ContentSigner contentSigner = new ContentSigner() {

            private final OutputStream outputStream = new ByteArrayOutputStream();

            @Override
            public byte[] getSignature() {
                // 从 outputStream中获取需要签名的数据
                byte[] dataToSign = ((ByteArrayOutputStream) outputStream).toByteArray();

                int innerRet = -1;

                innerRet = sja.SDF_HashInit(phSessionHandle, AlgrithmID.SGD_SM3, pubKey, alg_id.getBytes(), 0);
                innerRet = sja.SDF_HashUpdate(phSessionHandle, dataToSign, dataToSign.length);

                byte[] pucHash = new byte[32];
                int [] puiHashLength = {0};
                innerRet = sja.SDF_HashFinal(phSessionHandle, pucHash, puiHashLength);


                byte[] rawSig = new byte[128];
                byte[] signature = new byte[64];

                // 使用密码机中的密钥进行签名
                innerRet = sja.SDF_InternalSign_ECC(phSessionHandle, uiKeyIndex, pucHash, 32, rawSig);
                System.arraycopy(rawSig, 32, signature, 0, 32);
                System.arraycopy(rawSig, 96, signature, 32, 32);

                // 返回签名结果
                return signature;
            }

            @Override
            public OutputStream getOutputStream() {
                return outputStream;
            }

            @Override
            public AlgorithmIdentifier getAlgorithmIdentifier() {
                return new AlgorithmIdentifier(GMObjectIdentifiers.sm2sign_with_sm3);
            }
        };

        // 生成证书
        X509CertificateHolder certificateHolder = certificateBuilder.build(contentSigner);
        X509Certificate certificate = new JcaX509CertificateConverter()
                .setProvider("BC")
                .getCertificate(certificateHolder);

        return certificate;
    }


    private PublicKey convert(byte[] publicKeyBytes) throws Exception {

        // 获取 SM2椭圆曲线参数
        ECNamedCurveParameterSpec ecSpec = ECNamedCurveTable.getParameterSpec("sm2p256v1");
        ECDomainParameters ecParams = new ECDomainParameters(ecSpec.getCurve(), ecSpec.getG(), ecSpec.getN(), ecSpec.getH());

        // 从公钥字节数组中提取 X和 Y坐标的字节数组
        byte[] xBytes = new byte[32];
        byte[] yBytes = new byte[32];
        System.arraycopy(publicKeyBytes, 0, xBytes, 0, 32);
        System.arraycopy(publicKeyBytes, 32, yBytes, 0, 32);

        // 根据 X和 Y坐标的字节数组构造椭圆曲线公钥
        BigInteger x = new BigInteger(1, xBytes);
        BigInteger y = new BigInteger(1, yBytes);
        ECPublicKeyParameters pubKeyParams = new ECPublicKeyParameters(ecParams.getCurve().createPoint(x, y), ecParams);
        ECPublicKeySpec publicKeySpec = new ECPublicKeySpec(pubKeyParams.getQ(), ecSpec);
        KeyFactory keyFactory = KeyFactory.getInstance("EC", new org.bouncycastle.jce.provider.BouncyCastleProvider());

        return keyFactory.generatePublic(publicKeySpec);
    }


//    public static class Builder {
//
//        private String IP_ADDRESS;
//        private int PORT;
//
//        // 无参构造函数
//        public Builder(){}
//
//        // 有参构造方法
//        public Builder(String IP_ADDRESS, int PORT) {
//            this.IP_ADDRESS = IP_ADDRESS;
//            this.PORT = PORT;
//        }
//
//
//        public Builder setIpAddress(String IP_ADDRESS) {
//            this.IP_ADDRESS = IP_ADDRESS;
//            return this;
//        }
//
//
//        public Builder setPort(int PORT) {
//            this.PORT = PORT;
//            return this;
//        }
//
//
//        // 创建并返回外部类对象
//        public User build() throws Exception{
//            return getInstance(this);
//        }
//
//    }


}

