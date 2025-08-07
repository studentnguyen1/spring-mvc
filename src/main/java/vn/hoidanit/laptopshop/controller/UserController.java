package vn.hoidanit.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import vn.hoidanit.laptopshop.domain.User;
import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {
    // // DI: Dependency Injection
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/")

    public String getHomePage(Model model) {
        String test = this.userService.handleHello();
        String controlService = "Hello from Controller";
        model.addAttribute("test", test);
        model.addAttribute("controller", controlService);
        return "hello";
    }

    @RequestMapping("/admin/user")

    public String getCreateUser(Model model) {
        model.addAttribute("newUser", new User());
        return "admin/user/create";

    }

    @RequestMapping(value = "/admin/user/create", method = RequestMethod.POST)

    public String createUserPage(Model model, @ModelAttribute("newUser") User hoidanit) {
        System.out.println(hoidanit);
        String test1 = "Hello Create ADmin";
        return "hello";

    }

}
