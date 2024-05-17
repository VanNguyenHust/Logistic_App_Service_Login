package com.hust.globalict.main.controllers;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.dtos.DepartmentDTO;
import com.hust.globalict.main.constants.Uri;
import com.hust.globalict.main.mappers.DepartmentMapper;
import com.hust.globalict.main.modules.Department;
import com.hust.globalict.main.responses.DepartmentResponse;
import com.hust.globalict.main.services.department.IDepartmentService;
import com.hust.globalict.main.services.user.IUserService;
import com.hust.globalict.main.utils.MessageKeys;
import com.hust.globalict.main.utils.ResponseObject;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping(value = Uri.DEPARTMENT)
@Validated
@RequiredArgsConstructor
public class DepartmentController {
    private final IDepartmentService departmentService;
    private final DepartmentMapper departmentMapper;
    private final IUserService userService;

    private final LocalizationUtils localizationUtils;

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> createDepartment(@Valid @RequestBody DepartmentDTO departmentDTO) throws Exception {
        Department newDepartment = departmentMapper.mapToDepartmentEntity(departmentDTO);
        if (departmentDTO.getUserId() != null) {
            newDepartment.setUser(userService.getUserById(departmentDTO.getUserId()));
        }

        departmentService.createDepartment(newDepartment);

        DepartmentResponse departmentResponse = departmentMapper.mapToDepartmentResponse(newDepartment);

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.CREATED)
                .message(localizationUtils.getLocalizedMessage(MessageKeys.DEPARTMENT_CREATE_SUCCESSFULLY))
                .data(departmentResponse)
                .build());
    }

    @GetMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_SYS-ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> getDepartmentById(@RequestHeader(name = "department_id") Long departmentId) throws Exception {
        Department department = departmentService.getDepartmentById(departmentId);

        DepartmentResponse departmentResponse = departmentMapper.mapToDepartmentResponse(department);

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK)
                .message(localizationUtils.getLocalizedMessage(MessageKeys.DEPARTMENT_GET_BY_ID_SUCCESSFULLY))
                .data(departmentResponse)
                .build());
    }

    @PutMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> updateDepartment(@RequestHeader(name = "department_id") Long departmentId, @Valid @RequestBody DepartmentDTO departmentDTO) throws Exception {
        Department department = departmentService.getDepartmentById(departmentId);

        Department departmentUpdate = departmentMapper.mapToDepartmentEntity(departmentDTO);
        departmentUpdate.setId(departmentId);
        departmentUpdate.setCreatedAt(department.getCreatedAt());

        departmentUpdate = departmentService.updateDepartment(departmentId, departmentUpdate);

        DepartmentResponse departmentResponse = departmentMapper.mapToDepartmentResponse(departmentUpdate);

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK)
                .message(localizationUtils.getLocalizedMessage(MessageKeys.DEPARTMENT_UPDATE_SUCCESSFULLY))
                .data(departmentResponse)
                .build());
    }

    @DeleteMapping()
    @PreAuthorize("hasRole('ROLE_ADMIN') or hasRole('ROLE_USER')")
    public ResponseEntity<ResponseObject> deleteDepartmentById(@RequestHeader(name = "department_id") Long departmentId) throws Exception {
        departmentService.deleteDepartment(departmentId);

        return ResponseEntity.ok(ResponseObject.builder()
                .status(HttpStatus.OK)
                .message(localizationUtils.getLocalizedMessage(MessageKeys.DEPARTMENT_DELETE_SUCCESSFULLY))
                .build());
    }
}
