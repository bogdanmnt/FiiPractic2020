package fii.practic.health.boundry.controller;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fii.practic.health.boundry.dto.AppointmentDTO;
import fii.practic.health.boundry.exceptions.BadRequestException;
import fii.practic.health.boundry.exceptions.NotFoundException;
import fii.practic.health.control.service.AppointmentService;
import fii.practic.health.control.service.DoctorService;
import fii.practic.health.control.service.PatientService;
import fii.practic.health.entity.model.Appointment;
import fii.practic.health.entity.model.Doctor;
import fii.practic.health.entity.model.Patient;

@RestController
@RequestMapping(value = "/api/appointments")
@CrossOrigin
public class AppointmentController {

	private DoctorService doctorService;
	private ModelMapper modelMapper;
	private PatientService patientService;
	private AppointmentService appointmentService;

	@Autowired
	public AppointmentController(DoctorService doctorService, ModelMapper modelMapper, PatientService patientService,
			AppointmentService appointmentService) {
		this.doctorService = doctorService;
		this.modelMapper = modelMapper;
		this.patientService = patientService;
		this.appointmentService = appointmentService;
	}

	/**
	 * Endpoint for returning all instances of the Appointment type
	 * 
	 * @return a list of appointment object from DB
	 */
	@GetMapping
	public ResponseEntity<List<AppointmentDTO>> getAppointments() {
		List<Appointment> appointments = appointmentService.getAll();
		return new ResponseEntity<>(
				(List<AppointmentDTO>) modelMapper.map(appointments, new TypeToken<List<AppointmentDTO>>() {
				}.getType()), HttpStatus.OK);
	}

	/**
	 * Endpoint for returning a list of appointments object from the time is called
	 * 
	 * @return appointments object from DB
	 */
	@GetMapping(value = "/future/appointments")
	public ResponseEntity<List<AppointmentDTO>> getFutureAppointments() {
		List<Appointment> appointments = appointmentService.findFutureAppointments();

		return new ResponseEntity<>(
				(List<AppointmentDTO>) modelMapper.map(appointments, new TypeToken<List<AppointmentDTO>>() {
				}.getType()), HttpStatus.OK);
	}

	/**
	 * Endpoint for returning all the appointments that can be canceled. An
	 * appointment which already took place in the past can’t be canceled. An
	 * appointment which will occur in the next hour can’t be canceled.
	 * 
	 * @return List<AppointmendDTO> list of appointment object
	 */
	@GetMapping(value = "/future/cancelabe/appointments")
	public ResponseEntity<List<AppointmentDTO>> getFutureAppointmentsThatCanBeCanceled() {
		List<Appointment> appointments = appointmentService.findFutureAppointmentsThatCanBeCanceled();

		return new ResponseEntity<>(
				(List<AppointmentDTO>) modelMapper.map(appointments, new TypeToken<List<AppointmentDTO>>() {
				}.getType()), HttpStatus.OK);
	}

	/**
	 * Endpoint for returning all future appointments of a specific doctor by its id
	 * 
	 * @param doctor_id the id of the doctor
	 * @return List<AppointmentDTO> a list of appointment
	 */
	@GetMapping(value = "/future/appointments/doctor/{doctor_id}")
	public ResponseEntity<List<AppointmentDTO>> getFutureAppointments(@PathVariable("doctor_id") Long doctor_id) {
		List<Appointment> appointments = appointmentService.findFutureAppointmentsByDoctorId(doctor_id);

		return new ResponseEntity<>(
				(List<AppointmentDTO>) modelMapper.map(appointments, new TypeToken<List<AppointmentDTO>>() {
				}.getType()), HttpStatus.OK);
	}

	/**
	 * Endpoint that returns all the appointments of a specific doctor, from all the
	 * time
	 * 
	 * @param id must not be null.
	 * @return a list of appointment
	 * @throws NotFoundException if the doctor with specific id does not exist
	 */
	@GetMapping(value = "/doctor/{id}")
	public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByDoctorId(@PathVariable("id") Long id)
			throws NotFoundException {
		Doctor doctor = doctorService.getById(id);
		if (doctor == null) {
			throw new NotFoundException(String.format("Doctor with id %d was not found", id));
		}
		List<Appointment> appointments = appointmentService.findAppByDoctorId(id);

		return new ResponseEntity<>(
				(List<AppointmentDTO>) modelMapper.map(appointments, new TypeToken<List<AppointmentDTO>>() {
				}.getType()), HttpStatus.OK);
	}

	/**
	 * Endpoint returning all the appointments of a specific patient
	 * 
	 * @param id most not be null.
	 * @return a list of appointment
	 * @throws NotFoundException if no patient with the id exists
	 */
	@GetMapping(value = "/patient/{id}")
	public ResponseEntity<List<AppointmentDTO>> getAllPatientAppointmentsById(@PathVariable("id") Long id)
			throws NotFoundException {
		Patient patient = patientService.getById(id);
		if (patient == null) {
			throw new NotFoundException(String.format("Patient with id %d was not found", id));
		}
		List<Appointment> appointments = appointmentService.findAppByPatientId(id);

		return new ResponseEntity<>(
				(List<AppointmentDTO>) modelMapper.map(appointments, new TypeToken<List<AppointmentDTO>>() {
				}.getType()), HttpStatus.OK);
	}

	/**
	 * Endpoint that saves a given appointment object. The doctor and patient
	 * entities should exist An patient should belong to a specific doctor The end
	 * date cannot be earlier than the start date A doctor cannot have more than one
	 * appointment between an interval of time
	 * 
	 * @param appointmentDTO object
	 * @return an appointment object saved in DB
	 * @throws NotFoundException   if the id of the doctor and patient objects does
	 *                             not exist in DB
	 * @throws BadRequestException if the appointment end date is earlier than the
	 *                             start date, and if the user tries to make an
	 *                             appointment in the past
	 */
	@PostMapping
	public ResponseEntity<AppointmentDTO> save(@RequestBody AppointmentDTO appointmentDTO)
			throws NotFoundException, BadRequestException {

		Doctor doc = doctorService.getById(appointmentDTO.getDoctorId());
		if (doc == null) {
			throw new NotFoundException(String.format("Doctor with id %d was not found", appointmentDTO.getDoctorId()));
		}

		Patient patient = patientService.getById(appointmentDTO.getPatientId());
		if (patient == null) {
			throw new NotFoundException(
					String.format("Patient with id %d was not found", appointmentDTO.getPatientId()));
		}

		if (!doc.getPatients().contains(patient))
			throw new NotFoundException(
					"The patient named: " + patient.getFirstName() + " does not belong to " + doc.getFirstName());

		if (appointmentDTO.getStartDate().compareTo(appointmentDTO.getEndTime()) >= 0) {
			throw new BadRequestException("The end date cannot be earlier than the start date!");
		}
		if (appointmentDTO.getStartDate().compareTo(LocalDateTime.now()) < 0) {
			throw new BadRequestException("You cannot make an appointment in the past!");
		}

		List<Appointment> appointmnets = null;
		appointmnets = appointmentService.findAppByDoctorId(doc.getId());
		for (Appointment p : appointmnets) {
			if (appointmentDTO.getStartDate().isAfter(p.getStartDate())
					&& appointmentDTO.getStartDate().isBefore(p.getEndTime()))
				throw new BadRequestException("An appointment already exists between this time");
			if (appointmentDTO.getEndTime().isAfter(p.getStartDate())
					&& appointmentDTO.getEndTime().isBefore(p.getEndTime()))
				throw new BadRequestException("An appointment already exists between this time");
			if (appointmentDTO.getStartDate().isEqual(p.getStartDate()))
				throw new BadRequestException("An appointment already exists between this time");

		}
		// Appointment newAppointment =
		// appointmentService.save(modelMapper.map(appointmentDTO, Appointment.class));
		// Error dispatching
		Appointment app = new Appointment(appointmentDTO.getCause(), appointmentDTO.getStartDate(),
				appointmentDTO.getEndTime(), doc, patient);
		appointmentService.save(app);
		return new ResponseEntity<>(modelMapper.map(app, AppointmentDTO.class), HttpStatus.CREATED);
	}

	/**
	 * Endpoint that deletes a given appointment by id
	 * 
	 * @param id of the targeted appointment
	 * @return {@link Void}
	 * @throws BadRequestException if appointment with given id does not exist in DB
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> deleteAppointmentById(@PathVariable Long id) throws BadRequestException {
		Appointment app = appointmentService.getById(id);
		if (app == null)
			throw new BadRequestException("The appointment with id " + id + " does not exist!");

		appointmentService.deleteAppointment(id);
		return ResponseEntity.noContent().build();
	}

}
