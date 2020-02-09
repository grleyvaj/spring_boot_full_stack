package cu.application.controller;

import cu.application.model.User;
import cu.application.service.IRoleService;
import cu.application.service.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @Autowired
    IUserService userService;
    @Autowired
    IRoleService roleService;

    @GetMapping("/")
    public String index(){
        return "index";
    }

    @GetMapping("/userForm")
    public String getUserForm(Model model){
        model.addAttribute("userForm", new User());
        model.addAttribute("userList", userService.getAllUsers());
        model.addAttribute("roles",roleService.getAllRole());
        model.addAttribute("listTab", "active");
        return "user-form/user-view";
    }
}
