package com.petclinic.repository;

import com.petclinic.model.Pet;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Runs against the real (already-seeded) PostgreSQL database — see application.properties.
 * The first test filters results down to its own "TESTTAG-B1-" tagged pets before asserting
 * exact content/order, so pre-existing Step 0 seed data (which also includes dogs) never
 * affects the outcome.
 */
@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
class PetRepositoryTest {

    private static final String TAG_PREFIX = "TESTTAG-B1-";

    @Autowired
    private PetRepository petRepository;

    @Test
    void findBySpeciesIgnoreCaseOrderByPetNameAsc_returnsCaseInsensitiveMatchesSortedByName() {
        petRepository.save(newPet("Zara", "DOG", TAG_PREFIX + "1"));
        petRepository.save(newPet("Amy", "dog", TAG_PREFIX + "2"));
        petRepository.save(newPet("Mimi", "Cat", TAG_PREFIX + "3"));

        List<Pet> dogs = petRepository.findBySpeciesIgnoreCaseOrderByPetNameAsc("dog");

        List<String> ourDogNames = dogs.stream()
                .filter(p -> p.getTagNumber() != null && p.getTagNumber().startsWith(TAG_PREFIX))
                .map(Pet::getPetName)
                .toList();

        assertThat(ourDogNames).containsExactly("Amy", "Zara");
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
