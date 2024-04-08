package com.dmt;

import com.dmt.config.CertConfigInfo;

import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Map;

public interface CryptoAdaptorInterface {


    /**
     * 生成 6组主站对称密钥
     * @param keyGroup      [IN]主站对称密钥
     * @param keyGroupSize  [IN]主站对称密钥对数
     *
     * @return 0 成功 其他 失败
     */
    int genMasterKeyGroup(ArrayList<byte[]> keyGroup, int keyGroupSize);


    /**
     * 生成主站证书
     * @param config            [IN]主站证书配置参数
     *
     * @return                  主站证书
     * @throws Exception        如果异常发生
     */
    byte[] genMasterCert(CertConfigInfo config) throws Exception;


    /**
     * 生成终端密钥、证书及私钥
     *
     * @param terKeyCertAndPriKey   [OUT]终端生成的 6组密钥、证书及私钥
     *                              (Map中 key的取值：ks1_s、ks2_s、agr_s、mac_s、cp_s、m_s、cert、priKey)
     * @param keyGroup              [IN]主站对称密钥(6组，单个密钥长度 180字节)
     * @param TESAMID               [IN]TESAMID
     *
     * @return 0 成功 其他 失败
     *
     * @throws Exception        如果错误发生
     *
     */
    int genTerminalKeyAndCert(Map<String, byte[]> terKeyCertAndPriKey, ArrayList keyGroup, byte[] TESAMID, CertConfigInfo config) throws Exception;

}
