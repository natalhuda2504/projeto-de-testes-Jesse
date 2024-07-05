package org.isf.vaccine;

import java.util.Arrays;
import java.util.List;

import org.isf.utils.exception.OHDataIntegrityViolationException;
import org.isf.utils.exception.OHDataValidationException;
import org.isf.utils.exception.OHServiceException;
import org.isf.utils.exception.model.OHExceptionMessage;
import org.isf.vaccine.manager.VaccineBrowserManager;
import org.isf.vaccine.model.Vaccine;
import org.isf.vaccine.service.VaccineIoOperations;
import org.isf.vactype.model.VaccineType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

class VaccineBrowserManagerTest {

    @Mock
    private VaccineIoOperations ioOperations;

    @InjectMocks
    private VaccineBrowserManager manager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    /* Testes Caixa Preta */

    @Test
    void testGetVaccine() throws OHServiceException {
        Vaccine vaccine1 = new Vaccine();
        vaccine1.setCode("VACC1");
        vaccine1.setDescription("Vaccine 1");

        Vaccine vaccine2 = new Vaccine();
        vaccine2.setCode("VACC2");
        vaccine2.setDescription("Vaccine 2");

        when(ioOperations.getVaccine(null)).thenReturn(Arrays.asList(vaccine1, vaccine2));

        List<Vaccine> vaccines = manager.getVaccine();
        assertNotNull(vaccines);
        assertEquals(2, vaccines.size());
        assertEquals("VACC1", vaccines.get(0).getCode());
        assertEquals("VACC2", vaccines.get(1).getCode());
    }

    @Test
    void testNewVaccine() throws OHServiceException {
        Vaccine vaccine = new Vaccine();
        vaccine.setCode("VACC1");
        vaccine.setDescription("Vaccine 1");

        when(ioOperations.newVaccine(vaccine)).thenReturn(vaccine);
        when(ioOperations.isCodePresent("VACC1")).thenReturn(false);

        Vaccine createdVaccine = manager.newVaccine(vaccine);
        assertNotNull(createdVaccine);
        assertEquals("VACC1", createdVaccine.getCode());
        assertEquals("Vaccine 1", createdVaccine.getDescription());
    }

    @Test
    void testUpdateVaccine() throws OHServiceException {
        Vaccine vaccine = new Vaccine();
        vaccine.setCode("VACC1");
        vaccine.setDescription("Vaccine 1");

        when(ioOperations.updateVaccine(vaccine)).thenReturn(vaccine);

        Vaccine updatedVaccine = manager.updateVaccine(vaccine);
        assertNotNull(updatedVaccine);
        assertEquals("VACC1", updatedVaccine.getCode());
        assertEquals("Vaccine 1", updatedVaccine.getDescription());
    }

    @Test
    void testDeleteVaccine() throws OHServiceException {
        Vaccine vaccine = new Vaccine();
        vaccine.setCode("VACC1");
        vaccine.setDescription("Vaccine 1");

        doNothing().when(ioOperations).deleteVaccine(vaccine);

        manager.deleteVaccine(vaccine);
        verify(ioOperations, times(1)).deleteVaccine(vaccine);
    }

    /* Testes de Interface */

    // Teste getVaccine() sem filtro
    @Test
    void testGetVaccineWithoutFilter() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Type A");
        Vaccine v1 = new Vaccine("001", "Vaccine 1", vt);
        Vaccine v2 = new Vaccine("002", "Vaccine 2", vt);
        List<Vaccine> vaccines = Arrays.asList(v1, v2);

        when(ioOperations.getVaccine(null)).thenReturn(vaccines);

        List<Vaccine> result = manager.getVaccine();

        assertNotNull(result);
        assertEquals(2, result.size());
        assertTrue(result.contains(v1));
        assertTrue(result.contains(v2));
    }

    // Teste getVaccine(String vaccineTypeCode) com filtro válido
    @Test
    void testGetVaccineWithValidFilter() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Type A");
        Vaccine v1 = new Vaccine("001", "Vaccine 1", vt);
        List<Vaccine> vaccines = Arrays.asList(v1);

        when(ioOperations.getVaccine("A")).thenReturn(vaccines);

        List<Vaccine> result = manager.getVaccine("A");

        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.contains(v1));
    }

    // Teste getVaccine(String vaccineTypeCode) com filtro inválido
    @Test
    void testGetVaccineWithInvalidFilter() throws OHServiceException {
        when(ioOperations.getVaccine("InvalidCode")).thenReturn(Arrays.asList());

        List<Vaccine> result = manager.getVaccine("InvalidCode");

        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    // Teste newVaccine() com dados válidos
    @Test
    void testNewVaccineWithValidData() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Type A");
        Vaccine vaccine = new Vaccine("003", "Vaccine 3", vt);

        when(ioOperations.newVaccine(vaccine)).thenReturn(vaccine);

        Vaccine result = manager.newVaccine(vaccine);

        assertEquals(vaccine, result);
    }

    // Teste newVaccine() com código existente
    @Test
    void testNewVaccineWithExistingCode() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Type A");
        Vaccine vaccine = new Vaccine("003", "Vaccine 3", vt);

        when(ioOperations.newVaccine(vaccine)).thenThrow(new OHDataIntegrityViolationException(new OHExceptionMessage("The code is already in use.")));

        OHServiceException thrown = assertThrows(OHServiceException.class, () -> manager.newVaccine(vaccine));

        assertEquals("The code is already in use.", thrown.getMessage());
    }

    // Teste newVaccine() com código ou descrição vazios
    @Test
    void testNewVaccineWithInvalidData() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Type A");
        Vaccine vaccine = new Vaccine("", "", vt);

        OHServiceException thrown = assertThrows(OHServiceException.class, () -> manager.newVaccine(vaccine));

        assertTrue(thrown.getCause() instanceof OHDataValidationException);
    }

    // Teste updateVaccine() com dados válidos
    @Test
    void testUpdateVaccineWithValidData() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Type A");
        Vaccine vaccine = new Vaccine("001", "Updated Vaccine 1", vt);

        when(ioOperations.updateVaccine(vaccine)).thenReturn(vaccine);

        Vaccine result = manager.updateVaccine(vaccine);

        assertEquals(vaccine, result);
    }

    // Teste updateVaccine() com vacina nula
    @Test
    void testUpdateVaccineWithNullVaccine() throws OHServiceException {
        OHServiceException thrown = assertThrows(OHServiceException.class, () -> manager.updateVaccine(null));

        assertTrue(thrown.getCause() instanceof OHDataValidationException);
    }

    // Teste updateVaccine() com código inexistente
    @Test
    void testUpdateVaccineWithNonExistingCode() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Type A");
        Vaccine vaccine = new Vaccine("999", "Non-existing Vaccine", vt);

        when(ioOperations.updateVaccine(vaccine)).thenThrow(new OHServiceException(new OHExceptionMessage("Vaccine does not exist")));

        OHServiceException thrown = assertThrows(OHServiceException.class, () -> manager.updateVaccine(vaccine));

        assertEquals("Vaccine does not exist", thrown.getMessage());
    }

    // Teste deleteVaccine() com vacina válida
    @Test
    void testDeleteVaccineWithValidData() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Type A");
        Vaccine vaccine = new Vaccine("001", "Vaccine 1", vt);

        manager.deleteVaccine(vaccine);

        verify(ioOperations, times(1)).deleteVaccine(vaccine);
    }

    // Teste deleteVaccine() com vacina nula
    @Test
    void testDeleteVaccineWithNullVaccine() throws OHServiceException {
        OHServiceException thrown = assertThrows(OHServiceException.class, () -> manager.deleteVaccine(null));

        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
    }

    // Teste isCodePresent() com código existente
    @Test
    void testIsCodePresentWithExistingCode() throws OHServiceException {
        when(ioOperations.isCodePresent("001")).thenReturn(true);

        boolean result = manager.isCodePresent("001");

        assertTrue(result);
    }

    // Teste isCodePresent() com código não existente
    @Test
    void testIsCodePresentWithNonExistingCode() throws OHServiceException {
        when(ioOperations.isCodePresent("999")).thenReturn(false);

        boolean result = manager.isCodePresent("999");

        assertFalse(result);
    }

    // Teste findVaccine() com código válido
    @Test
    void testFindVaccineWithValidCode() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Type A");
        Vaccine vaccine = new Vaccine("001", "Vaccine 1", vt);

        when(ioOperations.findVaccine("001")).thenReturn(vaccine);

        Vaccine result = manager.findVaccine("001");

        assertEquals(vaccine, result);
    }

    // Teste findVaccine() com código nulo
    @Test
    void testFindVaccineWithNullCode() throws OHServiceException {
        OHServiceException thrown = assertThrows(OHServiceException.class, () -> manager.findVaccine(null));

        assertTrue(thrown.getCause() instanceof IllegalArgumentException);
    }
}
