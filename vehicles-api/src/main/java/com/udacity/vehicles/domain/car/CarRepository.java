package com.udacity.vehicles.domain.car;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface CarRepository extends JpaRepository<Car, Long> {

}
