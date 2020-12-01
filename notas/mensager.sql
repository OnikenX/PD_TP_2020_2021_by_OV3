SET GLOBAL time_zone = '+0:00'; /* Necessario porque temos uma coluna do tipo timestamp/date/datetime */

CREATE DATABASE IF NOT EXISTS messager_db;
USE messager_db;

DROP TABLE IF EXISTS mensagens;
DROP TABLE IF EXISTS canais;
DROP TABLE IF EXISTS utilizadores;
drop table if exists ficheiros;


create table ficheiros
(
    id   int  not null auto_increment unique,
    path text not null,
    PRIMARY KEY (id)
);
drop table utilizadores;
drop table canais;
drop table mensagens;
create table utilizadores
(
    id           int         not null auto_increment unique,
    username     varchar(50) not null unique,
    hash         text        not null,
    profilepicId int         not null default 1,
    PRIMARY KEY (id),
    FOREIGN KEY (profilepicId) REFERENCES ficheiros (id)
);

create table canais
(
    id          int  not null auto_increment unique,
    nome        text not null,
    descricao   text,
    password    text not null,
    moderadorId int  not null,
    PRIMARY KEY (id),
    foreign key (moderadorId) references utilizadores (id)
);

drop table mensagens;
create table mensagens
(
    id            int                                 not null auto_increment unique,
    dataHoraEnvio timestamp DEFAULT CURRENT_TIMESTAMP not null,
    authorId      int                                 not null,
    canalId       int                                 not null,
    mensagem    text not null ,
        isAFile bool not null ,
    PRIMARY KEY (id),
    FOREIGN KEY (authorId) REFERENCES utilizadores (id),
    foreign key (canalId) references canais (id)

);

CREATE USER IF NOT EXISTS `server`@`localhost` IDENTIFIED BY 'W-pass-123';
GRANT SELECT, INSERT, UPDATE, DELETE ON * TO `server`@`localhost`;

FLUSH PRIVILEGES;

COMMIT;