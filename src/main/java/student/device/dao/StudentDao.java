package student.device.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import student.device.entity.Student;

public interface StudentDao extends JpaRepository<Student, Long> {

}
