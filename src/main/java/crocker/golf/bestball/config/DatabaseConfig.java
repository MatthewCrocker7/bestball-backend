package crocker.golf.bestball.config;

import crocker.golf.bestball.core.dao.*;
import crocker.golf.bestball.core.repository.DraftRepository;
import crocker.golf.bestball.core.repository.GameRepository;
import crocker.golf.bestball.core.repository.PgaRepository;
import crocker.golf.bestball.core.repository.UserRepository;
import org.apache.commons.dbcp2.BasicDataSource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
import java.net.URI;
import java.net.URISyntaxException;

@Configuration
@EnableCaching
public class DatabaseConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseConfig.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${server.env}")
    private String environment;

    @Profile("!local")
    @Bean("postgresDataSource")
    public BasicDataSource postgresDataSource()  throws URISyntaxException {

        BasicDataSource basicDataSource = new BasicDataSource();
        String datasourceUrl;

        if(environment.equals("uat")) {
            URI uri = new URI(dbUrl);

            String username = uri.getUserInfo().split(":")[0];
            String password = uri.getUserInfo().split(":")[1];
            basicDataSource.setUsername(username);
            basicDataSource.setPassword(password);

            datasourceUrl = "jdbc:postgresql://" + uri.getHost() + ':' + uri.getPort() + uri.getPath() + "?sslmode=require";
        } else {
            datasourceUrl = dbUrl;
        }

        basicDataSource.setUrl(datasourceUrl);
        logger.info("Datasource configured with following connection: {}", datasourceUrl);

        return basicDataSource;
    }

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParamJdbcTemplate(DataSource dataSource) {return new NamedParameterJdbcTemplate(dataSource); }

    @Bean
    public UserRepository userRepository(UserDao userDao) {
        return new UserRepository(userDao);
    }

    @Bean
    public UserDao userDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new UserDao(namedParameterJdbcTemplate);
    }

    @Bean
    public PgaRepository pgaRepository(PgaDao pgaDao) {
        return new PgaRepository(pgaDao);
    }

    @Bean
    public PgaDao pgaDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new PgaDao(namedParameterJdbcTemplate);
    }

    @Bean
    public GameRepository gameRepository(GameDao gameDao, TeamDao teamDao) { return new GameRepository(gameDao, teamDao); }

    @Bean
    public GameDao gameDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) { return new GameDao(namedParameterJdbcTemplate); }

    @Bean
    public DraftRepository draftRepository(DraftDao draftDao) { return new DraftRepository(draftDao); }

    @Bean
    public DraftDao draftDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) { return new DraftDao(namedParameterJdbcTemplate); }

    @Bean
    public TeamDao teamDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) { return new TeamDao(namedParameterJdbcTemplate); }
}
