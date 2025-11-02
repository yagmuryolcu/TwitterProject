package com.workintech.twitter.service;

import com.workintech.twitter.dto.patchrequest.UsersPatchRequestDto;
import com.workintech.twitter.dto.request.UsersRequestDto;
import com.workintech.twitter.dto.response.UsersResponseDto;
import com.workintech.twitter.entity.Users;
import com.workintech.twitter.mapper.UsersMapper;
import com.workintech.twitter.repository.UsersRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UsersServiceImpl implements UsersService{

    private final UsersRepository usersRepository;
    private final UsersMapper usersMapper;


    @Autowired
    public UsersServiceImpl(UsersRepository usersRepository, UsersMapper usersMapper){
        this.usersRepository=usersRepository;
        this.usersMapper=usersMapper;
    }


    @Override
    public List<UsersResponseDto> getAll() {
        return usersRepository
                .findAll()
                .stream()
                .map(usersMapper::toResponseDto)
                .toList();
    }

    @Override
    public UsersResponseDto findById(Long id) {

        Optional<Users> optionalUser = usersRepository
                .findById(id);

        if(optionalUser.isPresent()){

            Users user = optionalUser.get();

            return usersMapper.toResponseDto(user);
        }

        throw new RuntimeException(id + "id'li kullanıcı bulunamadı");
    }

    @Override
    public UsersResponseDto create(UsersRequestDto userRequestDto) {
        Users users = usersMapper.toEntity(userRequestDto);
        users= usersRepository.save(users);
        return usersMapper.toResponseDto(users);
    }

    @Override
    public UsersResponseDto replaceOrCreate(Long id, UsersRequestDto userRequestDto) {
        Users users = usersMapper.toEntity(userRequestDto);
        Optional<Users> optionalUsers =usersRepository.findById(id);

        if (optionalUsers.isPresent()){
            users.setId(id);
            usersRepository.save(users);
            return usersMapper.toResponseDto(users);
        }
            usersRepository.save(users);
            return usersMapper.toResponseDto(users);
    }

    @Override
    public UsersResponseDto update(Long id, UsersPatchRequestDto userPatchRequestDto) {
        Users userToUpdate = usersRepository
                .findById(id)
                .orElseThrow(()-> new RuntimeException(id + "id'li kullanici bulunamadi"));

        userToUpdate = usersMapper.updateEntity(userToUpdate, userPatchRequestDto);

        usersRepository.save(userToUpdate);

        return usersMapper.toResponseDto(userToUpdate);
    }

    @Override
    public void deleteById(Long id) {
        usersRepository.deleteById(id);
    }
}
