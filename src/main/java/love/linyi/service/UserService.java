package love.linyi.service;
import love.linyi.domin.User;
import java.util.List;
public interface UserService {

     void save(User user);
     User getByusername(String username );
    List<User> getAll();
}
