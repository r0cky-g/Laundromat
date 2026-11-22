package ca.yorku.group12.eecs4413.machine.service;

import ca.yorku.group12.eecs4413.machine.model.Machine;
import ca.yorku.group12.eecs4413.machine.model.MachineRepository;
import ca.yorku.group12.eecs4413.machine.model.MachineStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MachineService {

	private final MachineRepository machineRepository;
	private final CycleSimulator cycleSimulator;
	private final ReservationClient reservationClient;

	public MachineService(
			MachineRepository machineRepository,
			CycleSimulator cycleSimulator,
			ReservationClient reservationClient) {
		this.machineRepository = machineRepository;
		this.cycleSimulator = cycleSimulator;
		this.reservationClient = reservationClient;
	}

	public Machine getMachine(int machineId) {
		return machineRepository.findById(machineId).orElse(null);
	}

	public List<Machine> getAllMachines() {
		return machineRepository.findAll();
	}

	public List<Machine> getAvailableMachines() {
		return machineRepository.findByStatus(MachineStatus.AVAIL);
	}

	public boolean updateMachineStatus(int machineId, MachineStatus status) {
		Machine machine = getMachine(machineId);
		if (machine == null) {
			return false;
		}

		machine.setStatus(status);
		machineRepository.save(machine);
		reservationClient.notifyReservationUpdate(machineId, status.name());
		return true;
	}

	public Machine createMachine(Machine machine) {
		if (machine.getStatus() == null) {
			machine.setStatus(MachineStatus.AVAIL);
		}
		return machineRepository.save(machine);
	}

	public boolean removeMachine(int machineId) {
		if (!machineRepository.existsById(machineId)) {
			return false;
		}
		machineRepository.deleteById(machineId);
		return true;
	}

	public boolean checkAvailability(
			int machineId,
			LocalDateTime startTime,
			LocalDateTime endTime) {
		Machine machine = getMachine(machineId);
		if (machine == null || machine.getStatus() != MachineStatus.AVAIL) {
			return false;
		}
		if (startTime == null || endTime == null || !endTime.isAfter(startTime)) {
			return false;
		}
		return reservationClient.checkReservation(machineId, startTime, endTime);
	}

	@Transactional
	public boolean startCycle(int machineId, int duration) {
		if (duration <= 0) {
			return false;
		}

		Machine machine = getMachine(machineId);
		if (machine == null || machine.getStatus() != MachineStatus.RES) {
			return false;
		}

		machine.setStatus(MachineStatus.IN_USE);
		machineRepository.save(machine);
		cycleSimulator.startCycle(machineId, duration);
		return true;
	}

	public boolean completeCycle(int machineId) {
		Machine machine = getMachine(machineId);
		if (machine == null || machine.getStatus() != MachineStatus.IN_USE) {
			return false;
		}

		machine.setStatus(MachineStatus.AVAIL);
		machineRepository.save(machine);
		reservationClient.notifyReservationUpdate(machineId, MachineStatus.AVAIL.name());
		return true;
	}

	public boolean reportMaintenance(int machineId) {
		Machine machine = getMachine(machineId);
		if (machine == null || machine.getStatus() == MachineStatus.IN_USE) {
			return false;
		}

		machine.setStatus(MachineStatus.MAINTENANCE);
		machineRepository.save(machine);
		reservationClient.notifyReservationUpdate(machineId, MachineStatus.MAINTENANCE.name());
		return true;
	}
}
