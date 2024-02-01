package com.routerecommendationbackend.repositories;

import com.routerecommendationbackend.DTOs.FriendshipDTO;
import com.routerecommendationbackend.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface FriendshipRepository extends JpaRepository<FriendshipDTO, Long> {

    @Query("SELECT fs.user02 FROM FriendshipDTO fs WHERE fs.user01.username = :username AND fs.status = 1")
    List<User> findAllByUser01_UsernameCompleted(String username);

    @Query("SELECT fs.user01 FROM FriendshipDTO fs WHERE fs.user02.username = :username AND fs.status = 1")
    List<User> findAllByUser02_UsernameCompleted(String username);

    @Query("SELECT fs.user02 FROM FriendshipDTO fs WHERE fs.user01.username = :username AND fs.status = 0")
    List<User> findAllByUser01_UsernamePending(String username);

    @Query("SELECT fs.user01 FROM FriendshipDTO fs WHERE fs.user02.username = :username AND fs.status = 0")
    List<User> findAllByUser02_UsernamePending(String username);

    @Query("SELECT fs FROM FriendshipDTO fs WHERE fs.user01.username = :username01 AND fs.user02.username = :username02")
    Optional<FriendshipDTO> findByUser01AndUser02(String username01, String username02);

//    @Query("SELECT fs FROM FriendshipDTO fs WHERE ((fs.user01.username = :username01 AND fs.user02.username = :username02) OR (fs.user01.username = :username02 AND fs.user02.username = :username01)) AND fs.status = 2")
//    Optional<List<FriendshipDTO>> findByUsername01AndUsername02AndStatusDeclined(String username01, String username02);

    @Query("SELECT fs FROM FriendshipDTO fs WHERE fs.user01.username = :username01 AND fs.user02.username = :username02 AND fs.status = 0")
    FriendshipDTO findByUser01AndUser02Accept(String username01, String username02);

    @Query("SELECT fs FROM FriendshipDTO fs WHERE ((fs.user01.username = :username01 AND fs.user02.username = :username02) OR (fs.user01.username = :username02 AND fs.user02.username = :username01)) AND (fs.status = 0 OR fs.status = 1)")
    FriendshipDTO deleteFriendshipByUsername01AndUsername02(String username01, String username02);





}
