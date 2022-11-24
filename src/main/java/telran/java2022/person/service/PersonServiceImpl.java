package telran.java2022.person.service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import telran.java2022.person.dao.PersonRepository;
import telran.java2022.person.dto.AddressDto;
import telran.java2022.person.dto.CityPopulationDto;
import telran.java2022.person.dto.PersonDto;
import telran.java2022.person.dto.exceptions.PersonNotFoundException;
import telran.java2022.person.model.Address;
import telran.java2022.person.model.Person;

@Service
@RequiredArgsConstructor
public class PersonServiceImpl implements PersonService {

    final PersonRepository personRepository;
    final ModelMapper modelMapper;

    @Override
    public Boolean addPerson(PersonDto personDto) {
	personRepository.save(modelMapper.map(personDto, Person.class));
	return true;
    }

    @Override
    public PersonDto findPersonById(Integer id) {
	Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException());
	return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto removePerson(Integer id) {
	Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException());
	personRepository.deleteById(id);
	return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto updatePersonName(Integer id, String name) {
	Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException());
	person.setName(name);
	personRepository.save(person);
	return modelMapper.map(person, PersonDto.class);
    }

    @Override
    public PersonDto updatePersonAddress(Integer id, AddressDto addressDto) {
	Person person = personRepository.findById(id).orElseThrow(() -> new PersonNotFoundException());
	person.setAddress(modelMapper.map(addressDto, Address.class));
	personRepository.save(person);
	return modelMapper.map(person, PersonDto.class);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<PersonDto> findPersonsByCity(String city) {
	return personRepository.findByAddressCity(city).map(p -> modelMapper.map(p, PersonDto.class))
		.collect(Collectors.toList());

    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<PersonDto> findPersonsByName(String name) {
	return personRepository.findByName(name).map(p -> modelMapper.map(p, PersonDto.class))
		.collect(Collectors.toList());
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<PersonDto> findPersonsBetweenAge(Integer minAge, Integer maxAge) {
	return personRepository
		.findByBirthDateGreaterThanOrBirthDateLessThan(LocalDate.now().minusYears(maxAge),
			LocalDate.now().minusYears(minAge))
		.map(p -> modelMapper.map(p, PersonDto.class)).collect(Collectors.toList());
    }

    @Override
    public Iterable<CityPopulationDto> getCitiesPopulation() {
	Iterable<String> uniqueCities = personRepository.findAllByAddressCity();
	List<CityPopulationDto> cityCount = new ArrayList<>();
	for (String city : uniqueCities) {
	    cityCount.add(new CityPopulationDto(city, personRepository.countByAddressCity(city)));
	}
	return cityCount;
    }

}
