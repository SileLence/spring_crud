package dv.trunov.dao;

import dv.trunov.models.Person;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PersonDAO {

    private static int PEOPLE_COUNT;
    private List<Person> people;

    {
        people = new ArrayList<>();
        people.add(new Person(++PEOPLE_COUNT, "Tom", 24, "example@domain.com"));
        people.add(new Person(++PEOPLE_COUNT, "Bob", 31, "email@domain.de"));
        people.add(new Person(++PEOPLE_COUNT, "Mike", 20, "box@domain.fr"));
        people.add(new Person(++PEOPLE_COUNT, "Katy", 25, "girl@domain.kz"));
    }

    public List<Person> index() {
        return people;
    }

    public Person show(int id) {
        return people.stream()
            .filter(person -> person.getId() == id)
            .findFirst()
            .orElse(null);
    }

    public void save(Person person) {
        person.setId(++PEOPLE_COUNT);
        people.add(person);
    }

    public void update(int id, Person updatedPerson) {
        Person personToBeUpdated = show(id);
        personToBeUpdated.setName(updatedPerson.getName());
        personToBeUpdated.setAge(updatedPerson.getAge());
        personToBeUpdated.setEmail(updatedPerson.getEmail());
    }

    public void delete(int id) {
        people.removeIf(person -> person.getId() == id);
    }
}
