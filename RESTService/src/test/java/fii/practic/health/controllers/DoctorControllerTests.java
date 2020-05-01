package fii.practic.health.controllers;

import fii.practic.health.boundry.controller.DoctorController;
import fii.practic.health.boundry.dto.DoctorDTO;
import fii.practic.health.boundry.dto.EmailDTO;
import fii.practic.health.boundry.exceptions.BadRequestException;
import fii.practic.health.boundry.exceptions.NotFoundException;
import fii.practic.health.control.service.DoctorServiceImpl;
import fii.practic.health.entity.model.Doctor;
import fii.practic.health.entity.model.Email;
import org.assertj.core.api.Assertions;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import static org.junit.Assert.fail;

public class DoctorControllerTests {

    @Mock
    private DoctorServiceImpl doctorService;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private DoctorController doctorController;

    private Doctor doctor;
    private DoctorDTO doctorDTO;

    @Before
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        doctor = buildDoctor();
        doctorDTO = buildDoctorDTO();
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

    private DoctorDTO buildDoctorDTO(){
        DoctorDTO doctor = new DoctorDTO();
        doctor.setId(1L);
        doctor.setFirstName("FirstName");
        doctor.setLastName("LastName");
        doctor.setSpeciality("Speciality");

        EmailDTO email = new EmailDTO();
        email.setId(1L);
        email.setEmail("email@email.com");

        doctor.setEmail(email);

        return doctor;
    }

    @Test
    public void shouldReturnResponseEntityForSave(){
        //arrange
        Mockito.when(modelMapper.map(doctorDTO, Doctor.class)).thenReturn(doctor);
        Mockito.when(doctorService.save(doctor)).thenReturn(doctor);
        Mockito.when(modelMapper.map(doctor, DoctorDTO.class)).thenReturn(doctorDTO);

        //act
        ResponseEntity<DoctorDTO> doctorDTOResponseEntity = doctorController.save(doctorDTO);

        //assert
        Assertions.assertThat(doctorDTOResponseEntity).isNotNull();
        Assertions.assertThat(doctorDTOResponseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        Assertions.assertThat(doctorDTOResponseEntity.getBody()).isEqualToComparingFieldByFieldRecursively(doctorDTO);
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnNotFoundExceptionForGetById() throws NotFoundException {
        //arrange
        Mockito.when(doctorService.getById(2L)).thenReturn(null);

        //act
        ResponseEntity<DoctorDTO> doctor = doctorController.getById(2L);

        //assert
        fail();
    }

    @Test(expected = BadRequestException.class)
    public void shouldReturnBadRequestExceptionForUpdate() throws NotFoundException, BadRequestException {
        DoctorDTO doctorDTOUpdate = new DoctorDTO();
        doctorDTOUpdate.setId(3L);

        ResponseEntity<DoctorDTO> doctor = doctorController.update(2L, doctorDTOUpdate);

        fail();
    }

    @Test(expected = NotFoundException.class)
    public void shouldReturnNotFoundExceptionForUpdate() throws NotFoundException, BadRequestException {
        DoctorDTO doctorDTOUpdate = new DoctorDTO();
        doctorDTOUpdate.setId(2L);
        Mockito.when(doctorService.getById(2L)).thenReturn(null);

        ResponseEntity<DoctorDTO> doctor = doctorController.update(2L, doctorDTOUpdate);

        fail();
    }

    @After
    public void tearDown(){
        doctorDTO = null;
        doctor = null;
    }

}
