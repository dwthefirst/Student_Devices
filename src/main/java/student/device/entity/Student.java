package student.device.entity;

import java.util.HashSet;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

@Entity
@Data
public class Student {
	
	//instance variables
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long studentId; //primary key
	
	private String name;
	private String grade;
	private String school;
	
	//Many to many - (JOIN TABLE) many students can have many devices
	@ManyToMany(cascade = CascadeType.PERSIST)
	@JoinTable(name = "student_device", 
		joinColumns = @JoinColumn(name = "student_id"),
		inverseJoinColumns = @JoinColumn(name = "device_id")
	)
	@EqualsAndHashCode.Exclude
	@ToString.Exclude
	private Set<Device> devices = new HashSet<>();

}
