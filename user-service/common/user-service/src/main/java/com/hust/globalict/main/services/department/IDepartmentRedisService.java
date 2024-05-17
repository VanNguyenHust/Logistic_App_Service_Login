package com.hust.globalict.main.services.department;

import com.hust.globalict.main.modules.Department;

public interface IDepartmentRedisService {
	void clear(Long departmentId);

	Department getDepartmentById(Long departmentId) throws Exception;

	void saveDepartmentById(Long departmentId, Department department) throws Exception;
}
