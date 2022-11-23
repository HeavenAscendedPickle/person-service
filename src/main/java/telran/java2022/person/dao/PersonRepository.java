package telran.java2022.person.dao;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import telran.java2022.person.model.CityPopulation;
import telran.java2022.person.model.Person;

public interface PersonRepository extends CrudRepository<Person, Integer> {
   
   

    Iterable<Person> findByAddressCity(String city);
    
    @Query(value = "SELECT DISTINCT city FROM persons", nativeQuery = true)
    Iterable<String> findAllByAddressCity();

    Iterable<Person> findByName(String name);

    Iterable<Person> findByBirthDateGreaterThanOrBirthDateLessThan(LocalDate minDate, LocalDate maxDate);

    long countByAddressCity(String city);

}
