package fii.practic.health.services;

import fii.practic.health.control.service.DoctorServiceImpl;
import fii.practic.health.entity.model.Doctor;
import fii.practic.health.entity.model.Email;
import fii.practic.health.entity.repository.DoctorRepository;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class DoctorServiceTest {

    @Mock
    private DoctorRepository doctorRepository;

    @InjectMocks
    private DoctorServiceImpl doctorService;

    private Doctor doctor;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        doctor = buildDoctor();
    }

    private Doctor buildDoctor(){
        Doctor doctor = new Doctor();
        doctor.setId(1L);
        doctor.setFirstName("FirstName");
        doctor.setLastName("LastName");
        doctor.setSpeciality("Speciality");

        Email email = new Email();
        email.setId(1L);
        email.setEmail("email@email.com");

        doctor.setEmail(email);

        return doctor;
    }


    @Test
    public void shouldReturnDoctorById(){
        Mockito.when(doctorRepository.findById(1L)).thenReturn(Optional.ofNullable(doctor));

        Doctor doctorById = doctorService.getById(1L);

        Assertions.assertThat(doctorById).isNotNull();
        Assertions.assertThat(doctorById).isEqualToComparingFieldByFieldRecursively(doctor);
    }

    @Test
    public void shouldReturnListOfDoctors(){
        Doctor doctor1 = buildDoctor();
        List<Doctor> doctors = Arrays.asList(doctor, doctor1);
        Mockito.when(doctorRepository.findAll()).thenReturn(doctors);

        List<Doctor> allDoctors = doctorService.getAll();

        Assertions.assertThat(allDoctors).isNotNull();
        Assertions.assertThat(allDoctors.size()).isEqualTo(2);
    }

    @After
    public void tearDown(){
        doctor = null;
    }
}
