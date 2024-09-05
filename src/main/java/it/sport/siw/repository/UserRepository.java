package it.sport.siw.repository;


import org.springframework.data.repository.CrudRepository;

import it.sport.siw.model.User;



public interface UserRepository extends CrudRepository<User, Long> {
}