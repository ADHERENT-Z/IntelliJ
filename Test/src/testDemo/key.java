package testDemo;

public interface key {

    /**
     * 下载鉴权根密钥
     * @param bChipIndex    [IN] 设备安全芯片索引号
     * @param bKeyData      [IN] 密钥数据
     *
     * @return 0成功 非0失败
     */
    public int loadAuthRootKey(byte bChipIndex, byte[] bKeyData);

    /**
     * 获取设备安全芯片的随机数
     * @param bChipIndex    [IN] 设备安全芯片索引号
     * @param RandomNumLen  [IN] 获取的随机数长度
     * @param bRandomData   [OUT] 随机数数据
     *
     * @return 0成功 非0失败
     */
    public int getRandomNum(byte bChipIndex, int RandomNumLen, byte[] bRandomData);

    /**
     * 分发密钥分散因子
     * 下发密钥分散因子，安全芯片收到密钥分散因子，根据鉴权根密钥和分散规则计算出加密密钥
     * @param bChipIndex    [IN] 设备安全芯片索引号
     * @param bScaFacData   [IN] 分散因子数据
     *
     * @return 0成功 非0失败
     */
    public int distriKeyScaFac(byte bChipIndex, byte[] bScaFacData);

    /**
     * 芯片认证
     * @param bChipIndex    [IN] 设备安全芯片索引号
     * @param bRandomData   [IN] 随机数数据
     * @param bCipherData   [OUT] 密文
     *
     * @return 0成功 非0失败
     */
    public int chipAuth(byte bChipIndex, byte[] bRandomData, byte[] bCipherData);

}
