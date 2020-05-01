package fii.practic.health.control.service;

import fii.practic.health.entity.model.Doctor;

import java.util.List;

public interface DoctorService {

    List<Doctor> getAll();

    Doctor getById(Long id);

    Doctor save(Doctor doctor);

    List<Doctor> findDoctorsByPatientsFirstName(String firstName);

    Doctor update(Doctor doctor);

    Doctor patch(Doctor doctor);

    void delete(Doctor doctor);
    

}
