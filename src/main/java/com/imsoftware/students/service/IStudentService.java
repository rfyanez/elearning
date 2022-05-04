package com.imsoftware.students.service;

import java.util.Collection;

import com.imsoftware.students.domain.StudentDTO;

public interface IStudentService {
	Collection<StudentDTO> findAll();

	Collection<StudentDTO> findAllAndShowIfHaveAPopularSubject();

}
