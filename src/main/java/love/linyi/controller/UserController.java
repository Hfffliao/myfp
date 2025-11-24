package love.linyi.controller;

import love.linyi.domin.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;


@Controller
//@RequestMapping("/user")
//@Tag(name = "用户控制接口示例", description = "对用户的增删查改")
public class UserController {
    //@Operation(summary = "注册用户", description = "注册注册")
    @RequestMapping(value = "/user/{id}",method= RequestMethod.POST)
    public String save(@PathVariable int id){
        System.out.println("user save"+id);
        return "";
    }
    @RequestMapping(value = "/user",method = RequestMethod.DELETE)
    public String delete(){
        System.out.println("user delete...");
        return "";
    }
    @RequestMapping(value = "/user",method = RequestMethod.PUT)
    public String update(){
        System.out.println("user update...");
        return "";
    }
    @GetMapping("/user")
    public void get(){
        System.out.println("get");
    }
    @RequestMapping("/param")
    public void param( User user){
        System.out.println(user.toString());
    }
    @RequestMapping("/paralike")
    public void paralike(String[] likes ){
        System.out.println(Arrays.toString(likes));
    }
    @RequestMapping("/paralikelist")
    public void paralike(@RequestParam List<String> likes ){
        System.out.println(likes);
    }
}
