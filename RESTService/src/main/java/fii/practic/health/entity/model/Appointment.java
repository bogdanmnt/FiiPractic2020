package fii.practic.health.entity.model;

import java.time.LocalDateTime;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.validation.constraints.Size;

@Entity
public class Appointment {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Size(max = 50, message = "Cause may receive a limit of maximum 256 characters")
	private String cause;

	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private LocalDateTime startDate;

	@Column(columnDefinition = "TIMESTAMP WITH TIME ZONE")
	private LocalDateTime endTime;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "doctor_id")
	private Doctor doctor;

	@OneToOne(cascade = { CascadeType.PERSIST, CascadeType.MERGE, CascadeType.DETACH, CascadeType.REFRESH })
	@JoinColumn(name = "patient_id")
	private Patient patient;

	public Appointment(String cause, LocalDateTime startDate, LocalDateTime endTime, Doctor doctor, Patient patient) {
		super();
		this.cause = cause;
		this.startDate = startDate;
		this.endTime = endTime;
		this.doctor = doctor;
		this.patient = patient;
	}

	public Appointment() {

	}

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

	public Doctor getDoctor() {
		return doctor;
	}

	public void setDoctor(Doctor doctor) {
		this.doctor = doctor;
	}

	public Patient getPatient() {
		return patient;
	}

	public void setPatient(Patient patient) {
		this.patient = patient;
	}
	
	public String getPatientName() {
		return patient.firstName +" " + patient.lastName;
	}
	
	public String getDoctorName() {
		return doctor.firstName +" " + doctor.lastName;
	}

	@Override
	public String toString() {
		return "Appointment [id=" + id + ", cause=" + cause + ", startDate=" + startDate + ", endTime=" + endTime
				+ ", doctor=" + doctor + ", patient=" + patient + "]";
	}

}
