package leetcode.zyw.twosum;

import java.util.HashMap;
import java.util.Map;

public class Solution {

    /**
     *
     * @param nums
     * @param target
     * @return
     *
     * 时间复杂度：O(N^2)，其中N是数组中的元素数量。最坏情况下数组中任意两个数都要被匹配一次
     * 空间复杂度：O(1)
     */
    public int[] twoSum_1(int[] nums, int target) {
        int len = nums.length;
        for (int i = 0; i < len - 1; i++) {
            for (int j = 0; j < len; j++) {
                if (nums[i] + nums[j] == target) {
                    return new int[]{i, j};
                }
            }
        }
        throw new IllegalArgumentException("No two sum Solution");
    }

    /**
     *
     * @param nums
     * @param target
     * @return
     *
     *时间复杂度：O(N)，其中N是数组中的元素数量。对于每一个元素 x，我们可以 O(1)地寻找 target - x。
     *时间复杂度：O(N)，其中N是数组中的元素数量。对于每一个元素 x，我们可以 O(1)地寻找 target - x。
     */
    public int[] twoSum_2(int[] nums, int target) {
        int len = nums.length;
        Map<Integer, Integer> hashMap = new HashMap<>(len - 1);
        hashMap.put(nums[0], 0);
        for (int i = 0; i < len; i++) {
            int another = target - nums[i];
            if (hashMap.containsKey(another)) {
                return new int[]{i, hashMap.get(another)};
            }
            hashMap.put(nums[i], i);
        }

        throw new IllegalArgumentException("No two sum Solution");
    }
}
