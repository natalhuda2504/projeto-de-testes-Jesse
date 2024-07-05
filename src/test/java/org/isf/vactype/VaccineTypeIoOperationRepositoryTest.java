package org.isf.vactype;

import org.isf.vactype.model.VaccineType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.isf.vactype.service.VaccineTypeIoOperationRepository;

@DataJpaTest
public class VaccineTypeIoOperationRepositoryTest {

    @Autowired
    private VaccineTypeIoOperationRepository repository;

    @BeforeEach
    void setUp() {
        // Adiciona alguns dados de teste no banco de dados em memória
        repository.save(new VaccineType("A", "Vaccine A"));
        repository.save(new VaccineType("B", "Vaccine B"));
        repository.save(new VaccineType("C", "Vaccine C"));
    }


    /* Testes Caixa Preta */
    @Test
    void testFindAllByOrderByDescriptionAsc() {
        List<VaccineType> vaccineTypes = repository.findAllByOrderByDescriptionAsc();
        assertThat(vaccineTypes).hasSize(3)
            .extracting(VaccineType::getDescription)
            .containsExactly("Vaccine A", "Vaccine B", "Vaccine C");
    }

    @Test
    void testFindAllByOrderByDescriptionAsc_NoData() {
        // Remove todos os dados de teste do banco de dados
        repository.deleteAll();

        List<VaccineType> vaccineTypes = repository.findAllByOrderByDescriptionAsc();
        assertThat(vaccineTypes).isEmpty();
    }

    @Test
    void testFindAllByOrderByDescriptionAsc_SingleElement() {
        // Remove alguns dados de teste do banco de dados
        repository.deleteById("B");

        List<VaccineType> vaccineTypes = repository.findAllByOrderByDescriptionAsc();
        assertThat(vaccineTypes).hasSize(2)
            .extracting(VaccineType::getDescription)
            .containsExactly("Vaccine A", "Vaccine C");
    }

    /* Testes de Interface */
    @ParameterizedTest
    @CsvSource({
        "A, Vacina contra gripe",
        "B, Vacina contra febre amarela",
        "1, Vacina contra hepatite",
        "Z, Vacina contra COVID-19"
    })
    public void testSaveAndFindAllByOrderByDescriptionAsc_ValidInputs(String code, String description) {
        VaccineType vaccineType = new VaccineType(code, description);
        repository.save(vaccineType);
        List<VaccineType> vaccineTypes = repository.findAllByOrderByDescriptionAsc();
        assertTrue(vaccineTypes.contains(vaccineType));
    }

    @ParameterizedTest
    @CsvSource({
        "'', Vacina contra gripe", // código vazio
        "AB, Vacina contra febre amarela", // código com mais de 1 caractere
        "A, ''" // descrição vazia
    })
    public void testSave_InvalidInputs(String code, String description) {
        VaccineType vaccineType = new VaccineType(code, description);
        assertThrows(Exception.class, () -> repository.save(vaccineType));
    }

    @ParameterizedTest
    @CsvSource({
        "A, A very long description that exceeds the usual limit to test the upper boundary of the description length which should be 255 characters..............................................................",
        "Z, Another long description to ensure we test the upper boundary correctly which is essential for robustness testing..................................................................................................."
    })
    public void testSaveAndFindAllByOrderByDescriptionAsc_ValidBoundaryValues(String code, String description) {
        VaccineType vaccineType = new VaccineType(code, description);
        repository.save(vaccineType);
        List<VaccineType> vaccineTypes = repository.findAllByOrderByDescriptionAsc();
        assertTrue(vaccineTypes.contains(vaccineType));
    }
}
