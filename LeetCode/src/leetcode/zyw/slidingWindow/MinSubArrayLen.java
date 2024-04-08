package leetcode.zyw.slidingWindow;

public class MinSubArrayLen {


    /**
     * 思路：滑动窗口实现，定义一个变量（窗口内总和），
     * 小于 target右指针右移，大于 target左指针右移，等于记录更新窗口大小
     *
     */
    public static int solution(int target, int[] arr) {
        int result = Integer.MAX_VALUE;
        int arrLen = arr.length;
        int window = 0;

        //
        for (int l = 0, r = 0; r < arrLen; r++) {
            window += arr[r];
            // 如果区间和大于 target，左指针右移
            while (window >= target) {
                // 先更新进度
                result = Math.min(result, r - l + 1);
                // 缩小左窗口
                window -= arr[l];
                l++;
            }
        }

        return result == Integer.MAX_VALUE ? 0 : result;
    }


    public static void main(String args[]) {

        int target =4;
        int[] arr = {1 ,2, 2, 3, 2};

        int result = solution(target, arr);
        System.out.println(result);
    }


}
