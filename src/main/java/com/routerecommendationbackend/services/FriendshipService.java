package com.routerecommendationbackend.services;

import com.routerecommendationbackend.DTOs.FriendshipDTO;
import com.routerecommendationbackend.entities.User;
import com.routerecommendationbackend.enums.Status;
import com.routerecommendationbackend.exceptions.FriendRequestException;
import com.routerecommendationbackend.exceptions.RankingException;
import com.routerecommendationbackend.exceptions.UserNotFoundException;
import com.routerecommendationbackend.repositories.FriendshipRepository;
import com.routerecommendationbackend.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
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
    public List<User> searchFriends(String username, String search){

        List<User> listOfFriends =  this.getFriendsList(username);

        if(search.isBlank())
            return listOfFriends;

        List<User> searchedFriends = new ArrayList<>();

        for(User user: listOfFriends){
            if(user.getFullName().contains(search))
                searchedFriends.add(user);
        }

        return searchedFriends;
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

    public List<User> searchNonFriends(String username, String search){

        List<User> listOfNonFriends =  this.getNonFriendsList(username);

        if(search.isBlank())
            return listOfNonFriends;

        List<User> searchedNonFriends = new ArrayList<>();

        for(User user: listOfNonFriends){
            if(user.getFullName().contains(search) || user.getUsername().contains(search))
                searchedNonFriends.add(user);
        }

        return searchedNonFriends;
    }

    public List<User> getPendingFriendRequests(String username){


        return friendshipRepository.findAllByUser02_UsernamePending(username);
    }

    public FriendshipDTO sendFriendRequest(String username01, String username02) throws UserNotFoundException {

        User user01 = userRepository.findUserByUsername(username01);
        User user02 = userRepository.findUserByUsername(username02);

        if(user01 == null){
            throw new UserNotFoundException(username01);
        } else if (user02 == null) {
            throw new UserNotFoundException(username02);
        }

        if(friendshipRepository.findByUser01AndUser02(username01,username02).isPresent() || friendshipRepository.findByUser01AndUser02(username02,username01).isPresent()) {
            throw new FriendRequestException("Friendship between " + user01.getId() + " and " + user02.getId() + " already exists! ");
        }

        FriendshipDTO friendshipDTO = new FriendshipDTO(user01,user02);
        return friendshipRepository.save(friendshipDTO);
    }

    public FriendshipDTO acceptFriendRequest(String username01, String username02) throws FriendRequestException, UserNotFoundException {

        User user01 = userRepository.findUserByUsername(username01);
        User user02 = userRepository.findUserByUsername(username02);

        if(user01 == null){
            throw new UserNotFoundException(username01);
        } else if (user02 == null) {
            throw new UserNotFoundException(username02);
        }

        FriendshipDTO friendshipDTO = friendshipRepository.findByUser01AndUser02Accept(username01,username02);

        if(friendshipDTO == null){
            throw new FriendRequestException("Friend request between " + username01 + " and " + username02 + " does not exist!");
        }

        friendshipDTO.setStatus(Status.COMPLETED);

        return friendshipRepository.save(friendshipDTO);
    }


    public void deleteFriendShip(String username01, String username02) throws FriendRequestException{
        FriendshipDTO friendshipDTO = friendshipRepository.deleteFriendshipByUsername01AndUsername02(username01,username02);

        User user01 = userRepository.findUserByUsername(username01);
        User user02 = userRepository.findUserByUsername(username02);

        if(friendshipDTO == null){
            throw new FriendRequestException("Friendship between " + user01.getUsername() + " and " + user02.getUsername() + " does not exist!");
        }

//        friendshipDTO.setStatus(Status.DECLINED);
//        return friendshipRepository.save(friendshipDTO);

        friendshipRepository.deleteById(friendshipDTO.getId());
        friendshipRepository.delete(friendshipDTO);



    }

    public List<User> getFriendsRanking(String username){

        return  this.getFriendsList(username);
    }
}
