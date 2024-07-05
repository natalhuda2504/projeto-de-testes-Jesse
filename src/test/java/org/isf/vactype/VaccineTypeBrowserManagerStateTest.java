package org.isf.vactype;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import org.isf.generaldata.MessageBundle;
import org.isf.utils.exception.OHDataIntegrityViolationException;
import org.isf.utils.exception.OHDataValidationException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.vactype.manager.VaccineTypeBrowserManager;
import org.isf.vactype.model.VaccineType;
import org.isf.vactype.service.VacTypeIoOperation;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;

import org.mockito.MockitoAnnotations;

class VaccineTypeBrowserManagerStateTest {

    @Mock
    private VacTypeIoOperation ioOperations;

    @InjectMocks
    private VaccineTypeBrowserManager vaccineTypeBrowserManager;

    private VaccineType vaccineType;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        setupInitialState();
    }

    public void setupInitialState() {
        // Configure o estado inicial para os testes
    }

    @Test
    public void testNewVaccineType() throws OHServiceException {
        VaccineType vaccineType = new VaccineType();
        vaccineType.setCode("V001");
        vaccineType.setDescription("Vaccine 1");

        when(ioOperations.isCodePresent(any(String.class))).thenReturn(false);
        when(ioOperations.newVaccineType(any(VaccineType.class))).thenReturn(vaccineType);

        VaccineType result = vaccineTypeBrowserManager.newVaccineType(vaccineType);

        assertEquals("V001", result.getCode());
        assertEquals("Vaccine 1", result.getDescription());
        verify(ioOperations).newVaccineType(vaccineType);
    }

    @Test
    public void testUpdateVaccineType() throws OHServiceException{
        VaccineType vaccineType = new VaccineType();
        vaccineType.setCode("V001");
        vaccineType.setDescription("VaccineType 1");

        when(ioOperations.updateVaccineType(any(VaccineType.class))).thenReturn(vaccineType);

        VaccineType result = vaccineTypeBrowserManager.updateVaccineType(vaccineType);

        assertEquals("V001", result.getCode());
        assertEquals("VaccineType 1", result.getDescription());
        verify(ioOperations).updateVaccineType(vaccineType);
    }

    @Test
    public void testDeleteVaccineType() throws OHServiceException{
        VaccineType vaccineType = new VaccineType();
        vaccineType.setCode("V001");

        vaccineTypeBrowserManager.deleteVaccineType(vaccineType);

        verify(ioOperations).deleteVaccineType(vaccineType);
    }

    @Test
    public void testFindVaccineType() throws OHServiceException{
        VaccineType vaccineType = new VaccineType();
        vaccineType.setCode("V001");

        when(ioOperations.findVaccineType(any(String.class))).thenReturn(vaccineType);

        VaccineType result = vaccineTypeBrowserManager.findVaccineType("V001");

        assertEquals("V001", result.getCode());
        verify(ioOperations).findVaccineType("V001");
    }

    @Test
    public void executeSmokeTests() throws OHServiceException{
        testNewVaccineType();
        testFindVaccineType();
    }

    @Test
    public void executeFunctionalTests() throws OHServiceException{
        testUpdateVaccineType();
        testDeleteVaccineType();
    }

    @Test
    public void executeStabilityTests() throws OHServiceException{
        for (int i = 0; i < 100; i++) {
            VaccineType vaccineType = new VaccineType();
            vaccineType.setCode("V" + i);
            vaccineType.setDescription("VaccineType " + i);
            vaccineTypeBrowserManager.newVaccineType(vaccineType);
            vaccineTypeBrowserManager.deleteVaccineType(vaccineType);
        }

        VaccineType vaccineType = new VaccineType();
        vaccineType.setCode("V002");
        vaccineType.setDescription("VaccineType 2");
        vaccineTypeBrowserManager.newVaccineType(vaccineType);

        for (int i = 0; i < 100; i++) {
            vaccineType.setDescription("Updated VaccineType " + i);
            vaccineTypeBrowserManager.updateVaccineType(vaccineType);
        }
    }
}