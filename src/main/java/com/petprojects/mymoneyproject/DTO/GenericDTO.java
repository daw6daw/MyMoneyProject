package com.petprojects.mymoneyproject.DTO;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
public class GenericDTO {

    private Long id;
    private LocalDateTime createdWhen;
    private LocalDateTime updatedWhen;
    private String createdBy;
    private String updatedBy;
    private boolean isDeleted;
    private LocalDateTime deletedWhen;
    private String deletedBy;
    private LocalDateTime restoredWhen;
}
