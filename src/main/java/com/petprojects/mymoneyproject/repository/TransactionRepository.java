package com.petprojects.mymoneyproject.repository;

import com.petprojects.mymoneyproject.model.Transaction;
import com.petprojects.mymoneyproject.model.TransactionsType;
import com.petprojects.mymoneyproject.model.Wallet;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface TransactionRepository extends GenericRepository<Transaction> {
    Page<Transaction> findAllByUserId(Long userId, Pageable pageable);
    List<Transaction> findAllByUserId(Long userId);

    @Query(value = "SELECT t.* FROM transactions t " +
            "LEFT JOIN users u ON t.user_id = u.id " +
            "LEFT JOIN categories c ON t.category_id = c.id " +
            "WHERE (:id IS NULL OR t.id = :id) AND " +
            "(CAST(:createdWhen AS text) IS NULL OR t.created_when::date = CAST(:createdWhen AS timestamp)::date) AND " +
            "(:userLogin IS NULL OR :userLogin = '' OR u.login ILIKE CONCAT('%', :userLogin, '%')) AND " +
            "(:type IS NULL OR :type = '' OR t.type = :type) AND " +
            "(:categoryName IS NULL OR :categoryName = '' OR c.name ILIKE CONCAT('%', :categoryName, '%')) AND" +
            "(:fromWallet IS NULL OR t.from_wallet_id = :fromWallet) AND " + // Простое и быстрое сравнение чисел
            "(:toWallet IS NULL OR t.to_wallet_id = :toWallet)",
            nativeQuery = true)
    List<Transaction> findTransactions(
            @Param("id") Long id,
            @Param("createdWhen") LocalDateTime createdWhen,
            @Param("userLogin") String userLogin,
            @Param("type") String type,
            @Param("categoryName") String categoryName,
            @Param("fromWallet") Long fromWallet,
            @Param("toWallet") Long toWallet
    );

}
