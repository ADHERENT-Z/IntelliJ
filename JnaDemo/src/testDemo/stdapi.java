package testDemo;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.ptr.IntByReference;

public interface stdapi extends Library {

    // 动态库实例
    stdapi sdtDll  = (stdapi)Native.loadLibrary("sdtapi", stdapi.class);

    public int SDT_OpenPort(int iPortID);
    public int SDT_ClosePort(int iPortID);
    public int SDT_StartFindIDCard(int iPortID, byte[] pucIIN, int iIfOpen);
    public int SDT_SelectIDCard(int iPortID, byte[] pucSN, int iIfOpen);
    public int SDT_ReadBaseMsg(int iPortID, byte[] pucCHMsg, IntByReference puiCHMsgLen,
                               byte[] pucPHMsg, IntByReference puiPHMsgLen, int iIfOpen);
    public int SDT_ReadBaseFPMsg(int iPortID, byte[]  pucCHMsg, IntByReference puiCHMsgLen, byte[] pucPHMsg,
                                 IntByReference	puiPHMsgLen, byte[] pucFPMsg, IntByReference puiFPMsgLen, int bIfOpen);
}
