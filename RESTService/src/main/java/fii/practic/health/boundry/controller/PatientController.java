package fii.practic.health.boundry.controller;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fii.practic.health.boundry.dto.PatientDTO;
import fii.practic.health.boundry.exceptions.BadRequestException;
import fii.practic.health.boundry.exceptions.NotFoundException;
import fii.practic.health.control.service.PatientService;
import fii.practic.health.entity.model.Patient;

@RestController
@RequestMapping(value = "/api/patients")
public class PatientController {

	private PatientService patientService;
	private ModelMapper modelMapper;

	@Autowired
	public PatientController(PatientService patientService, ModelMapper modelMapper) {
		this.patientService = patientService;
		this.modelMapper = modelMapper;
	}

	/**
	 * Endpoint for showing all patients
	 * 
	 * @return A list of patient objects
	 */
	@GetMapping
	public ResponseEntity<List<PatientDTO>> getPatients() {
		List<Patient> patients = patientService.getAll();

		return new ResponseEntity<>((List<PatientDTO>) modelMapper.map(patients, new TypeToken<List<PatientDTO>>() {
		}.getType()), HttpStatus.OK);
	}

	/**
	 * Endpoint for showing a patient based on its id
	 * 
	 * @param id the `id` of the patient from DB
	 * @return A `patient` object
	 * @throws NotFoundException if targeted patient to be updated was not found
	 */
	@GetMapping(value = "/{id}")
	public ResponseEntity<PatientDTO> getById(@PathVariable("id") Long id) throws NotFoundException {
		Patient patient = patientService.getById(id);
		if (patient == null)
			throw new NotFoundException(String.format("Patient with id %d was not found", id));
		return new ResponseEntity<>(modelMapper.map(patient, PatientDTO.class), HttpStatus.OK);
	}

	/**
	 * Endpoint for saving a patient in database
	 * 
	 * @param patientDTO a json with a patient properties
	 * @return A 'patient' object representing the saved entry in the DB
	 */
	@PostMapping
	public ResponseEntity<PatientDTO> save(@RequestBody PatientDTO patientDTO) {
		Patient newPatient = patientService.save(modelMapper.map(patientDTO, Patient.class));

		return new ResponseEntity<>(modelMapper.map(newPatient, PatientDTO.class), HttpStatus.CREATED);
	}

	/**
	 * Endpont for partial 'patching' a patient based on its id in the database.
	 * 
	 * @param id         The 'id' in the database of the targeted patient
	 * @param patientDTO A json with the updated fields of the patient
	 * @return A 'patient' object representing the updated entry in the DB. Returns
	 *         null if id does not match with DB
	 */
	@PatchMapping(value = "/{id}")
	public ResponseEntity<PatientDTO> patch(@PathVariable Long id, @RequestBody PatientDTO patientDTO) {
		Patient dbPatient = patientService.getById(id);

		if (dbPatient != null) {
			modelMapper.map(patientDTO, dbPatient);

			return new ResponseEntity<>(modelMapper.map(patientService.patch(dbPatient), PatientDTO.class),
					HttpStatus.OK);
		}

		return null;
	}

	/**
	 * Endpoint for updating a patient based on its id in the database.
	 * 
	 * @param id         The 'id' in the database of the targeted patient
	 * @param patientDTO A json with the new version of the patient
	 * @return A 'patient' object representing the updated entry in the DB
	 * @throws NotFoundException   if targeted patient to be updated was not found
	 * @throws BadRequestException if id are not the same
	 */
	@PutMapping(value = "/{id}")
	public ResponseEntity<PatientDTO> update(@PathVariable Long id, @RequestBody PatientDTO patientDTO)
			throws NotFoundException {
		Patient dbPatient = patientService.getById(id);

		if (dbPatient != null) {
			modelMapper.getConfiguration().setSkipNullEnabled(false);
			modelMapper.map(patientDTO, dbPatient);
			modelMapper.getConfiguration().setSkipNullEnabled(true);

			return new ResponseEntity<>(modelMapper.map(patientService.update(dbPatient), PatientDTO.class),
					HttpStatus.OK);
		} else {
			throw new NotFoundException(String.format("Patient with id %d was not found", id));
		}

	}

	/**
	 * Endpoint for deleting a patient based on its id from database
	 * 
	 * @param id The `id` from database of the targeted patient
	 * @return
	 */
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> delete(@PathVariable Long id) {
		Patient dbPatient = patientService.getById(id);

		if (dbPatient != null) {
			patientService.delete(dbPatient);
		}

		return ResponseEntity.noContent().build();
	}
}
