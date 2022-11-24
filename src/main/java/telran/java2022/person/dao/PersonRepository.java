package telran.java2022.person.dao;

import java.time.LocalDate;
import java.util.stream.Stream;


import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import telran.java2022.person.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
   
    Stream<Person> findByAddressCity(String city);
    
    @Query(value = "SELECT DISTINCT city FROM persons", nativeQuery = true)
    Iterable<String> findAllByAddressCity();

    Stream<Person> findByName(String name);

    Stream<Person> findByBirthDateGreaterThanOrBirthDateLessThan(LocalDate minDate, LocalDate maxDate);

    long countByAddressCity(String city);

}
