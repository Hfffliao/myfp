package love.linyi.leetcode.list;

public class RotateLinkedList {
    public static ListNode function(ListNode head, int k) {
        // 处理特殊情况
        if (head == null || k == 0) return head;
        // 计算链表长度
        int length = 1;
        ListNode tail = head;
        while (tail.next != null) {
            tail = tail.next;
            length++;
        }

        // 处理k大于链表长度的情况
        k = k % length;
        if (!(k == 0)) {
            tail.next = head;
        }
        // 找到新的头节点
        ListNode prenewHead =head;
        for (int i = 0; i < length - k - 1; i++) {
            prenewHead = prenewHead.next;
        }
        ListNode newHead = prenewHead.next;
        prenewHead.next = null;
        return newHead;
    }
    public static void main(String[] args) {
        // 测试用例
        ListNode head = new ListNode(1);
        head.next = new ListNode(2);
        head.next.next = new ListNode(3);
        head.next.next.next = new ListNode(4);
        head.next.next.next.next = new ListNode(5);
        head.next.next.next.next.next = new ListNode(6);
        head.next.next.next.next.next.next = new ListNode(7);
        head.next.next.next.next.next.next.next = new ListNode(8);
        head.next.next.next.next.next.next.next.next = new ListNode(9);
        int k = 3;
        // 调用函数
        ListNode result = function(head, k);
        while (result!=null) {
            System.out.println(result.val);
            result = result.next;
        }
    }
}
