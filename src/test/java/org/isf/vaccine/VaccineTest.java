package org.isf.vaccine;

import org.isf.vaccine.model.Vaccine;
import org.isf.vactype.model.VaccineType;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class VaccineTest {

    private Vaccine vaccine;
    private VaccineType vaccineType;

    @BeforeEach
    void setUp() {
        vaccineType = new VaccineType();
        vaccineType.setCode("TYPE1");
        vaccineType.setDescription("Type 1");

        vaccine = new Vaccine("VACC1", "Vaccine 1", vaccineType);
    }

    /* Testes Caixa Preta */

    @Test
    void testGetCode() {
        assertEquals("VACC1", vaccine.getCode());
    }

    @Test
    void testSetCode() {
        vaccine.setCode("NEWCODE");
        assertEquals("NEWCODE", vaccine.getCode());
    }

    @Test
    void testGetDescription() {
        assertEquals("Vaccine 1", vaccine.getDescription());
    }

    @Test
    void testSetDescription() {
        vaccine.setDescription("New Description");
        assertEquals("New Description", vaccine.getDescription());
    }

    @Test
    void testGetVaccineType() {
        assertEquals(vaccineType, vaccine.getVaccineType());
    }

    @Test
    void testSetVaccineType() {
        VaccineType newVaccineType = new VaccineType();
        newVaccineType.setCode("TYPE2");
        newVaccineType.setDescription("Type 2");
        
        vaccine.setVaccineType(newVaccineType);
        assertEquals(newVaccineType, vaccine.getVaccineType());
    }

    @Test
    void testEquals() {
        Vaccine sameVaccine = new Vaccine("VACC1", "Vaccine 1", vaccineType);
        Vaccine differentVaccine = new Vaccine("VACC2", "Vaccine 2", vaccineType);

        assertTrue(vaccine.equals(sameVaccine));
        assertFalse(vaccine.equals(differentVaccine));
    }

    @Test
    void testPrint() {
        assertEquals("Vaccine code =.VACC1. description =.Vaccine 1.", vaccine.print());
    }

    @Test
    void testToString() {
        assertEquals("Vaccine 1", vaccine.toString());
    }

    /* Testes de Interface */

    // Teste do construtor e métodos getters e setters
    @Test
    void testVaccineConstructorAndGettersSetters() {
        VaccineType vaccineType = new VaccineType("A", "Type A");
        Vaccine vaccine = new Vaccine("001", "Vaccine 1", vaccineType);

        // Teste getters
        assertEquals("001", vaccine.getCode());
        assertEquals("Vaccine 1", vaccine.getDescription());
        assertEquals(vaccineType, vaccine.getVaccineType());
        assertEquals(1, vaccine.getActive());

        // Teste setters
        vaccine.setCode("002");
        vaccine.setDescription("Vaccine 2");
        VaccineType newVaccineType = new VaccineType("B", "Type B");
        vaccine.setVaccineType(newVaccineType);
        vaccine.setActive(0);

        assertEquals("002", vaccine.getCode());
        assertEquals("Vaccine 2", vaccine.getDescription());
        assertEquals(newVaccineType, vaccine.getVaccineType());
        assertEquals(0, vaccine.getActive());
    }

    // Teste do método equals
    @Test
    void testEquals_negative() {
        VaccineType vaccineTypeA = new VaccineType("A", "Type A");
        Vaccine vaccine1 = new Vaccine("001", "Vaccine 1", vaccineTypeA);
        Vaccine vaccine2 = new Vaccine("001", "Vaccine 1", vaccineTypeA);
        Vaccine vaccine3 = new Vaccine("002", "Vaccine 2", vaccineTypeA);

        assertEquals(vaccine1, vaccine2); // Teste positivo para objetos iguais
        assertNotEquals(vaccine1, vaccine3); // Teste negativo para objetos diferentes
        assertNotEquals(vaccine1, null); // Teste com null
        assertNotEquals(vaccine1, "String"); // Teste com objeto de tipo diferente
    }

    // Teste do método hashCode
    @Test
    void testHashCode() {
        VaccineType vaccineTypeA = new VaccineType("A", "Type A");
        Vaccine vaccine1 = new Vaccine("001", "Vaccine 1", vaccineTypeA);
        Vaccine vaccine2 = new Vaccine("001", "Vaccine 1", vaccineTypeA);

        assertEquals(vaccine1.hashCode(), vaccine2.hashCode()); // Teste para objetos com o mesmo código
    }
}

