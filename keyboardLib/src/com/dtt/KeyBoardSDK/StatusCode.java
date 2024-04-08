package com.dtt.KeyBoardSDK;

public interface StatusCode {


    public static final String TAG_ENC = "Encrypt_Flg"; // 加密状态的标签
    public static final int CMD_ControlBeeper               =   0x0001;     // 扩展指令 -- 键盘蜂鸣
    public static final int CMD_MoveCertificationCtrl       =   0x0002;     // 扩展指令 -- 键盘密钥拆机自毁
    public static final int CMD_ExCommand_1                 =   0x0003;     // 扩展指令
    public static final int CMD_ExCommand_2                 =   0x0004;     // 扩展指令
    public static final int CMD_ExCommand_3                 =   0x0005;     // 扩展指令
    public static final int PIN_MODE_ECB                    =   0x0000;     // 加解密中的 ECB模式
    public static final int PIN_MODE_CBC                    =   0x0001;     // 加解密中的 CBC模式
    public static final int PIN_MODE_CFB                    =   0x0002;     // 加解密中的 CFB模式
    public static final int MAC_ANSI_9_9                    =   0x08000;    // 使用 DES CBC传输模式的CBC MAC运算，即ANSI 9.9标准
    public static final int MAC_ANSI_9_19                   =   0x10000;    // 使用 DES ECB传输模式的ECB MAC运算，即ANSI 9.19标准
    public static final int MAC_ANSI_9_19_CON               =   0x20000;    // 使用 DES CBC传入模式的CBC MAC运算，使用密钥的前8字节作为实际计算MAC的密钥，即ANSI 9.19_CON标准
    public static final int PIN_RC_SUCCESS                  =   0x0000;     // 命令执行成功
    public static final int PIN_RC_FAILURE                  =   0x00F1;     // 命令执行失败
    public static final int PIN_RC_INVALID_PARAM            =   0x00F2;     // 参数错误
    public static final int PIN_RC_NOT_SUPPORT              =   0x00F3;     // 不支持的命令
    public static final int PIN_RC_NODEVICE                 =   0x00F4;     // 设备不存在
    public static final int PIN_RC_NOT_INIT                 =   0x00F5;     // 设备未初始化
    public static final int PIN_RC_TIMEOUT                  =   0x00F6;     // 操作超时
    public static final int PIN_RC_CANCEL                   =   0x00F7;     // 操作取消
    public static final int PIN_RC_IS_BUSY                  =   0x00F8;     // 设备正忙
    public static final int PIN_RC_INVALID_KEY_LENGTH       =   0x00F9;     // 无效的密钥长度
    public static final int PIN_RC_KEY_NO_VALUE             =   0x00FA;     // 密钥为空
    public static final int PIN_RC_NO_ACTIVE_KEY            =   0x00FB;     // 无有效按键

    /*******************旭子驱动返回的错误码*******************/
    public static final int PIN_RC_CommandNotSupport        =   0x0001;     //不支持命令码
    public static final int PIN_RC_Len_Error                =   0x0002;     //命令长度错误
    public static final int PIN_RC_DivisionSymbol_Error     =   0x0003;     //命令分割符错误(EPP固件解析不到正确的分割符)
    public static final int PIN_RC_DivisionSymbol_LenError  =   0x0004;     //命令分割符长度错误
    public static final int PIN_RC_Wrong_HSM_State          =   0x0006;     //HSM状态不正确
    public static final int PIN_RC_MAC_Incorrect            =   0x0007;     //MAC不正确
    public static final int PIN_RC_Algorithm_Not_Support    =   0x000C;     //算法不支持
    public static final int PIN_RC_DateType_NotSupport      =   0x000D;     //格式不支持
    public static final int PIN_RC_MODE_NOSUPPORT           =   0x000E;     //模式不支持
    public static final int PIN_RC_PROTOCOL_NOSUPPORT       =   0x000F;     //协议不支持
    public static final int PIN_RC_CRYPT_MOD_NOTALLOW       =   0x0010;     //密文模式不允许
    public static final int PIN_RC_KEYUSE_INVALID           =   0x0011;     //不正确的用途
    public static final int PIN_RC_ENCRYPT_NOT_READY        =   0x0012;     //加密模块的没初始化或没处于准备状态
    public static final int PIN_RC_No_Keyid                 =   0x0013;     //密钥槽号丢失
    public static final int PIN_RC_Keyid_NotFound           =   0x0014;     //密钥号没找到
    public static final int PIN_RC_Keyid_Error              =   0x0015;     //密钥号无效
    public static final int PIN_RC_Key_Not_Load             =   0x0017;     //密钥尚未下载
    public static final int PIN_RC_STORAGE_FULL             =   0x0018;     //密钥存储空间不足
    public static final int PIN_RC_KEY_NOTALLOW_OVERWRITE   =   0x0019;     //密钥不能被重写
    public static final int PIN_RC_ENCKEY_NOT_LOAD          =   0x001A;     //密钥加密密钥没有下载
    public static final int PIN_RC_Key_Attr_NotSupport      =   0x001B;     //无效的密钥用途
    public static final int PIN_RC_Key_Len_NotSupport       =   0x001C;     //无效的密钥长度
    public static final int PIN_RC_No_More_Key_Space        =   0x001D;     //无密钥存储空间
    public static final int PIN_RC_ALREAD_OPEN              =   0x001E;     //键盘已打开
    public static final int PIN_RC_ALREAD_CLOSE             =   0x001F;     //键盘已关闭
    public static final int PIN_RC_MinLen_Greater_MaxLen    =   0x0020;     //键盘最小长度大于最大长度
    public static final int PIN_RC_StartValueLen_error      =   0x0021;     //无效向量长度
    public static final int PIN_RC_KEYORFKS_DISALBE         =   0x0022;     //至少指定功能键或 FDKs 中之一不可用
    public static final int PIN_RC_KeyOrFSK_NotSupport      =   0x0023;     //至少指定功能键或FDKs中之一不被服务供应商支持
    public static final int PIN_RC_KCV_Error                =   0x0024;     //KCV对比错误
    public static final int PIN_RC_NOT_TERKEY_AND_AUTOEND   =   0x0025;     //没有指定的终止键并且 usMaxLen 没有设为 0， bAutoEnd 为 FALSE
    public static final int PIN_RC_MIN_OR_MAX_INVALID       =   0x0026;     //最小密码长度域不可用或其值大于最大密码长度域的值
    public static final int PIN_RC_Pin_Not_Input            =   0x0027;     //没有密码
    public static final int PIN_RC_PinBlockMode_NotSupport  =   0x0028;     //密码格式不支持
    public static final int PIN_RC_Card_Len_Err             =   0x0029;     //PAN长度无效
    public static final int PIN_RC_User_Card_Data_Err       =   0x002A;     //PAN数据无效
    public static final int PIN_RC_User_PinBlock_Len_Err    =   0x002B;     //密码块长度无效
    public static final int PIN_RC_User_PinBlock_Padding_Err =  0x002D;     //填充数字无效
    public static final int PIN_RC_Incorrect_Password       =   0x002F;     //管理员密码错误
    public static final int PIN_RC_Password_Not_Verified    =   0x0031;     //管理员密码没有校验
    public static final int PIN_RC_Data_Len_Error           =   0x0035;     //数据长度错误
    public static final int PIN_RC_Hash_Algorithm_NotSupport =  0x0041;     //哈希算法不支持
    public static final int PIN_RC_GM_Error                 =   0x0046;     //国密错误
    public static final int PIN_RC_DER_Encode_Error         =   0x0050;     //DER编码错误
    public static final int PIN_RC_RSA_Verify_Error         =   0x0051;     //RSA验签失败
    public static final int PIN_RC_RSA_PublicKey_DER_Error  =   0x0052;     //RSA公钥DER解码失败
    public static final int PIN_RC_INVALID_MOD_LEN          =   0x0053;     //错误的模长
    public static final int PIN_RC_RSA_KEY_GENERATION_ERROR =   0x0054;     //RSA产生密钥失败
    public static final int PIN_RC_RSA_SIG_NOT_SUPP         =   0x0055;     //RSA签名算法不支持
    public static final int PIN_RC_RSA_SIGNATUREINVALID     =   0x0056;     //RSA签名无效
    public static final int PIN_RC_RSA_NORSAKEYPAIR         =   0x0057;     //RSA密钥不存在
    public static final int PIN_RC_RSA_Key_NoSignature      =   0x0058;     //没有产生RSA签名
    public static final int PIN_RC_UID_NoSignature          =   0x0059;     //没有UID签名
    public static final int PIN_RC_RSA_DecryptKey_NotLoad   =   0x005A;     //RSA解密密钥不存在
    public static final int PIN_RC_RSA_Decrypt_Error        =   0x005B;     //RSA解密失败
    public static final int PIN_RC_RSA_RandomCheck_Error    =   0x005C;     //RSA对比随机数失败
    public static final int PIN_RC_UID_NotLoad              =   0x0060;     //UID没有下载
    public static final int PIN_RC_Earse_Flash_Error        =   0x0080;     //擦除FLASH错误
    public static final int PIN_RC_Load_App_Error           =   0x0081;     //下载APP失败

    public static final int PIN_RC_CRYPT_INVALID_ARG        =   0x00B0;     //RSA错误的参数
    public static final int PIN_RC_CRYPT_PK_INVALID_PADDING =   0x00B1;     //RSA填充算法无效
    public static final int PIN_RC_CRYPT_BUFFER_OVERFLOW    =   0x00B2;     //RSA内存不够
    public static final int PIN_RC_CRYPT_PK_INVALID_SIZE    =   0x00B3;     //RSA错误的模长
    public static final int PIN_RC_CRYPT_MEM                =   0x00B4;     //RSA输出内存越界
    public static final int PIN_RC_CRYPT_INVALID_HASH       =   0x00B5;     //RSA哈希算不支持
    public static final int PIN_RC_CRYPT_INVALID_PACKET     =   0x00B6;     //DER数据无法解析
    public static final int PIN_RC_UPKI_ERR_MSG_TOO_LONG    =   0x00B7;     //RSA加密数据长度错误
    public static final int PIN_RC_RSA_R_BAD_SIGNATURE      =   0x00B8;     //RSA错误的签名

    public static final int PIN_RC_Send_Error               =   0x00E1;     //发送数据失败
    public static final int PIN_RC_Receive_Error            =   0x00E2;     //接收数据失败
    public static final int PIN_RC_EEP_DivisionSymbol_Error =   0x00E3;     //解析分割符错误(EPP固件返回的指令，Jar没有发现正确的分割符)
    public static final int PIN_RC_Command_Error            =   0x00E4;     //命令错误
    public static final int PIN_RC_Found_NoVIDPID           =   0x00E5;     //没有找到 VID/PID
    public static final int PIN_RC_Open_Interface_Error     =   0x00E6;     //打开端口失败
    public static final int PIN_RC_NO_Permission			=	0x00E7;		//没有操作权限

}
