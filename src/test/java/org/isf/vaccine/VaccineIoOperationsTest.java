package org.isf.vaccine;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.isf.utils.exception.OHServiceException;
import org.isf.vaccine.model.Vaccine;
import org.isf.vaccine.service.VaccineIoOperationRepository;
import org.isf.vaccine.service.VaccineIoOperations;
import org.isf.vactype.model.VaccineType;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

class VaccineIoOperationsTest {

    @Mock
    private VaccineIoOperationRepository repository;

    @InjectMocks
    private VaccineIoOperations vaccineIoOperations;

    private VaccineType vaccineType1;
    private Vaccine vaccine1;
    private Vaccine vaccine2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vaccineType1 = new VaccineType();
        vaccineType1.setCode("TYPE1");
        vaccineType1.setDescription("Type 1");

        vaccine1 = new Vaccine("VACC1", "Vaccine A", vaccineType1);
        vaccine2 = new Vaccine("VACC2", "Vaccine B", vaccineType1);
    }

    /* Testes Caixa Preta */

    @Test
    void testGetVaccineWithTypeCode() throws OHServiceException {
        when(repository.findByVaccineType_CodeOrderByDescriptionAsc("TYPE1")).thenReturn(Arrays.asList(vaccine1, vaccine2));

        List<Vaccine> vaccines = vaccineIoOperations.getVaccine("TYPE1");

        assertEquals(2, vaccines.size());
        assertEquals("Vaccine A", vaccines.get(0).getDescription());
        assertEquals("Vaccine B", vaccines.get(1).getDescription());
        verify(repository).findByVaccineType_CodeOrderByDescriptionAsc("TYPE1");
    }

    @Test
    void testGetVaccineWithoutTypeCode() throws OHServiceException {
        when(repository.findAllByOrderByDescriptionAsc()).thenReturn(Arrays.asList(vaccine1, vaccine2));

        List<Vaccine> vaccines = vaccineIoOperations.getVaccine(null);

        assertEquals(2, vaccines.size());
        assertEquals("Vaccine A", vaccines.get(0).getDescription());
        assertEquals("Vaccine B", vaccines.get(1).getDescription());
        verify(repository).findAllByOrderByDescriptionAsc();
    }

    @Test
    void testNewVaccine() throws OHServiceException {
        when(repository.save(vaccine1)).thenReturn(vaccine1);

        Vaccine result = vaccineIoOperations.newVaccine(vaccine1);

        assertNotNull(result);
        assertEquals(vaccine1, result);
        verify(repository).save(vaccine1);
    }

    @Test
    void testUpdateVaccine() throws OHServiceException {
        when(repository.save(vaccine1)).thenReturn(vaccine1);

        Vaccine result = vaccineIoOperations.updateVaccine(vaccine1);

        assertNotNull(result);
        assertEquals(vaccine1, result);
        verify(repository).save(vaccine1);
    }

    @Test
    void testDeleteVaccine() throws OHServiceException {
        doNothing().when(repository).delete(vaccine1);

        assertDoesNotThrow(() -> vaccineIoOperations.deleteVaccine(vaccine1));
        verify(repository).delete(vaccine1);
    }

    @Test
    void testIsCodePresentTrue() throws OHServiceException {
        when(repository.existsById("VACC1")).thenReturn(true);

        boolean isPresent = vaccineIoOperations.isCodePresent("VACC1");

        assertTrue(isPresent);
        verify(repository).existsById("VACC1");
    }

    @Test
    void testIsCodePresentFalse() throws OHServiceException {
        when(repository.existsById("VACC1")).thenReturn(false);

        boolean isPresent = vaccineIoOperations.isCodePresent("VACC1");

        assertFalse(isPresent);
        verify(repository).existsById("VACC1");
    }

    @Test
    void testFindVaccineFound() throws OHServiceException {
        when(repository.findById("VACC1")).thenReturn(Optional.of(vaccine1));

        Vaccine result = vaccineIoOperations.findVaccine("VACC1");

        assertNotNull(result);
        assertEquals(vaccine1, result);
        verify(repository).findById("VACC1");
    }

    @Test
    void testFindVaccineNotFound() throws OHServiceException {
        when(repository.findById("VACC1")).thenReturn(Optional.empty());

        Vaccine result = vaccineIoOperations.findVaccine("VACC1");

        assertNull(result);
        verify(repository).findById("VACC1");
    }

    @Test
    void testCountAllActiveVaccinations() {
        when(repository.countAllActiveVaccinations()).thenReturn(5L);

        long count = vaccineIoOperations.countAllActiveVaccinations();

        assertEquals(5L, count);
        verify(repository).countAllActiveVaccinations();
    }

    /* Testes de Interface */

    // Teste para getVaccine com vaccineTypeCode válido
    @Test
    void testGetVaccine_ValidVaccineTypeCode() throws OHServiceException {
        String validCode = "A";
        List<Vaccine> expectedVaccines = new ArrayList<>();
        expectedVaccines.add(new Vaccine("001", "Vaccine 1", new VaccineType("A", "Type A")));
        expectedVaccines.add(new Vaccine("002", "Vaccine 2", new VaccineType("A", "Type A")));

        when(repository.findByVaccineType_CodeOrderByDescriptionAsc(validCode)).thenReturn(expectedVaccines);

        List<Vaccine> actualVaccines = vaccineIoOperations.getVaccine(validCode);

        assertEquals(expectedVaccines.size(), actualVaccines.size());
        assertEquals(expectedVaccines.get(0).getDescription(), actualVaccines.get(0).getDescription());
        assertEquals(expectedVaccines.get(1).getDescription(), actualVaccines.get(1).getDescription());
    }

    // Teste para getVaccine com vaccineTypeCode nulo
    @Test
    void testGetVaccine_NullVaccineTypeCode() throws OHServiceException {
        List<Vaccine> expectedVaccines = new ArrayList<>();
        expectedVaccines.add(new Vaccine("001", "Vaccine 1", new VaccineType("A", "Type A")));
        expectedVaccines.add(new Vaccine("002", "Vaccine 2", new VaccineType("B", "Type B")));

        when(repository.findAllByOrderByDescriptionAsc()).thenReturn(expectedVaccines);

        List<Vaccine> actualVaccines = vaccineIoOperations.getVaccine(null);

        assertEquals(expectedVaccines.size(), actualVaccines.size());
        assertEquals(expectedVaccines.get(0).getDescription(), actualVaccines.get(0).getDescription());
        assertEquals(expectedVaccines.get(1).getDescription(), actualVaccines.get(1).getDescription());
    }

    // Teste para newVaccine com vacina válida
    @Test
    void testNewVaccine_ValidVaccine() throws OHServiceException {
        Vaccine vaccine = new Vaccine("003", "Vaccine 3", new VaccineType("C", "Type C"));
        when(repository.save(vaccine)).thenReturn(vaccine);

        Vaccine result = vaccineIoOperations.newVaccine(vaccine);

        assertNotNull(result);
        assertEquals("003", result.getCode());
        assertEquals("Vaccine 3", result.getDescription());
    }

    // Teste para newVaccine com vacina nula
    @Test
    void testNewVaccine_NullVaccine() {
        assertThrows(OHServiceException.class, () -> vaccineIoOperations.newVaccine(null));
    }

    // Teste para updateVaccine com vacina válida
    @Test
    void testUpdateVaccine_ValidVaccine() throws OHServiceException {
        Vaccine vaccine = new Vaccine("004", "Vaccine 4", new VaccineType("D", "Type D"));
        when(repository.save(vaccine)).thenReturn(vaccine);

        Vaccine result = vaccineIoOperations.updateVaccine(vaccine);

        assertNotNull(result);
        assertEquals("004", result.getCode());
        assertEquals("Vaccine 4", result.getDescription());
    }

    // Teste para updateVaccine com vacina nula
    @Test
    void testUpdateVaccine_NullVaccine() {
        assertThrows(OHServiceException.class, () -> vaccineIoOperations.updateVaccine(null));
    }

    // Teste para deleteVaccine com vacina válida
    @Test
    void testDeleteVaccine_ValidVaccine() throws OHServiceException {
        Vaccine vaccine = new Vaccine("005", "Vaccine 5", new VaccineType("E", "Type E"));

        doNothing().when(repository).delete(vaccine);

        assertDoesNotThrow(() -> vaccineIoOperations.deleteVaccine(vaccine));
    }

    // Teste para deleteVaccine com vacina nula
    @Test
    void testDeleteVaccine_NullVaccine() {
        assertThrows(OHServiceException.class, () -> vaccineIoOperations.deleteVaccine(null));
    }

    // Teste para isCodePresent com código existente
    @Test
    void testIsCodePresent_ExistingCode() throws OHServiceException {
        String code = "006";
        when(repository.existsById(code)).thenReturn(true);

        boolean result = vaccineIoOperations.isCodePresent(code);

        assertTrue(result);
    }

    // Teste para isCodePresent com código não existente
    @Test
    void testIsCodePresent_NonExistingCode() throws OHServiceException {
        String code = "007";
        when(repository.existsById(code)).thenReturn(false);

        boolean result = vaccineIoOperations.isCodePresent(code);

        assertFalse(result);
    }

    // Teste para findVaccine com código válido
    @Test
    void testFindVaccine_ValidCode() throws OHServiceException {
        String code = "008";
        Vaccine expectedVaccine = new Vaccine(code, "Vaccine 8", new VaccineType("F", "Type F"));
        when(repository.findById(code)).thenReturn(Optional.of(expectedVaccine));

        Vaccine result = vaccineIoOperations.findVaccine(code);

        assertNotNull(result);
        assertEquals("008", result.getCode());
        assertEquals("Vaccine 8", result.getDescription());
    }

    // Teste para findVaccine com código inválido
    @Test
    void testFindVaccine_InvalidCode() throws OHServiceException {
        String code = "009";
        when(repository.findById(code)).thenReturn(Optional.empty());

        Vaccine result = vaccineIoOperations.findVaccine(code);

        assertNull(result);
    }
}