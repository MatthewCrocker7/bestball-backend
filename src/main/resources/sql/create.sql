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
    END_DATE            DATE                NOT NULL
);

CREATE TABLE GAMES (
    GAME_ID                 UUID PRIMARY KEY    NOT NULL,
    GAME_STATE              VARCHAR(255)        NOT NULL,
    GAME_VERSION            NUMERIC             NOT NULL,
    GAME_TYPE               VARCHAR(255)        NOT NULL,
    DRAFT_ID                UUID                NOT NULL,
    TOURNAMENT_ID           UUID                NOT NULL,
    NUM_PLAYERS             NUMERIC             NOT NULL,
    MONEY_POT               NUMERIC             NOT NULL
);

CREATE TABLE TEAMS (
    TEAM_ID                 UUID PRIMARY KEY    NOT NULL,
    USER_ID                 UUID                NOT NULL,
    GAME_ID                 UUID                NOT NULL,
    PLAYER_ONE_ID           UUID                NOT NULL,
    PLAYER_TWO_ID           UUID                NOT NULL,
    PLAYER_THREE_ID         UUID                NOT NULL,
    PLAYER_FOUR_ID          UUID                NOT NULL,
    TO_PAR                  NUMERIC,
    TOTAL_SCORE             NUMERIC
);

CREATE TABLE DRAFTS (
    DRAFT_ID                UUID                NOT NULL,
    DRAFT_VERSION           NUMERIC             NOT NULL,
    DRAFT_STATE             VARCHAR(100)        NOT NULL,
    PRIMARY KEY (DRAFT_ID, DRAFT_VERSION)
);

CREATE TABLE DRAFT_SCHEDULES (
    DRAFT_ID                UUID PRIMARY KEY    NOT NULL,
    RELEASE_STATUS          VARCHAR(100)        NOT NULL,
    RELEASE_TIME            TIMESTAMP           NOT NULL
);

-- INSERT INTO USERS (first_name, last_name, email, user_name) VALUES
-- ('Matthew', 'Crocker', 'matthewcroc@gmail.com', 'matthewcrocker7'),
-- ('Nicole', 'Tranchita', 'tranchita.nicole@gmail.com', 'nicciT');
