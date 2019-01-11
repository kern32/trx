package ua.in.javac.web.controller;

import ua.in.javac.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import ua.in.javac.service.MessageService;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@RequestMapping({"/", "/welcome"})
public class WelcomeController {

    @Autowired
    private MessageService messageService;

    @RequestMapping(method = GET)
    public String welcome(Model model) {
        model.addAttribute("user", new User("Johnny"));
        return "welcome";
    }

    @RequestMapping(value = "/message", method = GET)
    @ResponseBody
    public String message() {
        return messageService.printMessage();
    }

}

