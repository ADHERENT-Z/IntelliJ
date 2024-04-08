package com.dtt.KeyBoardSDK;

public interface KeyBoardInterface {

    /**
     * 初始化通信方式
     *
     * @param bType [IN]0x00 USB
     *                  0x01 UART
     *                  0x02 I2C
     *
     * 备注：
     * 该函数在打开设备之前调用
     */
    public void initComm(byte bType);

    /**
     * 打开 USB设备
     * @param vid [IN]设备 VID值
     * @param pid [IN]设备 PID值
     *
     * @return 0 - 成功 非 0失败
     */
    public int openUsb(int vid, int pid);


    /**
     * 关闭 USB设备
     *
     * @return 0 - 成功 非 0失败
     */
    public int closeUsb();


    /**
     * 查询 USB设备是否存在
     *
     * @return 0 - 设备存在 非 0设备不存在
     */
    public int getUsbStatus();


    /**
     * 打开 UART设备
     * @param port [IN]串口名（如/dev/ttyS4)
     *
     * @return 0 - 成功 非 0失败
     *
     * 备注：多线程创建不安全
     */
    public int openUart(String port);

    /**
     * 关闭 UART设备
     *
     * @return 0 - 成功 非 0失败
     */
    public int closeUart();


    /**
     * 打开 I2C设备
     *
     * @param strI2cFileName [IN] I2C端口名称（如/dev/i2c-2）
     *
     * @return 0 - 成功 非 0失败
     */
    public int openI2c(String strI2cFileName);


    /**
     * 关闭 I2C设备
     *
     * @return 0 - 成功 非 0失败
     */
    public int closeI2c();


    /**
     * 打开 SPI设备
     *
     * @param strSpiFileName [IN] SPI端口名称
     *
     * @return 0 - 成功 非 0失败
     */
    public int openSpi(String strSpiFileName);


    /**
     * 关闭 SPI设备
     *
     * @return 0 - 成功 非 0失败
     */
    public int closeSpi();


    /**
     * 复位设备
     *
     * @return 0 - 成功 非 0失败
     */
    public int reset();


    /**
     * 初始化芯片
     *
     * @return 0 - 成功 非 0失败
     */
    public int initialize() ;


    /**
     * 生成随机数
     *
     * @param inOutDataLen [IN/OUT]作为入参指定生成随机数的长度
     *                             作为出参表示生成的随机数长度
     * @param bData        [OUT]随机数
     *
     * @return 0 - 成功 非 0失败
     */
    public int generateRandomData(int[] inOutDataLen, byte[] bData);


    /**
     * 获取 SDK 版本
     *
     * @param bSDKVersion [OUT]SDK版本(6字节)
     *
     * @return 0 - 成功 非 0失败
     */
    public int getVersion(byte[] bSDKVersion);


    /**
     *  获取芯片固件版本
     *
     * @param bChipFirmwareVersion [OUT]芯片固件版本（6字节）
     *
     * @return 0 - 成功 非 0失败
     */
    public int getChipFirmwareVersion(byte[] bChipFirmwareVersion);


    /**
     * 获取芯片固件编译版本
     *
     * @param bChipFirmwareBuild [OUT]芯片固件编译版本（6字节）
     * @return 0 - 成功 非 0失败
     */
    public int getChipFirmwareBuild(byte[] bChipFirmwareBuild);


    /**
     * 获取键盘加密模式
     *
     * @param bMode [OUT]0x00 – 非国密
     *                   0x01 – 国密
     *
     * @return 0 - 成功 非 0失败
     */
    public int getAlgMode(byte[] bMode);


    /**
     * 设置键盘加密模式
     *
     * @param bMode [IN]0x00 – 非国密
     *                  0x01 – 国密
     *
     * @return 0 - 成功 非 0失败
     */
    public int setAlgMode(byte bMode);


    /**
     * 获取密钥信息
     *
     * @param bKeyNo     [IN]获取信息的密钥号 (0xFF表示获取所有)
     *                   若是获取全部密钥信息，则密钥名称按每 24字节进行解析
     * @param bRetKeyNo  [OUT]密钥槽号
     * @param bParentNo  [OUT]父密钥号
     * @param lKeyUsage  [OUT]密钥属性
     * @param bKeyName   [OUT]密钥名称
     * @param iKeyLength [OUT]密钥的长度
     *
     * @return 0 - 成功 非 0失败
     */
    public int getKeyDetailEx(byte bKeyNo, byte[] bRetKeyNo, byte[] bParentNo, long[] lKeyUsage,
                              byte[] bKeyName, int[] iKeyLength);

    /**
     * 以密钥名称方式下载密钥
     *
     * @param strKeyName    [IN]密钥名称
     * @param strParentName [IN]解密密钥名称 (为空 -明文下载 不为空 - 密文下载)
     * @param lKeyUsage     [IN]密钥属性 (属性 0x0400只能与 0x0200结合或单独存在)
     *                      0x0001 – 数据运算
     *                      0x0002 – PIN密钥
     *                      0x0004 – Mac密钥
     *                      0x0020 – 主密钥
     *                      0x0080 – CBC初始值加密
     *                      0x0200 – 输入方式下载密钥分量
     *                      0x0400 – 导入方式下载密钥分量
     * @param iKeyLength    [IN]密钥长度（8/16/24字节）
     * @param bKeyData      [IN]密钥
     * @param bKCVMode      [IN]KCV模式
     *                      0x00 – 无校验
     *                      0x01 – 自身校验
     *                      0x02 – 零校验
     * @param iKCVLen       [OUT]KCV的长度
     * @param bKCV          [OUT]KCV的值
     *
     * @return              0 - 成功 非 0失败
     */
    public int loadKeyByName(String strKeyName, String strParentName, long[] lKeyUsage, int iKeyLength,
                             byte[] bKeyData, byte bKCVMode, int[] iKCVLen, byte[] bKCV);


    /**
     * 以密槽号引的方式删除密钥
     *
     * @param bKeyNo [IN]密钥的密钥号（0x01~0x0F，0xFF-删除所有密钥）
     *
     * @return 0 - 成功 非 0失败
     */
    public int deleteKey(byte bKeyNo);


    /**
     * 以名称的方式删除密钥
     *
     * @param strKeyName [IN]密钥名称
     *
     * @return 0 - 成功 非 0失败
     */
    public int deleteKeyByName(String strKeyName);


    /**
     * 加密，支持 ECB/CBC/CFB
     *
     * @param iEncMode      [IN]加密模式
     *                      PIN_MODE_ECB   0x00     ECB加密
     *                      PIN_MODE_CBC   0x01     CBC加密
     *                      PIN_MODE_CFB   0x02     CFB加密
     * @param strKeyName    [IN]密钥名称
     * @param iInOutDataLen [IN/OUT]作为入参指定待加密数据的长度，作为出参表示加密后数据的长度
     * @param bInOutEncData [IN/OUT]作为入参指定待加密数据，作为出参表示加密后的数据
     * @param bytePadChar   [IN]填充字符 0x00 ~ 0x0F
     * @param bStartValue   [IN]初始向量的值，ECB模式传 null即可
     *
     * @return 0 - 成功 非 0失败
     */
    public int encryptByName(int iEncMode, String strKeyName, int[] iInOutDataLen, byte[] bInOutEncData,
                             byte bytePadChar, byte[] bStartValue);


    /**
     * 解密，支持ECB/CBC/CFB
     *
     * @param iEncMode    [IN]解密模式
     *                    PIN_MODE_ECB   0x00     ECB解密
     *                    PIN_MODE_CBC   0x01     CBC解密
     *                    PIN_MODE_CFB   0x02     CFB解密
     * @param strKeyName  [IN]密钥名称
     * @param iDataLen    [IN]作为入参指定待解密数据的长度，作为出参表示解密后数据的长度
     * @param bEncData    [IN]作为入参指定待解密数据，作为出参表示解密后的数据
     * @param bytePadChar [IN]输入填充字符  0x00 ~ 0x0F
     * @param bStartValue [IN]初始向量的值，ECB模式传null即可
     *
     * @return 0 - 成功 非0失败
     */
    public int decryptByName(int iEncMode, String strKeyName, int[] iDataLen,
                             byte[] bEncData, byte bytePadChar, byte[] bStartValue);


    /**
     * 以密钥名称方式 MAC运算
     *
     * @param strKeyName     [IN]密钥名称
     * @param lTransformMode [IN]Mac计算算法
     *                       0x08000 (使用 DES CBC传输模式的 CBC MAC运算，即ANSI 9.9标准)
     *                       0x10000 (使用 DES ECB传输模式的 CBC MAC运算，即ANSI 9.19标准）
     *                       0x20000 (使用 DES CBC传入模式的 CBC MAC运算，使用密钥的前 8字节作为实际计算MAC的密钥，即ANSI 9.19_CON标准-不支持）
     * @param iDataLen       [IN]待运算数据长度
     * @param bData          [IN]待运算数据
     * @param bytePadChar    [IN]填充字符  0x00 ~ 0x0F
     * @param bStartValue    [IN]初始向量值（8字节）
     * @param lResultLen     [OUT]Mac结果长度
     * @param bResult        [OUT]Mac结果
     *
     * @return 0 - 成功 非 0失败
     */
    public int calculateMACByName(String strKeyName, long lTransformMode, int iDataLen, byte[] bData,
                                  byte bytePadChar, byte[] bStartValue, long[] lResultLen, byte[] bResult);


    /**
     * 生成 SM2公私钥对
     *
     * @param strKeyName [IN]生成的密钥对名称
     * @param lKeyUsage  [IN]生成密钥对的用途
     *                  0x00020000 – 公钥加密
     *                  0x00040000 – 私钥解密
     *                  0x08000000 – 私钥签名
     *                  0x10000000 – 公钥验签
     *
     * @return 0 - 成功 非 0失败
     */
    public int generateSM2KeyPair(String strKeyName, long lKeyUsage);


    /**
     * 导入SM2密钥
     *
     * @param strKeyName    [IN]密钥名称
     * @param iKeyUsage     [IN]生成密钥对的用途
     *                      0x00020000 – 公钥加密
     *                      0x00040000 – 私钥解密
     *                      0x08000000 – 私钥签名
     *                      0x10000000 – 公钥验签
     * @param iPubKeyLen    [IN]公钥长度
     * @param bPubKeyData   [IN]公钥数据
     * @param iPriKeyLen    [IN]私钥长度
     * @param bPriKeyData   [IN]私钥数据
     *
     * @return 0 - 成功 非 0失败
     */
    public int loadSM2Key(String strKeyName, long iKeyUsage, int iPubKeyLen,
                          byte[] bPubKeyData, int iPriKeyLen, byte[] bPriKeyData);


    /**
     * 导出 SM2公钥
     *
     * @param strKeyName    [IN]密钥名称
     * @param iPubKeylen    [OUT]公钥数据的长度
     * @param bPubKey       [OUT]公钥数据
     * @param lPubKeyUsage  [OUT]公钥的密钥属性
     *                      0x00020000 – 公钥加密
     *                      0x00040000 – 私钥解密
     *                      0x08000000 – 私钥签名
     *                      0x10000000 – 公钥验签
     *
     * @return 0 - 成功 非 0失败
     */
    public int exportSM2PublicKey(String strKeyName, int[] iPubKeylen,
                                  byte[] bPubKey, long[] lPubKeyUsage);


    /**
     * SM2公钥加密
     *
     * @param strKeyName    [IN]密钥对名称
     * @param iInDataLen    [IN]待加密的数据长度
     * @param bInData       [IN]待加密的数据
     * @param iOutDataLen   [OUT]加密后数据的长度
     * @param bOutData      [OUT]加密后的数据
     * @param iFlag         [IN] C1C2C3顺序标识
     *                      0x00 - C1C2C3
     *                      0x01 - C1C3C2
     * @return 0 - 成功 非 0失败
     */
    public int publicKeyEncBySM2(String strKeyName, int iInDataLen, byte[] bInData,
                                 int[] iOutDataLen, byte[] bOutData, int iFlag);


    /**
     * SM2私钥解密
     *
     * @param strKeyName    [IN]密钥对名称
     * @param iInDataLen    [IN]待解密的数据长度
     * @param bInData       [IN]待解密的数据
     * @param iOutDataLen   [OUT]输出解密后数据的长度
     * @param bOutData      [OUT]输出解密后的数据
     * @param iFlag         [IN]C1C2C3顺序标识
     *                      0x00 - C1C2C3
     *                      0x01 - C1C3C2
     * @return 0 - 成功 非 0失败
     */
    public int privateKeyDecBySM2(String strKeyName, int iInDataLen, byte[] bInData,
                                  int[] iOutDataLen, byte[] bOutData, int iFlag);


    /**
     * SM2私钥签名
     *
     * @param strKeyName    [IN]密钥对名称
     * @param iInDataLen    [IN]待签名的数据长度
     * @param bInData       [IN]待签名的数据
     * @param iOutDataLen   [OUT]签名后数据的长度
     * @param bOutData      [OUT]签名后的数据
     *
     * @return 0 - 成功 非 0失败
     */
    public int privateKeySignBySM2(String strKeyName, int iInDataLen, byte[] bInData,
                                   int[] iOutDataLen, byte[] bOutData);


    /**
     * SM2公钥验签
     *
     * @param strKeyName    [IN]密钥对名称
     * @param iRawDataLen   [IN]被签名前的原始数据长度
     * @param bRawData      [IN]被签名前的原始数据
     * @param iSignDataLen  [IN]待验签的签名数据长度
     * @param bSignData     [IN]待验签的签名数据
     *
     * @return 0 - 成功 非 0失败
     */
    public int publicKeyVerifyBySM2(String strKeyName, int iRawDataLen, byte[] bRawData,
                                    int[] iSignDataLen, byte[] bSignData);

    /**
     * Hash ID初始化
     *
     * @param strKeyName    [IN]SM2公钥名称
     * @param iIDLength     [IN]ID数据的长度
     * @param bIDData       [IN]ID数据
     *
     * @return 0 - 成功 非 0失败
     */
    public int initHashID(String strKeyName, int iIDLength, byte[] bIDData);


    /**
     * SM3 Hash运算
     * @param iHashType         [IN]Hash模式
     *                          0x01 – 初始化
     *                          0x02 – 更新
     *                          0x04 – 运算
     * @param iInOutDataLen     [IN/OUT]作为入参指定待运算数据的长度/作为出参表示运算后的数据长度
     * @param bInOutData        [IN/OUT]作为入参指定待运算的数据/作为出参表示运算的数据
     *
     * @return 0 - 成功 非 0失败
     */
    public int hashBySM3(int iHashType, int[] iInOutDataLen, byte[] bInOutData);


    /**
     * 生成 RSA公私钥对到键盘
     *
     * @param strKeyName    [IN]生成的密钥对名称
     * @param lKeyUsage     [IN]生成密钥对的用途
     *                      0x00020000 – 公钥加密
     *                      0x00040000 – 私钥解密
     *                      0x08000000 – 私钥签名
     *                      0x10000000 – 公钥验签
     * @param iKeyLen       [IN]要生成的 RSA密钥对的长度
     * @param lExponent     [IN]指数 E
     * @param bOutData      [OUT]生成密钥对数据
     *
     * @return 0 - 成功 非 0失败
     *
     * 备注:
     * 输出生成密钥对数据为 der
     */
    public int generateRSAKeyPair(String strKeyName, long[] lKeyUsage, int[] iKeyLen,
                                  long[] lExponent, byte[] bOutData);

    /**
     * 导出 RSA公钥
     *
     * @param strKeyName    [IN]密钥名称
     * @param iExportMode   [IN]导出模式
     * @param iPubKeyLen    [OUT]导出的公钥数据的长度
     * @param bPubKey       [OUT]导出的公钥数据
     *
     * @return 0 - 成功 非 0失败
     *
     * 备注:
     * 输出生成密钥对数据为 der
     */
    public int exportRSAPublicKey(String strKeyName, int iExportMode, int[] iPubKeyLen, byte[] bPubKey);

    /**
     * 导入RSA公钥
     *
     * @param strKeyName    [IN]密钥名称
     * @param iExportMode   [IN]模式
     * @param iPubKeyLen    [IN]导入的公钥数据的长度
     * @param bPubKey       [IN]导入的公钥数据
     *
     * @return 0 - 成功 非 0失败
     */
    public int loadRSAKey(String strKeyName, int iExportMode, int iPubKeyLen, byte[] bPubKey);

    /**
     * RSA公钥加密
     *
     * @param strKeyName    [IN]密钥名称
     * @param lSignAlg      [IN]签名的算法
     * 0x00 - No signature algorithm specified.(RSA签名算法不支持)
     * 0x01 - Use the RSASSA-PKCS1-v1.5 algorithm.
     * 0x02 - Use the RSASSA-PSS algorithm.(RSA签名算法不支持)
     * @param lHashMode     [IN]哈希模式
     *                      NID_MD5 = 0x00,(哈希算法不支持)
     *                      NID_sha1 = 0x01,
     *                      NID_sha256 = 0x02
     * @param iInOutDataLen [IN/OUT]输入待加密数据长度/输出加密后数据的长度
     * @param bInOutData    [IN/OUT]输入待加密数据/输出加密后的数据
     *
     * @return 0 - 成功 非 0失败
     */
    public int publicKeyEncByRSA(String strKeyName, long lSignAlg, long lHashMode, int[] iInOutDataLen, byte[] bInOutData);

    /**
     * RSA私钥解密
     *
     * @param strKeyName    [IN]密钥名称
     * @param lSignAlg      [IN]签名的算法
     * 0 - No signature algorithm specified.(RSA签名算法不支持)
     * 1 - Use the RSASSA-PKCS1-v1.5 algorithm.
     * 2 - Use the RSASSA-PSS algorithm.(RSA签名算法不支持)
     * @param lHashMode     [IN]哈希模式
     *                      NID_MD5 = 0x00,(哈希算法不支持)
     *                      NID_sha1 = 0x01,
     *                      NID_sha256 = 0x02
     * @param iInOutDataLen [IN/OUT]作为入参指定待解密数据的长度/作为出参表示解密后数据的长度
     * @param bInOutData    [IN/OUT]作为入参指定待解密数据/作为出参表示解密后的数据
     *
     * @return 0 - 成功 非0失败
     */
    public int privateKeyDecByRSA(String strKeyName, long lSignAlg, long lHashMode, int[] iInOutDataLen, byte[] bInOutData);


    /**
     * RSA私钥签名
     *
     * @param strKeyName    [IN]密钥名称
     * @param lSignAlg      [IN]签名的算法
     * 0 - No signature algorithm specified.(RSA签名算法不支持)
     * 1 - Use the RSASSA-PKCS1-v1.5 algorithm.
     * 2 - Use the RSASSA-PSS algorithm.(RSA签名算法不支持)
     * @param lHashMode     [IN]哈希模式
     *                      NID_MD5 = 0x00,(哈希算法不支持)
     *                      NID_sha1 = 0x01,
     *                      NID_sha256 = 0x02
     * @param iInOutDataLen [IN/OUT]作为入参指定待签名数据的长度/作为出参表示签名后数据的长度
     * @param bInOutData    [IN/OUT]作为入参指定待签名数据/作为出参表示签名后的数据
     *
     * @return 0 - 成功 非 0失败
     */
    public int privateKeySignByRSA(String strKeyName, long lSignAlg, long lHashMode, int[] iInOutDataLen, byte[] bInOutData);

    /**
     * RSA公钥验签
     *
     * @param strKeyName    [IN]密钥对名称
     * @param lSignAlg      [IN]签名的算法
     * 0 - No signature algorithm specified.(RSA签名算法不支持)
     * 1 - Use the RSASSA-PKCS1-v1.5 algorithm.
     * 2 - Use the RSASSA-PSS algorithm.(RSA签名算法不支持)
     * @param lHashMode     [IN]哈希模式
     *                      NID_MD5 = 0x00,(哈希算法不支持)
     *                      NID_sha1 = 0x01,
     *                      NID_sha256 = 0x02
     * @param iRawDataLen   [IN]被签名前的原始数据长度
     * @param bRawData      [IN]被签名前的原始数据
     * @param iSignDataLen  [IN]待验签的签名数据长度
     * @param bSignData     [IN]待验签的签名数据
     *
     * @return 0 - 成功 非 0失败
     */
    public int publicKeyVerifyByRSA(String strKeyName, long lSignAlg, long lHashMode, int iRawDataLen,
                                    byte[] bRawData, int[] iSignDataLen, byte[] bSignData);


    /**
     * 设置SM4对称算法密钥
     * 设置SM4算法密钥
     * @param bKeyIndex [IN]密钥索引号(小于6)
     * @param bMkey     [IN]密钥数据(16字节)
     * @return 0 - 成功 非 0失败
     *
     * 注:
     * 出参：无
     */
//    public int Dmt_Set_SymmetryMKey(byte bKeyIndex, byte[] bMkey);

    /**
     * 以密钥号方式下载密钥
     *
     * @param bKeyNo        [IN]加密密钥索引号
     * @param bPKeyNo       [IN]解密密钥索引号
     * @param lKeyUsage     [IN]密钥属性，其中属性0x0400只能与 0x0200结合或单独存在
     *                      0x0001 – 数据运算
     *                      0x0002 – PIN密钥
     *                      0x0004 – Mac密钥
     *                      0x0020 – 主密钥
     *                      0x0080 – CBC初始值加密
     *                      0x0200 – 输入方式下载密钥分量
     *                      0x0400 – 导入方式下载密钥分量
     * @param iKeyLength    [IN]密钥长度（8/16/24字节）
     * @param bKeyData      [IN]密钥的内容
     * @param bKCVMode      [IN]KCV的模式 0 – 无校验;1 – 自身校验;2 – 零校验
     * @param KCVLen        [OUT]KCV的长度
     * @param KCV           [OUT]KCV的值
     *
     * @return 0 - 成功 非 0失败
     */
    public int Dmt_DownLoad_Key(byte bKeyNo, byte bPKeyNo, long[] lKeyUsage, int iKeyLength, byte[] bKeyData,
                                byte bKCVMode, int[] KCVLen, byte[] KCV);

    /**
     * 硬件序列号的加密算法
     * @param bIndex    [IN]加密密钥索引
     * @param bInBuf    [IN]卡号后六位
     * @param bOutBuf   [OUT]硬件序列号加密数据
     *
     * @return 0 - 成功 非 0失败
     */
    public int Dmt_SM4_TUSN(byte bIndex, byte[] bInBuf, byte[] bOutBuf);

    /**
     * 以密钥名称方式获取 PINBLOCK
     *
     * @param strKeyName    [IN]密钥名称
     * @param lPinFormat    [IN]PINBLOCK算法
     *                      0x0001 – IBM3624
     *                      0x0002 – ANSI
     *                      0x0004 – ISO0
     *                      0x0008 – ISO1
     *                      0x0010 – ECI2
     *                      0x0080 – DIEBOLD
     *                      0x0100 – DIEBOLDCO
     *                      0x2000 – ISO3
     * @param byteDataLen   [IN]卡号长度
     * @param bCustomerData [IN]卡号数据
     * @param bytePadChar   [IN]填充字符  0x00 ~ 0x0F
     * @param bPinBlock     [OUT]PINBLOCK
     *
     * @return 0 - 成功 非 0失败
     */
    public int getPinBlockByName(String strKeyName, long lPinFormat, byte byteDataLen,
                                 byte[] bCustomerData, byte bytePadChar, byte[] bPinBlock);



}
