package love.linyi.leetcode.binarysearch;

import java.util.ArrayList;
import java.util.List;

public class FindAllOccurrences {
    
    /**
     * 在旋转排序数组中查找目标值的所有出现位置
     * 
     * @param nums 旋转排序数组（可能包含重复元素）
     * @param target 要查找的目标值
     * @return 目标值在数组中所有出现位置的索引列表
     */
    public static List<Integer> findAllOccurrences(int[] nums, int target) {
        List<Integer> indices = new ArrayList<>();
        if (nums == null || nums.length == 0) return indices;
        
        int left = 0;
        int right = nums.length - 1;
        
        // 第一步：找到目标值可能出现的最左位置
        while (left <= right) {
            int mid = left + (right - left) / 2;
            
            if (nums[mid] == target) {
                // 找到后立即开始扩展搜索范围
                expandSearch(nums, target, mid, indices);
                return indices;
            }
            
            // 处理重复元素情况
            if (nums[mid] == nums[left] && nums[mid] == nums[right]) {
                left++;
                right--;
            }
            // 左半部分有序
            else if (nums[left] <= nums[mid]) {
                // 目标在有序的左半部分
                if (nums[left] <= target && target <= nums[mid]) {
                    right = mid - 1;
                } else {
                    left = mid + 1;
                }
            }
            // 右半部分有序
            else {
                // 目标在有序的右半部分
                if (nums[mid] <= target && target <= nums[right]) {
                    left = mid + 1;
                } else {
                    right = mid - 1;
                }
            }
        }
        
        return indices; // 未找到则返回空列表
    }
    
    /**
     * 扩展搜索范围以查找所有匹配位置
     */
    private static void expandSearch(int[] nums, int target, int index, List<Integer> indices) {
        // 向左扩展搜索
        int temp = index;
        while (temp >= 0 && nums[temp] == target) {
            indices.add(temp);
            temp--;
        }
        
        // 向右扩展搜索
        temp = index + 1;
        while (temp < nums.length && nums[temp] == target) {
            indices.add(temp);
            temp++;
        }
    }
    
    // 测试函数
    public static void main(String[] args) {
        // 测试用例集
        int[][] testCases = {
            {5,6,7,1,2,5,5,8},   // 目标:5 → [0,5,6]
            {2,2,2,2,1,2,2,2},   // 目标:2 → [0,1,2,3,5,6,7]
            {1,3,5,5,5},          // 目标:5 → [2,3,4]
            {5,5,5,5,5,5},        // 目标:5 → [0,1,2,3,4,5]
            {4,5,6,7,0,1,2},      // 目标:3 → []
            {1,1,1,1,1},          // 目标:0 → []
            {3,4,5,1,2},          // 目标:1 → [3]
            {},                   // 目标:1 → []
            {1}                   // 目标:1 → [0]
        };
        
        int[] targets = {5, 2, 5, 5, 3, 0, 1, 1, 1};
        int[][] expected = {
            {0,5,6}, {0,1,2,3,5,6,7}, {2,3,4}, 
            {0,1,2,3,4,5}, {}, {}, {3}, {}, {0}
        };
        
        // 运行测试
        for (int i = 0; i < testCases.length; i++) {
            List<Integer> result = findAllOccurrences(testCases[i], targets[i]);
            System.out.print("数组: ");
            printArray(testCases[i]);
            System.out.println();
            System.out.println("目标: " + targets[i]);
            System.out.println("结果: " + result);
            System.out.println("期望: " + arrayToList(expected[i]));
            System.out.println("匹配: " + (checkResult(result, expected[i]) ? "✅" : "❌"));
            System.out.println("----------------------");
        }
    }
    
    // 验证结果
    private static boolean checkResult(List<Integer> result, int[] expected) {
        if (result.size() != expected.length) return false;
        for (int i = 0; i < expected.length; i++) {
            if (result.get(i) != expected[i]) return false;
        }
        return true;
    }
    
    // 数组转列表
    private static List<Integer> arrayToList(int[] arr) {
        List<Integer> list = new ArrayList<>();
        for (int num : arr) {
            list.add(num);
        }
        return list;
    }
    
    // 打印数组
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
