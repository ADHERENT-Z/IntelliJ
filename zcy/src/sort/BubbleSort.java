package sort;

public class BubbleSort {

    public static void bubbleSort(int[] arr) {
        if (arr == null || arr.length < 2) {
            return;
        }

        for (int j = arr.length - 1; j > 0; j--) {
            for (int i = 0; i < j; i++) {
                if (arr[i] > arr[i + 1]) {
                    int tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                }
            }
        }

    }

    public void bubbleSort2(int[] arr) {

        int i = arr.length - 1; //初始时,最后位置保持不变　　
        while(i > 0){
            int flag = 0; //每趟开始时,无记录交换
            for(int j = 0; j < i; j++){
                if (arr[j] > arr[j + 1]) {
                    flag = j; //记录交换的位置
                    int temp = arr[j + 1];
                    arr[j + 1] = arr[j];
                    arr[j] = temp;
                }
            }
            i = flag; //为下一趟排序作准备
        }
    }



    public static void main(String[] args){

        int[] arr = {3, 2, 1};
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        bubbleSort(arr);

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

    }
}
