package student.device.dao;

import org.springframework.data.jpa.repository.JpaRepository;

import student.device.entity.DeviceType;

public interface DeviceTypeDao extends JpaRepository<DeviceType, Long> {

}
