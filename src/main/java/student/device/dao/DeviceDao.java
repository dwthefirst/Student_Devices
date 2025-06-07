package student.device.dao;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;

import student.device.entity.Device;

public interface DeviceDao extends JpaRepository<Device, Long> {

	
}
