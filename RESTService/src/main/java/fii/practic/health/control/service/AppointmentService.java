package fii.practic.health.control.service;

import java.time.LocalDateTime;

import java.util.List;

import fii.practic.health.boundry.exceptions.BadRequestException;

import fii.practic.health.entity.model.Appointment;


public interface AppointmentService {

	
	  List<Appointment> getAll();

	  Appointment save(Appointment appointment);
	  
	  List<Appointment> findAppByDoctorId(Long doctor_id);
	  
	  List<Appointment> findAppByPatientId(Long patient_id);

	  List<Appointment> findFutureAppointments();
	  
	  List<Appointment> findFutureAppointmentsByDoctorId(Long doctor_id);
	  
	  void deleteAppointment(Long id) throws BadRequestException;

	  Appointment getById(Long id);

      List<Appointment> findFutureAppointmentsThatCanBeCanceled();
	
      Long findLastAppointmentId();
      
      List<Appointment> findAllEndedAppointments();
      
      List<Appointment> TestScheduller(LocalDateTime xxx);

}
