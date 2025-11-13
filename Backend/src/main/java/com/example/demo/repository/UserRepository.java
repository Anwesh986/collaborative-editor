package com.example.demo.repository;

import com.example.demo.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    
    // Custom SQL query to find user by email
    @Query(value = "SELECT * FROM users WHERE email = :email", nativeQuery = true)
    Optional<User> findByEmail(@Param("email") String email);
    
    // Custom SQL query to find user by username
    @Query(value = "SELECT * FROM users WHERE username = :username", nativeQuery = true)
    Optional<User> findByUsername(@Param("username") String username);
    
    // Custom SQL query to find user by verification token
    @Query(value = "SELECT * FROM users WHERE verification_token = :token", nativeQuery = true)
    Optional<User> findByVerificationToken(@Param("token") String verificationToken);
    
    // Custom SQL query to check if email exists
    @Query(value = "SELECT COUNT(*) > 0 FROM users WHERE email = :email", nativeQuery = true)
    boolean existsByEmail(@Param("email") String email);
    
    // Custom SQL query to check if username exists
    @Query(value = "SELECT COUNT(*) > 0 FROM users WHERE username = :username", nativeQuery = true)
    boolean existsByUsername(@Param("username") String username);
    
    // Custom SQL query to insert a temporary user (without password)
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users (email, first_name, last_name, username, is_verified, verification_token, verification_token_expiry, created_at) " +
           "VALUES (:email, :firstName, :lastName, :username, :isVerified, :verificationToken, :verificationTokenExpiry, :createdAt)", 
           nativeQuery = true)
    void insertTempUser(@Param("email") String email, 
                       @Param("firstName") String firstName,
                       @Param("lastName") String lastName,
                       @Param("username") String username, 
                       @Param("isVerified") boolean isVerified, 
                       @Param("verificationToken") String verificationToken, 
                       @Param("verificationTokenExpiry") LocalDateTime verificationTokenExpiry, 
                       @Param("createdAt") LocalDateTime createdAt);
    
    // Custom SQL query to complete registration (set password and verify)
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET password = :password, is_verified = :isVerified, verification_token = NULL, verification_token_expiry = NULL " +
           "WHERE email = :email", 
           nativeQuery = true)
    void completeRegistration(@Param("email") String email, 
                             @Param("password") byte[] password, 
                             @Param("isVerified") boolean isVerified);
    
    // Custom SQL query to insert a new user
    @Modifying
    @Transactional
    @Query(value = "INSERT INTO users (email, username, password, is_verified, verification_token, verification_token_expiry, created_at) " +
           "VALUES (:email, :username, :password, :isVerified, :verificationToken, :verificationTokenExpiry, :createdAt)", 
           nativeQuery = true)
    void insertUser(@Param("email") String email, 
                   @Param("username") String username, 
                   @Param("password") byte[] password, 
                   @Param("isVerified") boolean isVerified, 
                   @Param("verificationToken") String verificationToken, 
                   @Param("verificationTokenExpiry") LocalDateTime verificationTokenExpiry, 
                   @Param("createdAt") LocalDateTime createdAt);
    
    // Custom SQL query to update user verification status
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET is_verified = :isVerified, verification_token = NULL, verification_token_expiry = NULL " +
           "WHERE verification_token = :token AND verification_token_expiry > :currentTime", 
           nativeQuery = true)
    int verifyUserByToken(@Param("token") String verificationToken, 
                         @Param("isVerified") boolean isVerified, 
                         @Param("currentTime") LocalDateTime currentTime);
    
    // Custom SQL query to update last login time
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET last_login_at = :loginTime WHERE email = :email", nativeQuery = true)
    void updateLastLogin(@Param("email") String email, @Param("loginTime") LocalDateTime loginTime);
    
    // Custom SQL query to update verification token
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET verification_token = :token, verification_token_expiry = :expiry " +
           "WHERE email = :email", nativeQuery = true)
    void updateVerificationToken(@Param("email") String email, 
                                @Param("token") String verificationToken, 
                                @Param("expiry") LocalDateTime verificationTokenExpiry);
    
    // Custom SQL query to delete expired verification tokens
    @Modifying
    @Transactional
    @Query(value = "UPDATE users SET verification_token = NULL, verification_token_expiry = NULL " +
           "WHERE verification_token_expiry < :currentTime", nativeQuery = true)
    void cleanupExpiredTokens(@Param("currentTime") LocalDateTime currentTime);
    
    // Custom SQL query to get user login attempts (for security)
    @Query(value = "SELECT last_login_at FROM users WHERE email = :email", nativeQuery = true)
    Optional<LocalDateTime> getLastLoginTime(@Param("email") String email);
}
