package cu.application.service;

import cu.application.model.User;
import cu.application.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserRepository repo;

    @Override
    public Iterable<User> getAllUsers() {
        return repo.findAll();
    }

    private boolean checkUserNameAvailable(User user) throws Exception {
        Optional userFound = repo.findByUsername(user.getUsername());
        if (userFound.isPresent())
            throw new Exception("Username already exist");
        return true;
    }

    private boolean checkPasswordValid(User user) throws Exception {
        if (!user.getPassword().equals(user.getConfirmPassword()))
            throw new Exception("Password does not mach");
        return true;
    }

    @Override
    public User createUser(User user) throws Exception {
        if (checkUserNameAvailable(user) && checkPasswordValid(user)) {
            //tengo una session activa con ese usuario y mas adelante puedo hacer algo con ello.
            user = repo.save(user);
        }
        return user;
    }

}
