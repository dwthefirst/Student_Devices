package student.device.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Device {
	
	//instance variables
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long deviceId; //primary key
	
	private String assetTag;
	private String serialNumber;
	private String status;
	
	//Many To Many (JOIN TABLE - Student & Device)
	@ManyToMany(mappedBy = "devices", cascade = CascadeType.PERSIST)
	private Set<Student> students = new HashSet<>();
	
	//Many to One - Relationship (One DeviceType can have many devices)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	@ManyToOne(cascade = CascadeType.ALL)
	@JoinColumn(name = "deviceType_id") //foreign key to DeviceType
	private DeviceType deviceType;
	
	
	

}
