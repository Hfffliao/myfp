package love.linyi.controller;

import love.linyi.domin.ShiJian;
import love.linyi.service.ShiJianService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/shijian")
public class ShiJianCon {
    @Autowired
    private ShiJianService shiJianService;

    //@RequestMapping(value = "/shijian",method = {RequestMethod.POST})
//    @DeleteMapping("/{id}")
//    public boolean delete(@PathVariable Integer id) {
//        System.out.println("delete");
//        //System.out.println(id);
//        return shiJianService.delete(id);
//    }
//    @PostMapping
//    public Result save(@RequestBody ShiJian shiJian) {
//        boolean flag = shiJianService.save(shiJian);
//        return new Result(flag?Code.SAVE_OK:Code.SAVE_ERR,flag);
//    }
//    @PutMapping
//    public boolean update(@RequestBody ShiJian shiJian) {
//
//        return shiJianService.update(shiJian);
//    }
//    @GetMapping("/{id}")
//    public ShiJian getById(@PathVariable Integer id) {
//        System.out.println(shiJianService.getById(id));
//        return shiJianService.getById(id);
//    }
    @GetMapping("/all")
    public List<ShiJian> getAll() {
        System.out.println("all");
        return shiJianService.getAll();
    }

}
