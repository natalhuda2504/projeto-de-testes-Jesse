package org.isf.vaccine;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

import org.isf.utils.exception.OHServiceException;
import org.isf.vaccine.model.Vaccine;
import org.isf.vaccine.service.VaccineIoOperations;
import org.isf.vaccine.service.VaccineIoOperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class VaccineIoOperationsStateTest {

    @Mock
    private VaccineIoOperationRepository repository;

    @InjectMocks
    private VaccineIoOperations vaccineIoOperations;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testGetVaccine() throws OHServiceException {
        Vaccine vaccine1 = new Vaccine();
        vaccine1.setCode("V001");
        Vaccine vaccine2 = new Vaccine();
        vaccine2.setCode("V002");
        when(repository.findByVaccineType_CodeOrderByDescriptionAsc(anyString())).thenReturn(Arrays.asList(vaccine1, vaccine2));

        List<Vaccine> vaccines = vaccineIoOperations.getVaccine("TYPE_CODE");
        assertEquals(2, vaccines.size());
        assertEquals("V001", vaccines.get(0).getCode());
    }

    @Test
    public void testNewVaccine() throws OHServiceException {
        Vaccine vaccine = new Vaccine();
        vaccine.setCode("V003");
        vaccine.setDescription("New Vaccine");

        when(repository.save(any(Vaccine.class))).thenReturn(vaccine);

        Vaccine result = vaccineIoOperations.newVaccine(vaccine);
        assertEquals("V003", result.getCode());
        assertEquals("New Vaccine", result.getDescription());
    }

    @Test
    public void testUpdateVaccine() throws OHServiceException {
        Vaccine vaccine = new Vaccine();
        vaccine.setCode("V001");
        vaccine.setDescription("Updated Vaccine");

        when(repository.save(any(Vaccine.class))).thenReturn(vaccine);

        Vaccine result = vaccineIoOperations.updateVaccine(vaccine);
        assertEquals("V001", result.getCode());
        assertEquals("Updated Vaccine", result.getDescription());
    }

    @Test
    public void testDeleteVaccine() throws OHServiceException {
        Vaccine vaccine = new Vaccine();
        vaccine.setCode("V001");

        doNothing().when(repository).delete(any(Vaccine.class));

        vaccineIoOperations.deleteVaccine(vaccine);

        verify(repository).delete(vaccine);
    }

    @Test
    public void testIsCodePresent() throws OHServiceException {
        when(repository.existsById(anyString())).thenReturn(true);

        boolean exists = vaccineIoOperations.isCodePresent("V001");
        assertTrue(exists);
    }

    @Test
    public void testFindVaccine() throws OHServiceException {
        Vaccine vaccine = new Vaccine();
        vaccine.setCode("V001");
        when(repository.findById(anyString())).thenReturn(Optional.of(vaccine));

        Vaccine result = vaccineIoOperations.findVaccine("V001");
        assertNotNull(result);
        assertEquals("V001", result.getCode());
    }

    @Test
    public void testCountAllActiveVaccinations() throws OHServiceException {
        when(repository.countAllActiveVaccinations()).thenReturn(10L);

        long count = vaccineIoOperations.countAllActiveVaccinations();
        assertEquals(10L, count);
    }

    @Test
    public void executeSmokeTests() throws OHServiceException {
        testNewVaccine();
        testFindVaccine();
        testIsCodePresent();
    }

    @Test
    public void executeFunctionalTests() throws OHServiceException {
        testGetVaccine();
        testUpdateVaccine();
        testDeleteVaccine();
        testCountAllActiveVaccinations();
    }

    @Test
    public void executeStabilityTests() throws OHServiceException {
        for (int i = 0; i < 100; i++) {
            Vaccine vaccine = new Vaccine();
            vaccine.setCode("V" + i);
            vaccine.setDescription("Vaccine " + i);
            vaccineIoOperations.newVaccine(vaccine);
            vaccineIoOperations.deleteVaccine(vaccine);
        }

        Vaccine vaccine = new Vaccine();
        vaccine.setCode("V002");
        vaccine.setDescription("Vaccine 2");
        vaccineIoOperations.newVaccine(vaccine);

        for (int i = 0; i < 100; i++) {
            vaccine.setDescription("Updated Vaccine " + i);
            vaccineIoOperations.updateVaccine(vaccine);
        }
    }
}