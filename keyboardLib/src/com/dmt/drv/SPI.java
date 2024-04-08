package com.dmt.drv;

public interface SPI {

    public int Dmt_Open_SPI(String strSpiFileName);

    public int Dmt_Close_SPI();

    public int Dmt_SPI_Send(byte[] bTxData, int iTxDataLen);

    public int Dmt_SPI_Receive(byte[] bRxData, int iRxDataLen);

}
