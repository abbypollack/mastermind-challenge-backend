DROP DATABASE IF EXISTS mastermind;
CREATE DATABASE IF NOT EXISTS mastermind;
USE mastermind;

CREATE TABLE players (
    player_id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    score INT NOT NULL DEFAULT 0
);

CREATE TABLE games (
    game_id INT AUTO_INCREMENT PRIMARY KEY,
    secret_code VARCHAR(10) NOT NULL,
    game_start_time DATETIME NOT NULL,
    game_end_time DATETIME,
    difficulty_level VARCHAR(50),
    winner_id INT NOT NULL,
    hint_count INT NOT NULL
);

CREATE TABLE guesses (
    guess_id INT AUTO_INCREMENT PRIMARY KEY,
    game_id INT NOT NULL,
    player_id INT NOT NULL,
    guess_sequence VARCHAR(10) NOT NULL,
    feedback VARCHAR(255) NOT NULL,
    guess_time DATETIME NOT NULL,
    FOREIGN KEY (game_id) REFERENCES games(game_id),
    FOREIGN KEY (player_id) REFERENCES players(player_id)
);

CREATE TABLE game_players (
    game_id INT NOT NULL,
    player_id INT NOT NULL,
    PRIMARY KEY (game_id, player_id),
    FOREIGN KEY (game_id) REFERENCES games(game_id),
    FOREIGN KEY (player_id) REFERENCES players(player_id)
);
