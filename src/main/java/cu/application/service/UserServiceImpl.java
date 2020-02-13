package cu.application.service;

import cu.application.dto.ChangePasswordForm;
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
        if (user.getConfirmPassword() == null || user.getConfirmPassword().isEmpty())
            throw new Exception("Confirm Password is obligatory");
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

    @Override
    public User getUserById(Long id) throws Exception {
        User user = repo.findById(id).orElseThrow(() -> new Exception("The user does not exist"));
        return user;
    }

    @Override
    public User updateUser(User fromUser) throws Exception {
        //Hay q consultarlo porq sino cree q es uno nuevo porq no tiene session abierta
        User toUser = getUserById(fromUser.getId());
        mapUser(fromUser, toUser);
        return repo.save(toUser);
    }

    @Override
    public void deleteUser(Long id) throws Exception {
        User user = getUserById(id);
        repo.delete(user);
    }

    /**
     * Map everithing but the password
     *
     * @param from
     * @param to
     */
    protected void mapUser(User from, User to) {
        to.setUsername(from.getUsername());
        to.setFirstName(from.getFirstName());
        to.setLastName(from.getLastName());
        to.setEmail(from.getEmail());
        to.setRoles(from.getRoles());
        //to.setPassword(from.getPassword());
    }

    public User changePassword(ChangePasswordForm form) throws Exception {
        User user = getUserById(form.getId());

        if (!user.getPassword().equals(form.getCurrentPassword())) {
            throw new Exception("Current Password invalid");
        }
        if (user.getPassword().equals(form.getNewPassword())) {
            throw new Exception("Your new password could be different to actual");
        }
        if (!form.getNewPassword().equals(form.getConfirmPassword())) {
            throw new Exception("Passwords doesn't match");
        }
        user.setPassword(form.getNewPassword());
        return repo.save(user);
    }
}
