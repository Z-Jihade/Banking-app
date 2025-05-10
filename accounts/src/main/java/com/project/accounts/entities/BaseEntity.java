package com.project.accounts.entities;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Getter @Setter @ToString
public class BaseEntity {

    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(updatable = false)
    private String createdBy;

    @Column(insertable = false)
    private LocalDateTime updatedAt;

    @Column(insertable = false)
    private String updatedBy;

    @Column(updatable = false)
    private LocalDateTime deletedAt;

    @Column(updatable = false)
    private String deletedBy;
}
