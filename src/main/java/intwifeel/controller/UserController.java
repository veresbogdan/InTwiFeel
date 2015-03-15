package intwifeel.controller;

import intwifeel.model.User;
import intwifeel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    public @ResponseBody User createUser(@RequestBody User user) {
        return userService.createUser(user);
    }

    @RequestMapping(value = "/find/{name}", method = RequestMethod.GET)
    public @ResponseBody User findByName(@PathVariable String name) {
        return userService.findByName(name);
    }
}
