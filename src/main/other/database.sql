CREATE TABLE Roles (
	ID    SERIAL PRIMARY KEY,
	Title TEXT
);

CREATE TABLE Users (
	ID           SERIAL PRIMARY KEY,
	FirstName    TEXT NOT NULL,
	LastName     TEXT,
	Phone        TEXT NOT NULL,
	UserLogin    TEXT NOT NULL UNIQUE,
	UserPassword TEXT NOT NULL,
	RoleID       INT REFERENCES Roles(ID)
);

CREATE TABLE Foods (
	ID          SERIAL PRIMARY KEY,
	Title       TEXT NOT NULL,
	Description TEXT NOT NULL,
	Price       FLOAT NOT NULL,
	ImageURL    TEXT NOT NULL
);

CREATE TABLE Orders (
	ID                SERIAL PRIMARY KEY,
	OrderPublicID     TEXT NOT NULL UNIQUE,
	Name              TEXT,
	Phone             TEXT NOT NULL,
	Email             TEXT,
	deliveryAddress   TEXT NOT NULL,
	apartmentEntrance INT,
	apartmentNumber   INT NOT NULL,
	paymentType       TEXT NOT NULL

);

CREATE TABLE OrderCarts(
	ID            SERIAL PRIMARY KEY,
	OrderPublicID TEXT NOT NULL,
	FoodID        INT REFERENCES Foods(ID)
);

INSERT INTO Roles (Title) VALUES
('Администратор'),
('Сотрудник');

INSERT INTO Foods (Title, Description, Price, ImageUrl) VALUES
('Филадельфия', 'Сыр Филадельфия, лосось, авокадо, огурец', 470.0, 'https://316024.selcdn.ru/wiget/5081cf73-0cf2-11e4-badc-001b21b8a590/01e387f2-a61c-46bc-8e07-5fac29ca991a_Large_.jpg'),
('Филадельфия Лайт', 'Лосось, огурец, сливочный сыр', 450.0, 'https://316024.selcdn.ru/wiget/5081cf73-0cf2-11e4-badc-001b21b8a590/13bf5ab2-d5a4-4b91-8ff2-a5c5dbc6efb6_Large_.jpg');