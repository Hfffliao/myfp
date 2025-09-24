package love.linyi.leetcode.list;

public class ReverseLinkedListllm {


    public static ListNode function(ListNode head, int left, int right) {
        int i = 1;
        ListNode rightNode=head ;
        ListNode pre=null ;
        ListNode leftNode=head ;

        ListNode cur = head;
        while (cur != null ) {
            ListNode next = cur.next;

            if (i == left-1) {
                leftNode = cur;
            }
            if (i>=left && i<=right) {

                cur.next = pre;
                pre = cur;
                cur = next;
            }else {
                cur = cur.next;
            }
            if (i == left) {
                rightNode =pre ;
            }
            if (i == right) {
                rightNode.next = next;
            }
            i++;
        }
            if(left!=1){
                leftNode.next = pre;
            }
            if (left==1) {
                head = pre;
            }
        return head;
    }
    public static void main(String[] args) {
        // 测试用例
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        int left = 2;
        int right = 2;
        // 调用函数
        ListNode result = function(head, left, right);
        while (result!=null) {
            System.out.println(result.val);
            result = result.next;
        }
    }
}
