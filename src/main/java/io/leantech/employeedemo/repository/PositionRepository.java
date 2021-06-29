package io.leantech.employeedemo.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.stereotype.Component;

import io.leantech.employeedemo.model.Position;

@Component
@RepositoryRestResource(exported=false)
public interface PositionRepository extends CrudRepository<Position, Long>{
	@Override
	List<Position> findAll();
	Optional<Position> findByName(@Param("name") String name);
}