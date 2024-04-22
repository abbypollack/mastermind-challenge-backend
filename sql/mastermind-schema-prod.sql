DROP DATABASE IF EXISTS mastermind;
CREATE DATABASE IF NOT EXISTS mastermind;
USE mastermind;

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
