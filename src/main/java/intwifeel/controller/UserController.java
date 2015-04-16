package intwifeel.controller;

import intwifeel.model.UserEntity;
import intwifeel.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/user")
public class UserController extends BaseController {
    @Autowired
    private UserService userService;

    @RequestMapping(value = "/create", method = RequestMethod.POST)
    @PreAuthorize(value = "permitAll()")
    public @ResponseBody
    UserEntity createUser(@RequestBody UserEntity userEntity) {
        return userService.createUser(userEntity);
    }

    @RequestMapping(value = "/find/{name}", method = RequestMethod.GET)
    @PreAuthorize(value = "isAuthenticated()")
    public @ResponseBody


    UserEntity findByName(@PathVariable String name) {
        UserEntity userEntity = userService.findByName(name);
        userEntity.setPassword(null);

        return userEntity;
    }
}
