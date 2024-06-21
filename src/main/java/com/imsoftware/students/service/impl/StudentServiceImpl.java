package com.imsoftware.students.service.impl;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import com.imsoftware.students.repository.StudentRepository;
import org.springframework.stereotype.Service;

import com.imsoftware.students.domain.StudentDTO;
import com.imsoftware.students.entity.Student;
import com.imsoftware.students.service.IStudentService;

@Service
public class StudentServiceImpl implements IStudentService {

	private final StudentRepository studentRepository;

	public StudentServiceImpl(StudentRepository studentRepository) {
		super();
		this.studentRepository = studentRepository;
	}

	@Override
	public Collection<StudentDTO> findAll() {
		return studentRepository.findAll().stream().map(new Function<Student, StudentDTO>() {
			@Override
			public StudentDTO apply(Student student) {
				List<String> programmingLanguagesKnowAbout = student.getSubjects().stream()
						.map(pl -> new String(pl.getName())).collect(Collectors.toList());
				return new StudentDTO(student.getName(), programmingLanguagesKnowAbout);
			}

		}).collect(Collectors.toList());
		
	}

	@Override
	public Collection<StudentDTO> findAllAndShowIfHaveAPopularSubject() {
		// TODO Obtener la lista de todos los estudiantes e indicar la materia más concurrida existentes en la BD e
		// indicar si el estudiante cursa o no la materia más concurrida registrado en la BD.
		
		final Map<String, Integer> m = new HashMap<String, Integer>();
		
		final List<StudentDTO> ss = studentRepository.findAll().stream().map(new Function<Student, StudentDTO>() {
			@Override
			public StudentDTO apply(Student student) {
				List<String> programmingLanguagesKnowAbout = student.getSubjects().stream()
						.map(pl -> new String(pl.getName())).collect(Collectors.toList());
				
				programmingLanguagesKnowAbout.forEach(s -> {
					if(m.containsKey(s))
						m.put(s, m.get(s) + 1);
					else
						m.put(s, 1);
				});
				
				return new StudentDTO(student.getName(), programmingLanguagesKnowAbout);
			}

		}).collect(Collectors.toList());
		
		m.entrySet().stream()
		   .sorted(Map.Entry.<String, Integer> comparingByValue().reversed());
		
		final String mm =  m.keySet().stream()
				.collect(Collectors.toList()).get(0);
		
		return ss.stream().map(new Function<StudentDTO, StudentDTO>() {
			@Override
			public StudentDTO apply(StudentDTO dto) {
				AtomicBoolean ism = new AtomicBoolean(false);
				dto.getCurrentSubject().forEach(sb -> {
					ism.set(mm.equals(sb));
				});
				
				if(ism.get())
					return new StudentDTO(dto.getStudentName(), true);
				
				return dto;
			}
		}).collect(Collectors.toList());
	}

}
