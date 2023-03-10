package ru.kata.spring.boot_security.demo.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import ru.kata.spring.boot_security.demo.dao.RoleDaoImpl;
import ru.kata.spring.boot_security.demo.dao.UserDaoImpl;
import ru.kata.spring.boot_security.demo.model.Role;
import ru.kata.spring.boot_security.demo.model.User;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class UserServiceImpl{

    private final RoleDaoImpl roleDao;
    private final UserDaoImpl userDao;

    public PasswordEncoder bCryptPasswordEncoder() {
        return new BCryptPasswordEncoder(12);
    }

    @Autowired
    public UserServiceImpl(RoleDaoImpl roleDao, UserDaoImpl userDao) {
        this.roleDao = roleDao;
        this.userDao = userDao;
    }

    public void delete(Long id) {
        userDao.delete(id);
    }

    public void update(User us) {
        User userBas = findById(us.getId());
        System.out.println(userBas);
        System.out.println(us);
        if(!userBas.getPassword().equals(us.getPassword())) {
            us.setPassword(bCryptPasswordEncoder().encode(us.getPassword()));
        }
        userDao.update(us);
    }

    public boolean add(User user) {
        User userBas = userDao.findByName(user.getUsername());
        if(userBas != null) {
            return false;
        }
        user.setPassword(bCryptPasswordEncoder().encode(user.getPassword()));
        userDao.add(user);
        return true;
    }

    public List<User> listUsers() {
        return userDao.listUsers();
    }

    public User findById(Long id) {
        return userDao.findById(id);
    }

    public Role findByIdRole(Long id) {
        return roleDao.findByIdRole(id);
    }

    public List<Role> listRoles() {
        return roleDao.listRoles();
    }

    public Role findByNameRole(String name) {
        return roleDao.findByName(name);
    }

    public List<Role> listByRole(List<String> name) {
        return roleDao.listByName(name);
    }

    public boolean addRole(Role role) {
        Role userBas = roleDao.findByName(role.getRole());
        if(userBas != null) {
            return false;
        }
        roleDao.add(role);
        return true;
    }

//    public User findByUsername(String userName) {
//        return userDao.findByName(userName);
//    }

//    private Collection<? extends GrantedAuthority> aug(Collection<Role> roles) {
//        return roles.stream().map(r -> new SimpleGrantedAuthority(r.getRole()))
//                .collect(Collectors.toList());
//    }
}


