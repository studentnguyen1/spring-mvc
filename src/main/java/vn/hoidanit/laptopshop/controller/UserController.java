package vn.hoidanit.laptopshop.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import vn.hoidanit.laptopshop.service.UserService;

@Controller
public class UserController {
    // // DI: Dependency Injection
    private UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @RequestMapping("/hoidanit")

    public String getHomePage() {
        String test = this.userService.handleHello();
        return "eric.html";
    }

}
// @RestController
// public class UserController {
// // DI: Dependency Injection
// private UserService userService;

// public UserController(UserService userService) {
// this.userService = userService;
// }

// @GetMapping("")
// public String getHomePage() {
// return this.userService.handleHello();
// }

// }
