package love.linyi.leetcode;

public class List1 {
    public class NodeList{
        int i;
        NodeList nextlist;
    }
     NodeList nodeList=new NodeList();

     public void zhuan(){
        nodeList.i=1;
        nodeList.nextlist=new NodeList();
        nodeList.nextlist.i=2;
        nodeList.nextlist.nextlist=new NodeList();
        nodeList.nextlist.nextlist.i=3;
    }
    public static void main(String[] args) {
        List1 list1=new List1();
        list1.zhuan();

        NodeList header = list1.nodeList;
        // 反转链表
        NodeList prev = null;
        NodeList current = header;
        while (current != null) {
            NodeList next = current.nextlist;
            current.nextlist = prev;
            prev = current;
            current = next;
        }
        // prev 现在是反转后的链表头节点
        header = prev;
        while (header!=null){
            System.out.println(header.i);
            header=header.nextlist;
        }
        

    }
}

