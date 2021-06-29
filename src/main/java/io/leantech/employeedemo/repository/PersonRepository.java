package io.leantech.employeedemo.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import io.leantech.employeedemo.model.Person;

@Component
@RepositoryRestResource(exported=false)
public interface PersonRepository extends CrudRepository<Person, Long>{

}
