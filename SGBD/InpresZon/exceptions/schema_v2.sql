drop table errlog;
drop table client;
drop table my_constraints;

create table errlog (
    created_on date,
    errcode integer,
    errmsg varchar2(4000),    
    created_by varchar2(100)
);

create table my_constraints (
	name varchar2(100) constraint my_constraints_pk primary key,
	message varchar2(512)
);

create table client (
   nom varchar2(50),
   prenom varchar2(50),
   naissance date constraint client_naissance_nn not null,
   constraint client_pk primary key (nom, prenom)
);

insert into my_constraints (name, message) values ('client_naissance_nn', 'Il est nécessaire de fournir une date de naissance.');
insert into my_constraints (name, message) values ('client_pk', 'Il existe déjà un client portant ce nom et prénom.');
commit;
