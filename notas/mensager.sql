SET GLOBAL time_zone = '+0:00'; /* Necessario porque temos uma coluna do tipo timestamp/date/datetime */

drop database if exists messager_db_1;
CREATE DATABASE IF NOT EXISTS messager_db_1;
USE messager_db_1;

#reset das tables
DROP TABLE IF EXISTS mensagens;
DROP TABLE IF EXISTS canaisDM;
DROP TABLE IF EXISTS canaisGrupo;
DROP TABLE IF EXISTS utilizadores;
DROP TABLE IF EXISTS canais;


create table if not exists canais
(
    id int not NULL unique auto_increment,
    nome        text not null,
    descricao   text,
    primary key (id)
);


create table if not exists utilizadores
(
    id       int         not null auto_increment unique,
    username varchar(64) not null unique,
    nome     text        not null,
    hash     text        not null,
    PRIMARY KEY (id)
);

create table if not exists canaisGrupo
(
    canal_id    int not null,
    password    text not null,
    moderadorId int  not null,
    foreign key (canal_id) references canais (id),
    foreign key (moderadorId) references utilizadores (id)
);

create table if not exists canaisDM
(
    canal_id    int not null,
    pessoa1   int  not null,
    pessoa2   int  not null,
    foreign key (canal_id) references canais (id),
    foreign key (pessoa1) references utilizadores (id),
    foreign key (pessoa2) references utilizadores (id)
);

create table if not exists mensagens
(
    id            int                                 not null auto_increment unique,
    dataHoraEnvio timestamp DEFAULT CURRENT_TIMESTAMP not null,
    authorId      int                                 not null,
    canalId       int                                 not null,
    isAFile       bool                                not null,
    mensagem      varchar(255),
    PRIMARY KEY (id),
    FOREIGN KEY (authorId) REFERENCES utilizadores (id),
    foreign key (canalId) references canais (id)
);

CREATE USER IF NOT EXISTS `server_1`@`localhost` IDENTIFIED BY 'W-pass-123';
GRANT SELECT, INSERT, UPDATE, DELETE ON * TO `server_1`@`localhost`;

FLUSH PRIVILEGES;

COMMIT;

use messager_db_0;
DROP TABLE IF EXISTS mensagens;