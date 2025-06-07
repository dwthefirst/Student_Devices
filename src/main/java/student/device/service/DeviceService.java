package student.device.service;

import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import student.device.dao.DeviceDao;
import student.device.dao.DeviceTypeDao;
import student.device.dao.StudentDao;
import student.device.entity.Device;
import student.device.entity.DeviceType;
import student.device.entity.Student;
import student.device.model.DeviceData;
import student.device.model.DeviceData.DeviceTypeData;
import student.device.model.DeviceData.StudentData;

@Service
public class DeviceService {
	// instance variable
	@Autowired
	private DeviceDao deviceDao;
	
	@Autowired
	private DeviceTypeDao deviceTypeDao;
	
	@Autowired
	private StudentDao studentDao;

	
	
	@Transactional(readOnly = false)
	public DeviceData saveDevice(DeviceData deviceData) {

		Long deviceId = deviceData.getDeviceId();
		//finds if an existing device exists or makes a new one
		Device device = findOrCreateDevice(deviceId);
		
		copyDeviceFields(device, deviceData);
		return new DeviceData(deviceDao.save(device));

	}
	
	//including deviceTypeId to save as foreign key
	@Transactional(readOnly = false)
	public DeviceData saveDevice(Long deviceTypeId, DeviceData deviceData) {
		DeviceType deviceType = findDeviceTypeById(deviceTypeId);

		Long deviceId = deviceData.getDeviceId();
		//finds if an existing device exists or makes a new one
		Device device = findOrCreateDevice(deviceId);
		
		//copyDeviceFields(device, deviceData);
		device.setDeviceId(deviceData.getDeviceId());
		device.setAssetTag(deviceData.getAssetTag());
		device.setSerialNumber(deviceData.getSerialNumber());
		device.setStatus(deviceData.getStatus());	
		
		device.setDeviceType(deviceType);
		deviceType.getDevices().add(device);
		
		Device dbDevice = deviceDao.save(device);
		return new DeviceData(dbDevice);
		
		
		
	}	

	private DeviceType findDeviceTypeById(Long deviceTypeId) {
		return deviceTypeDao.findById(deviceTypeId)
				.orElseThrow(() -> new NoSuchElementException("Contributor with ID =" + deviceTypeId + " was not found."));
	}
	

	private void copyDeviceFields(Device device, DeviceData deviceData) {
		device.setDeviceId(deviceData.getDeviceId());
		device.setAssetTag(deviceData.getAssetTag());
		device.setSerialNumber(deviceData.getSerialNumber());
		device.setStatus(deviceData.getStatus());		
		
		
		//device.setDeviceType(deviceData.getDeviceType());
		//checking if deviceData.deviceType is null or if the deviceTypeId is null
		if (deviceData.getDeviceType() != null && deviceData.getDeviceType().getDeviceTypeId() != null) {
			Long typeId = deviceData.getDeviceType().getDeviceTypeId();
			DeviceType existingType = deviceTypeDao.findById(typeId)
					.orElseThrow(() -> new NoSuchElementException("DeviceType ID=" + typeId + " not found."));
			device.setDeviceType(existingType);
		}
	}

	private Device findOrCreateDevice(Long deviceId) {
		// returns a new PetStore object if the pet store ID is null.
		if (Objects.isNull(deviceId)) {
			return new Device();
		} else {
			// If not null, the method should call findPetStoreById
			return findDeviceById(deviceId);
		}
	}

	private Device findDeviceById(Long deviceId) {
		// Returns a PetStore object if a pet store with matching ID exists in the database.
		return deviceDao.findById(deviceId)
				.orElseThrow(() -> new NoSuchElementException("Device with ID=" + deviceId + " not found."));
		// If no matching pet store is found, the method should throw a NoSuchElementException
	}

	
	
	public List<DeviceData> retrieveAllDevices() {
		
		List<Device> devices = deviceDao.findAll();
		
		//Convert to DeviceData
		List<DeviceData> result = new LinkedList<>();
		
		for(Device device : devices) {
			DeviceData deviceData = new DeviceData(device);
			
			
//			deviceData.getStudents().clear();
			
			result.add(deviceData);
		}
		
		return result;
		
	}

	public DeviceData retrieveDeviceById(Long deviceId) {
		return new DeviceData(findDeviceById(deviceId));
	}

	
	@Transactional(readOnly = false)
	public void deleteDeviceById(Long deviceId) {
		Device device = findDeviceById(deviceId);
		deviceDao.delete(device);
	}

	
	//Saving student to deviceId - (MANY TO MANY with Student and Device)
	@Transactional
	public StudentData saveStudent(Long deviceId, StudentData studentData) {

		Device device = findDeviceById(deviceId);
		Long studentId = studentData.getStudentId();
		Student student = findOrCreateStudent(deviceId, studentId);
		
		
		//copy Student Fields
		copyStudentFields(student, studentData);
		
		//MANY TO MANY RELATIONSHIP! So have to add both on their sets of each other (Student and Device)
		student.getDevices().add(device);
		device.getStudents().add(student);
		
		Student dbStudent = studentDao.save(student);
		return new StudentData(dbStudent);
		
	}

	
	
	private void copyStudentFields(Student student, StudentData studentData) {
		student.setStudentId(studentData.getStudentId());
		student.setName(studentData.getName());
		student.setGrade(studentData.getGrade());
		student.setSchool(studentData.getSchool());
		
	}

	private Student findOrCreateStudent(Long deviceId, Long studentId) {
		//if student ID is null, return new student (so if the student doesn't exist yet in the db)
		if(Objects.isNull(studentId)) {
			return new Student();
		}
		
		//if studnetID does exist, call findStudentById()
		return findStudentById(deviceId, studentId);
	}

	
	
	
	private Student findStudentById(Long deviceId, Long studentId) {

		Student student = studentDao.findById(studentId).orElseThrow(() -> new NoSuchElementException("Student with ID=" + studentId + " was not found."));
		
		//many to many relationship - Student has Set<Devices> so need to loop through the set of Devices looking for the Device with the given ID passed through
		boolean found = false;
		
		for(Device device : student.getDevices()) {
			if(device.getDeviceId() == deviceId) {
				found = true;
				break;
			}
		}
		
		//if there is not a match with the deviceID matching one in the Student's Set<Device>
		if(!found) {
			throw new IllegalArgumentException("Student with ID = " + studentId + " is not assigned to a device with ID=" + deviceId);
		}
		
		return student;
		
	}

	
	@Transactional(readOnly = false)
	public DeviceTypeData saveDeviceType(DeviceTypeData deviceTypeData) {

		//get deviceTypeId
		Long deviceTypeId = deviceTypeData.getDeviceTypeId();
		
		DeviceType deviceType = findOrCreateDeviceType(deviceTypeId);
		
		//copy deviceType fields
		deviceType.setDeviceTypeId(deviceTypeData.getDeviceTypeId());
		deviceType.setTypeName(deviceTypeData.getTypeName());
		deviceType.setManufacturerName(deviceTypeData.getManufacturerName());
		
		return new DeviceTypeData(deviceTypeDao.save(deviceType));
		
	}

	private DeviceType findOrCreateDeviceType(Long deviceTypeId) {
		if(Objects.isNull(deviceTypeId)) {
			return new DeviceType();
		} else {
			return findDeviceTypeById(deviceTypeId);
		}
	}


}
