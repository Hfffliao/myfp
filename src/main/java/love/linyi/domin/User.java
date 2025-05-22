package love.linyi.domin;
public class User {
    private String userName;
    private String password;
public User(String userName, String password) {
    this.userName = userName;
    this.password = password;
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
