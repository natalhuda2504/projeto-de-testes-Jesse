package org.isf.vactype;

import org.isf.utils.exception.OHServiceException;
import org.isf.vactype.model.VaccineType;
import org.isf.vactype.service.VaccineTypeIoOperationRepository;
import org.isf.vactype.service.VacTypeIoOperation;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class VacTypeIoOperationTest {

    @Mock
    private VaccineTypeIoOperationRepository repository;

    @InjectMocks
    private VacTypeIoOperation service;

    private VacTypeIoOperation operation;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        operation = new VacTypeIoOperation(repository);
    }

    /* Testes Caixa Preta */

    @Test
    void testGetVaccineType() throws OHServiceException {
        // Setup
        List<VaccineType> vaccineTypes = new ArrayList<>();
        vaccineTypes.add(new VaccineType("A", "Vaccine A"));
        vaccineTypes.add(new VaccineType("B", "Vaccine B"));

        when(repository.findAllByOrderByDescriptionAsc()).thenReturn(vaccineTypes);

        // Execute
        List<VaccineType> result = service.getVaccineType();

        // Verify
        assertThat(result).hasSize(2)
            .extracting(VaccineType::getDescription)
            .containsExactly("Vaccine A", "Vaccine B");
    }

    @Test
    void testNewVaccineType() throws OHServiceException {
        // Setup
        VaccineType vaccineType = new VaccineType("A", "Vaccine A");
        when(repository.save(any(VaccineType.class))).thenReturn(vaccineType);

        // Execute
        VaccineType result = service.newVaccineType(vaccineType);

        // Verify
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("A");
        assertThat(result.getDescription()).isEqualTo("Vaccine A");
    }

    @Test
    void testUpdateVaccineType() throws OHServiceException {
        // Setup
        VaccineType vaccineType = new VaccineType("A", "Vaccine A");
        when(repository.save(any(VaccineType.class))).thenReturn(vaccineType);

        // Execute
        VaccineType result = service.updateVaccineType(vaccineType);

        // Verify
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("A");
        assertThat(result.getDescription()).isEqualTo("Vaccine A");
    }

    @Test
    void testDeleteVaccineType() throws OHServiceException {
        // Execute
        service.deleteVaccineType(new VaccineType("A", "Vaccine A"));

        // Verify
        verify(repository, times(1)).delete(any(VaccineType.class));
    }

    @Test
    void testIsCodePresent() throws OHServiceException {
        // Setup
        when(repository.existsById("A")).thenReturn(true);

        // Execute
        boolean result = service.isCodePresent("A");

        // Verify
        assertThat(result).isTrue();
    }

    @Test
    void testIsCodePresent_NotPresent() throws OHServiceException {
        // Setup
        when(repository.existsById("A")).thenReturn(false);

        // Execute
        boolean result = service.isCodePresent("A");

        // Verify
        assertThat(result).isFalse();
    }

    @Test
    void testFindVaccineType() {
        // Setup
        VaccineType vaccineType = new VaccineType("A", "Vaccine A");
        when(repository.findById("A")).thenReturn(Optional.of(vaccineType));

        // Execute
        VaccineType result = service.findVaccineType("A");

        // Verify
        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("A");
        assertThat(result.getDescription()).isEqualTo("Vaccine A");
    }

    @Test
    void testFindVaccineType_NotFound() {
        // Setup
        when(repository.findById("A")).thenReturn(Optional.empty());

        // Execute
        VaccineType result = service.findVaccineType("A");

        // Verify
        assertThat(result).isNull();
    }

    @Test
    void testFindVaccineType_NullCode() {
        // Execute & Verify
        assertThatThrownBy(() -> service.findVaccineType(null))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessage("VaccineType code must not be null.");
    }

    /* Testes de Interface */
     @ParameterizedTest
    @CsvSource({
        "A, Vacina contra gripe",
        "B, Vacina contra febre amarela",
        "1, Vacina contra hepatite",
        "Z, Vacina contra COVID-19"
    })
    public void testNewVaccineType_ValidInputs(String code, String description) throws OHServiceException {
        VaccineType vaccineType = new VaccineType(code, description);
        VaccineType savedVaccineType = operation.newVaccineType(vaccineType);
        assertNotNull(savedVaccineType);
        assertEquals(code, savedVaccineType.getCode());
        assertEquals(description, savedVaccineType.getDescription());
    }

    @ParameterizedTest
    @CsvSource({
        "'', Vacina contra gripe", // código vazio
        "AB, Vacina contra febre amarela", // código com mais de 1 caractere
        "A, ''" // descrição vazia
    })
    public void testNewVaccineType_InvalidInputs(String code, String description) {
        VaccineType vaccineType = new VaccineType(code, description);
        assertThrows(Exception.class, () -> operation.newVaccineType(vaccineType));
    }

    @ParameterizedTest
    @CsvSource({
        "A, A very long description that exceeds the usual limit to test the upper boundary of the description length which should be 255 characters..............................................................",
        "Z, Another long description to ensure we test the upper boundary correctly which is essential for robustness testing..................................................................................................."
    })
    public void testNewVaccineType_ValidBoundaryValues(String code, String description) throws OHServiceException {
        VaccineType vaccineType = new VaccineType(code, description);
        VaccineType savedVaccineType = operation.newVaccineType(vaccineType);
        assertNotNull(savedVaccineType);
        assertEquals(code, savedVaccineType.getCode());
        assertEquals(description, savedVaccineType.getDescription());
    }

    @ParameterizedTest
    @CsvSource({
        "A, Vacina contra gripe",
        "B, Vacina contra febre amarela"
    })
    public void testFindVaccineType(String code, String description) throws OHServiceException {
        VaccineType vaccineType = new VaccineType(code, description);
        operation.newVaccineType(vaccineType);
        VaccineType foundVaccineType = operation.findVaccineType(code);
        assertNotNull(foundVaccineType);
        assertEquals(code, foundVaccineType.getCode());
        assertEquals(description, foundVaccineType.getDescription());
    }

    @ParameterizedTest
    @CsvSource({
        "'',", // código nulo
        "AB, Vacina contra febre amarela"
    })
    public void testFindVaccineType_InvalidInputs(String code) {
        assertThrows(IllegalArgumentException.class, () -> operation.findVaccineType(code));
    }
}
