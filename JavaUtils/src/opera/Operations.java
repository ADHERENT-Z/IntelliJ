package opera;

public class Operations {

    public static byte getXor(final byte[] bytes, int len)
    {
        byte value = 0;
        for(int i=0; i<len; i++)
            value ^= bytes[i];
        return value;
    }

    public static byte getSum(final byte[] bytes, int len)
    {
        int value = 0;
        for(int i=0; i<len; i++)
            value += bytes[i];
        return (byte)(~value);
    }

    public static void main(String[] args){

    }

}


