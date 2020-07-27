DROP TABLE IF EXISTS USERS;

CREATE TABLE USERS {
    id INT AUTO_INCREMENT   PRIMARY KEY,
    first_name              VARCHAR(255) NOT NULL,
    last_name               VARCHAR(255) NOT NULL,
    email                   VARCHAR(255) NOT NULL,
    user_name               VARCHAR(255) NOT NULL
};

INSERT INTO USERS (first_name, last_name, email, user_name) VALUES
('Matthew', 'Crocker', 'matthewcroc@gmail.com', 'matthewcrocker7'),
('Nicole', 'Tranchita', 'tranchita.nicole@gmail.com', 'nicciT');
