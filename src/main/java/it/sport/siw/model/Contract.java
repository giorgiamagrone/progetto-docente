package it.sport.siw.model;

import jakarta.persistence.*;
import java.time.LocalDateTime;
import java.util.Objects;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "player_id")
    private Player player;
    
    @ManyToOne
    @JoinColumn(name = "team_id")  // Many contracts can be associated with one team
    private Team team;
    
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startCareer;
    
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime stopCareer;

   
    public Contract() {}

    public Contract(Player player, Team team, LocalDateTime startCareer, LocalDateTime stopCareer) {
        this.player = player;
        this.team = team;
        this.startCareer = startCareer;
        this.stopCareer = stopCareer;
    }

   
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public LocalDateTime getStartCareer() {
        return startCareer;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public void setStartCareer(LocalDateTime startCareer) {
        this.startCareer = startCareer;
    }

    public LocalDateTime getStopCareer() {
        return stopCareer;
    }

    public void setStopCareer(LocalDateTime stopCareer) {
        this.stopCareer = stopCareer;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Contract contract = (Contract) o;
        return Objects.equals(id, contract.id) &&
               Objects.equals(player, contract.player) &&
               Objects.equals(startCareer, contract.startCareer) &&
               Objects.equals(stopCareer, contract.stopCareer);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, player, startCareer, stopCareer);
    }
}
