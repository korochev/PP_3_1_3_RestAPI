package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.UserDAO;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

    private UserDAO userDAO;
    private RoleService roleService;
    BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    public UserServiceImpl(UserDAO userDAO, RoleService roleService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDAO = userDAO;
        this.roleService = roleService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


    @Override
    public List<User> showUsers() {
        return userDAO.findAll();
    }

    @Override
    public User showById(long id) {
        return userDAO.findById((long) id).get();
    }

    @Override
    @Transactional
    public void saveUser(User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }

    @Override
    @Transactional
    public void createUser(User user) {
        userDAO.save(user);
    }

    @Transactional
    @Override
    public void updateUser(long id, User user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        userDAO.save(user);
    }


    @Override
    @Transactional
    public void delete(Long id) {
        userDAO.deleteById(id);
    }


    @Override
    public User findByUsername(String username) {
        return userDAO.findByEmail(username);
    }


    @Override
    public User setRolesToUser(User user, int[] rolesIdArr) {
        List<Role> userRoles = new ArrayList<>();
        if (rolesIdArr != null) {
            for (int i : rolesIdArr) {
                userRoles.add(roleService.getRoleById(i));
            }
        }
        user.setRoles(userRoles);
        return user;
    }

}
