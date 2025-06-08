package student.device.model;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.CascadeType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import student.device.entity.Device;
import student.device.entity.DeviceType;
import student.device.entity.Student;

@Data
@NoArgsConstructor
public class DeviceData {
	// instance variables
	private Long deviceId; // primary key
	private String assetTag;
	private String serialNumber;
	private String status;

	private Set<StudentData> students = new HashSet<>();
	
	//@JsonIgnore //fixes the issue of JSON repeating infinitely
//	private DeviceType deviceType;
	
	//use deviceTypeData to avoid the bidirectional relationship looping 
	//between DeviceType and Device (OneToMany <> Many to One)
	private DeviceTypeData deviceTypeData;

	//constructor (takes in Device dataType)
	public DeviceData(Device device) {
		deviceId = device.getDeviceId();
		assetTag = device.getAssetTag();
		serialNumber = device.getSerialNumber();
		status = device.getStatus();
		
//		deviceType = device.getDeviceType();
		
		//prevent the circular infinite loop
		if(device.getDeviceType() != null) { //if the deviceType exists
			deviceTypeData = new DeviceTypeData(device.getDeviceType());
		}
		
		
		//for loop for Set<StudentData>
		for(Student student : device.getStudents()) {
			students.add(new StudentData(student));
		}
	}
	
	
	
	// inner class
	@Data
	@NoArgsConstructor
	public static class StudentData {
		// instance variables
		private Long studentId; // primary key
		private String name;
		private String grade;
		private String school;
		
		//constructor
		public StudentData(Student student) {
			studentId = student.getStudentId();
			name = student.getName();
			grade = student.getGrade();
			school = student.getSchool();
		}

	}
	
	//inner class
	@Data
	@NoArgsConstructor
	public static class DeviceTypeData{

		private Long deviceTypeId; //primary key
		private String typeName;
		private String manufacturerName;		
		//OneToMany (One DeviceType has many devices)
//		private Set<DeviceData> devices = new HashSet<>();
		
		//constructor
		public DeviceTypeData(DeviceType deviceType) {
			deviceTypeId = deviceType.getDeviceTypeId();
			typeName = deviceType.getTypeName();
			manufacturerName = deviceType.getManufacturerName();
		}
	}
}
