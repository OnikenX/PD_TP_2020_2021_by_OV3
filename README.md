# PD_TP_2020_2021_by_OV3
Trabalho Pratico de PD 2020/2021 feito por @OnikenX @360nobuggs e @joaovalente99

[Apontamentos e notas durante a escrita do projecto](./notas/NotasUteis.md)

[enunciado](./notas/PD-2020-21-enunciado-TP-fase1-v2.pdf)

# Aula de File-REST-API

Este foi um projecto feito especialmente para a aula 8 de PD pratica.

## Instruções para correr

1. Correr no IDE, sem esquecer que como argumento tem de meter num path o path do Files-test.
2. Para fazer login enviar as creedenciais de login assim:
```bash
curl -i -X POST -H "content-type: application/json" -d '{"username":"onikenx","password":"pass"}' localhost:8080/user/login
```
3. Depois para ir buscar um ficheiro correr:
```bash
curl -i -H "authorization: <token_recebido>" localhost:8080/download/a
```
