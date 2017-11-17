package TokenRing;

import com.google.gson.Gson;
import com.sun.deploy.util.ArrayUtil;
import com.sun.tools.javac.util.ArrayUtils;

public class TokenRingPackage{
    public byte SD = 0x7E;
    public int P = 0;
    public int R = 0;
    public boolean T = true;
    public boolean A = false;
    public boolean E = false;
    public int DA = 0;
    public int SA = 0;
    public String INFO = "";
    public byte ED = 0x7E;

    public String getPackage(){
        return new Gson().toJson(this);
    }
}
