package fii.practic.health.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import fii.practic.health.scheduler.SchedulerLastRecordedIdValue;

@Repository
public interface SchedulerLastRecordedRepository extends JpaRepository<SchedulerLastRecordedIdValue, Long> {

	@Query(value = "SELECT * FROM scheduler_last_recorded_id_value ORDER BY ID DESC LIMIT 1", nativeQuery = true)
	SchedulerLastRecordedIdValue findLastScheduled();

}
