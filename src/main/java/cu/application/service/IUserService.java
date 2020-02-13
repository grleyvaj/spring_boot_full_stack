package cu.application.service;

import cu.application.dto.ChangePasswordForm;
import cu.application.model.User;

public interface IUserService {

    public Iterable<User> getAllUsers();

    public User createUser(User user) throws Exception;

    public User getUserById(Long id) throws Exception;

    public User updateUser(User user) throws Exception;

    public void deleteUser(Long id) throws Exception;

    public User changePassword(ChangePasswordForm form) throws Exception;
}
