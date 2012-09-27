package org.icefaces.tutorials.ace;


public class PersonnelRowObject {
    Person person;
    City city;
    Region region;
    Organization organization;

    public PersonnelRowObject(Person person) {
        this.person = person;
    }

    public PersonnelRowObject(City city) {
        this.city = city;
    }

    public PersonnelRowObject(Region region) {
        this.region = region;
    }

    public PersonnelRowObject(Organization organization) {
        this.organization = organization;
    }

    public Person getPerson() {
        return person;
    }

    public void setPerson(Person person) {
        this.person = person;
    }

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Region getRegion() {
        return region;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public Organization getOrganization() {
        return organization;
    }

    public void setOrganization(Organization organization) {
        this.organization = organization;
    }
}
