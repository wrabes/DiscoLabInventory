package discoverylab.sate.com.inventory2;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by Cameron on 7/30/14.
 *
 * The Person list exists only here so that it cannot be directly mutated by other classes.
 */
public class PersonList {

    private static List<Person> persons = new ArrayList<Person>();;
    private static PersonList instance;
    private static final String TAG = "PersonList";
    private PersonComparator comp = new PersonComparator();

    private PersonList(){

    }

    public static PersonList getInstance(){
        if(instance == null){
            instance = new PersonList();
            instance.generateSamplePersonList();
        }
        return instance;
    }

    private void generateSamplePersonList(){
        persons.add(personWithNameOnly("Mike Yamada"));
        persons.add(personWithNameOnly("Ben Natarian"));
        persons.add(personWithNameOnly("Michelle Hurtubise"));
        persons.add(personWithNameOnly("Andrew Urschel"));
        persons.add(personWithNameOnly("Brandon Lundeen"));
        persons.add(personWithNameOnly("Cam Wrabel"));
        persons.add(personWithNameOnly("Chris Bennett"));
        persons.add(personWithNameOnly("David Quante"));
        persons.add(personWithNameOnly("Deepti Gupta"));
        persons.add(personWithNameOnly("Josh Lan"));
        persons.add(personWithNameOnly("Ryan Putman"));
    }

    public ArrayList<String> personListNames(){

        ArrayList<String> personsNames = new ArrayList<String>();
        for(int i=0; i<persons.size(); i++){
            personsNames.add(persons.get(i).getName());
        }

        return personsNames;
    }

    private Person personWithNameOnly(String name){
        Person person = new Person();
        person.setName(name);
        return person;
    }

    public List<Person> masterList(){
        Iterator<Person> it = persons.iterator();
        List<Person> masterList = new ArrayList<Person>();

        while(it.hasNext()){
            Person tmp = it.next();

            masterList.add(tmp);
        }
        return masterList;
    }

    public void addPerson(Person person){
        persons.add(person);
    }

    public List<Person> getSortedPersonList(){
        //This sorts the master list alphanumerically on every call to getPersonList
        Collections.sort(persons, comp);
        return persons;
    }

    public class PersonComparator implements Comparator<Person> {

        @Override
        public int compare(Person person1, Person person2){
            return person1.getName().compareTo(person2.getName());
        }
    }
}
