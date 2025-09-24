package love.linyi.leetcode.binarysearch;

public class GetAnyPeakValue {
    public static void main(String[] args) {
        int[] nums = {1, 2, 5, 4, 5, 6, 7, 8, 9, 10};
        int target = 10;
        search(nums);
        System.out.println(search(nums));


    }
    //获取任意峰值
   static int search(int[] nums) {
       int left = 0;
       int right = nums.length - 1;
       while (left < right) {
           int mid = left + (right - left) / 2;
           if(nums[mid]<nums[mid+1]){
               left=mid+1;

           }
           else{
               right=mid;
           }
       }
       return nums[left];
    }
}
