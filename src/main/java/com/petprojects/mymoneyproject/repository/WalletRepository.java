package com.petprojects.mymoneyproject.repository;

import com.petprojects.mymoneyproject.model.User;
import com.petprojects.mymoneyproject.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface WalletRepository extends GenericRepository<Wallet>{
    Page<Wallet> findAllByUserId(Long userId, Pageable pageable);
    List<Wallet> findAllByUserId(Long userId);

    @Query(value = "SELECT w.* FROM wallets w " +
            "LEFT JOIN users u ON w.user_id = u.id " + // Присоединяем таблицу пользователей
            "WHERE (:id IS NULL OR w.id = :id) AND " +
            "(:name IS NULL OR :name = '' OR w.name ILIKE CONCAT('%', :name, '%')) AND " +
            "(:isDeleted IS NULL OR w.is_deleted = :isDeleted) AND " +
            "(:createdBy IS NULL OR :createdBy = '' OR w.created_by ILIKE CONCAT('%', :createdBy, '%')) AND " +
            "(CAST(:createdWhen AS text) IS NULL OR w.created_when::date = CAST(:createdWhen AS timestamp)::date) AND " +
            "(:updatedBy IS NULL OR :updatedBy = '' OR w.updated_by ILIKE CONCAT('%', :updatedBy, '%')) AND " +
            "(CAST(:updatedWhen AS text) IS NULL OR w.updated_when::date = CAST(:updatedWhen AS timestamp)::date) AND " +
            "(:deletedBy IS NULL OR :deletedBy = '' OR w.deleted_by ILIKE CONCAT('%', :deletedBy, '%')) AND " +
            "(CAST(:deletedWhen AS text) IS NULL OR w.deleted_when::date = CAST(:deletedWhen AS timestamp)::date) AND " +
            "(CAST(:restoredWhen AS text) IS NULL OR w.restored_when::date = CAST(:restoredWhen AS timestamp)::date) AND " +
            "(:userLogin IS NULL OR :userLogin = '' OR u.login = :userLogin)", // Ищем по логину, если он передан
            nativeQuery = true)
    List<Wallet> findWallets(
            @Param("id") Long id,
            @Param("createdBy") String createdBy,
            @Param("createdWhen") LocalDateTime createdWhen,
            @Param("updatedBy") String updatedBy,
            @Param("updatedWhen") LocalDateTime updatedWhen,
            @Param("isDeleted") Boolean isDeleted,
            @Param("deletedBy") String deletedBy,
            @Param("deletedWhen") LocalDateTime deletedWhen,
            @Param("restoredWhen") LocalDateTime restoredWhen,
            @Param("name") String name,
            @Param("userLogin") String userLogin // Заменили userId на userLogin
    );

}
