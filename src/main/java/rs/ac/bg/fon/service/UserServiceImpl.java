package rs.ac.bg.fon.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import rs.ac.bg.fon.entity.Role;
import rs.ac.bg.fon.entity.Ticket;
import rs.ac.bg.fon.entity.User;
import rs.ac.bg.fon.repository.RoleRepository;
import rs.ac.bg.fon.repository.UserRepository;
import rs.ac.bg.fon.utility.ApiResponse;

import org.springframework.transaction.annotation.Transactional;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username);
        if (user == null) {
            log.error("User not found in database");
            throw new UsernameNotFoundException("User not found in database");
        } else {
            log.info("User found in database: {}", username);
        }
        Collection<SimpleGrantedAuthority> authorities = new ArrayList<>();
        user.getRoles().forEach(role -> {
            authorities.add(new SimpleGrantedAuthority(role.getName()));
        });

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), authorities);
    }

    @Override
    public User saveUser(User user) {
        log.info("Saving user {}  to database", user.getName());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    @Override
    public Role saveRole(Role role) {
        log.info("Saving role {} to database", role.getName());
        return roleRepository.save(role);
    }

    @Override
    public void addRoleToUser(String username, String roleName) {
        log.info("Adding role {} to user {}", roleName, username);
        User user = userRepository.findByUsername(username);
        Role role = roleRepository.findByName(roleName);
        user.getRoles().add(role);
    }

    @Transactional
    @Override
    public User getUser(String username) {
        log.info("Fetcing  user {}", username);
        return userRepository.findByUsername(username);
    }

    @Override
    public User getUser(Integer userId) {
        log.info("Fetcing  user {}", userId);
        return userRepository.findById(userId).get();
    }

    @Override
    public List<User> getUsers() {
        log.info("Fetcing all users");
        return userRepository.findAll();
    }

    @Override
    public User registerUser(User user) {
        if (!userRepository.existsByEmail(user.getEmail())) {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            System.out.println(user.getPassword());
            userRepository.save(user);
            addRoleToUser(user.getUsername(), "ROLE_CLIENT");
        }
        return null;
    }

    @Override
    public User deleteUser(String username) {
        User user = userRepository.findByUsername(username);
        userRepository.delete(user);
        return user;
    }


    @Override
    public ApiResponse<?> deleteUserApiResponse(String username) {
        ApiResponse<User> response = new ApiResponse<>();
        try {
            response.setData(deleteUser(username));
            response.addInfoMessage("Successfully deleted user \"" + username + "\".");
        } catch (Exception e) {
            response.addErrorMessage("Unable to delete user at this time, try again later!");
        }
        return response;
    }

    @Override
    public ApiResponse<?> registerUserApiResponse(User user) {
        ApiResponse<User> response = new ApiResponse<>();
        try {
            response.setData(registerUser(user));
            response.addInfoMessage("Successfully registered!\nWelcome " + user.getUsername() + "!");
        } catch (Exception e) {
            response.addErrorMessage("Unable to register user at this time, try again later!");
        }
        return response;
    }

    @Override
    public ApiResponse<?> getUsersApiResponse() {
        ApiResponse<List<User>> response = new ApiResponse<>();
        try {
            response.setData(getUsers());
        } catch (Exception e) {
            response.addErrorMessage("Unable to get users at this time, try again later!");
        }
        return response;
    }

    @Override
    public ApiResponse<?> getUserApiResponse(String username) {
        ApiResponse<User> response = new ApiResponse<>();
        try {
            response.setData(getUser(username));
        } catch (Exception e) {
            response.addErrorMessage("Unable to get user at this time, try again later!");
        }
        return response;
    }

    @Override
    public ApiResponse<?> addRoleToUserApiResponse(String username, String roleName) {
        ApiResponse<User> response = new ApiResponse<>();
        try {
            addRoleToUser(username, roleName);
            response.addInfoMessage("Successfully added role " + roleName + "to user " + username + "!");
        } catch (Exception e) {
            response.addErrorMessage("Unable to add role " + roleName + " to user " + username + " at this time, try again later!");
        }
        return response;
    }

    @Override
    public ApiResponse<?> saveRoleApiResponse(Role role) {
        ApiResponse<Role> response = new ApiResponse<>();
        try {
            response.setData(saveRole(role));
            response.addInfoMessage("Successfully added new role " + role.getName() + "!");
        } catch (Exception e) {
            response.addErrorMessage("Unable to add new role " + role.getName() + " at this time, try again later!");
        }
        return response;
    }

    @Override
    public User updateUser(User updatedUser) {
        User user = userRepository.findByUsername(updatedUser.getUsername());
        updatedUser.setId(user.getId());
        return userRepository.save(updatedUser);
    }

    @Override
    public ApiResponse<?> updateUserApiResponse(User user) {
        ApiResponse<User> response = new ApiResponse<>();
        try {
            response.setData(updateUser(user));
            response.addInfoMessage("Successfully updated user " + user.getUsername() + "!");
        } catch (Exception e) {
            response.addErrorMessage("Unable to update user " + user.getUsername() + " at this time, try again later!");
        }
        return response;
    }


}
