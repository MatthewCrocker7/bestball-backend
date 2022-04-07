DROP TABLE IF EXISTS EXAMPLE_TABLE;
/*
DROP TABLE IF EXISTS USER_CREDENTIALS;
DROP TABLE IF EXISTS WORLD_RANKINGS;
DROP TABLE IF EXISTS SEASON_SCHEDULE;
DROP TABLE IF EXISTS GAMES;
DROP TABLE IF EXISTS TEAMS;
DROP TABLE IF EXISTS DRAFTS;
DROP TABLE IF EXISTS DRAFT_SCHEDULES;
DROP TABLE IF EXISTS DRAFT_PGA_PLAYERS;
DROP TABLE IF EXISTS DRAFT_ORDER;
DROP TABLE IF EXISTS TOURNAMENT_FIELD;
DROP TABLE IF EXISTS TOURNAMENT_COURSES;
DROP TABLE IF EXISTS TOURNAMENT_ROUNDS;
DROP TABLE IF EXISTS PLAYER_ROUNDS;
DROP TABLE IF EXISTS TEAM_ROUNDS;
--
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
    TOURNAMENT_ID               UUID PRIMARY KEY    NOT NULL,
    EVENT_TYPE                  VARCHAR(255)        NOT NULL,
    PGA_SEASON                  NUMERIC             NOT NULL,
    TOURNAMENT_STATE            VARCHAR(255)        NOT NULL,
    TOURNAMENT_NAME             VARCHAR(255)        NOT NULL,
    TOURNAMENT_START_DATE       TIMESTAMP           NOT NULL,
    TOURNAMENT_END_DATE         DATE                NOT NULL
);

CREATE TABLE GAMES (
    GAME_ID                 UUID                NOT NULL,
    GAME_STATE              VARCHAR(255)        NOT NULL,
    GAME_VERSION            NUMERIC             NOT NULL,
    GAME_TYPE               VARCHAR(255)        NOT NULL,
    DRAFT_ID                UUID                NOT NULL,
    TOURNAMENT_ID           UUID                NOT NULL,
    NUM_PLAYERS             NUMERIC             NOT NULL,
    BUY_IN                  NUMERIC             NOT NULL,
    MONEY_POT               NUMERIC             NOT NULL,
    PRIMARY KEY (GAME_ID, GAME_VERSION)
);

CREATE TABLE TEAMS (
    TEAM_ID                 UUID                NOT NULL,
    USER_ID                 UUID                NOT NULL,
    GAME_ID                 UUID                NOT NULL,
    DRAFT_ID                UUID                NOT NULL,
    TOURNAMENT_ID           UUID                NOT NULL,
    TEAM_ROLE               VARCHAR(100)        NOT NULL,
    DRAFT_PICK              NUMERIC,
    PLAYER_ONE_ID           UUID,
    PLAYER_TWO_ID           UUID,
    PLAYER_THREE_ID         UUID,
    PLAYER_FOUR_ID          UUID,
    TO_PAR                  NUMERIC,
    TOTAL_STROKES           NUMERIC,
    PRIMARY KEY (TEAM_ID, USER_ID, GAME_ID, DRAFT_ID, TOURNAMENT_ID)
);

CREATE TABLE DRAFTS (
    DRAFT_ID                UUID                NOT NULL,
    DRAFT_VERSION           NUMERIC             NOT NULL,
    DRAFT_STATE             VARCHAR(100)        NOT NULL,
    DRAFT_TIME              TIMESTAMP           NOT NULL,
    CURRENT_PICK            NUMERIC             NOT NULL,
    PRIMARY KEY (DRAFT_ID, DRAFT_VERSION)
);

CREATE TABLE DRAFT_SCHEDULES (
    DRAFT_ID                UUID PRIMARY KEY    NOT NULL,
    RELEASE_STATUS          VARCHAR(100)        NOT NULL,
    RELEASE_TIME            TIMESTAMP           NOT NULL
);

--TODO: TABLE DRAFT_PGA_PLAYERS SHOULD BE HAVE ITS ENTRIES DELETED WHEN DRAFT_STATUS = COMPLETE
--RUN SCHEDULED JOB TO CLEAN UP TABLE
CREATE TABLE DRAFT_PGA_PLAYERS (
    DRAFT_ID                UUID                NOT NULL,
    PLAYER_ID               UUID                NOT NULL,
    PLAYER_RANK             NUMERIC             NOT NULL,
    PLAYER_NAME             VARCHAR(255)        NOT NULL,
    DRAFTED                 BOOLEAN             NOT NULL,
    PRIMARY KEY (DRAFT_ID, PLAYER_ID)
);

--TODO: TABLE DRAFT_ORDER SHOULD BE HAVE ITS ENTRIES DELETED WHEN DRAFT_STATUS = COMPLETE
--RUN SCHEDULED JOB TO CLEAN UP TABLE
CREATE TABLE DRAFT_ORDER (
    DRAFT_ID                UUID                NOT NULL,
    USER_ID                 UUID                NOT NULL,
    PICK_NUMBER             NUMERIC             NOT NULL,
    USER_NAME               VARCHAR(255)        NOT NULL,
    EMAIL                   VARCHAR(255)        NOT NULL,
    PRIMARY KEY (DRAFT_ID, USER_ID, PICK_NUMBER)
);

CREATE TABLE TOURNAMENT_FIELD (
    TOURNAMENT_ID               UUID                NOT NULL,
    PLAYER_ID                   UUID                NOT NULL,
    PRIMARY KEY (TOURNAMENT_ID, PLAYER_ID)
);

CREATE TABLE TOURNAMENT_COURSES (
    TOURNAMENT_ID               UUID                NOT NULL,
    COURSE_ID                   UUID                NOT NULL,
    COURSE_NAME                 VARCHAR(500)        NOT NULL,
    YARDAGE                     NUMERIC             NOT NULL,
    PAR                         NUMERIC             NOT NULL,
    HOLES                       BYTEA               NOT NULL,
    PRIMARY KEY (TOURNAMENT_ID, COURSE_ID)
);

CREATE TABLE TOURNAMENT_ROUNDS (
    TOURNAMENT_ID               UUID                NOT NULL,
    ROUND_ID                    UUID                NOT NULL,
    ROUND_NUMBER                NUMERIC             NOT NULL,
    STATUS                      VARCHAR(100)        NOT NULL,
    PRIMARY KEY (TOURNAMENT_ID, ROUND_ID)
);

CREATE TABLE PLAYER_ROUNDS (
    PLAYER_ID                   UUID                NOT NULL,
    TOURNAMENT_ID               UUID                NOT NULL,
    ROUND_ID                    UUID                NOT NULL,
    ROUND_NUMBER                NUMERIC             NOT NULL,
    COURSE_ID                   UUID                NOT NULL,
    TO_PAR                      NUMERIC             NOT NULL,
    THRU                        NUMERIC             NOT NULL,
    STROKES                     NUMERIC             NOT NULL,
    SCORES                      BYTEA               NOT NULL,
    PRIMARY KEY (PLAYER_ID, TOURNAMENT_ID, ROUND_ID)
);

CREATE TABLE TEAM_ROUNDS (
    TEAM_ID                     UUID                NOT NULL,
    GAME_ID                     UUID                NOT NULL,
    ROUND_ID                    UUID                NOT NULL,
    TOURNAMENT_ID               UUID                NOT NULL,
    ROUND_NUMBER                NUMERIC             NOT NULL,
    TO_PAR                      NUMERIC             NOT NULL,
    STROKES                     NUMERIC             NOT NULL,
    FRONT_NINE                  NUMERIC             NOT NULL,
    BACK_NINE                   NUMERIC             NOT NULL,
    SCORES                      BYTEA               NOT NULL,
    PRIMARY KEY (TEAM_ID, GAME_ID, ROUND_ID)
);

TRUNCATE TABLE WORLD_RANKINGS
TRUNCATE TABLE SEASON_SCHEDULE
TRUNCATE TABLE GAMES
TRUNCATE TABLE TEAMS
TRUNCATE TABLE DRAFTS
TRUNCATE TABLE DRAFT_SCHEDULES
TRUNCATE TABLE DRAFT_PGA_PLAYERS
TRUNCATE TABLE DRAFT_ORDER
TRUNCATE TABLE TOURNAMENT_FIELD
TRUNCATE TABLE TOURNAMENT_COURSES
TRUNCATE TABLE TOURNAMENT_ROUNDS
TRUNCATE TABLE PLAYER_ROUNDS
TRUNCATE TABLE TEAM_ROUNDS;
TRUNCATE TABLE PLAYER_ROUNDS;


delete game
truncate games;
truncate drafts;
truncate teams;
truncate draft_schedules;
truncate DRAFT_PGA_PLAYERS;
truncate DRAFT_ORDER;
truncate TEAM_ROUNDS;
 */

/*
1. Dentyn : e7496ab2-d3b3-4601-8f7f-e66ec0a3aacd
2. Matthew : cf8217b0-d23c-4a0d-ad96-90114b0e3ece
3. Jonathan : 082c3dc5-8945-4de1-940b-65e488645187
4. Donny : a838467f-b498-44e4-8a54-8c140408f31c
5. Drew : f829ac9a-fb9a-4c00-ba3c-73208960b25e
6. Matt : 870ccfee-ce05-42ad-96d9-9dfcf538b26a
7. Conner : cfd9418d-7b3d-459f-9716-ae950e06b059
8. Zack : 87a0b3d2-8401-4c96-9f9c-43da9f51292c
9. Shayne : a797385d-cf3c-42be-9316-9ec1d9a6d657
 */
/*










 */
