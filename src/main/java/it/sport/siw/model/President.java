package it.sport.siw.model;

import java.time.LocalDate;
import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
@Entity
public class President {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	@NotBlank
	private String name;
	@NotBlank
	private String surname;
	@NotBlank
	private String cf;
	@NotBlank
	private String city;
	@NotNull
	private LocalDate year;
	
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
	public  LocalDate getYear() {
		return year;
	}
	public void setYear( LocalDate year) {
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
