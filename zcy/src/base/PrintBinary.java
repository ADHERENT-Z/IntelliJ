package base;

public class PrintBinary {

    public static void print(int num) {

        System.out.println("高位在前：");
        for (int i = 31; i >= 0 ; i--) {
            System.out.print((num & (1 << i)) == 0 ? "0" : "1");
        }
        System.out.println();

//        System.out.println("低位在前：");
//        for (int i = 0; i <= 31; i++) {
//            System.out.print((num & (1 << i)) == 0 ? "0" : "1");
//        }
//        System.out.println();
    }

    public static void main(String[] args) {

        if (false) {
            int num = 4;
            print(num);

            int integer_max = Integer.MAX_VALUE;
            System.out.println(integer_max);
            print(integer_max);

            int integer_min = Integer.MIN_VALUE;
            System.out.println(integer_min);
            print(integer_min);
        }



    }


}

