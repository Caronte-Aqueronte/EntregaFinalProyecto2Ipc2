CREATE SCHEMA `proyecto_Revistas`;
CREATE USER usuarioRevistas IDENTIFIED BY '58650//813LaMg';
GRANT ALL PRIVILEGES ON proyecto_Revistas.* TO usuarioRevistas;
USE `proyecto_Revistas`;


CREATE TABLE `usuario` (
`nombre_de_usuario` VARCHAR(100) NOT NULL,
`password` LONGTEXT NOT NULL,
`Rol` VARCHAR(45) NOT NULL,
PRIMARY KEY (`nombre_de_usuario`));

CREATE TABLE `categoria` (
`nombre_de_categoria` VARCHAR(100) NOT NULL,
PRIMARY KEY (`nombre_de_categoria`));

CREATE TABLE `revista` (
`nombre_de_revista` VARCHAR(100) NOT NULL,
`descripcion` LONGTEXT NOT NULL,
`nombre_de_usuario_creador` VARCHAR(100) NOT NULL,
`categoria` VARCHAR(100) NOT NULL,
`contenido` LONGBLOB NOT NULL,
`miniatura` LONGBLOB NOT NULL,
`costo_de_suscripcion` DOUBLE(10,2) NOT NULL,
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

CREATE TABLE `costo_por_dia`(
`nombre_de_revista` VARCHAR(100) NOT NULL,
`nombre_de_usuario_creador` VARCHAR(100) NOT NULL,
`costo_por_dia` DOUBLE(10,2) NOT NULL,
`fecha_de_validez` DATE NOT NULL,
PRIMARY KEY (`nombre_de_revista`, `nombre_de_usuario_creador`,`costo_por_dia` , `fecha_de_validez`),
FOREIGN KEY (`nombre_de_revista` , `nombre_de_usuario_creador`)
REFERENCES `revista` (`nombre_de_revista` , `nombre_de_usuario_creador`)
ON DELETE CASCADE
ON UPDATE CASCADE
);

CREATE TABLE `edicion` (
`numero_de_edicion` INT NOT NULL,
`nombre_de_revista` VARCHAR(100) NOT NULL,
`nombre_de_usuario_creador` VARCHAR(100) NOT NULL,
`contenido` LONGBLOB NOT NULL,
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
`nombre_de_suscriptor` VARCHAR(100) NOT NULL,
`tipo_de_suscripcion` VARCHAR(100) NOT NULL,
`fecha_de_suscripcion` DATE NOT NULL,
PRIMARY KEY (`nombre_de_revista`, `nombre_de_usuario_creador`, `nombre_de_suscriptor`),
FOREIGN KEY (`nombre_de_revista` , `nombre_de_usuario_creador`)
REFERENCES `revista` (`nombre_de_revista` , `nombre_de_usuario_creador`)
ON DELETE NO ACTION
ON UPDATE NO ACTION,
FOREIGN KEY (`nombre_de_suscriptor`)
REFERENCES `usuario` (`nombre_de_usuario`)
ON DELETE CASCADE
ON UPDATE CASCADE);

CREATE TABLE `pago`(
`nombre_de_revista` VARCHAR(100) NOT NULL,
`nombre_de_usuario_creador` VARCHAR(100) NOT NULL,
`nombre_de_suscriptor` VARCHAR(100) NOT NULL,
`dinero_pago` DOUBLE(10,2) NOT NULL,
`fecha_de_pago` DATE NOT NULL,
PRIMARY KEY (`nombre_de_revista`, `nombre_de_usuario_creador`, `nombre_de_suscriptor`, `fecha_de_pago`),
FOREIGN KEY (`nombre_de_revista`, `nombre_de_usuario_creador`, `nombre_de_suscriptor`)
REFERENCES `suscripcion`(`nombre_de_revista`, `nombre_de_usuario_creador`, `nombre_de_suscriptor`)
ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `like` (
`nombre_de_revista` VARCHAR(100) NOT NULL,
`nombre_de_usuario_creador` VARCHAR(100) NOT NULL,
`fecha_de_like` DATE NOT NULL,
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


CREATE TABLE `anunciante`(
`nombre_anunciante` VARCHAR(100) NOT NULL,
PRIMARY KEY (`nombre_anunciante`)
);

CREATE TABLE `anuncio`(
`nombre_anunciante` VARCHAR(100) NOT NULL,
`nombre_anuncio` VARCHAR(100) NOT NULL,
`tipo_anuncio` VARCHAR(100) NOT NULL,
`pago` DOUBLE(10,2) NOT NULL,
`estado` VARCHAR(100) NOT NULL,
`fecha_creacion` DATE NOT NULL,
PRIMARY KEY (`nombre_anunciante`, `nombre_anuncio`),
FOREIGN KEY (`nombre_anunciante`) REFERENCES `anunciante`(`nombre_anunciante`)
ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `anuncio_imagen_texto`(
`nombre_anunciante` VARCHAR(100) NOT NULL,
`nombre_anuncio` VARCHAR(100) NOT NULL,
`imagen` LONGBLOB NOT NULL,
`texto` LONGTEXT NOT NULL,
PRIMARY KEY (`nombre_anunciante`, `nombre_anuncio`),
FOREIGN KEY (`nombre_anunciante`, `nombre_anuncio`) REFERENCES `anuncio`(`nombre_anunciante`, `nombre_anuncio`)
ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `anuncio_texto`(
`nombre_anunciante` VARCHAR(100) NOT NULL,
`nombre_anuncio` VARCHAR(100) NOT NULL,
`texto` LONGTEXT NOT NULL,
PRIMARY KEY (`nombre_anunciante`, `nombre_anuncio`),
FOREIGN KEY (`nombre_anunciante`, `nombre_anuncio`) REFERENCES `anuncio`(`nombre_anunciante`, `nombre_anuncio`)
ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `anuncio_video_texto`(
`nombre_anunciante` VARCHAR(100) NOT NULL,
`nombre_anuncio` VARCHAR(100) NOT NULL,
`link_video` LONGTEXT NOT NULL,
`texto` LONGTEXT NOT NULL,
PRIMARY KEY (`nombre_anunciante`, `nombre_anuncio`),
FOREIGN KEY (`nombre_anunciante`, `nombre_anuncio`) REFERENCES `anuncio`(`nombre_anunciante`, `nombre_anuncio`)
ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `tag_anuncio`(
`nombre_tag` VARCHAR(100) NOT NULL,
`nombre_anunciante` VARCHAR(100) NOT NULL,
`nombre_anuncio` VARCHAR(100) NOT NULL,
PRIMARY KEY(`nombre_tag`, `nombre_anunciante`, `nombre_anuncio`),
FOREIGN KEY(`nombre_tag`) REFERENCES `tag`(`nombre_tag`) ON DELETE CASCADE ON UPDATE CASCADE,
FOREIGN KEY(`nombre_anunciante`, `nombre_anuncio`) REFERENCES `anuncio`(`nombre_anunciante`, `nombre_anuncio`) ON DELETE CASCADE ON UPDATE CASCADE
);

CREATE TABLE `historial_anuncio`(
`codigo` INT NOT NULL AUTO_INCREMENT,
`nombre_anunciante` VARCHAR(100) NOT NULL,
`nombre_anuncio` VARCHAR(100) NOT NULL,
`link_donde_aparecio` LONGTEXT NOT NULL,
`fecha_de_aparicion` DATE NOT NULL,
PRIMARY KEY (`codigo`),
FOREIGN KEY (`nombre_anunciante`, `nombre_anuncio`) REFERENCES `anuncio`(`nombre_anunciante`, `nombre_anuncio`)
ON DELETE CASCADE ON UPDATE CASCADE
);