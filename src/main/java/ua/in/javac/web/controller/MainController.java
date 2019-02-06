package ua.in.javac.web.controller;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;
import ua.in.javac.dao.ManyToManyDao;
import ua.in.javac.dao.FirstTrxPropagationDao;
import ua.in.javac.dao.OneToManyDao;
import ua.in.javac.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import ua.in.javac.exceptions.CredentialNotFoundException;
import ua.in.javac.service.MessageService;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static org.springframework.web.bind.annotation.RequestMethod.GET;

@Controller
@Service
@RequestMapping({"/", "user"})
public class MainController {

    static final Logger logger = LogManager.getLogger(MainController.class);

    @Autowired
    private MessageService messageService;

    @Autowired
    private FirstTrxPropagationDao firstPropagationDAO;

    @Autowired
    private OneToManyDao oneToManyDao;

    @Autowired
    private ManyToManyDao manyToManyDao;

    @RequestMapping(method = GET)
    public String getUsers(Model model) {
        List<User> userList = firstPropagationDAO.getUserList();
        System.out.println(userList);

        model.addAttribute("userList", userList);
        return "index";
    }

   @RequestMapping("/{username}")
    public String getUser(@PathVariable String username, Model model) {
        if (username.length() < 4) {
            throw new CredentialNotFoundException(username);
        } else {
            model.addAttribute("userList", new ArrayList().add(firstPropagationDAO.getUser(username)));
            return "index";
        }
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String addUser() {
        return "form";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String saveNewCredential(@ModelAttribute("user") User user) throws IOException, InterruptedException, CloneNotSupportedException {

       //testFetching join fetch with onetoone and onetomany relation (lazyinitexception)
       oneToManyDao.testFetching();

        //testFetching join fetch with manytomany relation (lazyinitexception)
        //manyToManyDao.testFetching();

       /* firstPropagationDAO.saveUser(user);
        System.out.println(user.getUsername());*/
        return "redirect:/";
    }

    @RequestMapping(value = "/message", method = GET)
    @ResponseBody
    public String message() {
        return messageService.printMessage();
    }
}

