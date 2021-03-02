use messager_db_1;
insert into utilizadores(username, nome, hash) VALUES ("user", "nome", "password");
insert into utilizadores(username, nome, hash) VALUES ("lol", "nome", "password");

insert into canais(pessoaCria) values(1);
insert into canaisDM(id, pessoaDest) values(2, 3);
delete from canais where id = 4;
insert into canaisGrupo(id, nome, descricao, password) values (1, "canal", "isto e um canal","password");
insert into mensagens(authorId, canalId, isAFile, mensagem) values (1, 1, 0, "ficheiro");
insert into mensagens(authorId, canalId, isAFile, mensagem) values (1, 1, 0, "ficheiro");
insert into mensagens(authorId, canalId, isAFile, mensagem) values (1, 1, 0, "ficheiro");
insert into mensagens(authorId, canalId, isAFile, mensagem) values (1, 1, 0, "ficheiro");
insert into mensagens(authorId, canalId, isAFile, mensagem) values (1, 1, 0, "ficheiro");
insert into mensagens(authorId, canalId, isAFile, mensagem) values (1, 1, 0, "ficheiro");
insert into mensagens(authorId, canalId, isAFile, mensagem) values (1, 2, 0, "ficheiro");
select * from mensagens;
select * from canais;
select * from canaisDM;
select * from canaisGrupo;
select * from utilizadores;
insert into mensagens( authorId, canalId, isAFile, mensagem) values (  3, 5, 0, "lol ok");

SELECT canais.id, pessoaCria, pessoaDest FROM canais INNER JOIN canaisDM cD on canais.id = cD.id where (pessoaDest = 1 and pessoaCria = 1) or (pessoaDest = 1 and pessoaCria = 1) ;

insert into mensagens(authorId, canalId, isAFile, mensagem) values ();

delete from canais where id = 2
;

insert into
# insert into
# delete from utilizadores;
#
# select * from canaisDM where (pessoa1 = 'lol' or pessoa2 = 'lol') and (pessoa1 = 'lmao' or pessoa2 = 'lmao') ;
# SELECT MAX(id) as max FROM mensagens;

