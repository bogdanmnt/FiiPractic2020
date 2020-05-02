package fii.practic.health.boundry.controller;


import java.util.ArrayList;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
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
           this.appointmentService=appointmentService;
    }
    
    
    @GetMapping
    public ResponseEntity<List<AppointmentDTO>> getAppointments() {
        List<Appointment> appointments = appointmentService.getAll();

        return new ResponseEntity<>((List<AppointmentDTO>) modelMapper.map(appointments, new TypeToken<List<AppointmentDTO>>(){}.getType()), HttpStatus.OK);
    }
    
    
    @GetMapping(value = "/future/appointments")
    public ResponseEntity<List<AppointmentDTO>> getFutureAppointments() {
        List<Appointment> appointments = appointmentService.findFutureAppointments();

        return new ResponseEntity<>((List<AppointmentDTO>) modelMapper.map(appointments, new TypeToken<List<AppointmentDTO>>(){}.getType()), HttpStatus.OK);
    }
    
    @GetMapping(value = "/future/appointments/doctor/{doctor_id}")
    public ResponseEntity<List<AppointmentDTO>> getFutureAppointments(@PathVariable("doctor_id") Long doctor_id) {
        List<Appointment> appointments = appointmentService.findFutureAppointmentsByDoctorId(doctor_id);

        return new ResponseEntity<>((List<AppointmentDTO>) modelMapper.map(appointments, new TypeToken<List<AppointmentDTO>>(){}.getType()), HttpStatus.OK);
    }
    
    @GetMapping(value = "/doctor/{id}")
    public ResponseEntity<List<AppointmentDTO>> getAllAppointmentsByDoctorId(@PathVariable("id") Long id) throws NotFoundException {
        Doctor doctor = doctorService.getById(id);
        if(doctor == null){
            throw new NotFoundException(String.format("Doctor with id %d was not found", id));
        }
        List<Appointment> appointments = appointmentService.findAppByDoctorId(id);

        return new ResponseEntity<>((List<AppointmentDTO>) modelMapper.map(appointments, new TypeToken<List<AppointmentDTO>>(){}.getType()), HttpStatus.OK);
    }
    

    
    @GetMapping(value = "/patient/{id}")
    public ResponseEntity<List<AppointmentDTO>> getAllPatientAppointmentsById(@PathVariable("id") Long id) throws NotFoundException {
        Patient patient = patientService.getById(id);
        if(patient == null){
            throw new NotFoundException(String.format("Patient with id %d was not found", id));
        }
        List<Appointment> appointments = appointmentService.findAppByPatientId(id);

        return new ResponseEntity<>((List<AppointmentDTO>) modelMapper.map(appointments, new TypeToken<List<AppointmentDTO>>(){}.getType()), HttpStatus.OK);
    }
    
    
    @PostMapping 
    public ResponseEntity<AppointmentDTO> save(@RequestBody AppointmentDTO appointmentDTO) throws NotFoundException, BadRequestException{
    
        Doctor doc = doctorService.getById(appointmentDTO.getDoctorId());
        if(doc == null){
            throw new NotFoundException(String.format("Doctor with id %d was not found", appointmentDTO.getDoctorId()));
        }
        
        Patient patient = patientService.getById(appointmentDTO.getPatientId());
        if(patient == null){
            throw new NotFoundException(String.format("Patient with id %d was not found", appointmentDTO.getPatientId()));
        }
        
      if(!doc.getPatients().contains(patient))
    	  throw new NotFoundException("The patient named: " + patient.getFirstName() + " does not belong to " + 
       doc.getFirstName());
      
      
      if (appointmentDTO.getStartDate().compareTo(appointmentDTO.getEndTime()) >= 0) {
			throw new BadRequestException("The end date cannot be earlier than the start date!");
		}
      
      List<Appointment> appointmnets = null;
      appointmnets=appointmentService.findAppByDoctorId(doc.getId());
     for(Appointment p : appointmnets) {
    	 if(appointmentDTO.getStartDate().after(p.getStartDate()) && appointmentDTO.getStartDate().before(p.getEndTime()))
    		 throw new BadRequestException("An appointment already exists between this time");
    		 if(appointmentDTO.getEndTime().after(p.getStartDate()) && appointmentDTO.getEndTime().before(p.getEndTime())) 
    		 throw new BadRequestException("An appointment already exists between this time");
     }
     //Appointment newAppointment = appointmentService.save(modelMapper.map(appointmentDTO, Appointment.class));
     //Error dispatching
     Appointment app=new Appointment(appointmentDTO.getCause(), appointmentDTO.getStartDate(), appointmentDTO.getEndTime(), doc, patient);
     appointmentService.save(app);
     		return new ResponseEntity<>(modelMapper.map(app, AppointmentDTO.class), HttpStatus.CREATED);
    		}


    

    
}
