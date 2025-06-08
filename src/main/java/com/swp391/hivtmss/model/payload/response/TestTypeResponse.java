package com.swp391.hivtmss.model.payload.response;

import com.swp391.hivtmss.model.entity.Diagnosis;
import com.swp391.hivtmss.model.payload.enums.ActiveStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TestTypeResponse {

    @Column(name = "test_type_id")
    private Long id;

    @Column(name = "test_type_name")
    private String name;

    @Column(name = "test_type_description")
    private String description;

    @Column(name = "test_type_code")
    private String code;

    @Column(name = "is_active")
    private ActiveStatus isActive;

    @Column(name = "applicable")
    private String applicable;



}
