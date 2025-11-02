package com.workintech.twitter.mapper;


import com.workintech.twitter.dto.patchrequest.UsersPatchRequestDto;
import com.workintech.twitter.dto.request.UsersRequestDto;
import com.workintech.twitter.dto.response.UsersResponseDto;
import com.workintech.twitter.entity.Users;
import org.springframework.stereotype.Component;

@Component
public class UsersMapper {

    public Users toEntity(UsersRequestDto usersRequestDto){

        Users users = new Users();
        users.setUsername(usersRequestDto.username());
        users.setEmail(usersRequestDto.email());
        users.setPassword(usersRequestDto.password());
        users.setFullName(usersRequestDto.fullName());
        users.setBio(usersRequestDto.bio());

         return users;
    }


    public UsersResponseDto  toResponseDto (Users users){

        return  new UsersResponseDto(
                users.getId(),
                users.getUsername(),
                users.getEmail(),
                users.getFullName(),
                users.getBio(),
                users.getCreatedAt()

        );

    }

    public Users updateEntity (Users usersToUpdate, UsersPatchRequestDto usersPatchRequestDto){

            if (usersPatchRequestDto.username() != null)
                usersToUpdate.setUsername(usersPatchRequestDto.username());

            if (usersPatchRequestDto.email() != null)
                usersToUpdate.setEmail(usersPatchRequestDto.email());

            if(usersPatchRequestDto.password() != null)
                usersToUpdate.setPassword(usersPatchRequestDto.password());

            if(usersPatchRequestDto.fullName() != null)
                usersToUpdate.setFullName(usersPatchRequestDto.fullName());

            if(usersPatchRequestDto.bio() != null)
                usersToUpdate.setBio(usersPatchRequestDto.bio());

            return usersToUpdate;


    }
}
