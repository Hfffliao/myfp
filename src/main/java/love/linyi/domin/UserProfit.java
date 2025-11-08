package love.linyi.domin;

public class UserProfit {


    private int id;
    private int totalSize;
    private int usedSize;
    private int remainingSize;
    private int user_id;
    public UserProfit(){}


    @Override
    public String toString() {
        return "UserProfit{" +
                "id=" + id +
                ", totalSize=" + totalSize +
                ", usedSize=" + usedSize +
                ", remainingSize=" + remainingSize +
                ", user_id=" + user_id +
                '}';
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }
//    全参构造函数，用于查询用户利润信息
    public UserProfit(int id, int remainingSize, int totalSize, int usedSize, int user_id) {
        this.id = id;
        this.remainingSize = remainingSize;
        this.totalSize = totalSize;
        this.usedSize = usedSize;
        this.user_id = user_id;
    }
//    无id构造函数，用于存储用户利润信息
    public UserProfit( int remainingSize, int totalSize, int usedSize, int user_id) {
        this.remainingSize = remainingSize;
        this.totalSize = totalSize;
        this.usedSize = usedSize;
        this.user_id = user_id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getRemainingSize() {
        return remainingSize;
    }

    public void setRemainingSize(int remainingSize) {
        this.remainingSize = remainingSize;
    }

    public int getTotalSize() {
        return totalSize;
    }

    public void setTotalSize(int totalSize) {
        this.totalSize = totalSize;
    }

    public int getUsedSize() {
        return usedSize;
    }

    public void setUsedSize(int usedSize) {
        this.usedSize = usedSize;
    }
}
