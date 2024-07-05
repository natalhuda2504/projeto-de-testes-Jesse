package org.isf.vactype;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.assertThat;
import org.isf.vactype.model.VaccineType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VaccineTypeTest {

    private VaccineType vaccineType;

    @BeforeEach
    void setUp() {
        vaccineType = new VaccineType("A", "Vaccine Type A");
    }

    /* Testes Caixa Preta */

    @Test
    void testGetCode() {
        assertThat(vaccineType.getCode()).isEqualTo("A");
    }

    @Test
    void testSetCode() {
        vaccineType.setCode("B");
        assertThat(vaccineType.getCode()).isEqualTo("B");
    }

    @Test
    void testGetDescription() {
        assertThat(vaccineType.getDescription()).isEqualTo("Vaccine Type A");
    }

    @Test
    void testSetDescription() {
        vaccineType.setDescription("Vaccine Type B");
        assertThat(vaccineType.getDescription()).isEqualTo("Vaccine Type B");
    }

    @Test
    void testEqualsSameObject() {
        VaccineType sameVaccineType = new VaccineType("A", "Vaccine Type A");
        assertThat(vaccineType).isEqualTo(sameVaccineType);
    }

    @Test
    void testEqualsDifferentCode() {
        VaccineType differentCode = new VaccineType("B", "Vaccine Type A");
        assertThat(vaccineType).isNotEqualTo(differentCode);
    }

    @Test
    void testEqualsDifferentDescription() {
        VaccineType differentDescription = new VaccineType("A", "Vaccine Type B");
        assertThat(vaccineType).isNotEqualTo(differentDescription);
    }

    @Test
    void testEqualsDifferentObject() {
        assertThat(vaccineType).isNotEqualTo(new Object());
    }

    @Test
    void testPrint() {
        String expectedOutput = "vaccineType code=.A. description=.Vaccine Type A.";
        assertThat(vaccineType.print()).isEqualTo(expectedOutput);
    }

    @Test
    void testToString() {
        assertThat(vaccineType.toString()).isEqualTo("Vaccine Type A");
    }

    @Test
    void testHashCodeConsistency() {
        int initialHashCode = vaccineType.hashCode();
        assertThat(vaccineType.hashCode()).isEqualTo(initialHashCode);
    }

    @Test
    void testHashCodeEquality() {
        VaccineType sameVaccineType = new VaccineType("A", "Vaccine Type A");
        assertThat(vaccineType.hashCode()).isEqualTo(sameVaccineType.hashCode());
    }

    @Test
    void testHashCodeDifferentObject() {
        VaccineType differentVaccineType = new VaccineType("B", "Vaccine Type B");
        assertThat(vaccineType.hashCode()).isNotEqualTo(differentVaccineType.hashCode());
    }

    @Test
    void testEqualsAndHashCodeConsistency() {
        VaccineType sameVaccineType = new VaccineType("A", "Vaccine Type A");
        assertThat(vaccineType.equals(sameVaccineType)).isTrue();
        assertThat(vaccineType.hashCode()).isEqualTo(sameVaccineType.hashCode());
    }

    @Test
    void testEqualsNull() {
        assertThat(vaccineType).isNotEqualTo(null);
    }

    @Test
    void testSetCodeNull() {
        assertThrows(NullPointerException.class, () -> vaccineType.setCode(null));
    }

    @Test
    void testSetDescriptionNull() {
        assertThrows(NullPointerException.class, () -> vaccineType.setDescription(null));
    }

    /* Testes de Interface */

    @ParameterizedTest
    @CsvSource({
        "A, Vacina contra gripe",
        "B, Vacina contra febre amarela",
        "1, Vacina contra hepatite",
        "Z, Vacina contra COVID-19"
    })
    public void testVaccineType_ValidInputs(String code, String description) {
        VaccineType vaccineType = new VaccineType(code, description);
        assertEquals(code, vaccineType.getCode());
        assertEquals(description, vaccineType.getDescription());
    }

    @ParameterizedTest
    @CsvSource({
        "'', Vacina contra gripe", // código vazio
        "AB, Vacina contra febre amarela", // código com mais de 1 caractere
        "A, ''" // descrição vazia
    })
    public void testVaccineType_InvalidInputs(String code, String description) {
        assertThrows(IllegalArgumentException.class, () -> new VaccineType(code, description));
    }

    @ParameterizedTest
    @CsvSource({
        "A, A very long description that exceeds the usual limit to test the upper boundary of the description length which should be 255 characters..............................................................",
        "Z, Another long description to ensure we test the upper boundary correctly which is essential for robustness testing..................................................................................................."
    })
    public void testVaccineType_ValidBoundaryValues(String code, String description) {
        VaccineType vaccineType = new VaccineType(code, description);
        assertEquals(code, vaccineType.getCode());
        assertEquals(description, vaccineType.getDescription());
    }
}
