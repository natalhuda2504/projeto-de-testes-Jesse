package org.isf.vactype;


import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

import org.isf.utils.exception.OHServiceException;
import org.isf.vactype.model.VaccineType;
import org.isf.vactype.service.VacTypeIoOperation;
import org.isf.vactype.service.VaccineTypeIoOperationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

class VacTypeIoOperationStateTest {

    @Mock
    private VaccineTypeIoOperationRepository repository;

    @InjectMocks
    private VacTypeIoOperation vacTypeIoOperation;

    private VaccineType vaccineType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vaccineType = new VaccineType();
        vaccineType.setCode("A");
        vaccineType.setDescription("Vaccine Type A");
    }

    @Test
    void testGetVaccineType() throws OHServiceException {
        List<VaccineType> vaccineTypes = new ArrayList<>();
        vaccineTypes.add(vaccineType);
        when(repository.findAllByOrderByDescriptionAsc()).thenReturn(vaccineTypes);

        List<VaccineType> result = vacTypeIoOperation.getVaccineType();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("A");
        verify(repository).findAllByOrderByDescriptionAsc();
    }

    @Test
    void testGetVaccineType_Exception() throws OHServiceException {
        when(repository.findAllByOrderByDescriptionAsc()).thenThrow(new RuntimeException("Database Error"));

        OHServiceException thrown = assertThrows(OHServiceException.class, () -> vacTypeIoOperation.getVaccineType());
        assertThat(thrown.getMessage()).isEqualTo("Error retrieving VaccineTypes");
    }

    @Test
    void testNewVaccineType() throws OHServiceException {
        when(repository.save(vaccineType)).thenReturn(vaccineType);

        VaccineType result = vacTypeIoOperation.newVaccineType(vaccineType);

        assertThat(result).isEqualTo(vaccineType);
        verify(repository).save(vaccineType);
    }

    @Test
    void testNewVaccineType_Exception() throws OHServiceException {
        when(repository.save(vaccineType)).thenThrow(new RuntimeException("Database Error"));

        OHServiceException thrown = assertThrows(OHServiceException.class, () -> vacTypeIoOperation.newVaccineType(vaccineType));
        assertThat(thrown.getMessage()).isEqualTo("Error saving VaccineType");
    }

    @Test
    void testUpdateVaccineType() throws OHServiceException {
        when(repository.save(vaccineType)).thenReturn(vaccineType);

        VaccineType result = vacTypeIoOperation.updateVaccineType(vaccineType);

        assertThat(result).isEqualTo(vaccineType);
        verify(repository).save(vaccineType);
    }

    @Test
    void testUpdateVaccineType_Exception() throws OHServiceException {
        when(repository.save(vaccineType)).thenThrow(new RuntimeException("Database Error"));

        OHServiceException thrown = assertThrows(OHServiceException.class, () -> vacTypeIoOperation.updateVaccineType(vaccineType));
        assertThat(thrown.getMessage()).isEqualTo("Error updating VaccineType");
    }

    @Test
    void testDeleteVaccineType() throws OHServiceException {
        doNothing().when(repository).delete(vaccineType);

        vacTypeIoOperation.deleteVaccineType(vaccineType);

        verify(repository).delete(vaccineType);
    }

    @Test
    void testDeleteVaccineType_Exception() throws OHServiceException {
        doThrow(new RuntimeException("Database Error")).when(repository).delete(vaccineType);

        OHServiceException thrown = assertThrows(OHServiceException.class, () -> vacTypeIoOperation.deleteVaccineType(vaccineType));
        assertThat(thrown.getMessage()).isEqualTo("Error deleting VaccineType");
    }

    @Test
    void testIsCodePresent() throws OHServiceException {
        when(repository.existsById(vaccineType.getCode())).thenReturn(true);

        boolean result = vacTypeIoOperation.isCodePresent(vaccineType.getCode());

        assertThat(result).isTrue();
        verify(repository).existsById(vaccineType.getCode());
    }

    @Test
    void testIsCodePresent_Exception() throws OHServiceException {
        when(repository.existsById(vaccineType.getCode())).thenThrow(new RuntimeException("Database Error"));

        OHServiceException thrown = assertThrows(OHServiceException.class, () -> vacTypeIoOperation.isCodePresent(vaccineType.getCode()));
        assertThat(thrown.getMessage()).isEqualTo("Error checking if code is present");
    }

    @Test
    void testFindVaccineType() throws OHServiceException {
        when(repository.findById(vaccineType.getCode())).thenReturn(java.util.Optional.of(vaccineType));

        VaccineType result = vacTypeIoOperation.findVaccineType(vaccineType.getCode());

        assertThat(result).isEqualTo(vaccineType);
        verify(repository).findById(vaccineType.getCode());
    }

    @Test
    void testFindVaccineType_Exception() throws OHServiceException {
        when(repository.findById(vaccineType.getCode())).thenThrow(new RuntimeException("Database Error"));

        OHServiceException thrown = assertThrows(OHServiceException.class, () -> vacTypeIoOperation.findVaccineType(vaccineType.getCode()));
        assertThat(thrown.getMessage()).isEqualTo("Error finding VaccineType by code");
    }

    @Test
    void testFindVaccineType_NullCode() {
        assertThrows(IllegalArgumentException.class, () -> vacTypeIoOperation.findVaccineType(null));
    }

    @Test
    void testFindVaccineType_NotFound() throws OHServiceException {
        when(repository.findById(vaccineType.getCode())).thenReturn(java.util.Optional.empty());

        VaccineType result = vacTypeIoOperation.findVaccineType(vaccineType.getCode());

        assertThat(result).isNull();
    }
}