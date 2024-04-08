package com.dtt.busdrv;

import android.content.Context;

import com.busdrv.UART_Driver;
import com.busdrv.USB_Driver;
import com.dtt.KeyBoardSDK.KeyBoardInterface;
import com.impl.drv.spi.SPI_Driver;

import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1InputStream;
import org.bouncycastle.asn1.ASN1Integer;
import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.DERSequence;
import org.bouncycastle.crypto.CryptoServicesRegistrar;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.bouncycastle.crypto.digests.SHA256Digest;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.RSAPublicKeySpec;
import java.util.Arrays;
import java.util.Iterator;

import com.dmt.drv.I2C;

public class KeyBoard extends KeyBoradProtocol implements KeyBoardInterface {


    @Override
    public int reset() {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_Soft_Reset(tmpbuf);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("reset function:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("reset function:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Soft_Reset(tmpbuf);
        return ret;
    }


    @Override
    public int initialize() {
        //LOGGER.info("Call initialize function");
        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_Issue_Config(tmpbuf);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("initialize function:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("initialize function:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Issue_Config(tmpbuf);
        return ret;
    }


    @Override
    public int generateRandomData(int[] inOutDataLen, byte[] bData) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA + inOutDataLen[0]];
        if (inOutDataLen[0] == 0) {
            //LOGGER.error("generateRandomData function:PIN_RC_INVALID_PARAM");
            return PIN_RC_INVALID_PARAM;
        }
        Dmt_Command_Get_Random(tmpbuf, inOutDataLen[0]);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("generateRandomData function:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + DMT_CRC_LEN + inOutDataLen[0]) != 0) {
            //LOGGER.error("generateRandomData function:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Get_Random(tmpbuf, inOutDataLen, bData);
        return ret;
    }


    /**
     * 获取 SDK 版本信息(V3.0.1)
     *
     * @param psVersion 返回版本信息
     * @return 0 - 成功,非 0 - 失败
     */
    @Override
    public int getVersion(byte[] psVersion) {

        psVersion[0] = 0x76;
        psVersion[1] = 0x33;
        psVersion[2] = 0x2E;
        psVersion[3] = 0x30;
        psVersion[4] = 0x2E;
        psVersion[5] = 0x31;

        return 0;
    }


    @Override
    public int getChipFirmwareVersion(byte[] bChipFirmwareVersion) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_CHIP_FIRMWAREVERSION_SIZE + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_Get_ChipFirmwareVersion(tmpbuf);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("getChipFirmwareVersion function:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + DMT_CHIP_FIRMWAREVERSION_SIZE + DMT_CRC_LEN) != 0) {
            //LOGGER.error("getChipFirmwareVersion function:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Get_ChipFirmwareVersion(tmpbuf, bChipFirmwareVersion);
        return ret;
    }


    @Override
    public int getChipFirmwareBuild(byte[] bChipFirmwareBuild) {
        //LOGGER.info("Call getChipFirmwareBuild function");
        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_CHIP_FIRMWAREBUILD_SIZE + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_Get_ChipFirmwareBuild(tmpbuf);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("getChipFirmwareBuild function:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + DMT_CHIP_FIRMWAREBUILD_SIZE + DMT_CRC_LEN) != 0) {
            //LOGGER.error("getChipFirmwareBuild function:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Get_ChipFirmwareBuild(tmpbuf, bChipFirmwareBuild);
        return ret;
    }


    public int getAlgMode(byte[] bMode) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_SPI_HIGHSPEED_DATA + DMT_CRC_LEN];
        Dmt_Command_Get_AlgMode(tmpbuf);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("getAlgMode function:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA) != 0) {
            //LOGGER.error("getAlgMode function:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Get_AlgMode(tmpbuf, bMode);
        return ret;
    }


    public int setAlgMode(byte bMode) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE];
        Dmt_Command_Set_AlgMode(tmpbuf, bMode);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("setAlgMode function:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("setAlgMode function:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Set_AlgMode(tmpbuf);
        return ret;
    }


    @Override
    public int getKeyDetailEx(byte bKeyNo, byte[] bRetKeyNo, byte[] bParentNo, long[] lKeyUsage,
                              byte[] bKeyName, int[] iKeyLength) {

        int ret = 0;
        // 16 组，每组 55 字节
        byte[] tmpbuf = new byte[PACKET_SIZE + 55 * 16 + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_GetKeyDetailEx(tmpbuf, bKeyNo);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("getKeyDetailEx function:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + 55 * 16 + DMT_CRC_LEN) != 0) {
            //LOGGER.error("getKeyDetailEx function:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_GetKeyDetailEx(tmpbuf, bRetKeyNo, bParentNo, lKeyUsage,
                bKeyName, iKeyLength);
        return ret;
    }


    public int Dmt_DownLoad_Key(byte bKeyNo, byte bPKeyNo, long[] lKeyUsage, int iKeyLength, byte[] bKeyData,
                                byte bKCVMode, int[] KCVLen, byte[] KCV) {
        byte[] tmpbuf = new byte[PACKET_SIZE + 29 + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];

        Dmt_Command_DownLoad_KeyByNo(tmpbuf, bKeyNo, bPKeyNo, lKeyUsage, iKeyLength, bKeyData, bKCVMode);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + 29 + DMT_CRC_LEN) != 0) {
            return PIN_RC_Send_Error;
        }

        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + 16 + DMT_CRC_LEN) != 0) {
            return PIN_RC_Receive_Error;
        }

        return Dmt_Response_DownLoad_Key(tmpbuf, KCVLen, KCV);
    }


    @Override
    public int loadKeyByName(String strKeyName, String strParentName, long[] lKeyUsage, int iKeyLength,
                             byte[] bKeyData, byte bKCVMode, int[] iKCVLen, byte[] bKCV) {

        int ret = 0;
        if (bKeyData.length != iKeyLength) {
            return PIN_RC_INVALID_PARAM;
        }

        byte[] tmpbuf = new byte[PACKET_SIZE + DOWN_KEY_SIZE + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_DownLoad_Key(tmpbuf, (byte) 0x00, (byte) 0x01, strKeyName,
                strParentName, lKeyUsage, iKeyLength, bKeyData, bKCVMode);
        if (Dmt_Send_Total_Data(tmpbuf, DOWN_KEY_SIZE + PACKET_SIZE + DMT_CRC_LEN) != 0) {
            //LOGGER.error("loadKeyByName:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + 16 + DMT_CRC_LEN) != 0) {
            //LOGGER.error("loadKeyByName:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_DownLoad_Key(tmpbuf, iKCVLen, bKCV);
        return ret;
    }


    @Override
    public int deleteKey(byte bKeyNo) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_Delete_Key(tmpbuf, null, bKeyNo, (byte) 0x00, 0);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("deleteKey:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("deleteKey:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Delete_Key(tmpbuf);
        return ret;
    }


    @Override
    public int deleteKeyByName(String strKeyName) {

        int ret = 0;
        // 要删除密钥的密钥名称不能为空
        if (strKeyName.length() == 0) {
            //LOGGER.error("deleteKeyByName:PIN_RC_INVALID_PARAM");
            return PIN_RC_INVALID_PARAM;
        }

        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_SPI_HIGHSPEED_DATA + DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN];
        Arrays.fill(tmpbuf, 0, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA, (byte) 0xFF);
        Dmt_Command_Delete_Key(tmpbuf, strKeyName, (byte) 0x00, (byte) 0x01, DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN);

        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN) != 0) {
            //LOGGER.error("deleteKeyByName:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("deleteKeyByName:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Delete_Key(tmpbuf);
        return ret;
    }


    @Override
    public int encryptByName(int iEncMode, String strKeyName, int[] iInOutDataLen, byte[] bInOutEncData,
                             byte bytePadChar, byte[] bStartValue) {

        int ret = 0;
        int EncMode = 0;

        int recv_len = 0;
        byte[] bMode = new byte[1];
        getAlgMode(bMode);
        if (bMode[0] == 1) {
            recv_len = iInOutDataLen[0] % 16;
            if (recv_len != 0) {
                recv_len = iInOutDataLen[0] + 16 - recv_len;
            } else {
                recv_len = iInOutDataLen[0];
            }
        } else {
            recv_len = iInOutDataLen[0] % 8;
            if (recv_len != 0) {
                recv_len = iInOutDataLen[0] + 8 - recv_len;
            } else {
                recv_len = iInOutDataLen[0];
            }
        }

        byte[] tmpbuf = new byte[PACKET_SIZE + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN +
                DMT_SPI_HIGHSPEED_DATA + DMT_CRC_LEN + iInOutDataLen[0]];
        /**
         * P2[bit0]:0-加密, 1-解密
         * P2[bit1~2] 00-ECB, 01-CBC, 10-CFB;
         * P2[bit3]:1-TDES, 0-DES(当前版本不做区分，底层直接根据密钥长度做出选择)
         *
         * DES-ECB加密 mode:0000
         * DES-ECB解密 mode:0001
         * DES-CBC加密 mode:0010
         * DES-CBC解密 mode:0011
         * DES-CFB加密 mode:0100
         * DES-CFB解密 mode:0101
         *
         * TDES-ECB加密 mode:1000
         * TDES-ECB解密 mode:1001
         * TDES-CBC加密 mode:1010
         * TDES-CBC解密 mode:1011
         * TDES-CFB加密 mode:1100
         * TDES-CFB解密 mode:1101
         */
        switch (iEncMode) {
            case PIN_MODE_ECB:
                // ECB加密
                EncMode = 0;
                break;
            case PIN_MODE_CBC:
                // CBC加密
                EncMode = 2;
                break;
            case PIN_MODE_CFB:
                // CFB加密
                EncMode = 4;
                break;
        }

        // padding
//        byte remainder = (byte) (iInOutDataLen[0] % 8);
//        if (remainder != 0) {
//            byte[] bData_pad = new byte[iInOutDataLen[0] + (8 - remainder)];
//            System.arraycopy(bInOutEncData, 0, bData_pad, 0, iInOutDataLen[0]);
//            Arrays.fill(bData_pad, iInOutDataLen[0], bData_pad.length, bytePadChar);
//            iInOutDataLen[0] = bData_pad.length;
//            Dmt_Command_CrytoByName(tmpbuf, EncMode, strKeyName, bData_pad, iInOutDataLen[0], bStartValue);
//        } else {
//            Dmt_Command_CrytoByName(tmpbuf, EncMode, strKeyName, bInOutEncData, iInOutDataLen[0], bStartValue);
//        }

        Dmt_Command_CrytoByName(tmpbuf, EncMode, strKeyName, bInOutEncData, iInOutDataLen[0], bStartValue, bytePadChar);

        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN +
                DMT_CRC_LEN + iInOutDataLen[0]) != 0) {
            //LOGGER.error("encryptByName:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + DMT_CRC_LEN + recv_len) != 0) {
            //LOGGER.error("encryptByName:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Cryto(tmpbuf, bInOutEncData, iInOutDataLen);
        return ret;
    }


    @Override
    public int decryptByName(int iEncMode, String strKeyName, int[] iInOutDataLen, byte[] bInOutEncData,
                             byte bytePadChar, byte[] bStartValue) {

        int ret = 0;
        int EncMode = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN +
                DMT_SPI_HIGHSPEED_DATA + DMT_CRC_LEN + iInOutDataLen[0]];
        /**
         * P2[bit0]:1-加密, 0-解密
         * P2[bit1~2] 00-ECB, 01-CBC, 10-CFB;
         * P2[bit3]:1-TDES, 0-DES(当前版本不做区分，底层直接根据密钥长度做出选择)
         *
         * DES-ECB加密 mode:0000
         * DES-ECB解密 mode:0001
         * DES-CBC加密 mode:0010
         * DES-CBC解密 mode:0011
         * DES-CFB加密 mode:0100
         * DES-CFB解密 mode:0101
         *
         * TDES-ECB加密 mode:1000
         * TDES-ECB解密 mode:1001
         * TDES-CBC加密 mode:1010
         * TDES-CBC解密 mode:1011
         * TDES-CFB加密 mode:1100
         * TDES-CFB解密 mode:1101
         */
        switch (iEncMode) {
            case PIN_MODE_ECB:
                // ECB解密
                EncMode = 1;
                break;
            case PIN_MODE_CBC:
                // CBC解密
                EncMode = 3;
                break;
            case PIN_MODE_CFB:
                // CFB解密
                EncMode = 5;
                break;
        }

        // padding
//        byte remainder = (byte) (iInOutDataLen[0] % 8);
//        if (remainder != 0) {
//            byte[] bData_pad = new byte[iInOutDataLen[0] + (8 - remainder)];
//            System.arraycopy(bInOutEncData, 0, bData_pad, 0, iInOutDataLen[0]);
//            Arrays.fill(bData_pad, iInOutDataLen[0], bData_pad.length, bytePadChar);
//            iInOutDataLen[0] = bData_pad.length;
//            Dmt_Command_CrytoByName(tmpbuf, EncMode, strKeyName, bData_pad, iInOutDataLen[0], bStartValue);
//        } else {
//            Dmt_Command_CrytoByName(tmpbuf, EncMode, strKeyName, bInOutEncData, iInOutDataLen[0], bStartValue);
//        }

        Dmt_Command_CrytoByName(tmpbuf, EncMode, strKeyName, bInOutEncData, iInOutDataLen[0], bStartValue, bytePadChar);


        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN +
                DMT_CRC_LEN + iInOutDataLen[0]) != 0) {
            //LOGGER.error("decryptByName:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + DMT_CRC_LEN + iInOutDataLen[0]) != 0) {
            //LOGGER.error("decryptByName:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Cryto(tmpbuf, bInOutEncData, iInOutDataLen);
        return ret;
    }


    @Override
    public int calculateMACByName(String strKeyName, long lTransformMode, int iDataLen, byte[] bData,
                                  byte bytePadChar, byte[] bStartValue, long[] lResultLen, byte[] bResult) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN +
                DMT_SPI_HIGHSPEED_DATA + DMT_CRC_LEN + iDataLen];

        byte bTransformMode = 0;
        if (lTransformMode == MAC_ANSI_9_9) {
            bTransformMode = 0;
        } else if (lTransformMode == MAC_ANSI_9_19) {
            bTransformMode = 1;
        } else if (lTransformMode == MAC_ANSI_9_19_CON) {
            //LOGGER.warn("calculateMACByName function:PIN_RC_NOT_SUPPORT");
            return PIN_RC_NOT_SUPPORT;
        }

        // padding
//        byte remainder = (byte) (iDataLen % 8);
//        if (remainder != 0) {
//            byte[] bData_pad = new byte[iDataLen + (8 - remainder)];
//            System.arraycopy(bData, 0, bData_pad, 0, iDataLen);
//            Arrays.fill(bData_pad, iDataLen, bData_pad.length, bytePadChar);
//            iDataLen = bData_pad.length;
//            Dmt_Command_Mac_ByName(tmpbuf, bData_pad, bStartValue, strKeyName, bTransformMode, iDataLen, bytePadChar);
//        } else {
//            Dmt_Command_Mac_ByName(tmpbuf, bData, bStartValue, strKeyName, bTransformMode, iDataLen, bytePadChar);
//        }

        Dmt_Command_Mac_ByName(tmpbuf, bData, bStartValue, strKeyName, bTransformMode, iDataLen, bytePadChar);

        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN +
                DMT_CRC_LEN + iDataLen) != 0) {
            //LOGGER.error("calculateMACByName:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + 16 + DMT_CRC_LEN) != 0) {
            //LOGGER.error("calculateMACByName:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Mac_ByName(tmpbuf, bResult, lResultLen);
        return ret;
    }


    @Override
    public int generateSM2KeyPair(String strSM2KeyName, long lKeyUsage) {

        // 24byte name + 4byte KeyUsage + 2byte crc
        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_SM2_KEYUSAGE_LEN +
                DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_GenSM2KeyPair(tmpbuf, strSM2KeyName, lKeyUsage);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN
                + DMT_SM2_KEYUSAGE_LEN + DMT_CRC_LEN) != 0) {
            //LOGGER.error("generateSM2KeyPair:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("generateSM2KeyPair:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_GenSM2KeyPair(tmpbuf);
        return ret;
    }


    @Override
    public int loadSM2Key(String strKeyName, long lKeyUsage, int iPubKeyLen,
                          byte[] bPubKeyData, int iPriKeyLen, byte[] bPriKeyData) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_SM2_PRIKEY_SIZE + DMT_SM2_PUBKEY_SIZE +
                +DMT_NAME_UNIVERSAL_LEN + DMT_SM2_KEYUSAGE_LEN + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];

        Dmt_Command_load_SM2KeyPair(tmpbuf, bPriKeyData, bPubKeyData, strKeyName, lKeyUsage);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + DMT_SM2_PRIKEY_SIZE + DMT_SM2_PUBKEY_SIZE +
                DMT_NAME_UNIVERSAL_LEN + DMT_SM2_KEYUSAGE_LEN + DMT_CRC_LEN) != 0) {
            //LOGGER.error("loadSM2Key:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("loadSM2Key:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_load_SM2KeyPair(tmpbuf);
        return ret;
    }


    @Override
    public int exportSM2PublicKey(String strKeyName, int[] iPubKeylen, byte[] bPubKey, long[] lPubKeyUsage) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_SM2_PUBKEY_SIZE +
                DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];

        Dmt_Command_exportSM2PubKey(tmpbuf, strKeyName);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN) != 0) {
            //LOGGER.error("exportSM2PublicKey:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + DMT_SM2_PUBKEY_SIZE + DMT_CRC_LEN + DMT_SM2_KEYUSAGE_LEN) != 0) {
            //LOGGER.error("exportSM2PublicKey:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_exportSM2PubKey(tmpbuf, iPubKeylen, bPubKey, lPubKeyUsage);
        return ret;
    }


    @Override
    public int publicKeyEncBySM2(String strKeyName, int iInDataLen, byte[] bInData,
                                 int[] iOutDataLen, byte[] bOutData, int iFlag) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + iInDataLen + DMT_SM2_ENCRYPT_MORE_SIZE +
                DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
        if (iInDataLen == 0 || tmpbuf == null) {
            //LOGGER.error("publicKeyEncBySM2:PIN_RC_INVALID_PARAM");
            return PIN_RC_INVALID_PARAM;
        }

        Dmt_Command_EncBySM2(tmpbuf, strKeyName, bInData, iInDataLen, iFlag);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + iInDataLen + DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN) != 0) {
            //LOGGER.error("publicKeyEncBySM2:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + iInDataLen
                + DMT_SM2_ENCRYPT_MORE_SIZE + DMT_CRC_LEN) != 0) {
            //LOGGER.error("publicKeyEncBySM2:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        iOutDataLen[0] = iInDataLen + DMT_SM2_ENCRYPT_MORE_SIZE;
        ret = Dmt_Response_EncBySM2(tmpbuf, bOutData, iInDataLen);
        return ret;
    }


    @Override
    public int privateKeyDecBySM2(String strKeyName, int iInDataLen, byte[] bInData,
                                  int[] iOutDataLen, byte[] bOutData, int iFlag) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + iInDataLen + DMT_NAME_UNIVERSAL_LEN
                + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];

        if (iInDataLen <= DMT_SM2_ENCRYPT_MORE_SIZE || tmpbuf == null) {
            //LOGGER.error("privateKeyDecBySM2:PIN_RC_INVALID_PARAM");
            return PIN_RC_INVALID_PARAM;
        }

        Dmt_Command_DecBySM2(tmpbuf, strKeyName, bInData, iInDataLen, iFlag);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + iInDataLen +
                DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN) != 0) {
            //LOGGER.error("privateKeyDecBySM2:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + iInDataLen
                - DMT_SM2_ENCRYPT_MORE_SIZE + DMT_CRC_LEN) != 0) {
            //LOGGER.error("privateKeyDecBySM2:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        iOutDataLen[0] = iInDataLen - DMT_SM2_ENCRYPT_MORE_SIZE;
        ret = Dmt_Response_DecBySM2(tmpbuf, bOutData, iInDataLen);
        return ret;
    }


    @Override
    public int privateKeySignBySM2(String strKeyName, int iInDataLen, byte[] bInData,
                                   int[] iOutDataLen, byte[] bOutData) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_SM2_SIGN_SIZE + DMT_NAME_UNIVERSAL_LEN
                + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];

        Dmt_Command_SignBySM2(tmpbuf, bInData, strKeyName);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + SM3_HASH_SIZE +
                DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN) != 0) {
            //LOGGER.error("privateKeySignBySM2:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + DMT_SM2_SIGN_SIZE + DMT_CRC_LEN) != 0) {
            //LOGGER.error("privateKeySignBySM2:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        iOutDataLen[0] = DMT_SM2_SIGN_SIZE;
        ret = Dmt_Response_SignBySM2(tmpbuf, bOutData);
        return ret;
    }


    @Override
    public int publicKeyVerifyBySM2(String strKeyName, int iRawDataLen, byte[] bRawData,
                                    int[] iSignDataLen, byte[] bSignData) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_SM2_SIGN_SIZE + SM3_HASH_SIZE +
                DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];

        Dmt_Command_VerifyBySM2(tmpbuf, bSignData, bRawData, strKeyName);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + DMT_SM2_SIGN_SIZE +
                SM3_HASH_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN) != 0) {
            //LOGGER.error("publicKeyVerifyBySM2:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("publicKeyVerifyBySM2:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_VerifyBySM2(tmpbuf);
        return ret;
    }


    @Override
    public int hashBySM3(int iHashType, int[] iInOutDataLen, byte[] bInOutData) {

        if (0x01 == iHashType) {
            return (Dmt_SM3_Init());
        } else if (0x02 == iHashType) {
            return (Dmt_SM3_Update(bInOutData, iInOutDataLen[0]));
        } else if (0x04 == iHashType) {
            iInOutDataLen[0] = 32;
            return (Dmt_SM3_Final(bInOutData));
        }

        return PIN_RC_INVALID_PARAM;
    }


    @Override
    public int initHashID(String strKeyName, int iIDLength, byte[] bIDData) {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + 256 + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];

        if (iIDLength == 0 || (iIDLength > DMT_ID_SIZE)) {
            //LOGGER.error("initHashID:PIN_RC_INVALID_PARAM");
            return PIN_RC_INVALID_PARAM;
        }

        Dmt_Command_initHashID(tmpbuf, bIDData, iIDLength, strKeyName);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + iIDLength +
                DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN) != 0) {
            //LOGGER.error("initHashID:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("initHashID:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_initHashID(tmpbuf);
        return ret;
    }

    /*****************************SM3 BEGIN*****************************/

    public int Dmt_SM3_Init() {

        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_SM3_Init(tmpbuf);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("Dmt_SM3_Init:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("Dmt_SM3_Init:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_SM3_Init(tmpbuf);
        return ret;
    }

    public int Dmt_SM3_Update(byte[] msgbuf, int iMsgLen) {

        int ret = 0;
        if (iMsgLen == 0) {
            //LOGGER.error("Dmt_SM3_Update:PIN_RC_INVALID_PARAM");
            return PIN_RC_INVALID_PARAM;
        }

        byte[] tmpbuf = new byte[PACKET_SIZE + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA + iMsgLen];
        Dmt_Command_SM3_Update(tmpbuf, msgbuf, iMsgLen);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + DMT_CRC_LEN + iMsgLen) != 0) {
            //LOGGER.error("Dmt_SM3_Update:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("Dmt_SM3_Update:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_SM3_Update(tmpbuf);
        return ret;
    }

    public int Dmt_SM3_Final(byte[] bHashBuf) {
        //LOGGER.info("Call Dmt_SM3_Final function");
        int ret = 0;
        byte[] tmpbuf = new byte[PACKET_SIZE + SM3_HASH_SIZE + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_SM3_Final(tmpbuf);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("Dmt_SM3_Final:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + SM3_HASH_SIZE + DMT_CRC_LEN) != 0) {
            //LOGGER.error("Dmt_SM3_Final:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_SM3_Final(tmpbuf, bHashBuf);
        return ret;
    }
    /*****************************SM3 END*******************************/

    /*****************************RSA Start*******************************/

    public int Dmt_Gen_RSAKey(int iKeyLen, int iIndex, int iType) {

        int ret = 0;
        if (iIndex > MAX_RSA_KEY_INDEX) {
            //LOGGER.error("Dmt_Gen_RSAKey:PIN_RC_INVALID_PARAM");
            return PIN_RC_INVALID_PARAM;
        }

        byte[] tmpbuf = new byte[PACKET_SIZE + 6 + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_Gen_RSAKey(tmpbuf, iKeyLen, iIndex, iType);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + 6 + DMT_CRC_LEN) != 0) {
            //LOGGER.error("Dmt_Gen_RSAKey:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("Dmt_Gen_RSAKey:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Gen_RSAKey(tmpbuf);
        return ret;
    }

    public int Dmt_Import_RSAKey(byte[] bKeyData, int iKeyLen, int iType, int iIndex) {

        int ret = 0;
        if (iIndex > MAX_RSA_KEY_INDEX) {
            //LOGGER.error("Dmt_Import_RSAKey:PIN_RC_INVALID_PARAM");
            return PIN_RC_INVALID_PARAM;
        }
        byte[] tmpbuf = new byte[PACKET_SIZE + (5 * RSA_P_MAX_SIZE) + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_Import_RSAKey(tmpbuf, bKeyData, iKeyLen, iType, iIndex);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + iKeyLen + DMT_CRC_LEN) != 0) {
            //LOGGER.error("Dmt_Import_RSAKey:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("Dmt_Import_RSAKey:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Import_RSAKey(tmpbuf);
        return ret;
    }

    /**
     * 导出指定索引号的密钥数据
     *
     * @param bKeyData     [OUT]指向存储密钥数据的地址指针
     * @param iInOutKeyLen [IN/OUT]导入期望密钥长度，导出的密钥数据的字节长度
     *                     (2048-(e: 4字节； n/d：256字节; (p,q,dp,dq,iq): 640字节；))
     * @param iType        [IN]密钥类型(0 - e; 1 - n; 2 - d; 3 - (p,q,dp,dq,iq);)
     * @param iIndex       [IN]密钥索引号标识(0 ~ 1)
     * @return 0 - 成功 非 0失败
     *
     * 备注：调用该函数前需要确保密钥已被提前导入过，否则返回的数据无效;
     */
    private int Dmt_Export_RSAKey(byte[] bKeyData, int[] iInOutKeyLen, int iType, int iIndex) {

        int ret = 0;
        if (iIndex > MAX_RSA_KEY_INDEX) {
            //LOGGER.error("Dmt_Export_RSAKey:PIN_RC_INVALID_PARAM");
            return PIN_RC_INVALID_PARAM;
        }
//        int inKeyLen = 0;
//        if (iInOutKeyLen[0] == 0) {
//            return PIN_RC_INVALID_PARAM;
//        } else {
//            inKeyLen = iInOutKeyLen[0];
//        }
        byte[] tmpbuf = new byte[PACKET_SIZE + (5 * RSA_P_MAX_SIZE) + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_Export_RSAKey(tmpbuf, iType, iIndex);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
            //LOGGER.error("Dmt_Export_RSAKey:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + iInOutKeyLen[0] + DMT_CRC_LEN) != 0) {
            //LOGGER.error("Dmt_Export_RSAKey:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_Export_RSAKey(tmpbuf, bKeyData, iInOutKeyLen, iType);
        return ret;
    }

    /**
     * 使用指定索引号密钥进行RSA 加解密运算
     *
     * @param bOutbuf   [OUT]指向存储输出结果的地址指针
     * @param bInbuf    [IN]指向输入数据的地址指针
     * @param iMode     [IN]运算模式(0: 加密；1：解密：2：CRT解密)
     * @param iIndex    [IN]密钥索引号标识(0 ~ 1)
     * @param iInOutLen [IN]字节长度
     * @return 0 - 成功 非 0失败
     *
     * 备注:
     * 1)调用该函数之前，需要确保RSA密钥已经提前存储在安全芯片内;
     * 2)输入数据长度为 N字节长度，不足时需在高位补0；
     * 3)输入数据值必须小于模数 n;
     */
    private int Dmt_RSA_Crypto(byte[] bOutbuf, byte[] bInbuf, int iMode, int iIndex, int[] iInOutLen) {

        int ret = 0;
        if (iIndex > MAX_RSA_KEY_INDEX || iMode > MAX_RSA_KEY_INDEX) {
            //LOGGER.error("Dmt_RSA_Crypto:PIN_RC_INVALID_PARAM");
            return PIN_RC_INVALID_PARAM;
        }

        byte[] tmpbuf = new byte[PACKET_SIZE + RSA_N_MAX_SIZE + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
        Dmt_Command_RSA_Crypto(tmpbuf, bInbuf, iMode, iIndex, iInOutLen[0]);
        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + iInOutLen[0] + DMT_CRC_LEN) != 0) {
            //LOGGER.error("Dmt_RSA_Crypto:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + iInOutLen[0] + DMT_CRC_LEN) != 0) {
            //LOGGER.error("Dmt_RSA_Crypto:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        ret = Dmt_Response_RSA_Crypto(tmpbuf, bOutbuf, iInOutLen);
        return ret;
    }


    @Override
    public int generateRSAKeyPair(String strKeyName, long[] lKeyUsage,
                                  int[] iKeyLen, long[] lExponent, byte[] bOutData) {

        int ret = 0;
        byte bIndex[] = {0x00};
        byte[] bKeyName = String2Bytes(strKeyName);
        ret = Dmt_Set_RSA_Name(bKeyName, bKeyName.length, bIndex, lKeyUsage);
        if (ret != 0) {
            //LOGGER.error("generateRSAKeyPair function" + ret);
            return ret;
        }

        ret = Dmt_Gen_RSAKey(iKeyLen[0], bIndex[0], 0);
        if (ret != 0) {
            //LOGGER.error("generateRSAKeyPair function:PIN_RC_RSA_KEY_GENERATION_ERROR" + ret);
            return PIN_RC_RSA_KEY_GENERATION_ERROR;
        }

        // e
        int[] bELen = {0x04};
        byte[] bE = new byte[4];
        ret = Dmt_Export_RSAKey(bE, bELen, 0, bIndex[0]);
        if (ret != 0) {
            //LOGGER.error("generateRSAKeyPair function:PIN_RC_RSA_NORSAKEYPAIR" + ret);
            return PIN_RC_RSA_NORSAKEYPAIR;
        } else {
            lExponent[0] = byteArray2int(bE, 0, false);
        }

        // n
        byte[] bN = new byte[iKeyLen[0]];
        ret = Dmt_Export_RSAKey(bN, iKeyLen, 1, bIndex[0]);
        if (ret != 0) {
            //LOGGER.error("generateRSAKeyPair function:PIN_RC_INVALID_MOD_LEN" + ret);
            return PIN_RC_INVALID_MOD_LEN;
        }
        byte[] newN = new byte[iKeyLen[0] + 1];
        newN[0] = 0x00;
        System.arraycopy(bN, 0, newN, 1, iKeyLen[0]);

        // d
//        byte[] bD = new byte[iKeyLen[0]];
//        ret = Dmt_Export_RSAKey(bD, iKeyLen, 2, bIndex[0]);
//        if (ret != 0) {
//            return ret;
//        }
//        byte[] newD = new byte[iKeyLen[0] + 1];
//        newD[0] = 0x00;
//        System.arraycopy(bD, 0, newD, 1, iKeyLen[0]);

        try {
            byte[] outDer = Dmt_RSA_Pub_Key(newN, bE);
            System.arraycopy(outDer, 0, bOutData, 0, outDer.length);
        } catch (Exception e) {
            //LOGGER.info("generateRSAKeyPair function:PIN_RC_DER_Encode_Error");
            e.printStackTrace();
        }

        return 0;
    }


    @Override
    public int exportRSAPublicKey(String strKeyName, int iExportMode, int[] iPubKeyLen, byte[] bPubKey) {

        int ret = 0;
        byte bIndex[] = {0x00};
        int iKeyUsage[] = {0x00}; // 仅用于填充
        byte[] bKeyName = String2Bytes(strKeyName);
        ret = Dmt_Check_RSA_Name(bKeyName, bKeyName.length, bIndex, iKeyUsage);
        if (ret != 0) {
            //LOGGER.error("exportRSAPublicKey function:" + ret);
            return ret;
        }

        // e
        int[] bELen = {0x04};
        byte[] bE = new byte[4];
        ret = Dmt_Export_RSAKey(bE, bELen, 0, bIndex[0]);
        if (ret != 0) {
            //LOGGER.error("exportRSAPublicKey function:PIN_RC_RSA_NORSAKEYPAIR" + ret);
            return PIN_RC_RSA_NORSAKEYPAIR;
        }

        // n
        byte[] bN = new byte[iPubKeyLen[0]];
        ret = Dmt_Export_RSAKey(bN, iPubKeyLen, 0x01, bIndex[0]);
        if (ret != 0) {
            //LOGGER.error("exportRSAPublicKey function:PIN_RC_RSA_NORSAKEYPAIR" + ret);
            return PIN_RC_RSA_NORSAKEYPAIR;
        }
        byte[] newN = new byte[iPubKeyLen[0] + 1];
        newN[0] = 0x00;
        System.arraycopy(bN, 0, newN, 1, iPubKeyLen[0]);

        try {
            byte[] outDer = Dmt_RSA_Pub_Key(newN, bE);
            System.arraycopy(outDer, 0, bPubKey, 0, outDer.length);
            iPubKeyLen[0] = outDer.length;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return 0;
    }


    @Override
    public int loadRSAKey(String strKeyName, int iExportMode, int iPubKeyLen, byte[] bPubKey) {

        int ret = 0;
        byte bIndex[] = {0x00};
        long lKeyUsage[] = {0x00}; // 仅用于填充
        byte[] bKeyName = String2Bytes(strKeyName);
        ret = Dmt_Set_RSA_Name(bKeyName, bKeyName.length, bIndex, lKeyUsage);
        if (ret != 0) {
            //LOGGER.error("loadRSAKey function" + ret);
            return ret;
        }

        // 调用解密代码
        ASN1Sequence as = null;
        try {
            as = byteArrayToASN1Sequence(bPubKey);
        } catch (IOException e) {
            //LOGGER.error("loadRSAKey function:PIN_RC_CRYPT_INVALID_PACKET" + ret);
            e.printStackTrace();
        }

        Iterator<ASN1Encodable> it = as.iterator();

        // n
        if (it.hasNext()) {
            ASN1Encodable a = it.next();
            byte[] bN = ((ASN1Integer) a).getValue().toByteArray();
            ret = Dmt_Import_RSAKey(bN, bN.length - 1, 1, bIndex[0]);
            if (ret != 0) {
                //LOGGER.error("loadRSAKey function" + ret);
                return PIN_RC_CRYPT_INVALID_PACKET;
            }
        }

        // e
        byte[] bE = new byte[4];
        if (it.hasNext()) {
            ASN1Encodable a = it.next();
            byte[] tmpE = ((ASN1Integer) a).getValue().toByteArray();
            if (tmpE.length < 4) {
                System.arraycopy(tmpE, 0, bE, 4 - tmpE.length, tmpE.length);
            }
        }
        ret = Dmt_Import_RSAKey(bE, bE.length, 0, bIndex[0]);
        if (ret != 0) {
            //LOGGER.error("loadRSAKey function" + ret);
            return ret;
        }

        return 0;
    }


    @Override
    public int publicKeyEncByRSA(String strKeyName, long lSignAlg, long lHashMode,
                                 int[] iInOutDataLen, byte[] bInOutData) {

        int ret = 0;
        // 必须知道导出密钥的长度，否则无法导出，但是接口并没有提供相应的参数，该如何确定
        int[] rsaModeLen = {256};
        byte bIndex[] = {0x00};
        int iKeyUsage[] = {0x00}; // 仅用于填充
        byte[] bKeyName = String2Bytes(strKeyName);
        ret = Dmt_Check_RSA_Name(bKeyName, bKeyName.length, bIndex, iKeyUsage);
        if (ret != 0) {
            //LOGGER.error("publicKeyEncByRSA function" + ret);
            return ret;
        }

        // 获取公钥模数 N
//        byte[] nData = new byte[512];
//        ret = Dmt_Export_RSAKey(nData, rsaModeLen, 0x01, bIndex[0]);
//        if (ret != 0) {
//            return  ret;
//        }

        switch ((int) lSignAlg) {
            case 0x00: {
                //LOGGER.error("publicKeyEncByRSA function:PIN_RC_RSA_SIG_NOT_SUPP");
                ret = PIN_RC_RSA_SIG_NOT_SUPP;
                break;
            }
            case 0x01: {
                byte[] dataPadding = Dmt_PKCS1v1_5Padding(bInOutData, 0, rsaModeLen[0] * 8,
                        iInOutDataLen[0], (byte) lHashMode, true);
                int[] dataPaddingLen = {dataPadding.length};
                ret = Dmt_RSA_Crypto(bInOutData, dataPadding, 0, bIndex[0], dataPaddingLen);
                iInOutDataLen[0] = dataPaddingLen[0];
                if (ret != 0) {
                    //LOGGER.error("publicKeyEncByRSA function" + ret);
                    return ret;
                }
                break;
            }
            case 0x02: {
                //LOGGER.error("publicKeyEncByRSA function:PIN_RC_RSA_SIG_NOT_SUPP");
                ret = PIN_RC_RSA_SIG_NOT_SUPP;
                break;
            }
        }

        return ret;
    }


    @Override
    public int privateKeyDecByRSA(String strKeyName, long lSignAlg, long lHashMode,
                                  int[] iInOutDataLen, byte[] bInOutData) {

        int ret = 0;
        int[] rsaModeLen = {0x0100};
        byte bIndex[] = {0x00};
        int iKeyUsage[] = {0x00}; // 仅用于填充
        byte[] bKeyName = String2Bytes(strKeyName);
        ret = Dmt_Check_RSA_Name(bKeyName, bKeyName.length, bIndex, iKeyUsage);
        if (ret != 0) {
            //LOGGER.error("privateKeyDecByRSA function" + ret);
            return PIN_RC_RSA_Decrypt_Error;
        }

        ret = Dmt_RSA_Crypto(bInOutData, bInOutData, 1, bIndex[0], iInOutDataLen);
        if (ret != 0) {
            //LOGGER.error("privateKeyDecByRSA function:PIN_RC_RSA_Decrypt_Error");
            return PIN_RC_RSA_Decrypt_Error;
        }

        switch ((int) lSignAlg) {
            case 0x00: {
                //LOGGER.error("privateKeyDecByRSA function:PIN_RC_RSA_SIG_NOT_SUPP");
                ret = PIN_RC_RSA_SIG_NOT_SUPP;
                break;
            }
            case 0x01: {
                byte[] tmpOutData = Dmt_PKCS1v1_5Remove_Padding(bInOutData, 0, rsaModeLen[0] * 8,
                        iInOutDataLen[0], (byte) lHashMode, true);
                System.arraycopy(tmpOutData, 0, bInOutData, 0, tmpOutData.length);
                iInOutDataLen[0] = tmpOutData.length;
                break;
            }
            case 0x02: {
                //LOGGER.error("privateKeyDecByRSA function:PIN_RC_RSA_SIG_NOT_SUPP");
                ret = PIN_RC_RSA_SIG_NOT_SUPP;
                break;
            }
        }

        return (ret == 0 ? 0 : PIN_RC_RSA_Decrypt_Error);
    }


    @Override
    public int privateKeySignByRSA(String strKeyName, long lSignAlg, long lHashMode,
                                   int[] iInOutDataLen, byte[] bInOutData) {

        int ret = 0;
        int[] rsaModeLen = {0x0100};
        byte bIndex[] = {0x00};
        int iKeyUsage[] = {0x00}; // 仅用于填充
        byte[] bKeyName = String2Bytes(strKeyName);
        ret = Dmt_Check_RSA_Name(bKeyName, bKeyName.length, bIndex, iKeyUsage);
        if (ret != 0) {
            //LOGGER.error("privateKeySignByRSA function" + ret);
            return PIN_RC_RSA_SIGNATUREINVALID;
        }

        switch ((int) lSignAlg) {
            case 0x00: {
                //LOGGER.error("privateKeySignByRSA function:PIN_RC_RSA_SIG_NOT_SUPP");
                ret = PIN_RC_RSA_SIG_NOT_SUPP;
                break;
            }
            case 0x01: {
                switch ((int) lHashMode) {
                    case 0x00: {
                        //LOGGER.error("privateKeySignByRSA function:PIN_RC_Hash_Algorithm_NotSupport");
                        ret = PIN_RC_Hash_Algorithm_NotSupport;
                        break;
                    }
                    case 0x01: {
                        byte[] dataSHA1 = getSHA1(bInOutData, 0, iInOutDataLen[0]);
                        iInOutDataLen[0] = dataSHA1.length;
                        byte[] dataPadding1 = Dmt_PKCS1v1_5Padding(dataSHA1, 0, rsaModeLen[0] * 8, iInOutDataLen[0], (byte) lHashMode, false);
                        iInOutDataLen[0] = dataPadding1.length;
                        ret = Dmt_RSA_Crypto(bInOutData, dataPadding1, 1, bIndex[0], iInOutDataLen);
                        if (ret != 0) {
                            //LOGGER.error("privateKeySignByRSA function" + ret);
                            return PIN_RC_RSA_SIGNATUREINVALID;
                        }
                        break;
                    }
                    case 0x02: {
                        byte[] dataSHA256 = getSHA256(bInOutData, 0, iInOutDataLen[0]);
                        iInOutDataLen[0] = dataSHA256.length;
                        byte[] dataPadding256 = Dmt_PKCS1v1_5Padding(dataSHA256, 0, rsaModeLen[0] * 8, iInOutDataLen[0], (byte) lHashMode, false);
                        iInOutDataLen[0] = dataPadding256.length;
                        ret = Dmt_RSA_Crypto(bInOutData, dataPadding256, 1, bIndex[0], iInOutDataLen);
                        if (ret != 0) {
                            //LOGGER.error("privateKeySignByRSA function" + ret);
                            return PIN_RC_RSA_SIGNATUREINVALID;
                        }
                        break;
                    }
                    default: {
                        //LOGGER.error("publicKeyEncByRSA function:PIN_RC_RSA_SIG_NOT_SUPP");
                        ret = PIN_RC_CRYPT_INVALID_HASH;
                    }
                }
                break;
            }
            case 0x02: {
                //LOGGER.error("privateKeySignByRSA function:PIN_RC_RSA_SIG_NOT_SUPP");
                ret = PIN_RC_RSA_SIG_NOT_SUPP;
                break;
            }
        }

        return (ret == 0 ? 0 : PIN_RC_RSA_SIGNATUREINVALID);
    }


    @Override
    public int publicKeyVerifyByRSA(String strKeyName, long lSignAlg, long lHashMode, int iRawDataLen,
                                    byte[] bRawData, int[] iSignDataLen, byte[] bSignData) {

        int ret = 0;
        int[] rsaModeLen = {0x0100};
        byte bIndex[] = {0x00};
        int iKeyUsage[] = {0x00}; // 仅用于填充
        byte[] bKeyName = String2Bytes(strKeyName);
        ret = Dmt_Check_RSA_Name(bKeyName, bKeyName.length, bIndex, iKeyUsage);
        if (ret != 0) {
            //LOGGER.error("publicKeyVerifyByRSA function" + ret);
            return PIN_RC_RSA_Verify_Error;
        }

        switch ((int) lSignAlg) {
            case 0x00: {
                //LOGGER.error("publicKeyVerifyByRSA function:PIN_RC_RSA_SIG_NOT_SUPP/0x0055");
                ret = PIN_RC_RSA_SIG_NOT_SUPP;
                break;
            }
            case 0x01: {
                switch ((int) lHashMode) {
                    case 0x00: {
                        //LOGGER.error("publicKeyVerifyByRSA function:PIN_RC_Hash_Algorithm_NotSupport/0x0041");
                        ret = PIN_RC_Hash_Algorithm_NotSupport;
                        break;
                    }
                    case 0x01: {
                        byte[] RawDataSHA1 = getSHA1(bRawData, 0, iRawDataLen);
                        ret = Dmt_RSA_Crypto(bSignData, bSignData, 0, bIndex[0], iSignDataLen);
                        if (ret != 0) {
                            //LOGGER.error("publicKeyVerifyByRSA function" + ret);
                            return PIN_RC_RSA_Verify_Error;
                        }
                        byte[] raw = Dmt_PKCS1v1_5Remove_Padding(bSignData, 0, rsaModeLen[0] * 8, iSignDataLen[0], (byte) lHashMode, false);

                        if (!Arrays.equals(RawDataSHA1, raw)) {
                            //LOGGER.error("publicKeyVerifyByRSA function:PIN_RC_RSA_R_BAD_SIGNATURE");
                            return PIN_RC_RSA_Verify_Error;
                        }
                        break;
                    }
                    case 0x02: {
                        byte[] RawDataSHA256 = getSHA256(bSignData, 0, iRawDataLen);
                        ret = Dmt_RSA_Crypto(bSignData, bSignData, 0, bIndex[0], iSignDataLen);
                        if (ret != 0) {
                            //LOGGER.error("publicKeyVerifyByRSA function" + ret);
                            return PIN_RC_RSA_Verify_Error;
                        }
                        byte[] raw2 = Dmt_PKCS1v1_5Remove_Padding(bSignData, 0, rsaModeLen[0] * 8, iSignDataLen[0], (byte) lHashMode, false);
                        if (!Arrays.equals(RawDataSHA256, raw2)) {
                            //LOGGER.error("publicKeyVerifyByRSA function:PIN_RC_RSA_R_BAD_SIGNATURE");
                            return PIN_RC_RSA_Verify_Error;
                        }
                        break;
                    }
                }
                break;
            }
            case 0x02: {
                //LOGGER.error("publicKeyVerifyByRSA function:PIN_RC_RSA_SIG_NOT_SUPP/0x0055");
                ret = PIN_RC_RSA_SIG_NOT_SUPP;
                break;
            }
        }

        //LOGGER.info("publicKeyVerifyByRSA function end");
        return (ret == 0 ? 0 : PIN_RC_RSA_Verify_Error);
    }

    private int Dmt_Set_RSA_Name(byte[] bKeyName, int iKeyNameLen, byte[] bIndex, long[] lKeyUsage) {

        byte[] bOutBuf = {(byte) 0xFF, 0x00, 0x00, 0x00, 0x00};
        int ret = Dmt_SetAndCheck_Name(bOutBuf, bKeyName, iKeyNameLen, (byte) 0x01, lKeyUsage[0]);
        if (ret != 0) {
            //LOGGER.error("Dmt_Set_RSA_Name:" + ret);
            return ret;
        }

        bIndex[0] = bOutBuf[0];
        int flag = 0;
        flag |= (bOutBuf[1] << 24);
        flag |= (bOutBuf[2] << 16);
        flag |= (bOutBuf[3] << 8);
        flag |= bOutBuf[4];
        lKeyUsage[0] = flag;

        return 0;
    }

    private int Dmt_Check_RSA_Name(byte[] bKeyName, int iKeyNameLen, byte[] bIndex, int[] iKeyUsage) {
        //LOGGER.info("Call Dmt_Check_RSA_Name function");
        byte[] bOutBuf = new byte[]{(byte) 0xFF, 0x00, 0x00, 0x00, 0x00};
        int ret = Dmt_SetAndCheck_Name(bOutBuf, bKeyName, iKeyNameLen, (byte) 0x00, iKeyUsage[0]);
        if (ret != 0) {
            //LOGGER.error("Dmt_Check_RSA_Name" + ret);
            return ret;
        }

        bIndex[0] = bOutBuf[0];
        int flag = 0;
        flag |= (bOutBuf[1] << 24);
        flag |= (bOutBuf[2] << 16);
        flag |= (bOutBuf[3] << 8);
        flag |= bOutBuf[4];
        iKeyUsage[0] = flag;

        return 0;
    }

    private int Dmt_SetAndCheck_Name(byte[] bOutBuf, byte[] bKeyName,
                                     int iKeyNameLen, byte bIndex, long lKeyUsage) {

        int sendLen = 0;
        byte[] bNameBuf = new byte[DMT_NAME_UNIVERSAL_LEN + DMT_SM2_KEYUSAGE_LEN];
        if (iKeyNameLen > DMT_NAME_UNIVERSAL_LEN) {
            //LOGGER.error("Dmt_SetAndCheck_Name:PIN_RC_INVALID_PARAM");
            return PIN_RC_INVALID_PARAM;
        }
        System.arraycopy(bKeyName, 0, bNameBuf, 0, iKeyNameLen);
        byte[] tmpBuf = new byte[PACKET_SIZE + 32 + DMT_CRC_LEN];

        if (bIndex == 0x01) {
            int iKeyUsage = (int) lKeyUsage;
            bNameBuf[24] = (byte) (iKeyUsage >> 24);
            bNameBuf[25] = (byte) (iKeyUsage >> 16);
            bNameBuf[26] = (byte) (iKeyUsage >> 8);
            bNameBuf[27] = (byte) iKeyUsage;
            sendLen = 28;
            Dmt_Command_RSA_Name(tmpBuf, bNameBuf, bIndex, sendLen);
        } else {
            sendLen = 24;
            Dmt_Command_RSA_Name(tmpBuf, bNameBuf, bIndex, sendLen);
        }

        if (Dmt_Send_Total_Data(tmpBuf, PACKET_SIZE + sendLen + DMT_CRC_LEN) != 0) {
            //LOGGER.error("Dmt_SetAndCheck_Name:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpBuf, PACKET_SIZE + 5 + DMT_CRC_LEN) != 0) {
            //LOGGER.error("Dmt_SetAndCheck_Name:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        int ret = Dmt_Response_RSA_Name(tmpBuf, bOutBuf);
        return ret;
    }

    private byte[] Dmt_RSA_Pub_Key(byte[] n, byte[] e) throws Exception {

        RSAPublicKey rsaPublicKey = createPubKey(new BigInteger(n), new BigInteger(e));
        ASN1EncodableVector v = new ASN1EncodableVector();
        ASN1Integer modules = new ASN1Integer(rsaPublicKey.getModulus());
        ASN1Integer publicExponent = new ASN1Integer(rsaPublicKey.getPublicExponent());

        v.add(modules);
        v.add(publicExponent);

        DERSequence out = new DERSequence(v);
        return out.getEncoded();
    }


    private RSAPublicKey createPubKey(BigInteger n, BigInteger e)
            throws NoSuchAlgorithmException, InvalidKeySpecException {

        RSAPublicKeySpec rsaPublicKeySpec = new RSAPublicKeySpec(n, e);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        return (RSAPublicKey) keyFactory.generatePublic(rsaPublicKeySpec);
    }

    private SecureRandom random = CryptoServicesRegistrar.getSecureRandom();

    private static byte[] OID_SHA1 = {
            (byte) 0x30, (byte) 0x21, (byte) 0x30, (byte) 0x09, (byte) 0x06,
            (byte) 0x05, (byte) 0x2b, (byte) 0x0e, (byte) 0x03, (byte) 0x02,
            (byte) 0x1a, (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x14};

    private static byte[] OID_SHA256 = {
            (byte) 0x30, (byte) 0x31, (byte) 0x30, (byte) 0x0d, (byte) 0x06,
            (byte) 0x09, (byte) 0x60, (byte) 0x86, (byte) 0x48, (byte) 0x01,
            (byte) 0x65, (byte) 0x03, (byte) 0x04, (byte) 0x02, (byte) 0x01,
            (byte) 0x05, (byte) 0x00, (byte) 0x04, (byte) 0x20};

    /**
     * PKCS5V1_5填充
     *
     * @param input      isenc为true时为加密数据，isenc为false时为签名hash值
     * @param inOff
     * @param rsaModeLen 支持 1024和2048
     * @param inLen      input长度
     * @param bHashMode  支持SHA1和SHA256
     * @param isenc      加密或签名填充
     * @return
     */
    private byte[] Dmt_PKCS1v1_5Padding(byte[] input, int inOff, int rsaModeLen, int inLen,
                                        byte bHashMode, boolean isenc) {
        byte[] block;
        if (inLen > 117) {
            throw new IllegalArgumentException("input data too large");
        }
        if (rsaModeLen == RSA_N_LEN_1024) {
            block = new byte[128];
        } else {
            block = new byte[256];
        }
        block[0] = 0x00;
        // 加解密 0x00||0x02||PS||0x00||M :其中 PS为填充0xFF，M为有效数据
        // M为用户加密数据
        if (isenc) {
            block[1] = 0x02; // type code 1
            for (int i = 2; i < block.length - inLen - 1; i++) {
                while (block[i] == 0) {
                    block[i] = (byte) random.nextInt();
                }
            }
            block[block.length - inLen - 1] = 0x00; // mark the end of the padding
        } else {
            //签名验签 0x00||0x01||PS||0x00||M :其中 PS为填充的随机数如可取 0xFF，M为有效数据
            //M为 hash的 OID ASN.1编码 + 原始数据HASH计算的值
            int tmp = inLen;
            if (bHashMode == 0x01) {
                inLen += OID_SHA1.length;
            } else {
                inLen += OID_SHA256.length;
            }

            block[1] = 0x01; // type code 2
            // a zero byte marks the end of the padding, so all
            // the pad bytes must be non-zero.
            for (int i = 2; i < block.length - inLen - 1; i++) {
                block[i] = (byte) 0xFF;
            }
            block[block.length - inLen - 1] = 0x00; // mark the end of the padding

            if (bHashMode == 0x01) {
                System.arraycopy(OID_SHA1, 0, block, block.length - inLen, OID_SHA1.length);
            } else {
                System.arraycopy(OID_SHA256, 0, block, block.length - inLen, OID_SHA256.length);
            }
            inLen = tmp;
        }
        System.arraycopy(input, inOff, block, block.length - inLen, inLen);
        return block;
    }

    /**
     * PKCS5V1_5去填充
     *
     * @param input      isenc为true时为加密数据，isenc为false时为签名hash值
     * @param inOff
     * @param rsaModeLen 支持1024和2048
     * @param inLen      input长度
     * @param bHashMode  支持SHA1和SHA256
     * @param isenc      加密或签名填充
     * @return
     */
    private static byte[] Dmt_PKCS1v1_5Remove_Padding(byte[] input, int inOff, int rsaModeLen,
                                                      int inLen, byte bHashMode, boolean isenc) {
        byte[] block;
        int j = 0; //随机数的长度
        int offLen = 0;
        int mLen = 0;
        for (int i = 2; i < inLen; i++) {
            if (input[i] == 0) {
                break;
            } else {
                j++;
            }
        }
        if (isenc) {
            if (rsaModeLen == RSA_N_LEN_1024) {
                mLen = RSA_N_LEN_1024 - 3 - j;
            } else {
                mLen = RSA_N_LEN_2048 - 3 - j;
            }
            offLen = 3 + j;
        } else {
            if (rsaModeLen == RSA_N_LEN_1024) {
                if (bHashMode == 0x01) {
                    mLen = RSA_N_LEN_1024 - 3 - j - OID_SHA1.length;
                    offLen = 3 + j + OID_SHA1.length;
                } else {
                    mLen = RSA_N_LEN_1024 - 3 - j - OID_SHA256.length;
                    offLen = 3 + j + OID_SHA256.length;
                }
            } else {
                if (bHashMode == 0x01) {
                    mLen = RSA_N_LEN_2048 - 3 - j - OID_SHA1.length;
                    offLen = 3 + j + OID_SHA1.length;
                } else {
                    mLen = RSA_N_LEN_2048 - 3 - j - OID_SHA256.length;
                    offLen = 3 + j + OID_SHA256.length;
                }
            }
        }
        block = new byte[mLen];
        System.arraycopy(input, offLen, block, 0, mLen);
        return block;
    }

    private byte[] getSHA1(byte[] in, int inOff, int inLen) {
        SHA1Digest digester = new SHA1Digest();
        byte[] retValue = new byte[digester.getDigestSize()];
        System.out.println(retValue.length);
        digester.update(in, inOff, inLen);
        digester.doFinal(retValue, 0);
        return retValue;
    }

    private byte[] getSHA256(byte[] in, int inOff, int inLen) {
        SHA256Digest digester = new SHA256Digest();
        byte[] retValue = new byte[digester.getDigestSize()];
        System.out.println(retValue.length);
        digester.update(in, inOff, inLen);
        digester.doFinal(retValue, 0);
        return retValue;
    }


    private static ASN1Sequence byteArrayToASN1Sequence(byte[] data) throws IOException {
        ByteArrayInputStream bIn = new ByteArrayInputStream(data);
        ASN1InputStream aIn = new ASN1InputStream(bIn);
        return (ASN1Sequence) aIn.readObject();
    }
    /*****************************RSA END*******************************/

    /*****************************SM4 Start*******************************/

//    @Override
//    public int Dmt_Set_SymmetryMKey(byte bKeyIndex, byte[] bMkey) {
//
//        byte[] tmpbuf = new byte[PACKET_SIZE + SM4_MKEY_SIZE + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];
//        int ret = 0;
//        if (bKeyIndex > 5) {
//            //LOGGER.error("Dmt_Set_SymmetryMKey:PIN_RC_INVALID_PARAM");
//            return PIN_RC_INVALID_PARAM;
//        }
//        Dmt_Command_Set_SymmetryMKey(tmpbuf, bKeyIndex, bMkey);
//        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + SM4_MKEY_SIZE + DMT_CRC_LEN ) != 0) {
//            //LOGGER.error("Dmt_Set_SymmetryMKey:PIN_RC_Send_Error");
//            return PIN_RC_Send_Error;
//        }
//        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE) != 0) {
//            //LOGGER.error("Dmt_Set_SymmetryMKey:PIN_RC_Receive_Error");
//            return PIN_RC_Receive_Error;
//        }
//        ret = Dmt_Response_Set_SymmetryMKey(tmpbuf);
//        return ret;
//    }


    // 本SM4接口仅用于长城信息&科融电子
    @Override
    public int Dmt_SM4_TUSN(byte bIndex, byte[] bInBuf, byte[] bOutBuf) {

        byte[] tmpbuf = new byte[PACKET_SIZE + 10 + DMT_CRC_LEN + DMT_SPI_HIGHSPEED_DATA];

        Dmt_Command_SM4_TUSN(tmpbuf, bIndex, bInBuf, 6);

        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + 6 + DMT_CRC_LEN) != 0) {
            //LOGGER.error("Dmt_SM4_TUSN:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + 8 + DMT_CRC_LEN) != 0) {
            //LOGGER.error("Dmt_SM4_TUSN:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        int ret = Dmt_Response_SM4_TUSN(tmpbuf, bOutBuf);
        return ret;
    }


    @Override
    public int getPinBlockByName(String strKeyName, long lPinFormat, byte byteDataLen,
                                 byte[] bCustomerData, byte bytePadChar, byte[] bPinBlock) {

        byte[] bStartValue = {
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00,
                (byte) 0x00, (byte) 0x00, (byte) 0x00, (byte) 0x00
        };

        int recv_len = 0;
        byte[] bMode = new byte[1];
        getAlgMode(bMode);
        if (bMode[0] == 1) {
            recv_len = byteDataLen % 16;
            if (recv_len != 0) {
                recv_len = byteDataLen + 16 - recv_len;
            } else {
                recv_len = byteDataLen;
            }
        } else {
            recv_len = byteDataLen % 8;
            if (recv_len != 0) {
                recv_len = byteDataLen + 8 - recv_len;
            } else {
                recv_len = byteDataLen;
            }
        }

        byte[] tmpbuf = new byte[PACKET_SIZE + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN +
                DMT_SPI_HIGHSPEED_DATA + DMT_CRC_LEN + byteDataLen];

        Dmt_Command_PinBlockByName(tmpbuf, 0, strKeyName, bCustomerData, byteDataLen, bStartValue, bytePadChar);

        if (Dmt_Send_Total_Data(tmpbuf, PACKET_SIZE + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN +
                DMT_CRC_LEN + byteDataLen) != 0) {
            //LOGGER.error("encryptByName:PIN_RC_Send_Error");
            return PIN_RC_Send_Error;
        }
        if (Dmt_Recv_Total_Data(tmpbuf, PACKET_SIZE + DMT_CRC_LEN + recv_len) != 0) {
            //LOGGER.error("encryptByName:PIN_RC_Receive_Error");
            return PIN_RC_Receive_Error;
        }
        int[] iOutDataLen = new int[1];
        int ret = Dmt_Response_Cryto(tmpbuf, bPinBlock, iOutDataLen);

        return ret;
    }

    /*****************************SM4 END*******************************/

    private static final int STATUS_ERR_HANDLE = 0x8103;
    private static int COMM_TRANS = 0x00;
    private Context context = null;
    private USB_Driver usb = null;
    private UART_Driver uart = null;
    private SPI_Driver spi = null;
    private I2C i2c = null;

    public KeyBoard(Context context) {
        this.context = context;
    }

    @Override
    public void initComm(byte bType) {
        COMM_TRANS = bType;
    }

    @Override
    public int openUsb(int vid, int pid) {

        if (usb != null) {
            closeUsb();
        }

        usb = new USB_Driver(context);
        if (usb == null) {
            return STATUS_ERR_HANDLE; // USB 创建失败
        }

        int ret = usb.USB_Device_Register(vid, pid);
        if (ret != 0) {
            return ret;
        }

        ret = usb.USB_OpenDevice(0);
        if (ret != 0) {
            return ret; // USB 打开失败
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        return 0;
    }

    @Override
    public int closeUsb() {

        if (usb == null) {
            return STATUS_ERR_HANDLE;
        }
        int ret = usb.USB_CloseDevice(0);
        if (ret != 0) {
            return ret; // USB 关闭失败
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        usb = null;
        return 0;
    }

    @Override
    public int getUsbStatus() {

        if (usb == null) {
            return STATUS_ERR_HANDLE;
        }
        return usb.GetCount() > 0 ? 0 : 1;
    }


    /**
     * 打开 UART设备
     * @param port
     * @return
     *
     * 备注：多线程创建不安全
     */
    @Override
    public int openUart(String port) {

        if (uart != null) {
            closeUart();
        }

        uart = new UART_Driver(port);
        if (uart == null) {
            return STATUS_ERR_HANDLE; // UART 创建失败
        }

        int ret = uart.Open();
        if (ret != 0) {
            return ret; // UART 打开失败
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        return 0;
    }

    /**
     * 关闭 UART
     * @return
     */
    @Override
    public int closeUart() {

        if (uart == null) {
            return STATUS_ERR_HANDLE;
        }

        int ret = uart.Close();
        if (ret != 0) {
            return  -1; // UART 关闭失败
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        uart = null;
        return 0;
    }


    @Override
    public int openI2c(String strI2cFileName) {

        if (i2c != null) {
            closeI2c();
        }

        i2c = new I2C();
        if (i2c == null) {
            return STATUS_ERR_HANDLE; // I2C 创建失败
        }

        int ret = i2c.Dmt_Open_I2C(strI2cFileName);
        if (ret != 0) {
            return ret; // I2C 打开失败
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        return 0;
    }


    @Override
    public int closeI2c() {

        if (i2c == null) {
            return STATUS_ERR_HANDLE;
        }

        int ret = i2c.Dmt_Close_I2C();
        if (ret != 0) {
            return  -1;
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        i2c = null;
        return 0;
    }

    @Override
    public int openSpi(String strSpiFileName) {
        if (spi != null) {
            closeSpi();
        }

        spi = new SPI_Driver();
        if (spi == null) {
            return STATUS_ERR_HANDLE; // SPI 创建失败
        }

        int ret = spi.Dmt_Open_SPI(strSpiFileName);
        if (ret != 0) {
            return ret; // SPI 打开失败
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        return 0;
    }

    @Override
    public int closeSpi() {

        if (spi == null) {
            return STATUS_ERR_HANDLE;
        }

        int ret = spi.Dmt_Close_SPI();
        if (ret != 0) {
            return  -1;
        }

        try {
            Thread.sleep(50);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }

        spi = null;
        return 0;
    }

    private int Dmt_Send_Total_Data(byte[] txbuf, int size) {
        int ret = 0;

        if (COMM_TRANS == 0x00) {
            ret = usb.USB_Port_Send(txbuf, PACKET_SIZE);
        }
        if (COMM_TRANS == 0x01){
            ret = uart.Send(txbuf, PACKET_SIZE);
        }
        if (COMM_TRANS == 0x02) {
            ret = i2c.Dmt_I2c_Send(txbuf, PACKET_SIZE);
        }
        if (COMM_TRANS == 0x03) {
            ret = spi.Dmt_SPI_Send(txbuf, PACKET_SIZE);
        }

        if (ret != 0) {
            //LOGGER.error("Dmt_Send_Total_Data:head fail" + ret);
            return ret;
        }

        // 中间延时 20ms
        try {
            Thread.sleep(20);
        } catch (InterruptedException interruptedException) {
            interruptedException.printStackTrace();
        }


        int payload_size = size - PACKET_SIZE;
        if (payload_size > 0) {
            byte[] payload = new byte[payload_size];
            System.arraycopy(txbuf, PACKET_SIZE, payload, 0, payload_size);

            if (COMM_TRANS == 0x00) {
                ret = usb.USB_Port_Send(payload, payload_size);
            }
            if (COMM_TRANS == 0x01){
                ret = uart.Send(payload, payload_size);
            }
            if (COMM_TRANS == 0x02) {
                ret = i2c.Dmt_I2c_Send(payload, payload_size);
            }
            if (COMM_TRANS == 0x03) {
                ret = spi.Dmt_SPI_Send(txbuf, PACKET_SIZE);
            }

            if (ret != 0) {
                //LOGGER.error("Dmt_Send_Total_Data:payload fail" + ret);
                return ret;
            }
        }
        return 0;

    }

    private int Dmt_Recv_Total_Data(byte[] buffer, int totalsize) {
        int ret = 0;
        byte[] head = new byte[PACKET_SIZE];

        if (COMM_TRANS == 0x00) {
            ret = usb.USB_Port_Receive(head, PACKET_SIZE);
        }
        if (COMM_TRANS == 0x01){
            ret = uart.Receive(head, PACKET_SIZE);
        }
        if (COMM_TRANS == 0x02){
            ret = i2c.Dmt_I2c_Receive(head, PACKET_SIZE);
        }
        if (COMM_TRANS == 0x03){
            ret = i2c.Dmt_I2c_Receive(head, PACKET_SIZE);
        }
        if (ret != 0) {
            //LOGGER.error("Dmt_Recv_Total_Data:head fail" + ret);
            return PIN_RC_Receive_Error;
        }
        
        System.arraycopy(head, 0, buffer, 0, PACKET_SIZE);
        int flag = head[2] << 8 | head[3];
        int payLoadLen = head[4] << 8 | head[5] & 0xFF;
        if (flag == 0 && payLoadLen > 0 && totalsize > PACKET_SIZE) {
            if (payLoadLen > totalsize - PACKET_SIZE) {
                payLoadLen = totalsize - PACKET_SIZE;
            }
            byte[] payload = new byte[payLoadLen];
            if (COMM_TRANS == 0x00) {
                ret = usb.USB_Port_Receive(payload, payLoadLen);
            }
            if (COMM_TRANS == 0x01){
                ret = uart.Receive(payload, payLoadLen);
            }
            if (COMM_TRANS == 0x02){
                ret = i2c.Dmt_I2c_Receive(payload, payLoadLen);
            }
            if (COMM_TRANS == 0x03){
                ret = i2c.Dmt_I2c_Receive(head, PACKET_SIZE);
            }
            if (ret != 0) {
                //LOGGER.error("Dmt_Recv_Total_Data:payload fail" + ret);
                return PIN_RC_Receive_Error;
            }
            System.arraycopy(payload, 0, buffer, PACKET_SIZE, payLoadLen);
        }
        return 0;

    }
}
