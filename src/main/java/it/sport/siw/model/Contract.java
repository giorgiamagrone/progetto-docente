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

    @OneToOne
    @JoinColumn(name = "player_id")
    private Player player;
    
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime startCareer;
    
    @Column(nullable = false)
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)
    private LocalDateTime stopCareer;

   
    public Contract() {}

    public Contract(Player player, LocalDateTime startCareer, LocalDateTime stopCareer) {
        this.player = player;
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
