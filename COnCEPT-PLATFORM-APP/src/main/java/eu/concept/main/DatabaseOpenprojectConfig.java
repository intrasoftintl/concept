package eu.concept.main;

import javax.sql.DataSource;

import org.springframework.boot.autoconfigure.jdbc.DataSourceBuilder;
import org.springframework.boot.autoconfigure.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.AdviceMode;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 *
 * @author Christos Paraskeva
 */
@Configuration
@EnableTransactionManagement(mode = AdviceMode.ASPECTJ)
@EnableJpaRepositories(basePackages = "eu.concept.repository.openproject.dao",
        entityManagerFactoryRef = "openprojectEntityManagerFactory",
        transactionManagerRef = "openprojectTransactionManager")
public class DatabaseOpenprojectConfig {

    public static final String PersistentUnit = "openproject";

    @Bean(name = "openprojectDataSource")
    @ConfigurationProperties(prefix = "spring.openprojectdb")
    public DataSource openprojectDataSource() {
        return DataSourceBuilder.create().build();
    }

    @Bean(name = "openprojectEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean openprojectEntityManagerFactory(
            EntityManagerFactoryBuilder builder) {
        return builder
                .dataSource(openprojectDataSource())
                .packages("eu.concept.repository.openproject.domain")
                .persistenceUnit(PersistentUnit)
                .build();
    }

    @Bean(name = "openprojectTransactionManager")
    public PlatformTransactionManager openprojectTransactionManager() {
        JpaTransactionManager jpaTransactionManager = new JpaTransactionManager();
        jpaTransactionManager.setDataSource(openprojectDataSource());
        jpaTransactionManager.setPersistenceUnitName(PersistentUnit);
        return jpaTransactionManager;
    }

    @Bean(name = "openprojectJdbc")
    public JdbcTemplate jdbcTemplate(DataSource ds) {
        return new JdbcTemplate(ds);
    }

}
