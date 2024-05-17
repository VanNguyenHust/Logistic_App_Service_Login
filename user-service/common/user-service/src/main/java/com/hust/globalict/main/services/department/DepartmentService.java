package com.hust.globalict.main.services.department;

import com.hust.globalict.main.modules.User;
import com.hust.globalict.main.repositories.UserRepository;
import com.hust.globalict.main.services.user.IUserRedisService;
import com.hust.globalict.main.utils.MessageKeyExceptions;
import org.springframework.stereotype.Service;

import com.hust.globalict.main.components.LocalizationUtils;
import com.hust.globalict.main.exceptions.DataNotFoundException;
import com.hust.globalict.main.modules.Department;
import com.hust.globalict.main.repositories.DepartmentRepository;
import com.hust.globalict.main.utils.MessageKeys;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DepartmentService implements IDepartmentService {
	private final DepartmentRepository departmentRepository;
	private final IDepartmentRedisService departmentRedisService;

	private final UserRepository userRepository;
	private final IUserRedisService userRedisService;

	private final LocalizationUtils localizationUtils;

	@Override
	@Transactional
	public Department createDepartment(Department department) throws Exception {
		if (departmentRepository.existsByName(department.getName())) {
			throw new IllegalStateException(localizationUtils.getLocalizedMessage(MessageKeyExceptions.DEPARTMENT_NAME_EXISTED));
		}

		departmentRepository.save(department);
		User existingUser = department.getUser();
		existingUser.setDepartment(department);
		userRepository.save(existingUser);

		departmentRedisService.saveDepartmentById(department.getId(), department);
		userRedisService.saveUserById(existingUser.getId(), existingUser);

		return department;
	}

	@Override
	public Department getDepartmentById(Long departmentId) throws Exception {
		Department department = departmentRedisService.getDepartmentById(departmentId);
		if (department == null) {
			department = departmentRepository.findById(departmentId).orElseThrow(() -> new DataNotFoundException(
					localizationUtils.getLocalizedMessage(MessageKeys.DEPARTMENT_GET_BY_ID_NOT_FOUND)));

			departmentRedisService.saveDepartmentById(department.getId(), department);
		}

		return department;
	}

	@Override
	@Transactional
	public Department updateDepartment(Long departmentId, Department departmentUpdate) throws Exception {
		departmentUpdate = departmentRepository.save(departmentUpdate);
		departmentRedisService.saveDepartmentById(departmentId, departmentUpdate);
		
		return departmentUpdate;
	}

	@Override
	@Transactional
	public void deleteDepartment(Long departmentId) throws Exception {
		Department existingDepartment = getDepartmentById(departmentId);

		User user = userRepository.findByDepartment(existingDepartment);
		if (user != null) {
			user.setDepartment(null);
			userRepository.save(user);
		}

		departmentRepository.delete(existingDepartment);
	}

}
