package it.sport.siw.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class President {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//private Long id;
	
	private String name;
	private String surname;
	private String cf;
	private String city;
	private String year;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getSurname() {
		return surname;
	}
	public void setSurname(String surname) {
		this.surname = surname;
	}
	public String getCf() {
		return cf;
	}
	public void setCf(String cf) {
		this.cf = cf;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getYear() {
		return year;
	}
	public void setYear(String year) {
		this.year = year;
	}
	@Override
	public int hashCode() {
		return Objects.hash(cf, city, name, surname, year);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		President other = (President) obj;
		return Objects.equals(cf, other.cf) && Objects.equals(city, other.city) && Objects.equals(name, other.name)
				&& Objects.equals(surname, other.surname) && Objects.equals(year, other.year);
	}
	
	
}
