package cu.application.service;

import cu.application.model.User;
import cu.application.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    IUserRepository repo;

    @Override
    public Iterable<User> getAllUsers() {
       return  repo.findAll();
    }
}
