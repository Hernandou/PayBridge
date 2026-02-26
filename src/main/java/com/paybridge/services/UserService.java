package com.paybridge.services;

import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import com.paybridge.repository.UserRepository;
import java.util.Optional;
import com.paybridge.entities.UsersEntity;
import com.paybridge.mappers.UserMapper;
import com.paybridge.dto.UserDTO;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserMapper userMapper;

    public UserDTO getUserById(Long userId) {
        Optional<UsersEntity> user = this.userRepository.findById(userId);
        if (user.isPresent()) {
            return this.userMapper.mapEntityToDTO(user.get());
        } else {
            throw new RuntimeException("User not found");
        }

    }

}
