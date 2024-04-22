DROP DATABASE IF EXISTS mastermind_test;
CREATE DATABASE mastermind_test;
USE mastermind_test;

CREATE TABLE games (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    secret_code VARCHAR(10),
    created_at DATETIME,
    completed_at DATETIME
);

CREATE TABLE guesses (
    guess_id INT AUTO_INCREMENT PRIMARY KEY,
    game_id INT,
    guess_sequence VARCHAR(10),
    feedback VARCHAR(255),
    guess_time DATETIME,
    FOREIGN KEY (game_id) REFERENCES games(game_id)
);

DELIMITER //

CREATE PROCEDURE set_known_good_state()
BEGIN
    SET SQL_SAFE_UPDATES = 0;

    DELETE FROM guesses;
    ALTER TABLE guesses AUTO_INCREMENT = 1;

    DELETE FROM games;
    ALTER TABLE games AUTO_INCREMENT = 1;

    INSERT INTO games (secret_code, created_at, completed_at) VALUES
    ('0123', '2023-01-01 12:00:00', NULL); 

    INSERT INTO guesses (game_id, guess_sequence, feedback, guess_time) VALUES
    (1, '1234', '1 correct number and 0 correct location', '2023-01-01 12:05:00');

    SET SQL_SAFE_UPDATES = 1;
END //

DELIMITER ;

CALL set_known_good_state();