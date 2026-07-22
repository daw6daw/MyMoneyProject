package com.petprojects.mymoneyproject.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@MappedSuperclass
public class GenericModel {

    @Id
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "default_generator")
    private Long id;

    @Column(name = "created_when", nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdWhen;

    @Column(name = "updated_when")
    @UpdateTimestamp
    private LocalDateTime updatedWhen;

    @Column(name = "created_by", nullable = false, updatable = false)
    private String createdBy;

    @Column(name = "updated_by")
    private String updatedBy;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted = false;

    @Column(name = "deleted_when")
    private LocalDateTime deletedWhen;

    @Column(name = "deleted_by")
    private String deletedBy;

    @Column(name = "restored_when")
    private LocalDateTime restoredWhen;
}
