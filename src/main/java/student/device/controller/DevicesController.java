package student.device.controller;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import lombok.extern.slf4j.Slf4j;
import student.device.model.DeviceData;
import student.device.model.DeviceData.DeviceTypeData;
import student.device.model.DeviceData.StudentData;
import student.device.service.DeviceService;

@RestController
@RequestMapping("/student_devices")
@Slf4j
public class DevicesController {

	//instance variable
	@Autowired
	private DeviceService deviceService;
	
	
	
	//Request will be JSON (@RequestBody)
	@PostMapping("/device") //POST request  
	@ResponseStatus(code = HttpStatus.CREATED)
	public DeviceData insertDevice(
			@RequestBody DeviceData deviceData) {
		log.info("Creating device {}", deviceData);
		return deviceService.saveDevice(deviceData);
	}
	
	
	//including deviceTypeId to save for foreign key
	//Request will be JSON (@RequestBody)
	@PostMapping("/device/deviceTypeId/{deviceTypeId}") //POST request  
	@ResponseStatus(code = HttpStatus.CREATED)
	public DeviceData insertDeviceWithDeviceTypeId(@PathVariable Long deviceTypeId,
			@RequestBody DeviceData deviceData) {
		log.info("Creating device {}", deviceData);
		return deviceService.saveDevice(deviceTypeId, deviceData);
	}
	
	
	@PutMapping("/device/{deviceId}")
	public DeviceData updateDevice(@PathVariable Long deviceId, @RequestBody DeviceData deviceData) {		
		deviceData.setDeviceId(deviceId);
		log.info("Updating device {}", deviceData);
		return deviceService.saveDevice(deviceData);
	}
	
	
	@GetMapping
	public List<DeviceData> retrieveAllDevices(){
		return deviceService.retrieveAllDevices();
	}
	
	
	
	//retrieve pet store by id
	@GetMapping("/{deviceId}")
	public DeviceData retrieveDeviceById(@PathVariable Long deviceId) {
		log.info("Retrieving device with ID={}", deviceId);
		return deviceService.retrieveDeviceById(deviceId);
	}
	
	
	@DeleteMapping("/{deviceId}")
	public Map<String, String> deleteDeviceById(@PathVariable Long deviceId){
		log.info("Deleting device with ID={}", deviceId);
		
		deviceService.deleteDeviceById(deviceId);
		return Map.of("message", "Device with ID=" + deviceId + " deleted");
	}
	
	
	
	//adding new student
	@PostMapping("/{deviceId}/student")
	@ResponseStatus(code = HttpStatus.CREATED)
	public StudentData addStudentToDevice(@PathVariable Long deviceId, @RequestBody StudentData studentData) {
		log.info("Adding student {} to Device with ID={}", studentData, deviceId);
		return deviceService.saveStudent(deviceId, studentData);
	}
	
	
	//adding a new device Type
	@PostMapping("/deviceType")
	@ResponseStatus(code = HttpStatus.CREATED)
	public DeviceTypeData addDeviceType(@RequestBody DeviceTypeData deviceTypeData) {
		log.info("Creating Device Type {}", deviceTypeData);
		return deviceService.saveDeviceType(deviceTypeData);
	}
	
	
}
