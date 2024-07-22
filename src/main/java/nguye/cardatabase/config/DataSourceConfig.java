package nguye.cardatabase.config;

import nguye.cardatabase.security.JwtService;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

@Configuration
public class DataSourceConfig {

    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    DataSource dataSource() throws Exception {

        String password = JwtService.getVaultSecret("database_secret");

        return DataSourceBuilder
                .create()
                .url("jdbc:mariadb://cardb.czmm6swwk6yn.ap-southeast-2.rds.amazonaws.com:3306/cardb")
                .username("admin")
                .password(password)
                .driverClassName("org.mariadb.jdbc.Driver")
                .build();
    }
}