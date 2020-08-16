DROP TABLE IF EXISTS USER_CREDENTIALS;

CREATE TABLE USER_CREDENTIALS (
    USER_ID                 UUID PRIMARY KEY    NOT NULL,
    USER_NAME               VARCHAR(255)        NOT NULL,
    EMAIL                   VARCHAR(255)        NOT NULL,
    PASSWORD                VARCHAR(255)        NOT NULL,
    FIRST_NAME              VARCHAR(255)        NOT NULL,
    LAST_NAME               VARCHAR(255)        NOT NULL,
    ENABLED                 boolean
);

CREATE TABLE WORLD_RANKINGS (
    PLAYER_ID               UUID PRIMARY KEY    NOT NULL,
    PLAYER_RANK             NUMERIC             NOT NULL,
    PLAYER_NAME             VARCHAR(255)        NOT NULL
);

CREATE TABLE SEASON_SCHEDULE (
    TOURNAMENT_ID       UUID PRIMARY KEY    NOT NULL,
    EVENT_TYPE          VARCHAR(255)        NOT NULL,
    SEASON              NUMERIC             NOT NULL,
    STATE               VARCHAR(255)        NOT NULL,
    NAME                VARCHAR(255)        NOT NULL,
    START_DATE          TIMESTAMP           NOT NULL,
    END_DATE            TIMESTAMP           NOT NULL
);

-- INSERT INTO USERS (first_name, last_name, email, user_name) VALUES
-- ('Matthew', 'Crocker', 'matthewcroc@gmail.com', 'matthewcrocker7'),
-- ('Nicole', 'Tranchita', 'tranchita.nicole@gmail.com', 'nicciT');
