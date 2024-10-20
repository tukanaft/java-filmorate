CREATE TABLE IF NOT EXISTS MPA  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(20)
);
CREATE TABLE IF NOT EXISTS genres  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(50) NOT NULL
);
CREATE TABLE IF NOT EXISTS films  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(40) NOT NULL,
    description varchar(200) NOT NULL,
    release_date date NOT NULL,
    duration INTEGER NOT NULL,
    mpa_id INTEGER references MPA(id)
);
CREATE TABLE IF NOT EXISTS film_genres  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    film_id INTEGER NOT NULL references films(id),
    genres_id INTEGER references genres(id)
);
CREATE TABLE IF NOT EXISTS users  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar(40),
    email varchar (100),
    login varchar(200) NOT NULL,
    birthday date NOT NULL
);
CREATE TABLE IF NOT EXISTS likes  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL references users(id),
    film_id INTEGER NOT NULL references films(id)
);

CREATE TABLE IF NOT EXISTS friends  (
    id INTEGER GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    user_id INTEGER NOT NULL references users(id),
    friend_id INTEGER NOT NULL references users(id),
    status varchar not null
);

