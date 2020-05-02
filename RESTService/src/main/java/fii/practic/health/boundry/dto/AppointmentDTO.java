package fii.practic.health.boundry.dto;

import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIdentityInfo;
import com.fasterxml.jackson.annotation.JsonIdentityReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.ObjectIdGenerators;

import fii.practic.health.entity.model.Doctor;
import fii.practic.health.entity.model.Patient;

public class AppointmentDTO {

	
	private Long id;
	
	private String cause;
	
	private LocalDateTime startDate;
	
	private LocalDateTime endTime;
	
	private Long doctorId;
	
	private Long patientId;
	
//	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lastName")
//	@JsonIdentityReference(alwaysAsId = true)
//	private Doctor doctor;
//	@JsonIdentityInfo(generator = ObjectIdGenerators.PropertyGenerator.class, property = "lastName")
//	@JsonIdentityReference(alwaysAsId = true)
//	private Patient patient;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getCause() {
		return cause;
	}

	public void setCause(String cause) {
		this.cause = cause;
	}

	public LocalDateTime getStartDate() {
		return startDate;
	}

	public void setStartDate(LocalDateTime startDate) {
		this.startDate = startDate;
	}

	public LocalDateTime getEndTime() {
		return endTime;
	}

	public void setEndTime(LocalDateTime endTime) {
		this.endTime = endTime;
	}

	public Long getDoctorId() {
		return doctorId;
	}

	public void setDoctorId(Long doctorId) {
		this.doctorId = doctorId;
	}

	public Long getPatientId() {
		return patientId;
	}

	public void setPatientId(Long patientId) {
		this.patientId = patientId;
	}

	
	
}
