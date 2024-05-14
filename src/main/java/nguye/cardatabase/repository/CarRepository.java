package nguye.cardatabase.repository;

import nguye.cardatabase.model.Car;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.List;

@RepositoryRestResource
public interface CarRepository extends CrudRepository<Car, Long> {
    List<Car> findByBrand(@Param("brand") String brand);
    List<Car> findByColor(@Param("color") String color);
    List<Car> findByModelYear(int year);
    List<Car> findByBrandAndModel(String brand, String model);
    List<Car> findByBrandOrColor(String brand, String color);
    List<Car> findByBrandOrderByModelYearAsc(String brand);
    List<Car> findByColorOrderByModelYearAsc(String brand);
    List<Car> findByBrandOrderByModelYearDesc(String brand);
}
