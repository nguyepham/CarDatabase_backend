package nguye.cardatabase.repository;

import nguye.cardatabase.model.Car;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface CarRepository extends CrudRepository<Car, Long> {
    List<Car> findByBrand(String brand);
    List<Car> findByColor(String color);
    List<Car> findByModelYear(int year);
    List<Car> findByBrandAndModel(String brand, String model);
    List<Car> findByBrandOrColor(String brand, String color);
    List<Car> findByBrandOrderByModelYearAsc(String brand);
    List<Car> findByBrandOrderByModelYearDesc(String brand);
}
