package sort;

/**
 * 希尔排序，也称递减增量排序算法，是插入排序的一种更高效的改进版本
 *
 * 希尔排序基于插入排序的改进
 * 插入排序在对几乎已经排好序的数据操作时，效率高，即可以达到线性排序的效率
 * 但插入排序一般来说是低效的，因为插入排序每次只能将数据移动一位
 *
 * 原理：
 * 先将整个待排序的记录序列分割成为若干子序列分别进行直接插入排序，
 * 待整个序列中的记录"基本有序"时，再对全体记录进行依次直接插入排序
 *
 *
 *
 */
public class ShellSoret {

    public static void shellSort(int[] arr) {

        int length = arr.length;

        //增量 step，并逐步缩小增量
        for (int step = length / 2; step >= 1; step /= 2) {

            for (int i = step; i < length; i++) {
                // 将 array[i] 的下标赋值给一个变量 index
                int index = arr[i];

                int j = i - step;
                while (j >= 0 && arr[j] > index) {
                    // arr[i] = arr[j]
                    arr[j + step] = arr[j];
                    j -= step;
                }
                // arr[j] = arr[i]
                arr[j + step] = index;
            }
        }
    }



    public static void main(String[] args) {
        int[] arr = {9, 5, 8, 4, 7};

        shellSort(arr);

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }

}
