package nguye.cardatabase;

import nguye.cardatabase.model.AppUser;
import nguye.cardatabase.model.Car;
import nguye.cardatabase.repository.AppUserRepository;
import nguye.cardatabase.repository.CarRepository;
import nguye.cardatabase.model.Owner;
import nguye.cardatabase.repository.OwnerRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Arrays;

@SpringBootApplication
public class CardatabaseApplication implements CommandLineRunner {

	private static final Logger logger = LoggerFactory.getLogger(CardatabaseApplication.class);

	private final CarRepository carRepository;
	private final OwnerRepository ownerRepository;
	private final AppUserRepository appUserRepository;

    public CardatabaseApplication(CarRepository carRepository, OwnerRepository ownerRepository, AppUserRepository appUserRepository) {
        this.carRepository = carRepository;
        this.ownerRepository = ownerRepository;
        this.appUserRepository = appUserRepository;
    }

    public static void main(String[] args) {
		SpringApplication.run(CardatabaseApplication.class, args);
		logger.info("Application started.");
	}

	// The CommandLineRunner interface is used to indicate that a bean should run when it is contained within a SpringApplication.
	// The run method is called after the application context is loaded and before the Spring Application.run method returns.
	// The run method is used to perform any actions that should happen when the application starts.
	@Override
	public void run(String... args) {
		logger.info("Saving a couple of owners...");
		Owner owner1 = new Owner("John", "Smith");
		Owner owner2 = new Owner("Mary", "Jane");
		ownerRepository.saveAll(Arrays.asList(owner1, owner2));

		logger.info("Saving a couple of cars...");
		carRepository.save(new Car("Ford", "Mustang", "Red", "ADF-1121", 2017, 59000, owner1));
		carRepository.save(new Car("Nissan", "Leaf", "White", "SSJ-3002", 2014, 29000, owner2));
		carRepository.save(new Car("Toyota", "Camry", "Black", "KKO-2345", 2020, 49000, owner1));
		carRepository.save(new Car("Toyota", "Century", "White", "G70-3744", 2023, 170000, owner2));

		logger.info("Saving a couple of app users...");
		appUserRepository.save(new AppUser(
			"user",
			"$2a$12$2y7ZRQj1isl.paMVsEAIeeBfBU/G8XrbP149dZhCQvzR2PdSg69v2",
			"USER"));
		appUserRepository.save(new AppUser(
			"admin",
			"$2a$12$QSVXPTtgpmpLjRa.1Ibt1eqO4SHe0SHyEdRs24ouJT50znQ2gcTN2",
			"ADMIN"));

		logger.info("Fetching all cars...");
		for (Car car : carRepository.findAll()) {
            logger.info("Car: {} {} {}", car.getBrand(), car.getModel(), car.getModelYear());
		}
	}
}
