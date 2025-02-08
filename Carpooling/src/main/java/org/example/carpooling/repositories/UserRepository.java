package org.example.carpooling.repositories;

import org.example.carpooling.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {
    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByUserId(int userId);

    Optional<User> findUserByEmail(String email);

    Optional<User> findUserByPhoneNumber(String phoneNumber);

    @Query("select u from User u where (:username is null or u.username like %:username%)" +
                                "and (:email is null or u.email like %:email%)" +
                                "and (:phoneNumber is null or u.phoneNumber like %:phoneNumber%)")
    User searchUsers(@Param("username") String username,
                     @Param("email") String email,
                     @Param("phoneNumber") String phoneNumber);
}
