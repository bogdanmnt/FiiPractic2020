package fii.practic.health.control.service;


import java.io.File;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fii.practic.health.entity.model.Appointment;

import fii.practic.health.entity.repository.AppointmentRepository;
import fii.practic.health.entity.repository.DoctorRepository;
import fii.practic.health.entity.repository.PatientRepository;
@Service
public class AppointmentServiceImpl implements AppointmentService{
	
	private AppointmentRepository appointmentRepository;
	private DoctorRepository doctorRepository;
	private PatientRepository patientRepository;
	private JavaMailUtil mail;
	
	@Autowired
	public AppointmentServiceImpl(AppointmentRepository appointmentRepository,DoctorRepository doctorRepository,
										PatientRepository patientRepository,JavaMailUtil mail) {
		this.appointmentRepository=appointmentRepository;
		this.doctorRepository=doctorRepository;
		this.patientRepository=patientRepository;
		this.mail=mail;
	}

	@Override
	public List<Appointment> getAll() {
		
		return appointmentRepository.findAll();
	}

	@Override
	public Appointment getById(Long id) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Appointment save(Appointment appointment) {
		

    	
		return appointmentRepository.save(appointment);
	}
	
	@Override
	public List<Appointment> findAppByDoctorId(Long doctor_id){
		return appointmentRepository.findAppointmentsByDoctorId(doctor_id);
	}

	@Override
	public List<Appointment> findAppByPatientId(Long patient_id) {
		return appointmentRepository.findAppointmentsByPatientId(patient_id);
	}

	@Override
	public List<Appointment> findFutureAppointments() {
		return appointmentRepository.findFutureAppointments();
	}

	

}
