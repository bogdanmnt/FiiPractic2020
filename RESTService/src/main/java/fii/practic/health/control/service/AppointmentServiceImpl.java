package fii.practic.health.control.service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fii.practic.health.boundry.exceptions.BadRequestException;
import fii.practic.health.entity.model.Appointment;

import fii.practic.health.entity.repository.AppointmentRepository;
import fii.practic.health.entity.repository.DoctorRepository;
import fii.practic.health.entity.repository.PatientRepository;

/**
 * 
 * @author bogdan
 * 
 */
@Service
public class AppointmentServiceImpl implements AppointmentService {

	private AppointmentRepository appointmentRepository;
	private DoctorRepository doctorRepository;
	private PatientRepository patientRepository;
	private JavaMailUtil mail;

	@Autowired
	public AppointmentServiceImpl(AppointmentRepository appointmentRepository, DoctorRepository doctorRepository,
			PatientRepository patientRepository, JavaMailUtil mail) {
		this.appointmentRepository = appointmentRepository;
		this.doctorRepository = doctorRepository;
		this.patientRepository = patientRepository;
		this.mail = mail;
	}

	/**
	 * It returns all the appointments from DB
	 * 
	 * @return All the appointments from the past and future
	 */
	@Override
	public List<Appointment> getAll() {
		return appointmentRepository.findAll();
	}

	/**
	 * It saves into the DB an appointment object Before an appointment making, it
	 * send both to the doctor and patient an email with confirmation and details of
	 * the appointment
	 * 
	 * @param appointment must not be null
	 * @return an appointment entity
	 */
	@Override
	public Appointment save(Appointment appointment) {

		File htmlTemplateFileDoctor = new File("src/main/resources/DocAppointment.html");
		String htmlStringDoctor = null;
		File htmlTemplateFilePatient = new File("src/main/resources/PatientAppointment.html");
		String htmlStringPatient = null;
		try {
			htmlStringDoctor = FileUtils.readFileToString(htmlTemplateFileDoctor, "UTF-8");
			mail.sendMail(appointment.getDoctor().getEmail().getEmail(), htmlStringDoctor, appointment);
			htmlStringPatient = FileUtils.readFileToString(htmlTemplateFilePatient, "UTF-8");
			mail.sendMail(appointment.getPatient().getEmail().getEmail(), htmlStringDoctor, appointment);

		} catch (Exception e1) {

			e1.printStackTrace();
		}

		return appointmentRepository.save(appointment);
	}

	/**
	 * This method is looking for a doctor`s appointments
	 * 
	 * @param Long doctor_id doctor`s id
	 * @return All of the doctor`s appointments
	 */
	@Override
	public List<Appointment> findAppByDoctorId(Long doctor_id) {
		return appointmentRepository.findAppointmentsByDoctorId(doctor_id);
	}

	/**
	 * The method is looking for all appointments of a specific patient
	 * 
	 * @param Long patient_id Id of the patient
	 * @return A list of Appointment
	 */
	@Override
	public List<Appointment> findAppByPatientId(Long patient_id) {
		return appointmentRepository.findAppointmentsByPatientId(patient_id);
	}

	/**
	 * This method is looking for every appointment that did not take place
	 * 
	 * @return A list of Appointment from the current time
	 */
	@Override
	public List<Appointment> findFutureAppointments() {
		return appointmentRepository.findFutureAppointments();
	}

	/**
	 * It find all the apppointments that can be canceled
	 * 
	 * @return List<Appointment>
	 */
	@Override
	public List<Appointment> findFutureAppointmentsThatCanBeCanceled() {
		return appointmentRepository.findAllAppointmentsCanBeCanceled();
	}

	/**
	 * This method is looking for all doctor`s appointments, from the moment this
	 * method is called
	 * 
	 * @param Long doctor_id the doctor`s id
	 * @return List<Appointment>
	 */
	@Override
	public List<Appointment> findFutureAppointmentsByDoctorId(Long doctor_id) {

		return appointmentRepository.findFutureAppointmentsByDoctorId(doctor_id);
	}

	/**
	 * This method deletes an appointment
	 * 
	 * @param Long id The id of the appointment
	 * @throws BadRequestException
	 */
	@Override
	public void deleteAppointment(Long id) throws BadRequestException {
		Appointment app = appointmentRepository.findWhatAppointmentsCanBeCanceledByAppId(id);
		if (app == null)
			throw new BadRequestException("The appointment with id: " + id + " cannot be canceled!");

		appointmentRepository.delete(app);
	}

	/**
	 * This method returns an appointment by id
	 * 
	 * @param id
	 */
	@Override
	public Appointment getById(Long id) {
		return appointmentRepository.findById(id).orElse(null);
	}

	@Override
	public Long findLastAppointmentId() {
		return appointmentRepository.findLastAppId();

	}

	@Override
	public List<Appointment> findAllEndedAppointments() {

		return appointmentRepository.findAllEndedAppointments();
	}

	@Override
	public List<Appointment> TestScheduller(LocalDateTime xxx) {
		// TODO Auto-generated method stub
		return appointmentRepository.TestScheduller(xxx);
	}

}
