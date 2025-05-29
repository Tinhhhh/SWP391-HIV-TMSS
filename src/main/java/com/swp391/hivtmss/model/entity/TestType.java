package com.swp391.hivtmss.model.entity;

import com.swp391.hivtmss.model.payload.enums.availableStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "test_type")
public class TestType {

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

    @Column(name = "status", nullable = false)
    @Enumerated(EnumType.STRING)
    private availableStatus status;

    @Column(name = "applicable", nullable = false)
    private String applicable;
}
