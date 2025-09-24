package love.linyi.leetcode.binarysearch;

public class SearchMinWhenRotatednumber {
    public static void main(String[] args) {
        int[] nums = {2,1,2,2,2};
        int min = searchMinWhenRepeat(nums);
        System.out.println("最小值为：" + min);
    }
    public static int searchMinWhenNoRepeat(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
        while (left < right) {
            int mid = left + (right - left) / 2;
            if (nums[mid] < nums[right]) {
               right = mid;
            } else {
                left = mid+1;
            }
        }
        return nums[left];
    }
    public static int searchMinWhenRepeat(int[] nums) {
        int left = 0;
        int right = nums.length - 1;
            while (left < right) {
                int mid = left + (right - left) / 2;
                if (nums[mid] < nums[right]) {
                    right = mid;
                }else if (nums[mid]==nums[right]){
                    right-=1;
                }
                else {
                    left = mid+1;
                }
            }
        return nums[left];
    }
}
