package com.petprojects.mymoneyproject.repository;

import com.petprojects.mymoneyproject.model.Category;
import com.petprojects.mymoneyproject.model.Transaction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface CategoryRepository extends GenericRepository<Category> {
    Page<Category> findAllByUserId(Long userId, Pageable pageable);
    List<Category> findAllByUserId(Long userId);

    @Query(value = "SELECT c.* FROM categories c " +
            "WHERE (:id IS NULL OR c.id = :id) AND " +
            "(:createdBy IS NULL OR :createdBy = '' OR c.created_by ILIKE CONCAT('%', :createdBy, '%')) AND " +

            "(CAST(:createdWhen AS text) IS NULL OR c.created_when::date = CAST(:createdWhen AS timestamp)::date) AND " +
            "(:name IS NULL OR :name = '' OR c.name ILIKE CONCAT('%', :name, '%')) AND " +
            "(:type IS NULL OR :type = '' OR c.type = :type) AND " +
            "(:isDeleted IS NULL OR c.is_deleted = :isDeleted)  " ,


//            "(:fromWallet IS NULL OR t.from_wallet_id = :fromWallet) AND " + // Простое и быстрое сравнение чисел
//            "(:toWallet IS NULL OR t.to_wallet_id = :toWallet)",
            nativeQuery = true)
    List<Category> findCategories(
            @Param("id") Long id,
            @Param("createdBy") String createdBy,
            @Param("createdWhen") LocalDateTime createdWhen,
            @Param("name") String name,
            @Param("type") String type,
            @Param("isDeleted") Boolean isDeleted
    );
}
