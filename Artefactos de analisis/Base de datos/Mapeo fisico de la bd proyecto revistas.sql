CREATE SCHEMA `proyecto_Revistas`;
CREATE USER usuarioRevistas IDENTIFIED BY '58650//813LaMg';
GRANT ALL PRIVILEGES ON proyecto_Revistas.* TO usuarioRevistas;
USE `proyecto_Revistas`;


CREATE TABLE `usuario` (
`nombre_de_usuario` VARCHAR(100) NOT NULL,
`password` VARCHAR(100) NOT NULL,
`Rol` VARCHAR(45) NOT NULL,
PRIMARY KEY (`nombre_de_usuario`));

CREATE TABLE `categoria` (
`nombre_de_categoria` VARCHAR(100) NOT NULL,
PRIMARY KEY (`nombre_de_categoria`));

CREATE TABLE `revista` (
`nombre_de_revista` VARCHAR(100) NOT NULL,
`nombre_de_usuario_creador` VARCHAR(100) NOT NULL,
`categoria` VARCHAR(100) NOT NULL,
`contenido` LONGBLOB NOT NULL,
`fecha_de_publicacion` DATETIME NOT NULL,
`estado_de_suscripciones` VARCHAR(45) NOT NULL,
`estado_de_comentarios` VARCHAR(45) NOT NULL,
`estado_de_likes` VARCHAR(45) NOT NULL,
PRIMARY KEY (`nombre_de_revista`, `nombre_de_usuario_creador`),
FOREIGN KEY (`nombre_de_usuario_creador`)
REFERENCES `usuario` (`nombre_de_usuario`)
ON DELETE CASCADE
ON UPDATE CASCADE,
FOREIGN KEY (`categoria`)
REFERENCES `categoria` (`nombre_de_categoria`)
ON DELETE NO ACTION
ON UPDATE CASCADE);
    
CREATE TABLE `edicion` (
`numero_de_edicion` INT NOT NULL,
`nombre_de_revista` VARCHAR(100) NOT NULL,
`nombre_de_usuario_creador` VARCHAR(100) NOT NULL,
PRIMARY KEY (`numero_de_edicion`, `nombre_de_revista`, `nombre_de_usuario_creador`),
FOREIGN KEY (`nombre_de_revista` , `nombre_de_usuario_creador`)
REFERENCES `revista` (`nombre_de_revista` , `nombre_de_usuario_creador`)
ON DELETE CASCADE
ON UPDATE CASCADE);

CREATE TABLE `perfil` (
`nombre_de_usuario` VARCHAR(100) NOT NULL,
`foto` LONGBLOB NOT NULL,
`descripcion` LONGTEXT NOT NULL,
`hobbies` LONGTEXT NOT NULL,
PRIMARY KEY (`nombre_de_usuario`),
FOREIGN KEY (`nombre_de_usuario`)
REFERENCES `usuario` (`nombre_de_usuario`)
ON DELETE CASCADE
ON UPDATE CASCADE);

CREATE TABLE `comentario` (
`nombre_de_revista` VARCHAR(100) NOT NULL,
`revista_nombre_de_usuario_creador` VARCHAR(100) NOT NULL,
`fecha_de_comentario` DATETIME NOT NULL,
`contenido_de_comentario` LONGTEXT NOT NULL,
PRIMARY KEY (`nombre_de_revista`, `revista_nombre_de_usuario_creador`, `fecha_de_comentario`),
FOREIGN KEY (`nombre_de_revista` , `revista_nombre_de_usuario_creador`)
REFERENCES `revista` (`nombre_de_revista` , `nombre_de_usuario_creador`)
ON DELETE CASCADE
ON UPDATE CASCADE);

CREATE TABLE `suscripcion` (
`nombre_de_revista` VARCHAR(100) NOT NULL,
`nombre_de_usuario_creador` VARCHAR(100) NOT NULL,
`usuario_nombre_de_usuario` VARCHAR(100) NOT NULL,
`fecha_de_suscripcion` DATETIME NOT NULL,
PRIMARY KEY (`nombre_de_revista`, `nombre_de_usuario_creador`, `usuario_nombre_de_usuario`, `fecha_de_suscripcion`),
FOREIGN KEY (`nombre_de_revista` , `nombre_de_usuario_creador`)
REFERENCES `revista` (`nombre_de_revista` , `nombre_de_usuario_creador`)
ON DELETE NO ACTION
ON UPDATE NO ACTION,
FOREIGN KEY (`usuario_nombre_de_usuario`)
REFERENCES `usuario` (`nombre_de_usuario`)
ON DELETE CASCADE
ON UPDATE CASCADE);

CREATE TABLE `like` (
`nombre_de_revista` VARCHAR(100) NOT NULL,
`nombre_de_usuario_creador` VARCHAR(100) NOT NULL,
`fecha_de_like` DATETIME NOT NULL,
PRIMARY KEY (`nombre_de_revista`, `nombre_de_usuario_creador`, `fecha_de_like`),
FOREIGN KEY (`nombre_de_revista` , `nombre_de_usuario_creador`)
REFERENCES `revista` (`nombre_de_revista` , `nombre_de_usuario_creador`)
ON DELETE CASCADE
ON UPDATE CASCADE);

CREATE TABLE `tag`(
`nombre_tag` VARCHAR(100) NOT NULL,
PRIMARY KEY(`nombre_tag`)
);

CREATE TABLE `tag_usuario`(
`nombre_tag` VARCHAR(100) NOT NULL,
`nombre_de_usuario` VARCHAR(100) NOT NULL,
PRIMARY KEY(`nombre_tag`, `nombre_de_usuario`),
FOREIGN KEY(`nombre_tag`) REFERENCES `tag`(`nombre_tag`) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(`nombre_de_usuario`) REFERENCES `usuario`(`nombre_de_usuario`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `tag_revista`(
`nombre_tag` VARCHAR(100) NOT NULL,
`nombre_de_revista` VARCHAR(100) NOT NULL,
`nombre_de_usuario_creador` VARCHAR(100) NOT NULL,
PRIMARY KEY(`nombre_tag`, `nombre_de_revista`, `nombre_de_usuario_creador`),
FOREIGN KEY(`nombre_tag`) REFERENCES `tag`(`nombre_tag`) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(`nombre_de_revista`, `nombre_de_usuario_creador`) REFERENCES `revista`(`nombre_de_revista`, `nombre_de_usuario_creador`) ON DELETE CASCADE ON UPDATE CASCADE
);