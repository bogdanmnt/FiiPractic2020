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
	 public List<Appointment> findAppointmentsByDoctorId(@Param("doctor_id") Long doctor_id);
	 
	 @Query("SELECT a FROM Appointment a WHERE patient_id = :patient_id")
	 public List<Appointment> findAppointmentsByPatientId(@Param("patient_id") Long patient_id);
	 
	 @Query("SELECT a FROM Appointment a WHERE start_date >= current_timestamp")
	 public List<Appointment> findFutureAppointments();
			
	 
}