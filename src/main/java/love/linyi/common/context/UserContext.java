package love.linyi.common.context;

// UserContext.java
public class UserContext {
    private static final ThreadLocal<UserInfo> userThreadLocal = new ThreadLocal<>();
    
    public static class UserInfo {
        private String username;
        private int id;
        
        public UserInfo(String username, int id) {
            this.username = username;
            this.id = id;
        }
        
        public String getUsername() { return username; }
        public int getId() { return id; }
    }
    
    public static void setUserInfo(UserInfo userInfo) {
        userThreadLocal.set(userInfo);
    }
    
    public static UserInfo getUserInfo() {
        return userThreadLocal.get();
    }

    public static String getUsername() {
        UserInfo userInfo = userThreadLocal.get();
        return userInfo != null ? userInfo.getUsername() : null;
    }
    
    public static int getUserId() {
        UserInfo userInfo = userThreadLocal.get();
        return userInfo != null ? userInfo.getId() : 0;
    }
    
    public static void clear() {
        userThreadLocal.remove();
    }
}