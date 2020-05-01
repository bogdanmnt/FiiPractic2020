package fii.practic.health.control.service;

import fii.practic.health.entity.model.Doctor;
import fii.practic.health.entity.repository.DoctorRepository;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import java.io.File;
import java.util.List;


@Service
public class DoctorServiceImpl implements DoctorService {

    private DoctorRepository doctorRepository;
    private JavaMailUtil mail;

    @Autowired
    public DoctorServiceImpl(DoctorRepository doctorRepository,JavaMailUtil mail) {
        this.doctorRepository = doctorRepository;
        this.mail=mail;
    }

    @Override
    public List<Doctor> getAll() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor getById(Long id) {
        return doctorRepository.findById(id).orElse(null);
    }
    
    @Override
    public Doctor save(Doctor doctor) {
   
    	File htmlTemplateFile = new File("src/main/resources/DoctorTemplate.html");
  
    	String htmlString=null;
		
    	try {
    		htmlString = FileUtils.readFileToString(htmlTemplateFile, "UTF-8");
      
			mail.sendMail(doctor.getEmail().getEmail(), htmlString, doctor);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
    	
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> findDoctorsByPatientsFirstName(String firstName) {
        return doctorRepository.findDoctorsByPatientsFirstName(firstName);
    }

    @Override
    public Doctor update(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public Doctor patch(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public void delete(Doctor doctor) {
        doctorRepository.delete(doctor);
    }

	
}
