package crocker.golf.bestball.config;

import crocker.golf.bestball.core.dao.*;
import crocker.golf.bestball.core.dao.h2.H2DraftDaoImpl;
import crocker.golf.bestball.core.dao.h2.H2PgaDaoImpl;
import crocker.golf.bestball.core.mapper.TeamRowMapper;
import crocker.golf.bestball.core.repository.PgaRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;

@Configuration
@Profile("local")
@Import({DatabaseConfig.class})
public class DatabaseLocalConfig {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseLocalConfig.class);

    @Value("${spring.datasource.url}")
    private String dbUrl;

    @Value("${server.env}")
    private String environment;

    @Bean
    public JdbcTemplate jdbcTemplate(DataSource dataSource) {
        return new JdbcTemplate(dataSource);
    }

    @Bean
    public NamedParameterJdbcTemplate namedParamJdbcTemplate(DataSource dataSource) {return new NamedParameterJdbcTemplate(dataSource); }

    @Bean
    public UserDao userDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new UserDao(namedParameterJdbcTemplate);
    }

    @Bean
    public PgaDao pgaDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        return new H2PgaDaoImpl(namedParameterJdbcTemplate);
    }

    @Bean
    public GameDaoImpl gameDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) { return new GameDaoImpl(namedParameterJdbcTemplate); }


    @Bean
    public DraftDao draftDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate) { return new H2DraftDaoImpl(namedParameterJdbcTemplate); }

    @Bean
    public TeamDao teamDao(NamedParameterJdbcTemplate namedParameterJdbcTemplate, TeamRowMapper teamRowMapper) { return new TeamDao(namedParameterJdbcTemplate, teamRowMapper); }

    @Bean
    public TeamRowMapper teamRowMapper(PgaRepository pgaRepository) {
        return new TeamRowMapper(pgaRepository);
    }
}
