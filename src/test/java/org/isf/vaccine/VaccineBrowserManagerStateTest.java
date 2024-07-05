package org.isf.vaccine;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

import java.util.ArrayList;
import java.util.List;

import org.isf.utils.exception.OHDataIntegrityViolationException;
import org.isf.utils.exception.OHDataValidationException;
import org.isf.utils.exception.OHServiceException;
import org.isf.vaccine.manager.VaccineBrowserManager;
import org.isf.vaccine.model.Vaccine;
import org.isf.vaccine.service.VaccineIoOperations;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class VaccineBrowserManagerStateTest {

    @Mock
    private VaccineIoOperations vaccineIoOperations;

    @InjectMocks
    private VaccineBrowserManager vaccineBrowserManager;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        setupInitialState();
    }

    public void setupInitialState() {
        // Configure o estado inicial para os testes
    }

    @Test
    public void testNewVaccine() throws OHServiceException {
        Vaccine vaccine = new Vaccine();
        vaccine.setCode("V001");
        vaccine.setDescription("Vaccine 1");

        when(vaccineIoOperations.isCodePresent(any(String.class))).thenReturn(false);
        when(vaccineIoOperations.newVaccine(any(Vaccine.class))).thenReturn(vaccine);

        Vaccine result = vaccineBrowserManager.newVaccine(vaccine);

        assertEquals("V001", result.getCode());
        assertEquals("Vaccine 1", result.getDescription());
        verify(vaccineIoOperations).newVaccine(vaccine);
    }

    @Test
    public void testUpdateVaccine() throws OHServiceException{
        Vaccine vaccine = new Vaccine();
        vaccine.setCode("V001");
        vaccine.setDescription("Vaccine 1");

        when(vaccineIoOperations.updateVaccine(any(Vaccine.class))).thenReturn(vaccine);

        Vaccine result = vaccineBrowserManager.updateVaccine(vaccine);

        assertEquals("V001", result.getCode());
        assertEquals("Vaccine 1", result.getDescription());
        verify(vaccineIoOperations).updateVaccine(vaccine);
    }

    @Test
    public void testDeleteVaccine() throws OHServiceException{
        Vaccine vaccine = new Vaccine();
        vaccine.setCode("V001");

        vaccineBrowserManager.deleteVaccine(vaccine);

        verify(vaccineIoOperations).deleteVaccine(vaccine);
    }

    @Test
    public void testFindVaccine() throws OHServiceException{
        Vaccine vaccine = new Vaccine();
        vaccine.setCode("V001");

        when(vaccineIoOperations.findVaccine(any(String.class))).thenReturn(vaccine);

        Vaccine result = vaccineBrowserManager.findVaccine("V001");

        assertEquals("V001", result.getCode());
        verify(vaccineIoOperations).findVaccine("V001");
    }

    @Test
    public void executeSmokeTests() throws OHServiceException{
        testNewVaccine();
        testFindVaccine();
    }

    @Test
    public void executeFunctionalTests() throws OHServiceException{
        testUpdateVaccine();
        testDeleteVaccine();
    }

    @Test
    public void executeStabilityTests() throws OHServiceException{
        for (int i = 0; i < 100; i++) {
            Vaccine vaccine = new Vaccine();
            vaccine.setCode("V" + i);
            vaccine.setDescription("Vaccine " + i);
            vaccineBrowserManager.newVaccine(vaccine);
            vaccineBrowserManager.deleteVaccine(vaccine);
        }

        Vaccine vaccine = new Vaccine();
        vaccine.setCode("V002");
        vaccine.setDescription("Vaccine 2");
        vaccineBrowserManager.newVaccine(vaccine);

        for (int i = 0; i < 100; i++) {
            vaccine.setDescription("Updated Vaccine " + i);
            vaccineBrowserManager.updateVaccine(vaccine);
        }
    }
}
