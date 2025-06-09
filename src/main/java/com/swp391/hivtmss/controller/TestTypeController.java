package com.swp391.hivtmss.controller;

import com.swp391.hivtmss.model.payload.exception.ResponseBuilder;
import com.swp391.hivtmss.model.payload.request.TestTypeRequest;
import com.swp391.hivtmss.model.payload.response.TestTypeResponse;
import com.swp391.hivtmss.service.TestTypeService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.websocket.server.PathParam;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/test-types")
@RequiredArgsConstructor
public class TestTypeController {

    private final TestTypeService testTypeService;

    @Operation(summary = "Create TestType", description = "Create TestType")
    @PostMapping
    public ResponseEntity<Object> createTestType(@Valid @RequestBody TestTypeRequest testTypeRequest) {

        testTypeService.createTestType(testTypeRequest);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Your account is created successfully");

    }

    @Operation(summary = "Get TestType By TestTypeID", description = "Get TestType By TestTypeID")
    @GetMapping
    public ResponseEntity<Object> getTestTypeById(@PathParam("id") Long id) {

        return ResponseBuilder.returnData(HttpStatus.OK, "Your account is created successfully",
                testTypeService.getTestTypeByID(id));
    }


    @Operation(summary = "Get All TestType ", description = "Get All TestType")
    @GetMapping("/all")
    public ResponseEntity<Object> getAllTestType() {
        List<TestTypeResponse> testTypeResponses = testTypeService.getAllTestType();

        return ResponseBuilder.returnMessage(HttpStatus.OK, "Your account is created successfully");
    }

    @Operation(summary = "Update TestType By ID", description = "Get TestType By ID")
    @PutMapping
    public ResponseEntity<Object> updateTestType(@PathParam("id") Long id,
                                                 @Valid @RequestBody TestTypeRequest testTypeRequest) {

        testTypeService.updateTestType(id, testTypeRequest);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Your account is created successfully");
    }

    @Operation(summary = "Delete TestType", description = "Delete TestType")
    @DeleteMapping
    public ResponseEntity<Object> deleteTestType(@PathParam("id") Long id,
                                                 @Valid @RequestBody TestTypeRequest testTypeRequest) {
        // delete TestType by change TestType , not delete all information

        testTypeService.deleteTestType(id, testTypeRequest);
        return ResponseBuilder.returnMessage(HttpStatus.OK, "Your account is created successfully");
    }

}
