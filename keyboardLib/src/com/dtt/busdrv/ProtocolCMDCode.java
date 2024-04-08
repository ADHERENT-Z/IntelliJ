package com.dtt.busdrv;

public interface ProtocolCMDCode {

    public static final int CRC_INIT_VALUE                  = 0x6363;
    public static final int COMMAND_HEAD_VALUE              = 0x003A;
    public static final int RESPONSE_HEAD_VALUE             = 0x003B;
    public static final int DOWN_KEY_SIZE                   = 0x004F;
    public static final int PACKET_SIZE                     = 0x0007;
    public static final int SM3_HASH_SIZE                   = 0x0020;
    public static final int DMT_SM2_PRIKEY_SIZE             = 0x0020;
    public static final int DMT_SM2_PUBKEY_SIZE             = 0x0040;
    public static final int DMT_SM2_ENCRYPT_MORE_SIZE       = 0x0060;
    public static final int DMT_SM2_SIGN_SIZE               = 0x0040;
    public static final int DMT_SM2_KEYUSAGE_LEN            = 0x0004;
    public static final int DMT_ID_SIZE                     = 0x0040;
    public static final int DMT_CRC_LEN                     = 0x0002;
    public static final int DMT_SPI_HIGHSPEED_DATA          = 0x0001;
    public static final int MAX_RSA_KEY_INDEX               = 0x0002;
    public static final int RSA_N_MAX_SIZE                  = 0x0200;
    public static final int RSA_P_MAX_SIZE                  = 0x0100;
    public static final int RSA_N_LEN_1024                  = 0x0080;
    public static final int RSA_N_LEN_2048                  = 0x0100;
    public static final int DMT_CHIP_FIRMWAREVERSION_SIZE   = 0x0004;
    public static final int DMT_CHIP_FIRMWAREBUILD_SIZE	    = 0x0006;
    /**
     * 加解密密钥名称通用长度
     */
    public static final int DMT_NAME_UNIVERSAL_LEN          = 0x0018;
    public static final int	CMD_GET_RANDOM                  = 0x0002;
    public static final int	CMD_SWITCH_TO_BOOT              = 0x0003;
    public static final int CMD_SYSTEM_SOFT_RESET           = 0x0004;
    public static final int	CMD_GET_CHIP_FIRMWARE_VERSION   = 0x0005;
    public static final int	CMD_GET_CHIP_FIRMWARE_BUILD_VERSION = 0x0022;;
    public static final int CMD_SM3_INIT                    = 0x0010;
    public static final int SM3_UPDATE                      = 0x0011;
    public static final int CMD_SM3_FINAL                   = 0x0012;
    /******************* RSA ***********************/
    public static final int CMD_RSA_GENKEY                  = 0x0030;
    public static final int CMD_IMPORT_RSAKEY               = 0x0031;
    public static final int CMD_EXPORT_RSAKEY               = 0x0032;
    public static final int CMD_RSA_CRYPTO	                = 0x0033;
    /******************* RSA ***********************/
    public static final int CMD_DOWNLOAD_KEY                = 0x0050;
    public static final int CMD_DELETE_KEY                  = 0x0051;
    public static final int CMD_ECB_CRYPTO                  = 0x0053;
    public static final int CMD_PINBLOCK                    = 0x0052;
    public static final int CMD_CBC_CRYPTO                  = 0x0054;
    public static final int CMD_CFB_CRYPTO                  = 0x0055;
    public static final int CMD_MAC                         = 0x0056;
    public static final int CMD_MASTER_KCV                  = 0x0057;
    public static final int CMD_SINGLE_KCV                  = 0x0058;
    public static final int CMD_SM2_GEN_KEYPAIR             = 0x0059;
    public static final int CMD_SM2_IMPORT_KEY              = 0x005A;
    public static final int CMD_SM2_ENCRYPT                 = 0x005B;
    public static final int CMD_SM2_DECRYPT                 = 0x005C;
    public static final int CMD_SM2_SIGN                    = 0x005D;
    public static final int CMD_SM2_VERIFY                  = 0x005E;
    public static final int CMD_SM2_EXPORT_PUBKEY           = 0x005F;
    public static final int CMD_GET_KEYUSAGE                = 0x0060;
    public static final int CMD_GET_KEYDETAIL               = 0x0062;   //获取键盘加密模式
    public static final int CMD_GET_ALGMODE                 = 0x0071;
    public static final int CMD_SET_ALGMODE                 = 0x0072;
    public static final int CMD_KEYBOARD_OPEN               = 0x0073;   //打开键盘
    public static final int CMD_KEYBOARD_CLOSE              = 0x0074;   //关闭键盘
    public static final int CMD_GET_PRESSMODE               = 0x0075;   //获取按键获取模式
    public static final int CMD_SET_PRESSMODE               = 0x0076;   //设置按键获取模式
    public static final int CMD_SET_BEEPER                  = 0x0077;   //设置蜂鸣器
    public static final int CMD_INIT_HASHID                 = 0x0078;   //设置蜂鸣器
    public static final int CMD_RSA_NAME_SET                = 0x0080;   //设置蜂鸣器
    public static final int CMD_ISSUE_CONFIG                = 0x00FD;   //Hash ID初始化

    public static final int CMD_SET_SYMMETRY_MKEY           = 0x0014;

    public static final int SYMMETRY_KEY_SIZE               = 0x0010;
    public static final int SYMMETRY_ICV_SIZE               = 0x0010;
    public static final int SYMMETRY_BLOCK_SIZE             = 0x0010;

    // SM4
    public static final int SM4_ICV_SIZE                    = 0x0010;
    public static final int SM4_MKEY_SIZE                   = 0x0010;
    public static final int SM4_BLOCK_SIZE                  = 0x0010;
    public static final int CMD_SM4_INIT                    = 0x0015;
    public static final int CMD_SM4_UPDATE                  = 0x0016;
    public static final int CMD_SM4_FINAL                   = 0x0017;
    public static final int CMD_SM4_CRYPTO                  = 0x0018;
    public static final int SM4_MKEY_FLAG                   = 0x00A5;

    // 本SM4接口仅用于长城信息&科融电子
    public static final int CMD_SM4_TUSN                    = 0x7B;   // TUSN加密上送
}
