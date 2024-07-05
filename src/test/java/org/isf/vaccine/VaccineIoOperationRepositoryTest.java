package org.isf.vaccine;

import java.util.List;

import org.isf.vaccine.model.Vaccine;
import org.isf.vaccine.service.VaccineIoOperationRepository;
import org.isf.vactype.model.VaccineType;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

@DataJpaTest
class VaccineIoOperationRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private VaccineIoOperationRepository repository;

    private VaccineType vaccineType1;
    private VaccineType vaccineType2;
    private Vaccine vaccine1;
    private Vaccine vaccine2;
    private Vaccine vaccine3;

    @BeforeEach
    void setUp() {
        vaccineType1 = new VaccineType();
        vaccineType1.setCode("TYPE1");
        vaccineType1.setDescription("Type 1");

        vaccineType2 = new VaccineType();
        vaccineType2.setCode("TYPE2");
        vaccineType2.setDescription("Type 2");

        entityManager.persist(vaccineType1);
        entityManager.persist(vaccineType2);

        vaccine1 = new Vaccine("VACC1", "Vaccine A", vaccineType1);
        vaccine1.setActive(1);
        vaccine2 = new Vaccine("VACC2", "Vaccine B", vaccineType1);
        vaccine2.setActive(1);
        vaccine3 = new Vaccine("VACC3", "Vaccine C", vaccineType2);
        vaccine3.setActive(1);

        entityManager.persist(vaccine1);
        entityManager.persist(vaccine2);
        entityManager.persist(vaccine3);
    }

    /* Testes Caixa Preta */

    @Test
    void testFindAllByOrderByDescriptionAsc() {
        List<Vaccine> vaccines = repository.findAllByOrderByDescriptionAsc();
        assertEquals(3, vaccines.size());
        assertEquals("Vaccine A", vaccines.get(0).getDescription());
        assertEquals("Vaccine B", vaccines.get(1).getDescription());
        assertEquals("Vaccine C", vaccines.get(2).getDescription());
    }

    @Test
    void testFindByVaccineType_CodeOrderByDescriptionAsc() {
        List<Vaccine> vaccines = repository.findByVaccineType_CodeOrderByDescriptionAsc("TYPE1");
        assertEquals(2, vaccines.size());
        assertEquals("Vaccine A", vaccines.get(0).getDescription());
        assertEquals("Vaccine B", vaccines.get(1).getDescription());
    }

    @Test
    void testCountAllActiveVaccinations() {
        long count = repository.countAllActiveVaccinations();
        assertEquals(3, count);
    }

    /* Testes de Interface */

    // Teste para findByVaccineType_CodeOrderByDescriptionAsc com código inválido
    @Test
    void testFindByVaccineType_CodeOrderByDescriptionAsc_InvalidCode() {
        List<Vaccine> vaccines = repository.findByVaccineType_CodeOrderByDescriptionAsc("InvalidCode");

        assertEquals(0, vaccines.size());
    }

    // Teste para countAllActiveVaccinations com nenhuma vacina ativa
    @Test
    void testCountAllActiveVaccinations_NoActiveVaccines() {
        Vaccine inactiveVaccine = new Vaccine("005", "Inactive Vaccine", vaccineType1);
        inactiveVaccine.setActive(0);

        repository.save(inactiveVaccine);
        long count = repository.countAllActiveVaccinations();

        assertEquals(3, count); // Mesmo após adicionar vacina inativa, o número de vacinas ativas não muda.
    }
}