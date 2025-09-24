package love.linyi.domin;
public class User {
    private String userName;
    private String password;
    private int id;

public User(String userName, String password,int id) {
    this.userName = userName;
    this.password = password;
    this.id = id;
}
public User(String userName,String password){
    this.userName = userName;
    this.password = password;
}
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    @Override
    public String toString() {
        return "User{" +
                "password='" + password + '\'' +
                ", userName='" + userName + '\'' +
                '}';
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
