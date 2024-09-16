package it.sport.siw.model;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Entity
public class Team {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @NotBlank
    private String name;

    @NotNull
    @Max(2024)
    private Integer year;

    private String address;

    @OneToOne
    @JoinColumn(name = "president_id")
    private President president;

    // Lista di giocatori associati alla squadra
    @OneToMany(mappedBy = "team", cascade = CascadeType.ALL)
    private Set<Player> players = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public President getPresident() {
        return president;
    }

    public void setPresident(President president) {
        if (!Objects.equals(this.president, president)) {
            this.president = president;
            if (president != null && !Objects.equals(president.getTeam(), this)) {
                president.setTeam(this);
            }
        }
    }

    public Set<Player> getPlayers() {
        return players;
    }

    public void setPlayers(Set<Player> players) {
        this.players = players;
    }

    public void addPlayer(Player player) {
        this.players.add(player);
        player.setTeam(this);
    }

    public void removePlayer(Player player) {
        this.players.remove(player);
        player.setTeam(null);
    }

	@Override
	public int hashCode() {
		return Objects.hash(address, id, name, year);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Team other = (Team) obj;
		return Objects.equals(address, other.address) && Objects.equals(id, other.id)
				&& Objects.equals(name, other.name) && Objects.equals(year, other.year);
	}

    
}
