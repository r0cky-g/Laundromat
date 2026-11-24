package ca.yorku.group12.eecs4413.reservation.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ca.yorku.group12.eecs4413.reservation.entity.Reservation;
import ca.yorku.group12.eecs4413.reservation.entity.ReservationStatus;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	List<Reservation> findByUserIdAndStatusOrderByStartTimeDesc(String userId, ReservationStatus status);
	
	List<Reservation> findByMachineIdAndStatusOrderByStartTimeDesc(int machineId, ReservationStatus status);
}
