package com.dmt;

import com.dmt.config.CertConfigInfo;
import com.dmt.util.ConvertUtil;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.junit.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.math.BigInteger;
import java.security.Security;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.util.*;

public class Sample {


    @Test
    public void test() throws Exception {

        final String ip = "10.10.27.106";
        final int port = 9190;

        // 创建一个实例对象
        CryptoAdaptor adaptor = CryptoAdaptor.getInstance(ip, port);

        ArrayList<byte[]> keyGroup = new ArrayList<>(6);
        adaptor.genMasterKeyGroup(keyGroup, 6);

        for (int i = 0; i < keyGroup.size(); i++) {
            String tmp = ConvertUtil.bytes2HexString((byte[]) keyGroup.get(i));
            System.out.println();
        }

        // 构建证书主题
        X500Name issuer = new X500Name("CN=Issuer");
        X500Name subject = new X500Name("CN=Subject");
        // 生成证书序列号
        BigInteger serialNumber = BigInteger.valueOf(System.currentTimeMillis());
        // 设置证书有效期
        Date startDate = new Date();
        Date endDate = new Date(System.currentTimeMillis() + 365 * 24 * 60 * 60 * 1000L); // 有效期为一年

        CertConfigInfo config = new CertConfigInfo.Builder()
                .setIssuer(issuer)
                .setSubject(subject)
                .setSerialNumber(serialNumber)
                .setStartDate(startDate)
                .setEndDate(endDate)
                .build();

        // 获取主站密钥
        byte[] certificate = adaptor.genMasterCert(config);
        String tmp = ConvertUtil.bytes2HexString(certificate);


        Map termial = new HashMap();
        byte[] TESAMID = {
                (byte) 0xA0, 0x00, 0x00, 0x00,
                0x00, 0x00, 0x00, 0x01
        };

        adaptor.genTerminalKeyAndCert(termial, keyGroup, TESAMID, config);

        String ks1 = ConvertUtil.bytes2HexString((byte[]) termial.get("ks1_s"));
        String ks2 = ConvertUtil.bytes2HexString((byte[]) termial.get("ks2_s"));
        String agr = ConvertUtil.bytes2HexString((byte[]) termial.get("agr_s"));
        String mac = ConvertUtil.bytes2HexString((byte[]) termial.get("mac_s"));
        String cp = ConvertUtil.bytes2HexString((byte[]) termial.get("cp_s"));
        String m = ConvertUtil.bytes2HexString((byte[]) termial.get("m_s"));

        String cert = ConvertUtil.bytes2HexString((byte[]) termial.get("cert"));
        String pri = ConvertUtil.bytes2HexString((byte[]) termial.get("pri"));


        System.out.println("test end");
    }

    @Test
    public void test2() {

        ArrayList<byte[]> arrayList = new ArrayList<>();

        byte[] a = {0x01, 0x02};
        byte[] b = {0x03, 0x04};

        arrayList.add(a);
        arrayList.add(a);

        System.arraycopy(b, 0, a, 0, 2);
        arrayList.add(a);

        System.out.println();

    }


    public  X509Certificate fileToCertificate(String filepath) throws Exception{
        Security.addProvider(new BouncyCastleProvider()); //注册BouncyCastleProvider
        InputStream inputStream = new FileInputStream(new File(filepath));
        // BC 的意思，是指定用BouncyCastleProvider 。
        CertificateFactory certFactory = CertificateFactory.getInstance("X.509","BC");
//        CertificateFactory certFactory = CertificateFactory.getInstance("X.509");
        X509Certificate ca = (X509Certificate)certFactory.generateCertificate(inputStream);
        byte[] tmp = ca.getEncoded();
        String str = ConvertUtil.bytes2HexString(tmp);
        return ca;
    }
    @Test
    public void test3() throws Exception {
        String path = "F:\\KX\\GM\\terminal.cer";
        fileToCertificate(path);
    }
}
