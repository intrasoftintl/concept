package eu.concept.configuration;

import com.atomikos.jdbc.AtomikosDataSourceBean;
import com.mysql.jdbc.jdbc2.optional.MysqlXADataSource;
import eu.concept.main.AtomikosJtaPlatform;
import java.util.HashMap;
import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.stereotype.Component;

/**
 *
 * @author Christos Paraskeva
 */
@Configuration
@EnableJpaRepositories(basePackages = "eu.concept.repository.concept.dao",
        entityManagerFactoryRef = "conceptEntityManagerFactory",
        transactionManagerRef = "transactionManager")
public class DatasourceConceptConfig {

    public static final String PersistentUnit = "conceptPersistenceUnit";

    @Autowired
    private JpaVendorAdapter jpaVendorAdapter;

    @Autowired
    private DatasourceOpenprojectProperties datasourceOpenprojectProperties;

    @Primary
    @Bean(name = "conceptDataSource", initMethod = "init", destroyMethod = "close")
    public DataSource customerDataSource() {
        MysqlXADataSource mysqlXaDataSource = new MysqlXADataSource();
        mysqlXaDataSource.setUrl(datasourceOpenprojectProperties.getUrl());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);
        mysqlXaDataSource.setUser(datasourceOpenprojectProperties.getUsername());
        mysqlXaDataSource.setPassword(datasourceOpenprojectProperties.getPassword());
        mysqlXaDataSource.setPinGlobalTxToPhysicalConnection(true);

        AtomikosDataSourceBean xaDataSource = new AtomikosDataSourceBean();
        xaDataSource.setXaDataSource(mysqlXaDataSource);
        xaDataSource.setUniqueResourceName("conceptDS");
        return xaDataSource;

    }

    @Primary
    @Bean(name = "conceptEntityManagerFactory")
    @DependsOn("transactionManager")
    public LocalContainerEntityManagerFactoryBean customerEntityManager() throws Throwable {

        HashMap<String, Object> properties = new HashMap<>();
        properties.put("hibernate.transaction.jta.platform", AtomikosJtaPlatform.class.getName());
        properties.put("javax.persistence.transactionType", "JTA");

        LocalContainerEntityManagerFactoryBean entityManager = new LocalContainerEntityManagerFactoryBean();
        entityManager.setJtaDataSource(customerDataSource());
        entityManager.setJpaVendorAdapter(jpaVendorAdapter);
        entityManager.setPackagesToScan("eu.concept.repository.concept.domain");
        entityManager.setPersistenceUnitName(PersistentUnit);
        entityManager.setJpaPropertyMap(properties);
        return entityManager;
    }

    @ConfigurationProperties(prefix = "spring.conceptdb")
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
