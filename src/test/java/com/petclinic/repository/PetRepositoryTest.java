package com.petclinic.repository;

import com.petclinic.model.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PetRepositoryTest {

    @Autowired
    private PetRepository petRepository;

    @Test
    void findBySpeciesIgnoreCaseOrderByPetNameAsc_returnsCaseInsensitiveMatchesSortedByName() {
        petRepository.save(newPet("Zara", "DOG", "TESTTAG-B1-1"));
        petRepository.save(newPet("Amy", "dog", "TESTTAG-B1-2"));
        petRepository.save(newPet("Mimi", "Cat", "TESTTAG-B1-3"));

        List<Pet> dogs = petRepository.findBySpeciesIgnoreCaseOrderByPetNameAsc("dog");

        assertThat(dogs).extracting(Pet::getPetName).containsExactly("Amy", "Zara");
    }

    @Test
    void findBySpeciesIgnoreCaseOrderByPetNameAsc_returnsEmptyListWhenNoMatch() {
        List<Pet> result = petRepository.findBySpeciesIgnoreCaseOrderByPetNameAsc("Dragon-NoSuchSpecies");

        assertThat(result).isEmpty();
    }

    private Pet newPet(String name, String species, String tag) {
        Pet pet = new Pet();
        pet.setPetName(name);
        pet.setOwnerName("Test Owner");
        pet.setTagNumber(tag);
        pet.setSpecies(species);
        pet.setOwnerPhone("0000000000");
        return pet;
    }
}
