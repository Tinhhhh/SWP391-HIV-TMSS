package com.swp391.hivtmss.model.payload.response;

import com.swp391.hivtmss.model.entity.Diagnosis;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import jakarta.persistence.*;

import java.util.List;

public class TestTypeResponse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "test_type_id")
    private Long id;

    @Column(name = "test_type_name", nullable = false)
    private String name;

    @Column(name = "test_type_description")
    private String description;

    @Column(name = "test_type_code", nullable = false, unique = true)
    private String code;

    @Column(name = "is_active", nullable = false)
    @Enumerated(EnumType.STRING)
    private ActiveStatus isActive;

    @Column(name = "applicable", nullable = false)
    private String applicable;


}
