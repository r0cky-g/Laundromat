package ca.yorku.group12.eecs4413.machine.service;

import ca.yorku.group12.eecs4413.machine.model.MachineStatus;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Component
public class CycleSimulator {

	private final MachineService machineService;

	private final ScheduledExecutorService scheduler =
			Executors.newScheduledThreadPool(2);

	public CycleSimulator(@Lazy MachineService machineService) {
		this.machineService = machineService;
	}

	public void startCycle(int machineId, int duration) {
		scheduler.schedule(
				() -> simulateCycle(machineId),
				duration,
				TimeUnit.MINUTES);
	}

	public void simulateCycle(int machineId) {
		try {
			completeCycle(machineId);
		} catch (Exception e) {
			handleCycleFailure(machineId);
		}
	}

	public void completeCycle(int machineId) {
		machineService.completeCycle(machineId);
	}

	public void handleCycleFailure(int machineId) {
		machineService.updateMachineStatus(machineId, MachineStatus.OUT_OF_SERVICE);
	}
}
