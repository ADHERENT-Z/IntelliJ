package testDemo;

import sun.misc.Unsafe;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ThreadPoolExecutor;

public class test {

    public static void printBinary(short num){
        System.out.println("高位在前：");
        for (int i = 15; i >= 0 ; i--) {
            System.out.print((num & (1 << i)) == 0 ? "0" : "1");
        }
        System.out.println();
    }

    public static final String bytes2HexString(byte[] bArray) {
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

//        ThreadContainer<Ticket> ticketThreadContainer =
//                new ThreadContainer<Ticket>(2, 5, new LinkedList<>());
//
//        Date time = new Date();
//
//        Producer producer = new Producer(ticketThreadContainer, time);
//        Customer customer = new Customer(ticketThreadContainer);
//
//        new Thread(producer).start();
//        new Thread(customer).start();



    }
}




