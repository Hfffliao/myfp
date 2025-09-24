package love.linyi.leetcode.list;
//未完成
public class ReverseLinkedListDiguillb {
    public static ListNode function(ListNode head, int left, int right) {
       ListNode preNode =new ListNode(0);
       preNode.next=head;
       for(int i=1;i<left;i++){
           preNode=preNode.next;
       }
       ListNode listNodes=digui(preNode.next);
//       preNode.next.next=listNodes[1];
//       preNode.next=listNodes[0];
       return head;
    }//ListNode[] listNodes=new ListNode[2];
    private static ListNode digui(ListNode head){
        if(head.next==null){

        }
        if(head==null||head.next==null){
            return head;
        }
        ListNode newd =digui(head.next);
        head.next.next=head;
        head.next=null;
        return newd;
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