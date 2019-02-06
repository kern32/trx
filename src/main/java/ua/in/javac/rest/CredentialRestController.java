package ua.in.javac.rest;

import org.springframework.beans.factory.annotation.Autowired;
import ua.in.javac.dao.FirstTrxPropagationDao;
import ua.in.javac.entity.User;
import ua.in.javac.exceptions.CredentialNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

@RestController
@RequestMapping("/api/users")
public class CredentialRestController {

    @Autowired
    private FirstTrxPropagationDao credentialDao;

    @RequestMapping("/{username}")
    public User get(@PathVariable String username) {
        if (username.length() < 4) {
            throw new CredentialNotFoundException(username);
        } else {
            return credentialDao.getUser(username);
        }
    }

    @RequestMapping(method = RequestMethod.POST, consumes = "application/json")
    @ResponseStatus(HttpStatus.CREATED)
    public void save(@RequestBody User credential) throws IOException, InterruptedException {
        credentialDao.saveUser(credential);
        System.out.println(credential);
    }
}
