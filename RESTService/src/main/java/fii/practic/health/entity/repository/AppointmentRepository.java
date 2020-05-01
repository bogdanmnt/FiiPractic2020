package fii.practic.health.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fii.practic.health.entity.model.Appointment;


@Repository
public interface AppointmentRepository extends JpaRepository<Appointment, Long> {
	
	 @Query("SELECT a FROM Appointment a WHERE doctor_id = :doctor_id")
	 public List<Appointment> findAppointmentsById(@Param("doctor_id") Long doctor_id);

}
