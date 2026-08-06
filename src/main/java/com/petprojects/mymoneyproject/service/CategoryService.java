package com.petprojects.mymoneyproject.service;

import com.petprojects.mymoneyproject.DTO.CategoryDTO;
import com.petprojects.mymoneyproject.DTO.TransactionDTO;
import com.petprojects.mymoneyproject.DTO.WalletDTO;
import com.petprojects.mymoneyproject.mapper.CategoryMapper;
import com.petprojects.mymoneyproject.mapper.UserMapper;
import com.petprojects.mymoneyproject.model.CategoriesTypes;
import com.petprojects.mymoneyproject.model.Category;
import com.petprojects.mymoneyproject.model.Wallet;
import com.petprojects.mymoneyproject.repository.CategoryRepository;
import com.petprojects.mymoneyproject.repository.UserRepository;
import com.petprojects.mymoneyproject.service.userdetails.CustomUserDetails;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class CategoryService extends GenericService<Category, CategoryDTO> {

    protected CategoryRepository categoryRepository;
    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public CategoryService(CategoryRepository categoryRepository,
                           CategoryMapper categoryMapper,
                           UserMapper userMapper,
                           UserRepository userRepository) {
        super(categoryRepository, categoryMapper);
        this.categoryRepository = categoryRepository;
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    public void createExpense(CategoryDTO object,
                              Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Category category = mapper.toEntity(object);
        category.setUser(userRepository.findById(userDetails.getUserId().longValue()).orElseThrow(() -> new EntityNotFoundException("User not found")));
        category.setType(CategoriesTypes.EXPENSE);
        category.setCreatedBy(category.getUser().getLogin());
        repository.save(category);
    }

    public void createIncome(CategoryDTO object,
                             Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
        Category category = mapper.toEntity(object);
        category.setUser(userRepository.findById(userDetails.getUserId().longValue()).orElseThrow(() -> new EntityNotFoundException("User not found")));
        category.setType(CategoriesTypes.INCOME);
        category.setCreatedBy(category.getUser().getLogin());
        repository.save(category);
    }

    public List<CategoryDTO> getAllExpense(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 1. Получаем логин авторизованного пользователя ОДИН раз (из сессии, без запросов в БД)
        String currentLogin = userDetails.getUsername();

        // 2. Достаем из БД ВСЕ категории, фильтруем стримом по типу и по логину
        List<Category> filteredCategories = repository.findAll().stream()
                .filter(category -> category.getType() == CategoriesTypes.EXPENSE)
                .filter(category -> category.getUser().getLogin().equals(currentLogin))
                .filter(category -> !category.isDeleted())
                .toList();

        // 3. Передаем чистый, уже отфильтрованный список в маппер
        return mapper.toDTOs(filteredCategories);
    }

    public List<CategoryDTO> getAllIncome(Authentication authentication) {
        CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

        // 1. Получаем логин авторизованного пользователя ОДИН раз (из сессии, без запросов в БД)
        String currentLogin = userDetails.getUsername();

        // 2. Достаем из БД ВСЕ категории, фильтруем стримом по типу и по логину
        List<Category> filteredCategories = repository.findAll().stream()
                .filter(category -> category.getType() == CategoriesTypes.INCOME)
                .filter(category -> category.getUser().getLogin().equals(currentLogin))
                .filter(category -> !category.isDeleted())
                .toList();

        // 3. Передаем чистый, уже отфильтрованный список в маппер
        return mapper.toDTOs(filteredCategories);
    }

    public void delete (Long id,
                        Authentication authentication) {
        String currentUsername = authentication.getName();

        Category category = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("Категория не найдена с ID: " + id));
        category.setDeleted(true);
        category.setUpdatedBy(currentUsername);
        category.setDeletedBy(currentUsername);
        category.setDeletedWhen(LocalDateTime.now());
        repository.save(category);
    }

    public void editCategory(CategoryDTO categoryDTO,
                           Authentication authentication) {
        Category oldCategory = repository.findById(categoryDTO.getId()).orElseThrow(() -> new IllegalArgumentException("Категория не найдена" ));

        String currentUsername = authentication.getName();
        oldCategory.setName(categoryDTO.getName());
        oldCategory.setUpdatedBy(currentUsername);

        repository.save(oldCategory);
    }

    public Page<CategoryDTO> getAllCategories(Pageable pageable){
        Page<Category> categoryPaginated = repository.findAll(pageable);
        List<CategoryDTO> categoryDTOList = mapper.toDTOs(categoryPaginated.getContent());
        return new PageImpl<>(categoryDTOList, pageable, categoryPaginated.getTotalElements());
    }

    public void restore (Long id) {
        Category category = repository.findById(id).orElseThrow(() -> new IllegalArgumentException("category не найден с ID: " + id));

        category.setDeleted(false);
        category.setRestoredWhen(LocalDateTime.now());

        repository.save(category);
    }

    public List<CategoryDTO> findCategories(CategoryDTO categoryDTO,
                                            Boolean finalDeletedStatus) {
        String typeParam = (categoryDTO.getType() != null) ? categoryDTO.getType().name() : null;



        return mapper.toDTOs(categoryRepository.findCategories(
                categoryDTO.getId(),
                categoryDTO.getCreatedBy(),
                categoryDTO.getCreatedWhen(),
                categoryDTO.getName(),
                typeParam,
                finalDeletedStatus
            //TODO: сделать админу возможность менять роль пользователям, подумать, что нужно из поиска реализовать для пользователя и нужно ли это для пет проекта?
        ));
    }
}
