package love.linyi.leetcode.list;
public class ReverseLinkedListllb {
    public static ListNode function(ListNode head, int left, int right) {
        // 添加虚拟头节点简化操作
        ListNode dummy = new ListNode(0);
        dummy.next = head;
        
        // 定位left的前驱节点
        ListNode preNode = dummy;
        for (int i = 1; i < left; i++) {
            preNode = preNode.next;
        }
        
        // 开始反转
        ListNode cur = preNode.next;
        ListNode prev = null;
        for (int i = left; i <= right; i++) {
            ListNode next = cur.next;
            cur.next = prev;
            prev = cur;
            cur = next;
        }
        
        // 重新连接链表
        preNode.next.next = cur;  // 连接反转后的尾节点到剩余链表
        preNode.next = prev;      // 连接前驱节点到反转后的头节点

        
        return dummy.next;
    }
    public static void main(String[] args) {
        // 测试用例
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        
        int left = 2;
        int right = 4;
        
        // 调用函数
        ListNode result = function(head, left, right);
        
        // 打印结果
        while (result != null) {
            System.out.print(result.val + " ");
            result = result.next;
        }
        // 输出: 1 4 3 2 5
    }
}