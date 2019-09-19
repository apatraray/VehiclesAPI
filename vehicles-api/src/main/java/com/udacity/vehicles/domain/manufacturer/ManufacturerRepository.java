package com.udacity.vehicles.domain.manufacturer;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.config.*;
import org.springframework.stereotype.Repository;

@Repository
@EnableJpaRepositories
public interface ManufacturerRepository extends JpaRepository<Manufacturer, Integer> {

}
