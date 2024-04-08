package sort;

public class InsertSort {

    public static void insertSort(int[] arr) {

        if (arr == null || arr.length < 2) {
            return;
        }

        for (int i = 1; i < arr.length; i++) {

            for (int j = i; j > 0 ; j--) {
                if (arr[j] < arr[j - 1]) {
                    int tmp = arr[j];
                    arr[j] = arr[j - 1];
                    arr[j - 1] = tmp;
                }
            }
        }

    }


    public static void main(String[] args) {
        int[] arr = {3, 2, 1, 7, 5, 6, 9};

        insertSort(arr);

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
