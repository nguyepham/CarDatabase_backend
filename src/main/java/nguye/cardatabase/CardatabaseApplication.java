package nguye.cardatabase;

import nguye.cardatabase.domain.Car;
import nguye.cardatabase.domain.CarRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class CardatabaseApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(CardatabaseApplication.class);

	private final CarRepository repository;

	// The CarRepository interface is injected into the constructor of the CardatabaseApplication class.
	public CardatabaseApplication(CarRepository repository) {
		this.repository = repository;
	}

	public static void main(String[] args) {
		SpringApplication.run(CardatabaseApplication.class, args);
		logger.info("Application started.");
	}

	// The CommandLineRunner interface is used to indicate that a bean should run when it is contained within a SpringApplication.
	// The run method is called after the application context is loaded and before the Spring Application.run method returns.
	// The run method is used to perform any actions that should happen when the application starts.
	@Override
	public void run(String... args) throws Exception {
		logger.info("Saving a couple of cars...");
		repository.save(new Car("Ford", "Mustang", "Red", "ADF-1121", 2017, 59000));
		repository.save(new Car("Nissan", "Leaf", "White", "SSJ-3002", 2014, 29000));
		repository.save(new Car("Toyota", "Camry", "Black", "KKO-2345", 2020, 49000));
		repository.save(new Car("Toyota", "Century", "White", "G70-3744", 2023, 170000));

		logger.info("Fetching all cars...");
		for (Car car : repository.findAll()) {
			logger.info(car.toString());
		}

		logger.info("Fetching cars by brand...");
		repository.findByBrand("Toyota").forEach(car -> {
			logger.info(car.toString());
		});

		logger.info("Fetching cars by brand and sort by year...");
		repository.findByBrandOrderByModelYearDesc("Toyota").forEach(car -> {
			logger.info(car.toString());
		});
	}
}
