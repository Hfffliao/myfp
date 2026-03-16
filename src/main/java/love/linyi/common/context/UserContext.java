package love.linyi.common.context;

import java.util.Optional;

public class UserContext {
    private static final ThreadLocal<UserInfo> userThreadLocal = new ThreadLocal<>();
    
    public static class UserInfo {
        private final String username;
        private final int id;
        
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
    
    public static void setUserInfo(String username, int id) {
        userThreadLocal.set(new UserInfo(username, id));
    }
    
    public static Optional<UserInfo> getUserInfo() {
        return Optional.ofNullable(userThreadLocal.get());
    }

    public static Optional<String> getUsername() {
        return getUserInfo().map(UserInfo::getUsername);
    }
    
    public static Optional<Integer> getUserId() {
        return getUserInfo().map(UserInfo::getId);
    }
    
    public static boolean isLoggedIn() {
        return userThreadLocal.get() != null;
    }
    
    public static void clear() {
        userThreadLocal.remove();
    }
}