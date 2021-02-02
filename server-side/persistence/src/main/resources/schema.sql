
	/*
		Define un usuario genérico dentro de la aplicación.

		Implementa un esquema de herencia mediante Shared-Primary Key, lo que
		permite extender los diferentes roles disponibles en el sistema sin
		modificar las propiedades ya adquiridas (e.g., el dueño de un bar
		podría utilizar el sistema de chat/matching simplemente extendiendo su
		perfil).

		Debido a que se aplica el algoritmo Blowfish (a.k.a. 'BCrypt'), para
		encriptar contraseñas, el campo 'password' DEBE poder retener hasta 60
		caracteres.
	*/
	CREATE TABLE IF NOT EXISTS Users (

		id			SERIAL			NOT NULL,
		username	VARCHAR(32)		NOT NULL,
		password	VARCHAR(60)		NOT NULL,
		name		VARCHAR(64)		NOT NULL,
		mail		VARCHAR(254)	NOT NULL,

		CONSTRAINT Users_PK PRIMARY KEY (id),
		CONSTRAINT Username_U UNIQUE (username),
		CONSTRAINT Mail_U UNIQUE (mail)
	);

	/*
		Agregación de un usuario genérico. Se provee imágen de perfil, género y
		descripción, parámetros esenciales para el sistema de matching.
	*/
	CREATE TABLE IF NOT EXISTS Client (

		id			INTEGER			NOT NULL,
		gender		VARCHAR(6)		NOT NULL DEFAULT 'OTHER',
		avatar		BYTEA			NULL,
		content		VARCHAR(16)		NULL,
		bio			VARCHAR(256)	NOT NULL DEFAULT '',

		CHECK (gender IN ('MALE', 'FEMALE', 'OTHER')),

		CONSTRAINT Client_PK PRIMARY KEY (id),
		CONSTRAINT UsersClient_FK FOREIGN KEY (id)
			REFERENCES Users(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);

	/*
		Almacena la información pertinente de un bar/boliche. No se corresponde
		directamente con el objeto Pub del modelo ya que le falta la cantidad
		de gente que indicó que va a asistir. Este dato se consigue mediante un
		JOIN con la tabla Attendance.
	*/
	CREATE TABLE IF NOT EXISTS Pub (

		id			SERIAL	 		NOT NULL,
		userId 		INTEGER 		NOT NULL,
		name		VARCHAR(32)		NOT NULL,
		description	VARCHAR(256)	NOT NULL DEFAULT '',
		openTime	TIME			NOT NULL,
		image		BYTEA			NOT NULL,
		content		VARCHAR(16)		NOT NULL,

		CONSTRAINT Pub_PK PRIMARY KEY (id),
		CONSTRAINT Pub_U UNIQUE (name),
		CONSTRAINT UserPub_FK FOREIGN KEY (userId)
			REFERENCES Users(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);

	/*
		Guarda las asistencias de los usuarios a un determinado boliche, para
		una determinada fecha.
	*/
	CREATE TABLE IF NOT EXISTS Attendance (

		id 			SERIAL 			NOT NULL,
		pubId		INTEGER			NOT NULL,
		userId 		INTEGER 		NOT NULL,
		date		DATE			NOT NULL,

		CONSTRAINT Attendance_PK PRIMARY KEY (id),
		CONSTRAINT Attendance_U UNIQUE (pubId, userId, date),
		CONSTRAINT PubAttendance_FK FOREIGN KEY (pubId)
			REFERENCES Pub(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT,
		CONSTRAINT UserAttendance_FK FOREIGN KEY (userId)
			REFERENCES Users(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);

	/*
		Coordenadas para hallar el bar/boliche en un mapa interactivo.
	*/
	CREATE TABLE IF NOT EXISTS Coordinates (

		id 			SERIAL 			NOT NULL,
		pubId		INTEGER			NOT NULL,
		latitude	NUMERIC(12, 8)	NOT NULL,
		longitude	NUMERIC(12, 8)	NOT NULL,

		CONSTRAINT PubCoordinates_PK PRIMARY KEY (id),
		CONSTRAINT PubCoordinates_U UNIQUE (pubId),
		CONSTRAINT PubCoordinates_FK FOREIGN KEY (pubId)
			REFERENCES Pub(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);

	/*
		Promociones ofrecidas por un bar/boliche.
	*/
	CREATE TABLE IF NOT EXISTS Promo (

		id 				SERIAL 			NOT NULL,
		pubId			INTEGER			NOT NULL,
		name			VARCHAR(32)		NOT NULL,
		description		VARCHAR(128)	NOT NULL,

		CONSTRAINT Promo_PK PRIMARY KEY (id),
		CONSTRAINT PubPromo_FK FOREIGN KEY (pubId)
			REFERENCES Pub(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);

	/*
		Nombre y precio de las bebidas de un bar/boliche.
	*/
	CREATE TABLE IF NOT EXISTS PubDrink (

		id		SERIAL 			NOT NULL,
		pubId	INTEGER			NOT NULL,
		drink	VARCHAR(32)		NOT NULL,
		price	DECIMAL(10, 2)	NOT NULL,

		CONSTRAINT PubDrink_PK PRIMARY KEY (id),
		CONSTRAINT PubDrink_FK FOREIGN KEY (pubId)
			REFERENCES Pub(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);

	/*
		Nombre y precio de las comidas de un bar/boliche.
	*/
	CREATE TABLE IF NOT EXISTS PubFood (

		id 		SERIAL 			NOT NULL,
		pubId	INTEGER			NOT NULL,
		food	VARCHAR(32)		NOT NULL,
		price	DECIMAL(10, 2)	NOT NULL,

		CONSTRAINT PubFood_PK PRIMARY KEY (id),
		CONSTRAINT PubFood_FK FOREIGN KEY (pubId)
			REFERENCES Pub(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);

	/*
		Dirección y precio de la entrada de un bar/boliche. Ambos datos únicos
		por boliche, por lo que el ID es Primary Key.
	*/
	CREATE TABLE IF NOT EXISTS PubInfo (

		id			SERIAL 			NOT NULL,
		pubId		INTEGER			NOT NULL,
		address		VARCHAR(64)		NOT NULL,
		price		DECIMAL(10, 2)	NOT NULL,

		CONSTRAINT PubInfo_PK PRIMARY KEY (id),
		CONSTRAINT PubIndo_U UNIQUE (pubId),
		CONSTRAINT PubInfo_FK FOREIGN KEY (pubId)
			REFERENCES Pub(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);

	/*
		Lleva registro de cuando un usuario le da like a otro.
	*/
	CREATE TABLE IF NOT EXISTS Likes (

		id 				SERIAL 		NOT NULL,
		senderId		INTEGER		NOT NULL,
		receiverId		INTEGER		NOT NULL,
		pubId			INTEGER		NOT NULL,
		date			DATE		NOT NULL,

		CONSTRAINT Likes_PK PRIMARY KEY (id),
		CONSTRAINT Likes_U UNIQUE (senderId, receiverId, pubId, date),
		CONSTRAINT UserLikesFrom_FK FOREIGN KEY (senderId)
			REFERENCES Users(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT,
		CONSTRAINT UserLikesTo_FK FOREIGN KEY (receiverId)
			REFERENCES Users(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT,
		CONSTRAINT PubLikes_FK FOREIGN KEY (pubId)
			REFERENCES Pub(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);

	/*
		Lleva registro de cuando un usuario indica que no quiere establecer un
		chat privado con otro usuario y quiere que no le vuelva a aparecer en
		la lista de matching.
	*/
	CREATE TABLE IF NOT EXISTS Reject (

		id  			SERIAL 		NOT NULL,
		senderId		INTEGER		NOT NULL,
		receiverId		INTEGER		NOT NULL,
		pubId			INTEGER		NOT NULL,
		date			DATE		NOT NULL,

		CONSTRAINT Reject_PK PRIMARY KEY (id),
		CONSTRAINT Reject_U UNIQUE (senderId, receiverId, pubId, date),
		CONSTRAINT UserRejectFrom_FK FOREIGN KEY (senderId)
			REFERENCES Users(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT,
		CONSTRAINT UserRejectTo_FK FOREIGN KEY (receiverId)
			REFERENCES Users(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT,
		CONSTRAINT PubReject_FK FOREIGN KEY (pubId)
			REFERENCES Pub(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);

	/*
		Mensajes públicos de un usuario, dentro del canal de chat de un pub.
	*/
	CREATE TABLE IF NOT EXISTS PubMessage (

		id				SERIAL				NOT NULL,
		userId			INTEGER				NOT NULL,
		pubId			INTEGER				NOT NULL,
		timestamp		TIMESTAMP			NOT NULL,
		date			DATE				NOT NULL,
		message			VARCHAR(1024)		NOT NULL,

		CONSTRAINT PubMessage_PK PRIMARY KEY (id),
		CONSTRAINT UsersPubMessage_FK FOREIGN KEY (userId)
			REFERENCES Users(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT,
		CONSTRAINT PubPubMessage_FK FOREIGN KEY (pubId)
			REFERENCES Pub(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);

	/*
		Mensajes privados entre usuarios.
	*/
	CREATE TABLE IF NOT EXISTS UserMessage (

		id				SERIAL				NOT NULL,
		senderId		INTEGER				NOT NULL,
		recipientId		INTEGER				NOT NULL,
		timestamp		TIMESTAMP			NOT NULL,
		message			VARCHAR(1024)		NOT NULL,

		CONSTRAINT UserMessage_PK PRIMARY KEY (id),
		CONSTRAINT SenderUserMessage_FK FOREIGN KEY (senderId)
			REFERENCES Users(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT,
		CONSTRAINT RecipientUserMessage_FK FOREIGN KEY (recipientId)
			REFERENCES Users(id)
				ON DELETE CASCADE
				ON UPDATE RESTRICT
	);
