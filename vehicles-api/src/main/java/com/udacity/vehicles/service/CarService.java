package com.udacity.vehicles.service;

import com.udacity.vehicles.client.maps.*;
import com.udacity.vehicles.client.prices.*;
import com.udacity.vehicles.domain.*;
import com.udacity.vehicles.domain.car.Car;
import com.udacity.vehicles.domain.car.CarRepository;

import java.util.*;

import com.udacity.vehicles.domain.manufacturer.Manufacturer;
import org.modelmapper.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.*;

/**
 * Implements the car service create, read, update or delete
 * information about vehicles, as well as gather related
 * location and price data when desired.
 */
@Service
public class CarService {
    @Autowired
    private final CarRepository repository;

//    @Autowired
//    private Address address;

    @Autowired
    private ModelMapper modelMapper;

    @Qualifier("maps")
    @Autowired
    private WebClient webClientMap;

    @Qualifier("pricing")
    @Autowired
    private WebClient webClientPricing;

    public CarService(CarRepository repository, ModelMapper modelMapper, @Qualifier("maps") WebClient webClientMap, @Qualifier("pricing") WebClient webClientPricing) {
        this.webClientMap = webClientMap;
        this.webClientPricing = webClientPricing;
        this.repository = repository;
        this.modelMapper = modelMapper;
     //   this.address = address;
    }

    /**
     * Gathers a list of all vehicles
     * @return a list of all vehicles in the CarRepository
     */
    public List<Car> list() {

        return repository.findAll();
    }

    /**
     * Gets car information by ID (or throws exception if non-existent)
     * @param id the ID number of the car to gather information on
     * @return the requested car's information, including location and price
     */
    public Car findById(Long id) {
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         *   Remove the below code as part of your implementation.
         */

        Optional<Car> optionalCar = repository.findById(id);
        Car car = optionalCar.orElseThrow(CarNotFoundException::new);

        /**
         * TODO: Use the Pricing Web client you create in `VehiclesApiApplication`
         *   to get the price based on the `id` input'
         * TODO: Set the price of the car
         * Note: The car class file uses @transient, meaning you will need to call
         *   the pricing service each time to get the price.
         */
        PriceClient priceClient = new PriceClient(webClientMap);
        String price = priceClient.getPrice(id);
        car.setPrice(price);
        /**
         * TODO: Use the Maps Web client you create in `VehiclesApiApplication`
         *   to get the address for the vehicle. You should access the location
         *   from the car object and feed it to the Maps service.
         * TODO: Set the location of the vehicle, including the address information
         * Note: The Location class file also uses @transient for the address,
         * meaning the Maps service needs to be called each time for the address.
         */

       MapsClient mapsClient = new MapsClient(webClientMap, modelMapper);
        Location location = new Location();
 //       location.setAddress( String.valueOf( address ) );
        Location newAddress = mapsClient.getAddress(location);
        car.setLocation( newAddress );
        return car;
    }

    /**
     * Either creates or updates a vehicle, based on prior existence of car
     * @param car A car object, which can be either new or existing
     * @return the new/updated car is stored in the repository
     */
    public Car save(Car car) {
        if (car.getId() != null) {
            return repository.findById(car.getId())
                    .map(carToBeUpdated -> {
                        carToBeUpdated.setDetails(car.getDetails());
                        carToBeUpdated.setLocation(car.getLocation());
                        return repository.save(carToBeUpdated);
                    }).orElseThrow(CarNotFoundException::new);
        }

        return repository.save(car);
    }

    /**
     * Deletes a given car by ID
     * @param id the ID number of the car to delete
     */
    public void delete(Long id) throws CarNotFoundException{
        /**
         * TODO: Find the car by ID from the `repository` if it exists.
         *   If it does not exist, throw a CarNotFoundException
         */
        boolean deleted = false;
        Iterable<Car> allCars = repository.findAll();
        // Loop through all dogs to check their breed
        for (Car c:allCars) {
                repository.delete(c);
                deleted = true;
        }
        // Throw an exception if the breed doesn't exist
        if (!deleted) {
            throw new CarNotFoundException("Car Not Found");
        }
//        return deleted;


        /**
         * TODO: Delete the car from the repository.
         */


    }
}
