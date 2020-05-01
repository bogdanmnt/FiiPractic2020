package fii.practic.health.entity.repository;

import fii.practic.health.entity.model.Doctor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DoctorRepository extends JpaRepository<Doctor, Long> {

    /*
        Automatically generated query, only by naming convention
        It knows to generate a "SELECT * from Doctor d join Patient p on d.id = p.doctorId where p.firstName = <firstName>" because of method name
        IntelliJ has great support for these queries, try to write "find" and add press CTRL + Space for suggestions
        Search for JPA Named query documentation

        @return List of doctors, with the condition that every doctor has a patient with given firstName
    */
    List<Doctor> findDoctorsByPatientsFirstName(String firstName);

    @Query("SELECT d from Doctor d JOIN d.patients p WHERE p.lastName = :lastName")
    List<Doctor> findDoctorByPatientsLastName(@Param("lastName") String lastName);
}
