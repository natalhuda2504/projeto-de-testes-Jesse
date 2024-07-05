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
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

class VaccineTypeBrowserManagerTest {

    @Mock
    private VacTypeIoOperation ioOperations;

    @InjectMocks
    private VaccineTypeBrowserManager vaccineTypeBrowserManager;

    private VaccineType vaccineType;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        vaccineType = new VaccineType();
        vaccineType.setCode("A");
        vaccineType.setDescription("Vaccine Type A");
    }

    /* Testes Caixa Preta */

    @Test
    void testGetVaccineType() throws OHServiceException {
        List<VaccineType> vaccineTypes = new ArrayList<>();
        vaccineTypes.add(vaccineType);
        when(ioOperations.getVaccineType()).thenReturn(vaccineTypes);

        List<VaccineType> result = vaccineTypeBrowserManager.getVaccineType();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getCode()).isEqualTo("A");
        verify(ioOperations).getVaccineType();
    }

    @Test
    void testNewVaccineTypeValid() throws OHServiceException {
        when(ioOperations.newVaccineType(vaccineType)).thenReturn(vaccineType);
        when(ioOperations.isCodePresent(vaccineType.getCode())).thenReturn(false);

        VaccineType result = vaccineTypeBrowserManager.newVaccineType(vaccineType);

        assertThat(result).isEqualTo(vaccineType);
        verify(ioOperations).newVaccineType(vaccineType);
    }

    @Test
    void testNewVaccineTypeCodePresent() throws OHServiceException {
        when(ioOperations.isCodePresent(vaccineType.getCode())).thenReturn(true);

        assertThrows(OHDataIntegrityViolationException.class, () -> vaccineTypeBrowserManager.newVaccineType(vaccineType));

        verify(ioOperations, never()).newVaccineType(vaccineType);
    }

    @Test
    void testNewVaccineTypeInvalidCode() throws OHServiceException {
        vaccineType.setCode("");
        assertThrows(OHDataValidationException.class, () -> vaccineTypeBrowserManager.newVaccineType(vaccineType));

        verify(ioOperations, never()).newVaccineType(vaccineType);
    }

    @Test
    void testUpdateVaccineTypeValid() throws OHServiceException {
        when(ioOperations.updateVaccineType(vaccineType)).thenReturn(vaccineType);

        VaccineType result = vaccineTypeBrowserManager.updateVaccineType(vaccineType);

        assertThat(result).isEqualTo(vaccineType);
        verify(ioOperations).updateVaccineType(vaccineType);
    }

    @Test
    void testUpdateVaccineTypeInvalidCode() throws OHServiceException {
        vaccineType.setCode("");
        assertThrows(OHDataValidationException.class, () -> vaccineTypeBrowserManager.updateVaccineType(vaccineType));

        verify(ioOperations, never()).updateVaccineType(vaccineType);
    }

    @Test
    void testDeleteVaccineType() throws OHServiceException {
        doNothing().when(ioOperations).deleteVaccineType(vaccineType);

        vaccineTypeBrowserManager.deleteVaccineType(vaccineType);

        verify(ioOperations).deleteVaccineType(vaccineType);
    }

    @Test
    void testIsCodePresentTrue() throws OHServiceException {
        when(ioOperations.isCodePresent(vaccineType.getCode())).thenReturn(true);

        boolean result = vaccineTypeBrowserManager.isCodePresent(vaccineType.getCode());

        assertThat(result).isTrue();
        verify(ioOperations).isCodePresent(vaccineType.getCode());
    }

    @Test
    void testIsCodePresentFalse() throws OHServiceException {
        when(ioOperations.isCodePresent(vaccineType.getCode())).thenReturn(false);

        boolean result = vaccineTypeBrowserManager.isCodePresent(vaccineType.getCode());

        assertThat(result).isFalse();
        verify(ioOperations).isCodePresent(vaccineType.getCode());
    }

    @Test
    void testFindVaccineTypeFound() {
        when(ioOperations.findVaccineType(vaccineType.getCode())).thenReturn(vaccineType);

        VaccineType result = vaccineTypeBrowserManager.findVaccineType(vaccineType.getCode());

        assertThat(result).isEqualTo(vaccineType);
        verify(ioOperations).findVaccineType(vaccineType.getCode());
    }

    @Test
    void testFindVaccineTypeNotFound() {
        when(ioOperations.findVaccineType(vaccineType.getCode())).thenReturn(null);

        VaccineType result = vaccineTypeBrowserManager.findVaccineType(vaccineType.getCode());

        assertThat(result).isNull();
        verify(ioOperations).findVaccineType(vaccineType.getCode());
    }

    /* Testes de Interface */
    @Test
    public void testGetVaccineType_NotEmpty() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Description1");
        when(ioOperations.getVaccineType()).thenReturn(Arrays.asList(vt));
        List<VaccineType> result = vaccineTypeBrowserManager.getVaccineType();
        assertFalse(result.isEmpty());
        assertEquals(1, result.size());
        assertEquals("A", result.get(0).getCode());
    }

    @Test
    public void testGetVaccineType_Empty() throws OHServiceException {
        when(ioOperations.getVaccineType()).thenReturn(Collections.emptyList());
        List<VaccineType> result;
        result = vaccineTypeBrowserManager.getVaccineType();
        assertTrue(result.isEmpty());
    }

    @Test
    public void testNewVaccineType_Valid() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Description2");
        when(ioOperations.newVaccineType(vt)).thenReturn(vt);
        VaccineType result;
        result = vaccineTypeBrowserManager.newVaccineType(vt);
        assertNotNull(result);
        assertEquals("A", result.getCode());
    }

    @Test
    public void testNewVaccineType_InvalidCode() {
        VaccineType vt = new VaccineType("AB", "Description2");
        OHDataValidationException exception = assertThrows(OHDataValidationException.class, () -> vaccineTypeBrowserManager.newVaccineType(vt));
        assertTrue(exception.getMessages().contains(new OHExceptionMessage(MessageBundle.getMessage("angal.common.thecodeistoolongmax1char.msg"))));
    }

    @Test
    public void testNewVaccineType_Null() {
        assertThrows(OHServiceException.class, () -> vaccineTypeBrowserManager.newVaccineType(null));
    }

    @Test
    public void testUpdateVaccineType_Valid() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Description3");
        when(ioOperations.updateVaccineType(vt)).thenReturn(vt);
        VaccineType result = vaccineTypeBrowserManager.updateVaccineType(vt);
        assertNotNull(result);
        assertEquals("A", result.getCode());
    }

    @Test
    public void testUpdateVaccineType_InvalidDescription() {
        VaccineType vt = new VaccineType("A", "");
        OHDataValidationException exception = assertThrows(OHDataValidationException.class, () -> vaccineTypeBrowserManager.updateVaccineType(vt));
        assertTrue(exception.getMessages().contains(new OHExceptionMessage(MessageBundle.getMessage("angal.common.pleaseinsertavaliddescription.msg"))));
    }

    @Test
    public void testUpdateVaccineType_Null() {
        assertThrows(OHServiceException.class, () -> vaccineTypeBrowserManager.updateVaccineType(null));
    }

    @Test
    public void testDeleteVaccineType_Valid() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Description4");
        doNothing().when(ioOperations).deleteVaccineType(vt);
        vaccineTypeBrowserManager.deleteVaccineType(vt);
        verify(ioOperations, times(1)).deleteVaccineType(vt);
    }

    @Test
    public void testDeleteVaccineType_Null() {
        assertThrows(OHServiceException.class, () -> vaccineTypeBrowserManager.deleteVaccineType(null));
    }

    @Test
    public void testIsCodePresent_ExistingCode() throws OHServiceException {
        when(ioOperations.isCodePresent("A")).thenReturn(true);
        boolean result = vaccineTypeBrowserManager.isCodePresent("A");
        assertTrue(result);
    }

    @Test
    public void testIsCodePresent_NonExistingCode() throws OHServiceException {
        when(ioOperations.isCodePresent("B")).thenReturn(false);
        boolean result = vaccineTypeBrowserManager.isCodePresent("B");
        assertFalse(result);
    }

    @Test
    public void testFindVaccineType_ValidCode() throws OHServiceException {
        VaccineType vt = new VaccineType("A", "Description7");
        when(ioOperations.findVaccineType("A")).thenReturn(vt);
        VaccineType result = vaccineTypeBrowserManager.findVaccineType("A");
        assertNotNull(result);
        assertEquals("A", result.getCode());
    }

    @Test
    public void testFindVaccineType_InvalidCode() throws OHServiceException {
        when(ioOperations.findVaccineType("B")).thenReturn(null);
        VaccineType result = vaccineTypeBrowserManager.findVaccineType("B");
        assertNull(result);
    }
}