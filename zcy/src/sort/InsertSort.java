package sort;

public class InsertSort {

    public static void insertSort(int[] arr) {

        if (arr == null || arr.length < 2) {
            return;
        }

        for (int j = 1; j < arr.length; j++) {
            for (int i = j - 1; i >= 0 ; i--) {
                if (arr[i] > arr[i + 1]) {
                    int tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                }
            }
        }

    }


    public static void main(String[] args) {
        int[] arr = {3, 2, 1};
        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }

        insertSort(arr);

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
