package ca.yorku.group12.eecs4413.reservation.repository;

import java.time.OffsetDateTime;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ca.yorku.group12.eecs4413.reservation.entity.Reservation;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {
	
	List<Reservation> findByUserId(String userId);
	
	List<Reservation> findByMachineIdOrderByStartTimeDesc(int machineId);
	
	@Query("""
	        SELECT r FROM Reservation r
	        WHERE r.machineId = :machineId
	          AND r.startTime < :end
	          AND r.endTime > :start
	        """)
	    List<Reservation> findOverlapping(
	            @Param("machineId") int machineId,
	            @Param("start") OffsetDateTime start,
	            @Param("end") OffsetDateTime end);
}
