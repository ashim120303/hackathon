package com.pack.project.repos;

import com.pack.project.models.Tender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TenderRepository extends JpaRepository<Tender, Long> {
    // Методы для работы с данными...
}