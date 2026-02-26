package com.paybridge.mappers;

import org.springframework.stereotype.Component;
import com.paybridge.dto.UserDTO;
import com.paybridge.entities.UsersEntity;
import java.time.LocalDateTime;

@Component
public class UserMapper {

    public UsersEntity mapDTOToEntity(UserDTO userDTO) {
        UsersEntity userEntity = new UsersEntity();
        userEntity.setUserId(userDTO.getUserId());
        userEntity.setName(userDTO.getName());
        userEntity.setEmail(userDTO.getEmail());
        userEntity.setPassword(userDTO.getPassword());
        userEntity.setCreatedAt(
                userDTO.getCreatedAt() != null ? LocalDateTime.parse(userDTO.getCreatedAt()) : LocalDateTime.now());
        userEntity.setUpdatedAt(
                userDTO.getUpdatedAt() != null ? LocalDateTime.parse(userDTO.getUpdatedAt()) : LocalDateTime.now());
        return userEntity;
    }

    public UserDTO mapEntityToDTO(UsersEntity userEntity) {
        UserDTO userDTO = new UserDTO();
        userDTO.setUserId(userEntity.getUserId());
        userDTO.setName(userEntity.getName());
        userDTO.setEmail(userEntity.getEmail());
        userDTO.setPassword(userEntity.getPassword());
        userDTO.setCreatedAt(userEntity.getCreatedAt() != null ? userEntity.getCreatedAt().toString() : null);
        userDTO.setUpdatedAt(userEntity.getUpdatedAt() != null ? userEntity.getUpdatedAt().toString() : null);
        return userDTO;
    }
}
