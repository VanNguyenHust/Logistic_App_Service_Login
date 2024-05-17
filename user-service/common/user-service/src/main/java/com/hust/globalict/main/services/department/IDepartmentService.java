package com.hust.globalict.main.services.department;

import com.hust.globalict.main.modules.Department;

public interface IDepartmentService {
	Department createDepartment(Department department) throws Exception;
	
	Department getDepartmentById(Long departmentId) throws Exception;
	
	Department updateDepartment(Long departmentId, Department departmentUpdate) throws Exception;
	
	void deleteDepartment(Long departmentId) throws Exception;
}
