package sort;


/**
 * 算法描述：
 * 1、比较相邻的元素，如果第一个比第二个大，就交换它们
 * 2、对每一对相邻元素作同样的工作，从开始第一对到结尾的最后一对，这样在最后的元素应该会是最大的数
 * 3、针对所有的元素重复以上的步骤，除了最后一个
 * 4、重复步骤1~3，直到排序完成
 */
public class BubbleSort {


    public static void bubbleSort(int arr[]){

        if (arr == null || arr.length < 2) {
            return;
        }

        for (int j = arr.length - 1; j > 0; j--) {

            // 第一次需要进行 n-1次比较，故 j = arr.length - 1
            for (int i = 0; i < j; i++) {
                if (arr[i] > arr[i + 1]) {
                    int tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;
                }
            }
        }
    }

    /**
     * 针对问题：数据的顺序排好之后，冒泡算法仍然会继续进行下一轮的比较，直到 arr.length-1次，后面的比较没有意义
     * 优化方案：设置标志位 flag，如果发生了交换 flag设置为 true；如果没有交换就设置为 false
     *
     * @param arr
     */
    public static void bubbleSort2(int[] arr) {

        if (arr == null || arr.length < 2) {
            return;
        }

        boolean flag = false; //是否交换的标志
        for (int j = arr.length - 1; j > 0; j--) {

            //每次遍历标志位都要先置为 false，才能判断后面的元素是否发生了交换
            flag = false;

            // 第一次需要进行 n-1次比较，故 j = arr.length - 1
            for (int i = 0; i < j; i++) {
                if (arr[i] > arr[i + 1]) {
                    int tmp = arr[i];
                    arr[i] = arr[i + 1];
                    arr[i + 1] = tmp;

                    flag = true;    //只要有发生了交换，flag就置为 true
                }
            }

            // 判断标志位是否为 false，如果为 false说明后面的元素已经有序直接 return
            if(!flag){
                break;
            }
        }
    }



    /**
     * 针对问题：数据的顺序排好之后，冒泡算法仍然会继续进行下一轮的比较，直到 arr.length-1次，后面的比较没有意义
     * 优化方案：设置标志位 flag，如果发生了交换 flag 不为 0；如果没有交换就设置为 0
     *
     * @param arr
     */
    public static void bubbleSort3(int[] arr) {

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


    public static void main(String[] args) {

        int[] arr = {2, 3, 1, 6, 8, 7};

        bubbleSort2(arr);

        for (int i = 0; i < arr.length; i++) {
            System.out.println(arr[i]);
        }
    }
}
