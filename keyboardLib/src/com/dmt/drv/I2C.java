package com.dmt.drv;

public class I2C {

    static {
        System.loadLibrary("dmt_i2c");
    }

    public native int Dmt_Open_I2C(String strI2cFileName);

    public native int Dmt_Close_I2C();

    public native int Dmt_I2c_Send(byte[] bTxData, int iTxDataLen);

    public native int Dmt_I2c_Receive(byte[] bRxData, int iRxDataLen);

}
