package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.TransactionDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.mapper.TransactionMapper;
import com.petprojects.mymoneyproject.model.Transaction;
import com.petprojects.mymoneyproject.model.TransactionsType;
import com.petprojects.mymoneyproject.model.User;
import com.petprojects.mymoneyproject.model.Wallet;
import com.petprojects.mymoneyproject.repository.CategoryRepository;
import com.petprojects.mymoneyproject.repository.TransactionRepository;
import com.petprojects.mymoneyproject.repository.WalletRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionService extends GenericService<Transaction, TransactionDTO> {

    protected TransactionRepository transactionRepository;
    private final WalletRepository walletRepository;
    private final CategoryRepository categoryRepository;

    public TransactionService(TransactionRepository transactionRepository,
                              TransactionMapper transactionMapper,
                              WalletRepository walletRepository,
                              CategoryRepository categoryRepository) {
        super(transactionRepository, transactionMapper);
        this.transactionRepository = transactionRepository;
        this.walletRepository = walletRepository;
        this.categoryRepository = categoryRepository;
    }

    public void makeExpenseTransaction(Long selectedWalletId,
                                       String amount,
                                       String description,
                                       Long categoryID,
                                       Authentication authentication) {
        // Конвертируем строку ("100,50") в копейки (Long) с помощью BigDecimal
        String normalized = amount.replace(",", ".");
        java.math.BigDecimal decimalAmount = new java.math.BigDecimal(normalized);
        java.math.BigDecimal centsAmount = decimalAmount.multiply(new java.math.BigDecimal("100"));
        Long longBalance = centsAmount.setScale(0, java.math.RoundingMode.HALF_UP).longValue();

        String currentUsername = authentication.getName();

        Transaction transaction = new Transaction();
        transaction.setCreatedBy(currentUsername);
        transaction.setAmount(longBalance);

        Wallet fromWallet = walletRepository.getReferenceById(selectedWalletId);
        transaction.setFromWallet(fromWallet);

        User user = fromWallet.getUser();
        Wallet toWallet = walletRepository.findAllByUserId(user.getId())
                .stream()
                .filter(wallet -> wallet.getName().equals("Внешний мир"))
                .filter(wallet -> wallet.getUser().getLogin().equals(currentUsername))
                .filter(wallet -> wallet.getCreatedBy().equals("System"))
                .toList().get(0);

        transaction.setToWallet(toWallet);
        transaction.setType(TransactionsType.EXPENSE);
        transaction.setUser(user);
        transaction.setDescription(description);
        transaction.setCategory(categoryRepository.getReferenceById(categoryID));
        repository.save(transaction);

        fromWallet.setBalance(fromWallet.getBalance() - longBalance);
        fromWallet.setUpdatedBy(currentUsername);
        walletRepository.save(fromWallet);
    }

    public void makeIncomeTransaction(Long selectedWalletId,
                                      String amount,
                                      String description,
                                      Long categoryID,
                                      Authentication authentication) {
        // Конвертируем строку ("100,50") в копейки (Long) с помощью BigDecimal
        String normalized = amount.replace(",", ".");
        java.math.BigDecimal decimalAmount = new java.math.BigDecimal(normalized);
        java.math.BigDecimal centsAmount = decimalAmount.multiply(new java.math.BigDecimal("100"));
        Long longBalance = centsAmount.setScale(0, java.math.RoundingMode.HALF_UP).longValue();

        String currentUsername = authentication.getName();

        Transaction transaction = new Transaction();
        transaction.setCreatedBy(currentUsername);
        transaction.setAmount(longBalance);

        Wallet toWallet = walletRepository.getReferenceById(selectedWalletId);
        transaction.setToWallet(toWallet);

        User user = toWallet.getUser();
        Wallet fromWallet = walletRepository.findAllByUserId(user.getId())
                .stream()
                .filter(wallet -> wallet.getName().equals("Внешний мир"))
                .filter(wallet -> wallet.getUser().getLogin().equals(currentUsername))
                .filter(wallet -> wallet.getCreatedBy().equals("System"))
                .toList().get(0);

        transaction.setFromWallet(fromWallet);
        transaction.setType(TransactionsType.INCOME);
        transaction.setUser(user);
        transaction.setDescription(description);
        transaction.setCategory(categoryRepository.getReferenceById(categoryID));
        repository.save(transaction);

        toWallet.setBalance(toWallet.getBalance() + longBalance);
        toWallet.setUpdatedBy(currentUsername);
        walletRepository.save(toWallet);
    }

    public Page<TransactionDTO> getPageMyTransactionsById(Pageable pageable, Long id) {
        Page<Transaction> transactionsPaginated = transactionRepository.findAllByUserId(id, pageable);
        List<TransactionDTO> result = mapper.toDTOs(transactionsPaginated.getContent());
        return new PageImpl<>(result, pageable, transactionsPaginated.getTotalElements());
    }

    public void makeTransferTransaction(Long fromWalletId,
                                        String amount,
                                        Long toWalletId,
                                        Authentication authentication) {
        // Конвертируем строку ("100,50") в копейки (Long) с помощью BigDecimal
        String normalized = amount.replace(",", ".");
        java.math.BigDecimal decimalAmount = new java.math.BigDecimal(normalized);
        java.math.BigDecimal centsAmount = decimalAmount.multiply(new java.math.BigDecimal("100"));
        Long longBalance = centsAmount.setScale(0, java.math.RoundingMode.HALF_UP).longValue();

        String currentUsername = authentication.getName();

        Transaction transaction = new Transaction();
        transaction.setCreatedBy(currentUsername);
        transaction.setAmount(longBalance);

        Wallet fromWallet = walletRepository.getReferenceById(fromWalletId);
        Wallet toWallet = walletRepository.getReferenceById(toWalletId);

        transaction.setFromWallet(fromWallet);
        transaction.setToWallet(toWallet);

        User user = toWallet.getUser();

        transaction.setType(TransactionsType.TRANSFER);
        transaction.setUser(user);
        repository.save(transaction);

        fromWallet.setBalance(fromWallet.getBalance() - longBalance);
        fromWallet.setUpdatedBy(currentUsername);
        walletRepository.save(fromWallet);
        toWallet.setBalance(toWallet.getBalance() + longBalance);
        toWallet.setUpdatedBy(currentUsername);
        walletRepository.save(toWallet);
    }

    public Page<TransactionDTO> getAllTransactions(Pageable pageable){
        Page<Transaction> transactionsPaginated = repository.findAll(pageable);
        List<TransactionDTO> transactionDTOList = mapper.toDTOs(transactionsPaginated.getContent());
        return new PageImpl<>(transactionDTOList, pageable, transactionsPaginated.getTotalElements());
    }
}
