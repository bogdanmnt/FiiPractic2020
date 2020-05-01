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
import fii.practic.health.control.service.DoctorService;
import fii.practic.health.control.service.PatientService;
import fii.practic.health.entity.model.Patient;

@RestController
@RequestMapping(value = "/api/patients")
public class PatientController {

    private PatientService patientService;
    private DoctorService doctorService;
    private ModelMapper modelMapper;

    @Autowired
    public PatientController(PatientService patientService, DoctorService doctorService, ModelMapper modelMapper) {
        this.patientService = patientService;
        this.doctorService = doctorService;
        this.modelMapper = modelMapper;
    }

    @GetMapping
    public ResponseEntity<List<PatientDTO>> getPatients() {
        List<Patient> patients = patientService.getAll();

        return new ResponseEntity<>((List<PatientDTO>) modelMapper.map(patients, new TypeToken<List<PatientDTO>>(){}.getType()), HttpStatus.OK);
    }


    @GetMapping(value = "/{id}")
    public ResponseEntity<PatientDTO> getById(@PathVariable("id") Long id) {
        Patient patient = patientService.getById(id);

        return new ResponseEntity<>(modelMapper.map(patient, PatientDTO.class), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<PatientDTO> save(@RequestBody PatientDTO patientDTO){
        Patient newPatient = patientService.save(modelMapper.map(patientDTO, Patient.class));

        return new ResponseEntity<>(modelMapper.map(newPatient, PatientDTO.class), HttpStatus.CREATED);
    }

    @PatchMapping(value = "/{id}")
    public ResponseEntity<PatientDTO> patch(@PathVariable Long id, @RequestBody PatientDTO patientDTO){
        Patient dbPatient = patientService.getById(id);

        if(dbPatient != null) {
            modelMapper.map(patientDTO, dbPatient);

            return new ResponseEntity<>(modelMapper.map(patientService.patch(dbPatient), PatientDTO.class), HttpStatus.OK);
        }

        return null;
    }

    @PutMapping(value = "/{id}")
    public ResponseEntity<PatientDTO> update(@PathVariable Long id, @RequestBody PatientDTO patientDTO){
        Patient dbPatient = patientService.getById(id);

        if(dbPatient != null) {
            modelMapper.getConfiguration().setSkipNullEnabled(false);
            modelMapper.map(patientDTO, dbPatient);
            modelMapper.getConfiguration().setSkipNullEnabled(true);

            return new ResponseEntity<>(modelMapper.map(patientService.update(dbPatient), PatientDTO.class), HttpStatus.OK);
        }

        return null;
    }

    @DeleteMapping(value = "/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id){
        Patient dbPatient = patientService.getById(id);

        if(dbPatient != null){
            patientService.delete(dbPatient);
        }

        return ResponseEntity.noContent().build();
    }
}
