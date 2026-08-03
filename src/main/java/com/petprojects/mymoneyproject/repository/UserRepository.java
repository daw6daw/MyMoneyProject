package com.petprojects.mymoneyproject.repository;

import com.petprojects.mymoneyproject.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface UserRepository extends GenericRepository<User>{

    User findUserByLogin(String login);

    User findUserByEmail(String email);

    @Query(value = "SELECT u.* FROM users u WHERE " +
            "(:id IS NULL OR u.id = :id) AND " +
            "(:email IS NULL OR :email = '' OR u.email ILIKE CONCAT('%', :email, '%')) AND " +
            "(:login IS NULL OR :login = '' OR u.login ILIKE CONCAT('%', :login, '%')) AND " +
            "(:lastName IS NULL OR :lastName = '' OR u.last_name ILIKE CONCAT('%', :lastName, '%')) AND " +
            "(:firstName IS NULL OR :firstName = '' OR u.first_name ILIKE CONCAT('%', :firstName, '%')) AND " +
            "(:middleName IS NULL OR :middleName = '' OR u.middle_name ILIKE CONCAT('%', :middleName, '%')) AND " +
            "(:role IS NULL OR u.role_id = :role) AND " +
            "(:isDeleted IS NULL OR u.is_deleted = :isDeleted) AND " +
            "(:number IS NULL OR :number = '' OR u.number ILIKE CONCAT('%', :number, '%')) AND " +
            "(CAST(:createdWhen AS text) IS NULL OR u.created_when::date = CAST(:createdWhen AS timestamp)::date) AND " +
            "(:updatedBy IS NULL OR :updatedBy = '' OR u.updated_by ILIKE CONCAT('%', :updatedBy, '%')) AND " +
            "(CAST(:updatedWhen AS text) IS NULL OR u.updated_when::date = CAST(:updatedWhen AS timestamp)::date) AND " +
            "(:deletedBy IS NULL OR :deletedBy = '' OR u.deleted_by ILIKE CONCAT('%', :deletedBy, '%')) AND " +
            "(CAST(:deletedWhen AS text) IS NULL OR u.deleted_when::date = CAST(:deletedWhen AS timestamp)::date) AND " +
            "(CAST(:restoredWhen AS text) IS NULL OR u.restored_when::date = CAST(:restoredWhen AS timestamp)::date)",
            nativeQuery = true)
    List<User> searchUsers(
            @Param("id") Long id,
            @Param("email") String email,
            @Param("login") String login,
            @Param("lastName") String lastName,
            @Param("firstName") String firstName,
            @Param("middleName") String middleName,
            @Param("role") Long role,
            @Param("isDeleted") boolean isDeleted,
            @Param("number") String number,
            @Param("createdWhen") java.time.LocalDateTime createdWhen,
            @Param("updatedBy") String updatedBy,
            @Param("updatedWhen") java.time.LocalDateTime updatedWhen,
            @Param("deletedBy") String deletedBy,
            @Param("deletedWhen") java.time.LocalDateTime deletedWhen,
            @Param("restoredWhen") java.time.LocalDateTime restoredWhen
    );





    //todo: сделать поиск по большему количеству полей
}
