package fii.practic.health.boundry.controller;

import fii.practic.health.boundry.dto.DoctorDTO;
import fii.practic.health.boundry.exceptions.BadRequestException;
import fii.practic.health.boundry.exceptions.NotFoundException;
import fii.practic.health.control.service.PatientService;
import fii.practic.health.entity.model.Doctor;
import fii.practic.health.control.service.DoctorService;
import fii.practic.health.entity.model.Patient;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(value = "/api/doctors")
@CrossOrigin
public class DoctorController {
	Logger log = LoggerFactory.getLogger(DoctorController.class);
	private DoctorService doctorService;
	private ModelMapper modelMapper;
	private PatientService patientService;

	@Autowired
	public DoctorController(DoctorService doctorService, ModelMapper modelMapper, PatientService patientService) {
		this.doctorService = doctorService;
		this.modelMapper = modelMapper;
		this.patientService = patientService;
	}

	/**
	 * Endpoint for showing all doctors
	 * 
	 * @return A list of doctors objects
	 */
	@GetMapping
	public ResponseEntity<List<DoctorDTO>> getDoctors() {
		List<Doctor> doctors = doctorService.getAll();
		log.info("SALUT");
		return new ResponseEntity<>((List<DoctorDTO>) modelMapper.map(doctors, new TypeToken<List<DoctorDTO>>() {
		}.getType()), HttpStatus.OK);
	}

	/**
	 * Endpoint for showing a doctor based on its id
	 * 
	 * @param id the `id` of the doctor from DB
	 * @return A `doctor` object
	 * @throws NotFoundException if targeted doctor to be updated was not found
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<DoctorDTO> getById(@PathVariable("id") Long id) throws NotFoundException {
		Doctor doctor = doctorService.getById(id);

		if (doctor == null) {
			throw new NotFoundException(String.format("Doctor with id %d was not found", id));
		}

		return new ResponseEntity<>(modelMapper.map(doctor, DoctorDTO.class), HttpStatus.OK);
	}

	/**
	 * Endpoint for showing a doctor based on its `firstName`
	 * 
	 * @param firstName the firstName of the doctor from DB
	 * @return A 'doctor' object representing the updated entry in the DB
	 */
	@GetMapping(value = "/filter")
	public ResponseEntity<List<DoctorDTO>> getDoctorsByPatientsFirstName(
			@RequestParam(value = "firstName") String firstName) {
		List<Doctor> doctors = doctorService.findDoctorsByPatientsFirstName(firstName);

		return new ResponseEntity<>((List<DoctorDTO>) modelMapper.map(doctors, new TypeToken<List<DoctorDTO>>() {
		}.getType()), HttpStatus.OK);
	}

	/**
	 * Endpoint for saving a doctor in database
	 * 
	 * @param doctorDTO a json with a doctor properties
	 * @return A 'doctor' object representing the saved entry in the DB
	 */
	@PostMapping
	public ResponseEntity<DoctorDTO> save(@RequestBody DoctorDTO doctorDTO) {
		Doctor newDoctor = doctorService.save(modelMapper.map(doctorDTO, Doctor.class));
		return new ResponseEntity<>(modelMapper.map(newDoctor, DoctorDTO.class), HttpStatus.CREATED);
	}

	/**
	 * Endpont for partial 'patching' a doctor based on its id in the database.
	 * 
	 * @param id        The 'id' in the database of the targeted doctor
	 * @param doctorDTO A json with the updated fields of the doctor
	 * @return A 'doctor' object representing the updated entry in the DB. Returns null if id does not match with DB.
	 */
	@PatchMapping(value = "/{id}")
	public ResponseEntity<DoctorDTO> patch(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO) {
		Doctor dbDoctor = doctorService.getById(id);

		if (dbDoctor != null) {
			modelMapper.map(doctorDTO, dbDoctor);

			return new ResponseEntity<>(modelMapper.map(doctorService.patch(dbDoctor), DoctorDTO.class), HttpStatus.OK);
		}

		return null;
	}

	/**
	 * Endpoint for updating a doctor based on its id in the database.
	 * 
	 * @param id        The 'id' in the database of the targeted doctor
	 * @param doctorDTO A json with the new version of the doctor
	 * @return A 'doctor' object representing the updated entry in the DB
	 * @throws NotFoundException   if targeted doctor to be updated was not found
	 * @throws BadRequestException if ids are not the same
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<DoctorDTO> update(@PathVariable Long id, @RequestBody DoctorDTO doctorDTO)
			throws NotFoundException, BadRequestException {
		if (!id.equals(doctorDTO.getId())) {
			throw new BadRequestException(String
					.format("Id from PathVariable %d is different from id in Request body %d", id, doctorDTO.getId()));
		}

		Doctor dbDoctor = doctorService.getById(id);

		if (dbDoctor == null) {
			throw new NotFoundException(String.format("Doctor with id %d was not found", id));
		}

		modelMapper.getConfiguration().setSkipNullEnabled(false);
		modelMapper.map(doctorDTO, dbDoctor);
		modelMapper.getConfiguration().setSkipNullEnabled(true);

		return new ResponseEntity<>(modelMapper.map(doctorService.update(dbDoctor), DoctorDTO.class), HttpStatus.OK);
	}

	/**
	 * Endpoint for deleting a doctor based on its id from database
	 * 
	 * @param id The `id` from database of the targeted doctor
	 * @return
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		Doctor dbDoctor = doctorService.getById(id);

		if (dbDoctor != null) {
			List<Patient> patients = dbDoctor.getPatients();

			for (Patient patient : patients) {
				patient.setDoctor(null);
				patientService.save(patient);
			}
			dbDoctor.setPatients(null);
			doctorService.save(dbDoctor);
			doctorService.delete(dbDoctor);
		}
		return ResponseEntity.noContent().build();
	}

}
