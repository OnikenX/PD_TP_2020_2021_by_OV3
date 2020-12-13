use messager_db_1;
insert into utilizadores(username, nome, hash) VALUES ("user", "nome", "password");
insert into canais(pessoaCria) values(1);
insert into canaisGrupo(id, nome, descricao, password) values (1, "canal", "isto e um canal","password");
insert into mensagens(authorId, canalId, isAFile, mensagem) values (1, 1, 0, "ficheiro");
select * from mensagens;
select * from canais;
select * from canaisDM;
select * from canaisGrupo;

delete from canais where id = 1;

insert into
# insert into
# delete from utilizadores;
#
# select * from canaisDM where (pessoa1 = 'lol' or pessoa2 = 'lol') and (pessoa1 = 'lmao' or pessoa2 = 'lmao') ;
# SELECT MAX(id) as max FROM mensagens;

