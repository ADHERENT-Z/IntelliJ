package com.dtt.busdrv;

import com.dtt.KeyBoardSDK.StatusCode;

import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.util.Arrays;

public class KeyBoradProtocol implements StatusCode, ProtocolCMDCode {


    private static final char[] HEX_DIGITS_UPPER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    private static final char[] HEX_DIGITS_LOWER =
            {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};

    /**
     * 协议包头
     * @param cmdbuf
     * @param cmd
     * @param p1
     * @param p2
     * @param payload_len
     */
    private void CMD_PKT(byte[] cmdbuf, int cmd, int p1, int p2, int payload_len) {
        cmdbuf[0] = COMMAND_HEAD_VALUE;
        cmdbuf[1] = (byte)(cmd & 0xFF);
        cmdbuf[2] = (byte)(p1 & 0xFF);
        cmdbuf[3] = (byte)(p2 & 0xFF);
        cmdbuf[4] = (byte)(payload_len >> 8 & 0xFF);
        cmdbuf[5] = (byte)(payload_len & 0xFF);
        cmdbuf[6] = Calc_XOR(cmdbuf, 6);
    }

    private int Check_Response_Packet(byte[] rspbuf, int cmd, int payload_len) {
        int status = rspbuf[2] << 8 | rspbuf[3] & 0xFF;
        if (rspbuf[0] == RESPONSE_HEAD_VALUE) {
            if (rspbuf[1] == (byte)(cmd & 0xFF)) {
                if (rspbuf[6] == Calc_XOR(rspbuf, 6)) {
                    if (payload_len > DMT_CRC_LEN) {
                        int RspDataLen = payload_len - DMT_CRC_LEN;
                        int payload_crc = rspbuf[PACKET_SIZE + RspDataLen] << 8 & 0xFF00 | rspbuf[PACKET_SIZE + RspDataLen + 1] & 0xFF;
                        if (payload_crc != calcrc16(CRC_INIT_VALUE, rspbuf, RspDataLen)) {
                            status = PIN_RC_Command_Error;
                        }
                    }
                } else {
                    return statusAdapter(status);
                }
            } else {
                return statusAdapter(status);
            }
        } else {
            return statusAdapter(status);
        }

        return statusAdapter(status);
    }

    public void Dmt_Command_Soft_Reset(byte[] cmdbuf) {
        CMD_PKT(cmdbuf, CMD_SYSTEM_SOFT_RESET, 0, 0, 0);
    }

    public int Dmt_Response_Soft_Reset(byte[] rspbuf) {
        return Check_Response_Packet(rspbuf, CMD_SYSTEM_SOFT_RESET, 0);
    }

    public void Dmt_Command_Issue_Config(byte[] cmdbuf) {
        CMD_PKT(cmdbuf, CMD_ISSUE_CONFIG, 0, 0, 0);
    }

    int Dmt_Response_Issue_Config(byte[] rspbuf) {
        return Check_Response_Packet(rspbuf, CMD_ISSUE_CONFIG, 0);
    }

    void Dmt_Command_Get_Random(byte[] cmdbuf, int rndsize) {
        CMD_PKT(cmdbuf, CMD_GET_RANDOM, rndsize >> 8 & 0xFF, rndsize & 0xFF, 0);
    }

    int Dmt_Response_Get_Random(byte[] rspbuf, int[] inOutDataLen, byte[] bRndBuf) {
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_GET_RANDOM, payload_len);
        if (status == PIN_RC_SUCCESS) {
            System.arraycopy(rspbuf, PACKET_SIZE, bRndBuf, 0, payload_len - DMT_CRC_LEN);
            inOutDataLen[0] = payload_len - DMT_CRC_LEN;
        }
        return status;
    }

    public void Dmt_Command_Get_ChipFirmwareVersion(byte[] cmdbuf) {
        CMD_PKT(cmdbuf, CMD_GET_CHIP_FIRMWARE_VERSION, 0, 0, 0);
    }

    public int Dmt_Response_Get_ChipFirmwareVersion(byte[] rspbuf, byte[] bChipFirmwareVersion) {
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_GET_CHIP_FIRMWARE_VERSION, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == DMT_CHIP_FIRMWAREVERSION_SIZE) {
                System.arraycopy(rspbuf, PACKET_SIZE, bChipFirmwareVersion, 0, DMT_CHIP_FIRMWAREVERSION_SIZE);
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_Get_ChipFirmwareBuild(byte[] cmdbuf) {
        CMD_PKT(cmdbuf, CMD_GET_CHIP_FIRMWARE_BUILD_VERSION, 0, 0, 0);
    }

    public int Dmt_Response_Get_ChipFirmwareBuild(byte[] rspbuf, byte[] bChipFirmwareBuild) {
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_GET_CHIP_FIRMWARE_BUILD_VERSION, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == DMT_CHIP_FIRMWAREBUILD_SIZE) {
                System.arraycopy(rspbuf, PACKET_SIZE, bChipFirmwareBuild, 0, DMT_CHIP_FIRMWAREBUILD_SIZE);
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_GetKeyDetailEx(byte[] cmdbuf, byte bKeyNo){
        CMD_PKT(cmdbuf, CMD_GET_KEYDETAIL, bKeyNo, 0, 0);
    }

    public int Dmt_Response_GetKeyDetailEx(byte[] rspbuf, byte[] bRetKeyNo, byte[] bParentNo, long[] lKeyUsage,
                                           byte[] bKeyName, int[] iKeyLength){
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_GET_KEYDETAIL, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == 55 * 16) {
                byte[] bKeyUsage = new byte[32];
                for (int i = 0; i < 0x0F; i++){
                    bRetKeyNo[i] = rspbuf[PACKET_SIZE + 55 * i]; // 0
                    bParentNo[i] = rspbuf[PACKET_SIZE + 1 + 55 * i]; // 1
                    System.arraycopy(rspbuf, PACKET_SIZE + 3 + 55 * i, bKeyName, 24 * i, 24); // 2,3-26//27,28-52
                    System.arraycopy(rspbuf, PACKET_SIZE + 52 + 55 * i, bKeyUsage, 2 * i, 2); // 52-53
                    iKeyLength[i] = rspbuf[PACKET_SIZE + 54 + 55 * i]; // 54

                    for (int j = 0; j < bKeyUsage.length / 2; j++) {
                        byte[] tmpUsage = new byte[2];
                        System.arraycopy(bKeyUsage, i * 2, tmpUsage, 0, 2);
                        lKeyUsage[i] = byteArray2long(tmpUsage, 0);
                    }
                }
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_Get_AlgMode(byte[] cmdbuf){
        CMD_PKT(cmdbuf, CMD_GET_ALGMODE, 0x00, 0x00, 0);
    }

    public int Dmt_Response_Get_AlgMode(byte[] rspbuf, byte[] bMode) {
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_GET_ALGMODE, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == 1) {
                System.arraycopy(rspbuf, PACKET_SIZE, bMode, 0, 1);
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_Set_AlgMode(byte[] cmdbuf, byte mode){
        CMD_PKT(cmdbuf, CMD_SET_ALGMODE, mode, 0x00, 0);
    }

    public int Dmt_Response_Set_AlgMode(byte[] rspbuf) {
        return Check_Response_Packet(rspbuf, CMD_SET_ALGMODE, 0);
    }

    public void Dmt_Command_GetKeyUsage(byte[] cmdbuf, byte bKeyNo, String strKeyName){

        if (strKeyName.isEmpty()) {
            CMD_PKT(cmdbuf, CMD_GET_KEYUSAGE, bKeyNo, 0x00, 0);
        }else{
            CMD_PKT(cmdbuf, CMD_GET_KEYUSAGE, 0x00, 0x01, DMT_NAME_UNIVERSAL_LEN);
            byte[] bytesKeyName = String2Bytes(strKeyName);
            byte[] tmpKeyName = new byte[24];
            System.arraycopy(bytesKeyName, 0, tmpKeyName, 0, bytesKeyName.length);
            System.arraycopy(tmpKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);

            int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, DMT_NAME_UNIVERSAL_LEN);
            cmdbuf[PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
            cmdbuf[PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
        }
    }

    public int Dmt_Response_GetKeyUsage(byte[] rspbuf, byte[] bResult){
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_GET_KEYUSAGE, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == 2) {
                System.arraycopy(rspbuf, PACKET_SIZE, bResult, 0, 2);
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_DownLoad_KeyByNo(byte[] cmdbuf, byte bKeyNo, byte bPKeyNo, long[] lKeyUsage,
                                         int iKeyLength, byte[] bKeyData, byte bKCVMode){

        byte[] tmpdata = new byte[29];
        Arrays.fill(tmpdata, (byte) 0x00);

        tmpdata[0] = bKeyNo;
        tmpdata[1] = bPKeyNo;

        // 密钥的属性
        int iKeyUsage = (int) lKeyUsage[0]; // long2int
        byte[] bKeyUsage = int2byteArray(iKeyUsage, true); // int2byteArray(两字节)
        System.arraycopy(bKeyUsage, 0, tmpdata, 2, 2);

        // 密钥的长度和数据
        tmpdata[4] = (byte)iKeyLength;
        System.arraycopy(bKeyData, 0, tmpdata, 5, iKeyLength);

        /*
          p2 - bit7~4代表是秘钥下载方式, 0-以秘钥号方式; 1-秘钥名方式
               bit3~0代表密钥校验模式	, 0 – 无校验; 1 – 自身校验; 2 – 零校验
         */
        CMD_PKT(cmdbuf, CMD_DOWNLOAD_KEY, bKeyNo, bKCVMode, 29 + DMT_CRC_LEN);
        System.arraycopy(tmpdata, 0, cmdbuf, PACKET_SIZE, 29);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, 29);
        cmdbuf[PACKET_SIZE + 29] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + 29 + 1] = (byte) (payload_crc & 0xFF);

    }

    // 密钥名称下载
    public void Dmt_Command_DownLoad_Key(byte[] cmdbuf, byte bKeyNo, byte byName,
                                       String strKeyName, String strParentName, long[] lKeyUsage,
                                       int iKeyLength, byte[] bKeyData, byte bKCVMode){

        byte[] tmpdata = new byte[DOWN_KEY_SIZE];
        int iKeyNameLen = strKeyName.length();
        int iParentLen = strParentName.length();

        /*
          tmpdata[0] - key 的索引
          tmpdata[1] - Parentkey 索引
         */

        if (iKeyNameLen > 0) {
            // keyname 的长度(24字节)和名称
            tmpdata[2] = (byte) iKeyNameLen;
            byte[] bytesKeyName = String2Bytes(strKeyName);
            System.arraycopy(bytesKeyName, 0, tmpdata, 3, iKeyNameLen);
        }

        // Parentkeyname 的长度(24字节)和名称
        tmpdata[27] = (byte)iParentLen;

        if (iParentLen > 0) {
            byte[] bytesParentName = String2Bytes(strParentName);
            System.arraycopy(bytesParentName, 0, tmpdata, 28, iParentLen);
        }else{
            Arrays.fill(tmpdata, 28, 28 + DMT_NAME_UNIVERSAL_LEN, (byte) 0x00);
        }

        // 密钥的属性
        int iKeyUsage = (int) lKeyUsage[0]; // long2int
        byte[] bKeyUsage = int2byteArray(iKeyUsage, true); // int2byteArray(两字节)
        System.arraycopy(bKeyUsage, 0, tmpdata, 52, 2);

        // 密钥的长度和数据
        tmpdata[54] = (byte)iKeyLength;
        System.arraycopy(bKeyData, 0, tmpdata, 55, iKeyLength);

        /*
          p2 - bit7~4代表是秘钥下载方式, 0-以秘钥号方式; 1-秘钥名方式
               bit3~0代表密钥校验模式	, 0 – 无校验; 1 – 自身校验; 2 – 零校验
         */
//        CMD_PKT(cmdbuf, CMD_DOWNLOAD_KEY, bKeyNo, (byName << 4) + bKCVMode, DOWN_KEY_SIZE + DMT_CRC_LEN);
        CMD_PKT(cmdbuf, CMD_DOWNLOAD_KEY, bKeyNo, (1 << 4) + bKCVMode, DOWN_KEY_SIZE + DMT_CRC_LEN);
        System.arraycopy(tmpdata, 0, cmdbuf, PACKET_SIZE, DOWN_KEY_SIZE);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, DOWN_KEY_SIZE);
        cmdbuf[PACKET_SIZE + DOWN_KEY_SIZE] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + DOWN_KEY_SIZE + 1] = (byte) (payload_crc & 0xFF);

    }

    public int Dmt_Response_DownLoad_Key(byte[] rspbuf, int[] iKCVLen, byte[] bKCV){
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_DOWNLOAD_KEY, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len == 0) {
            	iKCVLen[0] = 0;
            } else if (payload_len - DMT_CRC_LEN == 16) {
                System.arraycopy(rspbuf, PACKET_SIZE, bKCV, 0, 16);
                iKCVLen[0] = 16;
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_Delete_Key(byte[] cmdbuf, String strKeyName, byte bKeyNo, byte bName, int iLen){

        if (iLen != 0) {
            /*
              p2 - bit7~4代表是秘钥下载方式, 0-以秘钥号方式; 1-秘钥名方式
                   bit3~0代表密钥校验模式	, 0 – 无校验; 1 – 自身校验; 2 – 零校验
             */
//            CMD_PKT(cmdbuf, CMD_DELETE_KEY, bKeyNo, bName, iLen);
            CMD_PKT(cmdbuf, CMD_DELETE_KEY, bKeyNo, bName, DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN);

            byte[] bytesKeyName = String2Bytes(strKeyName);
            if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
                byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
                System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
                System.arraycopy(tmpStrKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);
            }

            int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, DMT_NAME_UNIVERSAL_LEN);
            cmdbuf[PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
            cmdbuf[PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
        }else{
             CMD_PKT(cmdbuf, CMD_DELETE_KEY, bKeyNo, bName, 0);
        }
    }

    public int Dmt_Response_Delete_Key(byte[] rspbuf){
        return Check_Response_Packet(rspbuf, CMD_DELETE_KEY, 0);
    }

    public void Dmt_Command_CrytoByName(byte[] cmdbuf, int iEncMode, String strKeyName, byte[] bEncData,
                                        int iDataLen, byte[] bStartValue, byte bytePadChar){

        CMD_PKT(cmdbuf, CMD_ECB_CRYPTO, bytePadChar, (1 << 4) + iEncMode, SM4_BLOCK_SIZE +
                DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN + iDataLen);

        byte[] bytesKeyName = String2Bytes(strKeyName);
        if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
            byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
            System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
            System.arraycopy(tmpStrKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);
        }
        if (bStartValue.length > 0 && bStartValue.length <= SM4_BLOCK_SIZE){
            byte[] tmpStartValue = new byte[SM4_BLOCK_SIZE];
            System.arraycopy(bStartValue, 0, tmpStartValue, 0, bStartValue.length);
            System.arraycopy(tmpStartValue, 0, cmdbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN, SM4_BLOCK_SIZE);
        }

        System.arraycopy(bEncData, 0, cmdbuf, PACKET_SIZE + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN, iDataLen);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, iDataLen + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN);
        cmdbuf[PACKET_SIZE + iDataLen + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + iDataLen + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
    }

    public int Dmt_Response_Cryto(byte[] rspbuf, byte[] bEncData, int[] iDataLen){
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_ECB_CRYPTO, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN > 0) {
                System.arraycopy(rspbuf, PACKET_SIZE, bEncData, 0, payload_len - DMT_CRC_LEN);
                iDataLen[0] = payload_len - DMT_CRC_LEN;
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_PinBlockByName(byte[] cmdbuf, int iEncMode, String strKeyName, byte[] bEncData,
                                        int iDataLen, byte[] bStartValue, byte bytePadChar){

        CMD_PKT(cmdbuf, CMD_PINBLOCK, bytePadChar, (1 << 4) + iEncMode, SM4_BLOCK_SIZE +
                DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN + iDataLen);

        byte[] bytesKeyName = String2Bytes(strKeyName);
        if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
            byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
            System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
            System.arraycopy(tmpStrKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);
        }
        if (bStartValue.length > 0 && bStartValue.length <= SM4_BLOCK_SIZE){
            byte[] tmpStartValue = new byte[SM4_BLOCK_SIZE];
            System.arraycopy(bStartValue, 0, tmpStartValue, 0, bStartValue.length);
            System.arraycopy(tmpStartValue, 0, cmdbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN, SM4_BLOCK_SIZE);
        }

        System.arraycopy(bEncData, 0, cmdbuf, PACKET_SIZE + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN, iDataLen);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, iDataLen + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN);
        cmdbuf[PACKET_SIZE + iDataLen + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + iDataLen + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
    }

    public void Dmt_Command_CrytoByNo(byte[] cmdbuf, int iEncMode, byte bKeyNo, byte[] bEncData,
                                  int iDataLen, byte[] bStartValue){

        CMD_PKT(cmdbuf, CMD_ECB_CRYPTO, bKeyNo, iEncMode, SM4_BLOCK_SIZE + iDataLen);

        System.arraycopy(bStartValue, 0, cmdbuf, PACKET_SIZE, SM4_BLOCK_SIZE);
        System.arraycopy(bEncData, 0, cmdbuf, PACKET_SIZE + SM4_BLOCK_SIZE, iDataLen);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, DMT_NAME_UNIVERSAL_LEN);
        cmdbuf[PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
    }

    public void Dmt_Command_Mac_ByName(byte[] cmdbuf, byte[] bData, byte[] bStartValue, String strKeyName,
                                       byte bTransformMode, int iDataLen, byte bytePadChar){

        CMD_PKT(cmdbuf, CMD_MAC, bytePadChar, (1 << 4) + bTransformMode, SM4_BLOCK_SIZE +
                DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN + iDataLen);
        byte[] bytesKeyName = String2Bytes(strKeyName);
        if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
            byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
            System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
            System.arraycopy(tmpStrKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);
        }
        if (bStartValue.length > 0 && bStartValue.length <= SM4_BLOCK_SIZE){
            byte[] tmpStartValue = new byte[SM4_BLOCK_SIZE];
            System.arraycopy(bStartValue, 0, tmpStartValue, 0, bStartValue.length);
            System.arraycopy(tmpStartValue, 0, cmdbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN, SM4_BLOCK_SIZE);
        }
        System.arraycopy(bData, 0, cmdbuf, PACKET_SIZE + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN, iDataLen);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, iDataLen + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN);
        cmdbuf[PACKET_SIZE + iDataLen + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + iDataLen + SM4_BLOCK_SIZE + DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
    }

    public int Dmt_Response_Mac_ByName(byte[] rspbuf, byte[] bResult, long[] lResultLen){

        byte[] zero = new byte[8];
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_MAC, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == 16) {
                System.arraycopy(rspbuf, PACKET_SIZE, bResult, 0, 16);
                byte[] tmp = new byte[8];
                System.arraycopy(bResult, 8, tmp, 0, 8);
                if (Arrays.equals(zero, tmp)){
                    lResultLen[0] = payload_len - DMT_CRC_LEN - 8;
                }else {
                    lResultLen[0] = payload_len - DMT_CRC_LEN;
                }
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_GenSM2KeyPair(byte[] cmdbuf, String strKeyName, long lKeyUsage) {

        CMD_PKT(cmdbuf, CMD_SM2_GEN_KEYPAIR, 0, 0, DMT_NAME_UNIVERSAL_LEN + DMT_SM2_KEYUSAGE_LEN + DMT_CRC_LEN);

        byte[] bytesKeyName = String2Bytes(strKeyName);
        if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
            byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
            System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
            System.arraycopy(tmpStrKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);
        }

        int iKeyUsage = (int) lKeyUsage;
        byte[] bKeyUsage = int2byteArray(iKeyUsage,false);
        System.arraycopy(bKeyUsage, 0, cmdbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN, DMT_SM2_KEYUSAGE_LEN);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, 28);
        cmdbuf[PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_SM2_KEYUSAGE_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_SM2_KEYUSAGE_LEN + 1] = (byte) (payload_crc & 0xFF);
    }


    public int Dmt_Response_GenSM2KeyPair(byte[] rspbuf){
        return Check_Response_Packet(rspbuf, CMD_SM2_GEN_KEYPAIR, 0);
    }

    public void Dmt_Command_load_SM2KeyPair(byte[] cmdbuf, byte[] bPriKeyBuf, byte[] bPubKeyBuf,
                                            String strKeyName, long lKeyUsage) {

        CMD_PKT(cmdbuf, CMD_SM2_IMPORT_KEY, 0, 0x5A, DMT_SM2_PRIKEY_SIZE + DMT_SM2_PUBKEY_SIZE + 30);

        System.arraycopy(bPriKeyBuf, 0, cmdbuf, PACKET_SIZE, DMT_SM2_PRIKEY_SIZE);
        System.arraycopy(bPubKeyBuf, 0, cmdbuf, PACKET_SIZE + DMT_SM2_PRIKEY_SIZE, DMT_SM2_PUBKEY_SIZE);
        byte[] bytesKeyName = String2Bytes(strKeyName);
        if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
            byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
            System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
            System.arraycopy(tmpStrKeyName, 0, cmdbuf,
                    PACKET_SIZE + DMT_SM2_PRIKEY_SIZE + DMT_SM2_PUBKEY_SIZE, DMT_NAME_UNIVERSAL_LEN);
        }

        int iKeyUsage = (int) lKeyUsage;
        byte[] bKeyUsage = int2byteArray(iKeyUsage,false);
        System.arraycopy(bKeyUsage, 0, cmdbuf,
                PACKET_SIZE + DMT_SM2_PRIKEY_SIZE + DMT_SM2_PUBKEY_SIZE + DMT_NAME_UNIVERSAL_LEN, DMT_SM2_KEYUSAGE_LEN);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf,
                DMT_SM2_PRIKEY_SIZE + DMT_SM2_PUBKEY_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_SM2_KEYUSAGE_LEN);
        cmdbuf[PACKET_SIZE + DMT_SM2_PRIKEY_SIZE + DMT_SM2_PUBKEY_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_SM2_KEYUSAGE_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + DMT_SM2_PRIKEY_SIZE + DMT_SM2_PUBKEY_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_SM2_KEYUSAGE_LEN + 1] = (byte) (payload_crc & 0xFF);
    }

    public int Dmt_Response_load_SM2KeyPair(byte[] rspbuf){
        return Check_Response_Packet(rspbuf, CMD_SM2_IMPORT_KEY, 0);
    }

    public void Dmt_Command_exportSM2PubKey(byte[] cmdbuf, String strKeyName) {

        CMD_PKT(cmdbuf, CMD_SM2_EXPORT_PUBKEY, 0, 0, DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN);

        byte[] bytesKeyName = String2Bytes(strKeyName);
        if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
            byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
            System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
            System.arraycopy(tmpStrKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);
        }

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, DMT_NAME_UNIVERSAL_LEN);
        cmdbuf[PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
    }

    public int Dmt_Response_exportSM2PubKey(byte[] rspbuf, int[] iPublicKeylen, byte[] bPublicKey, long[] lPubKeyUsage){
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_SM2_EXPORT_PUBKEY, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == (DMT_SM2_PUBKEY_SIZE + DMT_SM2_KEYUSAGE_LEN)) {
                System.arraycopy(rspbuf, PACKET_SIZE, bPublicKey, 0, DMT_SM2_PUBKEY_SIZE);
                iPublicKeylen[0] = DMT_SM2_PUBKEY_SIZE;
                byte[] tmpUsage = new byte[DMT_SM2_KEYUSAGE_LEN];
                System.arraycopy(rspbuf, PACKET_SIZE + DMT_SM2_PUBKEY_SIZE, tmpUsage, 0, DMT_SM2_KEYUSAGE_LEN);
                lPubKeyUsage[0] = byteArray2int(tmpUsage, 0, false);
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_EncBySM2(byte[] cmdbuf, String strKeyName,
                                     byte[] bMsg, int iMsgLen, int iFlag) {

        CMD_PKT(cmdbuf, CMD_SM2_ENCRYPT, iFlag, 0, iMsgLen + DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN);

        byte[] bytesKeyName = String2Bytes(strKeyName);
        if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
            byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
            System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
            System.arraycopy(tmpStrKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);
        }
        System.arraycopy(bMsg, 0, cmdbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN, iMsgLen);


        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, iMsgLen + DMT_NAME_UNIVERSAL_LEN);
        cmdbuf[PACKET_SIZE + iMsgLen + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + iMsgLen + DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
    }

    public int Dmt_Response_EncBySM2(byte[] rspbuf, byte[] bCipher, int iMsgLen){
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_SM2_ENCRYPT, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == (iMsgLen + DMT_SM2_ENCRYPT_MORE_SIZE)) {
                System.arraycopy(rspbuf, PACKET_SIZE, bCipher, 0, iMsgLen + DMT_SM2_ENCRYPT_MORE_SIZE);
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_DecBySM2(byte[] cmdbuf, String strKeyName,
                                     byte[] bCipher, int iCipherLen, int iFlag) {

        CMD_PKT(cmdbuf, CMD_SM2_DECRYPT, iFlag, 0, iCipherLen + DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN);

        byte[] bytesKeyName = String2Bytes(strKeyName);
        if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
            byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
            System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
            System.arraycopy(tmpStrKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);
        }
        System.arraycopy(bCipher, 0, cmdbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN, iCipherLen);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, iCipherLen + DMT_NAME_UNIVERSAL_LEN);
        cmdbuf[PACKET_SIZE + iCipherLen + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + iCipherLen + DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
    }

    public int Dmt_Response_DecBySM2(byte[] rspbuf, byte[] bMsg, int iCipherLen){
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_SM2_DECRYPT, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == (iCipherLen - DMT_SM2_ENCRYPT_MORE_SIZE)) {
                System.arraycopy(rspbuf, PACKET_SIZE, bMsg, 0, payload_len - DMT_CRC_LEN);
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_SignBySM2(byte[] cmdbuf, byte[] bDigest, String strKeyName) {

        CMD_PKT(cmdbuf, CMD_SM2_SIGN, 0, 0, SM3_HASH_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN);

        byte[] bytesKeyName = String2Bytes(strKeyName);
        if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
            byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
            System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
            System.arraycopy(tmpStrKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);
        }
        System.arraycopy(bDigest, 0, cmdbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN, SM3_HASH_SIZE);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, SM3_HASH_SIZE + DMT_NAME_UNIVERSAL_LEN);
        cmdbuf[PACKET_SIZE + SM3_HASH_SIZE + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + SM3_HASH_SIZE + DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
    }

    public int Dmt_Response_SignBySM2(byte[] rspbuf, byte[] bSignBuf){
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_SM2_SIGN, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == DMT_SM2_SIGN_SIZE) {
                System.arraycopy(rspbuf, PACKET_SIZE, bSignBuf, 0, DMT_SM2_SIGN_SIZE);
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    public void Dmt_Command_VerifyBySM2(byte[] cmdbuf, byte[] bSignBuf, byte[] bDigest, String strKeyName) {

        CMD_PKT(cmdbuf, CMD_SM2_VERIFY, 0, 0, DMT_SM2_SIGN_SIZE + SM3_HASH_SIZE + DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN);

        byte[] bytesKeyName = String2Bytes(strKeyName);
        if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
            byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
            System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
            System.arraycopy(tmpStrKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);
        }
        System.arraycopy(bSignBuf, 0, cmdbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN, DMT_SM2_SIGN_SIZE);
        System.arraycopy(bDigest, 0, cmdbuf, PACKET_SIZE + DMT_SM2_SIGN_SIZE + DMT_NAME_UNIVERSAL_LEN, SM3_HASH_SIZE);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, DMT_SM2_SIGN_SIZE + SM3_HASH_SIZE + DMT_NAME_UNIVERSAL_LEN);
        cmdbuf[PACKET_SIZE + DMT_SM2_SIGN_SIZE + SM3_HASH_SIZE + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + DMT_SM2_SIGN_SIZE + SM3_HASH_SIZE + DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
    }

    public int Dmt_Response_VerifyBySM2(byte[] rspbuf){
        return Check_Response_Packet(rspbuf, CMD_SM2_VERIFY, 0);
    }

    public void Dmt_Command_initHashID(byte[] cmdbuf, byte[] bIdBuf, int iIdLen, String strKeyName) {

        CMD_PKT(cmdbuf, CMD_INIT_HASHID, 0, 0, iIdLen + DMT_NAME_UNIVERSAL_LEN + DMT_CRC_LEN);

        byte[] bytesKeyName = String2Bytes(strKeyName);
        if(strKeyName.length() > 0 && strKeyName.length() <= DMT_NAME_UNIVERSAL_LEN){
            byte[] tmpStrKeyName = new byte[DMT_NAME_UNIVERSAL_LEN];
            System.arraycopy(bytesKeyName, 0, tmpStrKeyName, 0, strKeyName.length());
            System.arraycopy(tmpStrKeyName, 0, cmdbuf, PACKET_SIZE, DMT_NAME_UNIVERSAL_LEN);
        }
        System.arraycopy(bIdBuf, 0, cmdbuf, PACKET_SIZE + DMT_NAME_UNIVERSAL_LEN, iIdLen);

        int payload_crc = calcrc16(CRC_INIT_VALUE, cmdbuf, iIdLen + DMT_NAME_UNIVERSAL_LEN);
        cmdbuf[PACKET_SIZE + iIdLen + DMT_NAME_UNIVERSAL_LEN] = (byte) (payload_crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + iIdLen+ DMT_NAME_UNIVERSAL_LEN + 1] = (byte) (payload_crc & 0xFF);
    }

    public int Dmt_Response_initHashID(byte[] rspbuf){
        return Check_Response_Packet(rspbuf, CMD_INIT_HASHID, 0);
    }

    /*****************************SM3 BEGIN*****************************/

    public void Dmt_Command_SM3_Init(byte[] cmdbuf) {
        CMD_PKT(cmdbuf, CMD_SM3_INIT, 0, 0, 0);
    }

    public int Dmt_Response_SM3_Init(byte[] rspbuf) {
        return Check_Response_Packet(rspbuf, CMD_SM3_INIT, 0);
    }

    public void Dmt_Command_SM3_Update(byte[] cmdbuf, byte[] bMsg, int iMsgLen) {
        CMD_PKT(cmdbuf, SM3_UPDATE, 0, 0, iMsgLen + 2);
        System.arraycopy(bMsg, 0, cmdbuf, PACKET_SIZE, iMsgLen);
        int crc = calcrc16(CRC_INIT_VALUE, cmdbuf, iMsgLen);
        cmdbuf[PACKET_SIZE + iMsgLen] = (byte)(crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + iMsgLen + 1] = (byte)(crc & 0xFF);
    }

    public int Dmt_Response_SM3_Update(byte[] rspbuf) {
        return Check_Response_Packet(rspbuf, SM3_UPDATE, 0);
    }

    public void Dmt_Command_SM3_Final(byte[] cmdbuf) {
        CMD_PKT(cmdbuf, CMD_SM3_FINAL, 0, 0, 0);
    }

    public int Dmt_Response_SM3_Final(byte[] rspbuf, byte[] bHashBuf) {
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_SM3_FINAL, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == SM3_HASH_SIZE) {
                System.arraycopy(rspbuf, PACKET_SIZE, bHashBuf, 0, SM3_HASH_SIZE);
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }
    /*****************************SM3 END*******************************/

    /*****************************RSA Start*******************************/
    void Dmt_Command_Gen_RSAKey(byte[] cmdbuf, int iKeyLen, int iIndex, int iType) {
        byte[] Edata = new byte[]{0x01, 0x00, 0x00, 0x01, 0x00, 0x01};
        CMD_PKT(cmdbuf, CMD_RSA_GENKEY, iIndex, iType, 8);
        Edata[0] = (byte)(iKeyLen >> 8 & 0xFF);
        Edata[1] = (byte)(iKeyLen & 0xFF);
        System.arraycopy(Edata, 0, cmdbuf, PACKET_SIZE, 6);
        int crc = calcrc16(CRC_INIT_VALUE, cmdbuf, 6);
        cmdbuf[13] = (byte)(crc >> 8 & 0xFF);
        cmdbuf[14] = (byte)(crc & 0xFF);
    }

    int Dmt_Response_Gen_RSAKey(byte[] rspbuf) {
        return Check_Response_Packet(rspbuf, CMD_RSA_GENKEY, 0);
    }

    void Dmt_Command_Import_RSAKey(byte[] cmdbuf, byte[] bKeyData, int iKeyLen, int iType, int iIndex) {
        CMD_PKT(cmdbuf, CMD_IMPORT_RSAKEY, iIndex, iType, iKeyLen + DMT_CRC_LEN);
        System.arraycopy(bKeyData, 0, cmdbuf, PACKET_SIZE, iKeyLen);
        int crc = calcrc16(CRC_INIT_VALUE, cmdbuf, iKeyLen);
        cmdbuf[PACKET_SIZE + iKeyLen] = (byte)(crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + iKeyLen + 1] = (byte)(crc & 0xFF);
    }

    int Dmt_Response_Import_RSAKey(byte[] rspbuf) {
        return Check_Response_Packet(rspbuf, CMD_IMPORT_RSAKEY, 0);
    }

    void Dmt_Command_Export_RSAKey(byte[] cmdbuf, int type, int Index) {
        CMD_PKT(cmdbuf, CMD_EXPORT_RSAKEY, Index, type, 0);
    }

    int Dmt_Response_Export_RSAKey(byte[] rspbuf, byte[] bKeybuf, int[] iKeyLen, int iType) {
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_EXPORT_RSAKEY, payload_len);
        int keyLen = iKeyLen[0];
        if (status == PIN_RC_SUCCESS && iType >= 0 && iType <= 3) {
            if (payload_len - DMT_CRC_LEN == keyLen) {
                System.arraycopy(rspbuf, PACKET_SIZE, bKeybuf, 0, keyLen);
                iKeyLen[0] = keyLen;
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }

        return status;
    }

    void Dmt_Command_RSA_Crypto(byte[] cmdbuf, byte[] bInbuf, int iMode, int iIndex, int iInLen) {
        CMD_PKT(cmdbuf, CMD_RSA_CRYPTO, iIndex, iMode, DMT_CRC_LEN + iInLen);
        System.arraycopy(bInbuf, 0, cmdbuf, PACKET_SIZE, iInLen);
        int crc = calcrc16(CRC_INIT_VALUE, cmdbuf, iInLen);
        cmdbuf[PACKET_SIZE + iInLen] = (byte)(crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + iInLen + 1] = (byte)(crc & 0xFF);
    }

    int Dmt_Response_RSA_Crypto(byte[] rspbuf, byte[] bOutbuf, int[] iInOutLen) {
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_RSA_CRYPTO, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == iInOutLen[0]) {
                System.arraycopy(rspbuf, PACKET_SIZE, bOutbuf, 0, iInOutLen[0]);
                iInOutLen[0] = payload_len - DMT_CRC_LEN;
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    void Dmt_Command_RSA_Name(byte[] cmdbuf, byte[] bInbuf, int iIndex, int iInLen) {
        CMD_PKT(cmdbuf, CMD_RSA_NAME_SET, iIndex, 0, iInLen + DMT_CRC_LEN);
        System.arraycopy(bInbuf, 0, cmdbuf, PACKET_SIZE, iInLen);
        int crc = calcrc16(CRC_INIT_VALUE, cmdbuf, iInLen);
        cmdbuf[PACKET_SIZE + iInLen] = (byte)(crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + iInLen + 1] = (byte)(crc & 0xFF);
    }

    int Dmt_Response_RSA_Name(byte[] rspbuf, byte[] bOutbuf) {
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_RSA_NAME_SET, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == 5) {
                System.arraycopy(rspbuf, PACKET_SIZE, bOutbuf, 0, 5);
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }
    /*****************************RSA END*******************************/

    /*****************************SM4 Start*******************************/

    public void Dmt_Command_Set_SymmetryMKey(byte[] cmdbuf, byte bKeyIndex, byte[] bMkey) {
        CMD_PKT(cmdbuf, CMD_SET_SYMMETRY_MKEY, SM4_MKEY_FLAG, bKeyIndex, SYMMETRY_KEY_SIZE + DMT_CRC_LEN);
        System.arraycopy(bMkey, 0, cmdbuf, PACKET_SIZE, SYMMETRY_KEY_SIZE);
        int crc = calcrc16(CRC_INIT_VALUE, cmdbuf, SYMMETRY_KEY_SIZE);
        cmdbuf[PACKET_SIZE + SYMMETRY_KEY_SIZE] = (byte)(crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + SYMMETRY_KEY_SIZE + 1] = (byte)(crc & 0xFF);
    }

    public int Dmt_Response_Set_SymmetryMKey(byte[] rspbuf) {
        return Check_Response_Packet(rspbuf, CMD_SET_SYMMETRY_MKEY, 0);
    }

    // 本SM4接口仅用于长城信息&科融电子
    void Dmt_Command_SM4_TUSN(byte[] cmdbuf, int iIndex, byte[] bInbuf, int iInLen) {

        CMD_PKT(cmdbuf, CMD_SM4_TUSN, iIndex, 0, iInLen + DMT_CRC_LEN);
        System.arraycopy(bInbuf, 0, cmdbuf, PACKET_SIZE, iInLen);
        int crc = calcrc16(CRC_INIT_VALUE, cmdbuf, iInLen);
        cmdbuf[PACKET_SIZE + iInLen] = (byte)(crc >> 8 & 0xFF);
        cmdbuf[PACKET_SIZE + iInLen + 1] = (byte)(crc & 0xFF);
    }

    int Dmt_Response_SM4_TUSN(byte[] rspbuf, byte[] bOutbuf) {
        int payload_len = rspbuf[4] << 8 | rspbuf[5] & 0xFF;
        int status = Check_Response_Packet(rspbuf, CMD_SM4_TUSN, payload_len);
        if (status == PIN_RC_SUCCESS) {
            if (payload_len - DMT_CRC_LEN == 8) {
                System.arraycopy(rspbuf, PACKET_SIZE, bOutbuf, 0, 8);
            } else {
                status = PIN_RC_Data_Len_Error;
            }
        }
        return status;
    }

    /*****************************SM4 END*******************************/


    private static byte Calc_XOR(byte[] buf, int size) {
        byte tmp = 0;
        for(int i = 0; i < size; ++i) {
            tmp ^= buf[i];
        }
        return tmp;
    }

    private int UpdateCrc(byte ch, int lpwCrc) {
        int tmp = ch & 0xFF;
        tmp ^= lpwCrc & 0xFF;
        tmp &= 0xFF;
        tmp ^= tmp << 4;
        tmp &= 0xFF;
        lpwCrc = (lpwCrc >> 8) ^ (tmp << 8) ^ (tmp << 3) ^ (tmp >> 4);
        return lpwCrc;
    }

    private int calcrc16(int init_value, byte[] data, int len) {
        int crc = init_value;
        for(int i = 0; i < len; ++i) {
            byte ch = data[PACKET_SIZE + i];
            crc = UpdateCrc(ch, crc);
        }
        return crc;
    }

    /**
     * String to bytes.
     */
    public byte[] String2Bytes(final String string) {
        return string2Bytes(string, "");
    }

    /**
     * Bytes to string.
     */
    private String Bytes2String(final byte[] bytes) {
        return bytes2String(bytes, "");
    }

    /**
     * String to bytes.
     */
    private byte[] string2Bytes(final String string, final String charsetName) {
        if (string == null) return null;
        try {
            return string.getBytes(getSafeCharset(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return string.getBytes();
        }
    }

    /**
     * Bytes to string.
     */
    private String bytes2String(final byte[] bytes, final String charsetName) {
        if (bytes == null) return null;
        try {
            return new String(bytes, getSafeCharset(charsetName));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            return new String(bytes);
        }
    }

    private String getSafeCharset(String charsetName) {
        String cn = charsetName;
        if (isSpace(charsetName) || !Charset.isSupported(charsetName)) {
            cn = "UTF-8";
        }
        return cn;
    }

    private boolean isSpace(final String s) {
        if (s == null) return true;
        for (int i = 0, len = s.length(); i < len; ++i) {
            if (!Character.isWhitespace(s.charAt(i))) {
                return false;
            }
        }
        return true;
    }

    public String bytes2HexString(final byte[] bytes) {
        return bytes2HexString(bytes, true);
    }

    private String bytes2HexString(final byte[] bytes, boolean isUpperCase) {
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
     * Int to 16bits or 32bits byteArray
     *
     * @param num
     * @param is16 true:16 bits byteArray false:32 bits byteArray
     * @return 数组高位在前，低位在后
     */
    private byte[] int2byteArray(final int num, boolean is16){
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
     * byte数组中取int数值，用于(高位在前,低位在后)的顺序
     *
     * @param bytes
     * @param offset
     * @return
     */
    public int byteArray2int(final byte[] bytes, int offset, boolean is16){
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
     * byte数组中取 long数值，用于(高位在前,低位在后)的顺序
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


    private int statusAdapter(int nRetCode) {
        switch (nRetCode & 0x0000FFFF) {
            case 0x8F01:
                return PIN_RC_CommandNotSupport;
            case 0x8F02:
                return PIN_RC_Command_Error;
            case 0x8F03:
                return PIN_RC_INVALID_PARAM;
            case 0x8F04:
                return PIN_RC_NOT_SUPPORT;
            case 0x8F05:
                return PIN_RC_Command_Error;
            case 0x8F06:
                return PIN_RC_Command_Error;
            case 0x0000:
                return PIN_RC_SUCCESS;
            case 0x6F01:
                return PIN_RC_FAILURE;
            case 0x6F02:
                return PIN_RC_FAILURE;
            case 0x6F05:
                return PIN_RC_FAILURE;
            case 0x6F06:
                return PIN_RC_FAILURE;
            case 0x6F07:
                return PIN_RC_FAILURE;
            case 0x6F08:
                return PIN_RC_FAILURE;
            case 0x6F09:
                return PIN_RC_FAILURE;
            case 0x6F0A:
                return PIN_RC_FAILURE;
            case 0x6F0B:
                return PIN_RC_FAILURE;
            case 0x6F0C:
                return PIN_RC_FAILURE;
            case 0x6F0D:
                return PIN_RC_FAILURE;
            case 0x6F0E:
                return PIN_RC_FAILURE;
            case 0x6F0F:
                return PIN_RC_FAILURE;
            case 0x6F10:
                return PIN_RC_FAILURE;
            case 0x6F11:
                return PIN_RC_FAILURE;
            case 0x6F12:
                return PIN_RC_FAILURE;
            case 0x6F13:
                return PIN_RC_FAILURE;
            case 0x6F14:
                return PIN_RC_FAILURE;
            case 0x6F15:
                return PIN_RC_FAILURE;
            case 0x6F16:
                return PIN_RC_FAILURE;
            case 0x6F17:
                return PIN_RC_RSA_Verify_Error;
            case 0x6F18:
                return PIN_RC_FAILURE;
            case 0x6F1A:
                return PIN_RC_FAILURE;
            case 0x6F1B:
                return PIN_RC_FAILURE;
            case 0x6C80:
                return PIN_RC_FAILURE;
            case 0x6C81:
                return PIN_RC_ENCKEY_NOT_LOAD;
            case 0x6C82:
                return PIN_RC_Send_Error;
            case 0x6C83:
                return PIN_RC_Receive_Error;
            case 0x6C84:
                return PIN_RC_STORAGE_FULL;
            case 0x6C85:
                return PIN_RC_INVALID_PARAM;
            case 0x6C86:
                return PIN_RC_Keyid_NotFound;
            case 0x6C88:
                return PIN_RC_TIMEOUT;
            case 0x6C89:
                return PIN_RC_NO_Permission;
            case 0x6C8A:
                return PIN_RC_NO_Permission;
            case 0x6C8B:
                return PIN_RC_NOT_INIT;
            case 0x6F32:
                return PIN_RC_Key_Attr_NotSupport;
            default:
                return PIN_RC_FAILURE;
        }
    }
}
