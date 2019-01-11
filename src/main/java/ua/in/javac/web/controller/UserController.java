package ua.in.javac.web.controller;

import ua.in.javac.entity.User;
import ua.in.javac.exceptions.UserNotFoundException;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
public class UserController {

    @RequestMapping
    public String getUserList(Model model) {
        List<User> userList = Arrays.asList(new User("Jimmy"));
        model.addAttribute("userList", userList);
        return "users";
    }

    @RequestMapping("/{username}")
    public String getUser(@PathVariable String username, Model model) {
        if (username.length() < 4) {
            throw new UserNotFoundException(username);
        } else {
            model.addAttribute("user", new User(username));
            return "user";
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addUser() {
        return "userForm";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveNewUser(User user) {
        System.out.println(user.getUsername());

        return "redirect:/users/" + user.getUsername();
    }
}
