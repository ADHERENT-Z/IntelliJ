package testDemo;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface wltrs extends Library {

    testDemo.wltrs wltrsDll  = (testDemo.wltrs) Native.loadLibrary("WltRS", testDemo.wltrs.class);

    public int GetBmp(String file_name, int intf);

//    wltrs wltrsDll  = (wltrs) Native.loadLibrary("DLL_File", wltrs.class);
//
//    public int unpack(String src, String dest, int intf);
}
