package com.impl.drv.spi;

import com.dmt.drv.SPI;

public class SPI_Driver implements SPI {

    @Override
    public int Dmt_Open_SPI(String strSpiFileName) {
        return 0;
    }

    @Override
    public int Dmt_Close_SPI() {
        return 0;
    }

    @Override
    public int Dmt_SPI_Send(byte[] bTxData, int iTxDataLen) {
        return 0;
    }

    @Override
    public int Dmt_SPI_Receive(byte[] bRxData, int iRxDataLen) {
        return 0;
    }

}
