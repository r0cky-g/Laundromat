package ca.yorku.group12.eecs4413.machine.controller;

import ca.yorku.group12.eecs4413.machine.model.Machine;
import ca.yorku.group12.eecs4413.machine.model.MachineStatus;
import ca.yorku.group12.eecs4413.machine.service.MachineService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/api/machines")
public class MachineController {

	private final MachineService machineService;

	public MachineController(MachineService machineService) {
		this.machineService = machineService;
	}

	@GetMapping("/{machineId}")
	public ResponseEntity<Machine> getMachine(@PathVariable int machineId) {
		Machine machine = machineService.getMachine(machineId);
		return machine != null
				? ResponseEntity.ok(machine)
				: ResponseEntity.notFound().build();
	}

	@GetMapping
	public ResponseEntity<List<Machine>> getAllMachines() {
		return ResponseEntity.ok(machineService.getAllMachines());
	}

	@GetMapping("/available")
	public ResponseEntity<List<Machine>> getAvailableMachines() {
		return ResponseEntity.ok(machineService.getAvailableMachines());
	}

	@PatchMapping("/{machineId}/status")
	public ResponseEntity<Void> updateMachineStatus(
			@PathVariable int machineId,
			@RequestParam MachineStatus status) {
		return machineService.updateMachineStatus(machineId, status)
				? ResponseEntity.ok().build()
				: ResponseEntity.notFound().build();
	}

	@PostMapping
	public ResponseEntity<Machine> createMachine(@RequestBody Machine machine) {
		return ResponseEntity.ok(machineService.createMachine(machine));
	}

	@DeleteMapping("/{machineId}")
	public ResponseEntity<Void> deleteMachine(@PathVariable int machineId) {
		return machineService.removeMachine(machineId)
				? ResponseEntity.noContent().build()
				: ResponseEntity.notFound().build();
	}

	@PostMapping("/{machineId}/start")
	public ResponseEntity<String> startCycle(
			@PathVariable int machineId,
			@RequestParam int duration) {
		return machineService.startCycle(machineId, duration)
				? ResponseEntity.ok("Cycle started")
				: ResponseEntity.badRequest().body("Unable to start cycle");
	}
}
