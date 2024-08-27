package it.sport.siw.model;

import java.util.Objects;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
@Entity
public class Player {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	//private Long id;
	
	private String name;
	private String surname;
	private String city;
	private String year;
	private String role;
	private Integer startCarreer;
	private Integer stopCarreer;
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
	public String getRole() {
		return role;
	}
	public void setRole(String role) {
		this.role = role;
	}
	public Integer getStartCarreer() {
		return startCarreer;
	}
	public void setStartCarreer(Integer startCarreer) {
		this.startCarreer = startCarreer;
	}
	public Integer getStopCarreer() {
		return stopCarreer;
	}
	public void setStopCarrer(Integer stopCarreer) {
		this.stopCarreer = stopCarreer;
	}
	@Override
	public int hashCode() {
		return Objects.hash(name, role, startCarreer, stopCarreer, surname, year);
	}
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Player other = (Player) obj;
		return Objects.equals(name, other.name) && Objects.equals(role, other.role)
				&& Objects.equals(startCarreer, other.startCarreer) && Objects.equals(stopCarreer, other.stopCarreer)
				&& Objects.equals(surname, other.surname) && Objects.equals(year, other.year);
	}

}
