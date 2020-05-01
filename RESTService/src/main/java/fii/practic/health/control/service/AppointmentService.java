package fii.practic.health.control.service;

import java.util.List;

import fii.practic.health.entity.model.Appointment;


public interface AppointmentService {

	
	  List<Appointment> getAll();

	  Appointment getById(Long id);

	  Appointment save(Appointment appointment);
	  
	  List<Appointment> findAppByDoctorId(Long doctor_id);
	  
	  List<Appointment> findAppByPatientId(Long patient_id);

	  List<Appointment> findFutureAppointments();

}
