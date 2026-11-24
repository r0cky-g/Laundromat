package ca.yorku.group12.eecs4413.machine.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "machines")
public class Machine {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "machine_id")
	private Integer machineId;

	@Enumerated(EnumType.STRING)
	@Column(name = "machine_type")
	private MachineType machineType;

	private BigDecimal capacity;

	@Enumerated(EnumType.STRING)
	private MachineStatus status;

	private String location;

	@Column(name = "bought_in")
	private LocalDateTime boughtIn;

	public Machine() {
	}

	public Integer getMachineId() {
		return machineId;
	}

	public void setMachineId(Integer machineId) {
		this.machineId = machineId;
	}

	public MachineType getMachineType() {
		return machineType;
	}

	public void setMachineType(MachineType machineType) {
		this.machineType = machineType;
	}

	public BigDecimal getCapacity() {
		return capacity;
	}

	public void setCapacity(BigDecimal capacity) {
		this.capacity = capacity;
	}

	public MachineStatus getStatus() {
		return status;
	}

	public void setStatus(MachineStatus status) {
		this.status = status;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public LocalDateTime getBoughtIn() {
		return boughtIn;
	}

	public void setBoughtIn(LocalDateTime boughtIn) {
		this.boughtIn = boughtIn;
	}
}
