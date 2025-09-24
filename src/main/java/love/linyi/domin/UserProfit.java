package love.linyi.domin;

public class UserProfit {


    private int id;
    private int totalSize;
    private int usedSize;
    private int remainingSize;
public UserProfit(){}
    public UserProfit(int id, int remainingSize, int totalSize, int usedSize) {
        this.id = id;
        this.remainingSize = remainingSize;
        this.totalSize = totalSize;
        this.usedSize = usedSize;
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
