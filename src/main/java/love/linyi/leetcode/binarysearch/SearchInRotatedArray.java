package love.linyi.leetcode.binarysearch;

public class SearchInRotatedArray {

    /**
     * 在包含重复元素的旋转排序数组中搜索目标值
     *
     * @param nums   旋转排序数组（可能包含重复元素）
     * @param target 要搜索的目标值
     * @return boolean 目标值是否存在于数组中
     */
    public static boolean search(int[] nums, int target) {
        if (nums == null || nums.length == 0) {
            return false;
        }

        int left = 0;
        int right = nums.length - 1;

        while (left <= right) {
            int mid = left + (right - left) / 2;

            // 找到目标值直接返回
            if (nums[mid] == target) {
                return true;
            }

            // 处理重复元素：当无法判断哪边有序时，缩小搜索范围
            if (nums[mid] == nums[left] && nums[mid] == nums[right]) {
                left++;
                right--;
            }
            // 左半部分有序
            else if (nums[left] <= nums[mid]) {
                // 目标在有序的左半部分
                if (nums[left] <= target && target < nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            // 右半部分有序
            else {
                // 目标在有序的右半部分
                if (nums[mid] < target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }

        return false;
    }

    public static void main(String[] args) {
        // 测试用例
        int[][] testCases = {
                {2, 5, 6, 0, 0, 1, 2}, // 目标:0 → true
                {2, 5, 6, 0, 0, 1, 2}, // 目标:3 → false
                {1, 1, 1, 1, 1, 1, 1}, // 目标:1 → true
                {1, 1, 1, 1, 1},       // 目标:0 → false
                {3, 4, 4, 5, 5, 6, 0, 1, 2}, // 目标:5 → true
                {3, 4, 4, 5, 5, 6, 0, 1, 2}, // 目标:7 → false
                {},                      // 目标:1 → false
                {1}                      // 目标:1 → true
        };

        int[] targets = {0, 3, 1, 0, 5, 7, 1, 1};
        boolean[] expected = {true, false, true, false, true, false, false, true};

        // 运行测试
        for (int i = 0; i < testCases.length; i++) {
            boolean result = search(testCases[i], targets[i]);
            System.out.printf("数组: ");
            printArray(testCases[i]);
            System.out.printf(", 目标: %d → %s (期望: %s)%n",
                    targets[i],
                    result ? "找到" : "未找到",
                    result == expected[i] ? "匹配" : "不匹配");
        }
    }

    // 辅助方法：打印数组
    private static void printArray(int[] arr) {
        System.out.print("[");
        for (int i = 0; i < arr.length; i++) {
            System.out.print(arr[i]);
            if (i < arr.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.print("]");
    }
}
