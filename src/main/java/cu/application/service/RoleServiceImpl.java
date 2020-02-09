package cu.application.service;

import cu.application.model.Role;
import cu.application.repository.IRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements IRoleService {

    @Autowired
    IRoleRepository repo;

    @Override
    public Iterable<Role> getAllRole() {
        return repo.findAll();
    }
}
