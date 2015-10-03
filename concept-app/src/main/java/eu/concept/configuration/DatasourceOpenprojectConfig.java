package eu.concept.configuration;

import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import eu.concept.main.AtomikosJtaPlatform;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christos Paraskeva
 */
@Configuration
@EnableJpaRepositories(basePackages = "eu.concept.repository.openproject.dao",
        entityManagerFactoryRef = "openprojectEntityManagerFactory",
        transactionManagerRef = "transactionManager")
public class DatasourceOpenprojectConfig {

    public static final String PersistentUnit = "openprojectPersistenceUnit";

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private DatasourceOpenprojectProperties datasourceOpenprojectProperties;

    @Bean(name = "openprojectDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource customerDataSource() {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();

//        mysqlXaDataSource.setUrl("jdbc:mysql://localhost:3306/openproject");
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

//        mysqlXaDataSource.setUser("openproject");
//        mysqlXaDataSource.setPassword("!dev35!");

        //Uncomment for production deployment
                mysqlXaDataSource.setUrl(datasourceOpenprojectProperties.getUrl());
                mysqlXaDataSource.setUser(datasourceOpenprojectProperties.getUsername());
        mysqlXaDataSource.setPassword(datasourceOpenprojectProperties.getPassword());
        
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("openprojectDS");
        return xaDataSource;

    }

    @Bean(name = "openprojectEntityManagerFactory")
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean customerEntityManager() throws Throwable {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(customerDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan("eu.concept.repository.openproject.domain");
        entityManager.setPersistenceUnitName(PersistentUnit);
        entityManager.setJpaPropertyMap(properties);
        return entityManager;
    }

    @ConfigurationProperties(prefix = "spring.openprojectdb")
    @Component
    private static class DatasourceOpenprojectProperties {

        private String url;

        private String username;

        private String password;

        public String getUrl() {
            return url;
        }

        public void setUrl(String url) {
            this.url = url;
        }

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}
