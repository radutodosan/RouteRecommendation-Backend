package com.routerecommendationbackend.services;

import com.routerecommendationbackend.DTOs.FriendshipDTO;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.exceptions.FriendRequestExistsException;
import com.routerecommendationbackend.repositories.FriendshipRepository;
import com.routerecommendationbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class FriendshipService {
    private final FriendshipRepository friendshipRepository;
    private final UserRepository userRepository;

    public List<User> getFriendsList(String username){

        List<User> list01 =  friendshipRepository.findAllByUser01_UsernameCompleted(username);
        List<User> list02 =  friendshipRepository.findAllByUser02_UsernameCompleted(username);

        return Stream.concat(list01.stream(),list02.stream()).toList();
    }

    public List<User> getPendingFriendRequests(String username){


        return friendshipRepository.findAllByUser02_UsernamePending(username);
    }

    public List<User> getNonFriendsList(String username){
        List<User> nonFriendsList = userRepository.findAll();

        List<User> list01 =  friendshipRepository.findAllByUser01_UsernamePending(username);
        List<User> list02 =  friendshipRepository.findAllByUser02_UsernamePending(username);
        List<User> pendingFriendRequests = Stream.concat(list01.stream(),list02.stream()).toList();

        List <User> friendsList = Stream.concat(this.getFriendsList(username).stream(), pendingFriendRequests.stream()).toList();

        nonFriendsList.removeIf(friendsList::contains);

        nonFriendsList.removeIf(user -> Objects.equals(user.getUsername(), username));


        return nonFriendsList;
    }

    public FriendshipDTO sendFriendRequest(String username01, String username02) throws FriendRequestExistsException{

        User user01 = userRepository.findUserByUsername(username01);
        User user02 = userRepository.findUserByUsername(username02);

        if(friendshipRepository.findByUser01AndUser02(user01,user02).isPresent() || friendshipRepository.findByUser01AndUser02(user02,user01).isPresent()) {
            throw new FriendRequestExistsException("Friendship between " + user01.getUsername() + " and " + user02.getUsername() + " already exists!");
        }

        FriendshipDTO friendshipDTO = new FriendshipDTO(user01,user02);
        return friendshipRepository.save(friendshipDTO);
    }

    public FriendshipDTO acceptFriendRequest(String username01, String username02){
        FriendshipDTO friendshipDTO = friendshipRepository.findByUser01AndUser02Accept(username01,username02);

        if(friendshipDTO == null){
            throw new FriendRequestExistsException("Friend request between " + username01 + " and " + username02 + " does not exist!");
        }

        friendshipDTO.setStatus(Status.COMPLETED);

        return friendshipRepository.save(friendshipDTO);
    }
}
