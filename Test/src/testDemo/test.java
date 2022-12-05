package testDemo;

public class test {

    public static void printBinary(short num){
        System.out.println("高位在前：");
        for (int i = 15; i >= 0 ; i--) {
            System.out.print((num & (1 << i)) == 0 ? "0" : "1");
        }
        System.out.println();
    }

    public static final String bytesToHexString(byte[] bArray) {
        StringBuffer sb = new StringBuffer(bArray.length);
        String sTemp;
        for (int i = 0; i < bArray.length; i++) {
            sTemp = Integer.toHexString(0xFF & bArray[i]);
            if (sTemp.length() < 2)
                sb.append(0);
            sb.append(sTemp.toUpperCase());
        }
        return sb.toString();
    }


    public static void main(String[] args) {
//
//        short s1 = Short.MAX_VALUE;
//        printBinary(s1);
//        System.out.println(s1);
//        short s2 = (short) (s1 + 1);
//        printBinary(s2);
//        System.out.println(s2);


//        String s1 = new String("abc");
//        String s2 = "abc";
//        String s3 = "ab" + "c";
//        String s4 = "a"+ "b" + "c";
//        String s5 = s1;
//
//        System.out.println(s2 == "abc"); // true


//        System.out.println(s1.equals(s2));
//        System.out.println(s1.equals(s3));
//        System.out.println(s1.equals(s4));
//        System.out.println(s1.equals(s5));
//
//        System.out.println("=============");
//
//        System.out.println(s1 == s2);
//        System.out.println(s1 == s3);
//        System.out.println(s1 == s4);
//        System.out.println(s1 == s5);





//        byte[] oper1 = {0x12, 0x34, 0x56, (byte) 0xAB};
//        String tmp = bytesToHexString(oper1);
//        byte[] resultBlock = tmp.getBytes();


    }


}




