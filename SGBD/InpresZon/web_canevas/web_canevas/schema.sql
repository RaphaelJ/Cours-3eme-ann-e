-----------------------------------------------------------
-- TABLE IMAGE. NECESSAIRE POUR COMPILER PROCESS_DOWNLOAD
-----------------------------------------------------------
drop table images purge;
create table images (
    id number primary key,
    name varchar2(128) unique not null,
    mime_type varchar2(128),
    image blob default empty_blob()
);

-----------------------------------------------------------
-- LOG
-----------------------------------------------------------
drop table log;

create table log (
    id number,
    message varchar2(4000),
    errcode integer default 0,
    created_on date default sysdate,
    created_by varchar2 (100) default user,
    constraint pk_log primary key (id)
);

drop sequence seq_log;

create sequence seq_log;

-----------------------------------------------------------
-- SESSION & CLIENT
-----------------------------------------------------------
drop table sessions;

drop table client;

create table client (
	id number,
	nom varchar2(50),
	prenom varchar2(50),
	mdp varchar2(8) not null,
	constraint client_pk primary key (id)
);

create table sessions (
	id varchar2(32),
	client number,
	last date,
	constraint sessions_pk primary key (id),
	constraint sessions_client_fk foreign key (client) references client (id)
);

insert into client values (0, 'Einstein', 'Albert', 'toto');
commit;

-----------------------------------------------------------
-- CONTRAINTES
-----------------------------------------------------------
drop table my_constraints;

create table my_constraints (
	name varchar2(100) constraint my_constraints_pk primary key,
	message varchar2(512)
);

--insert into my_constraints (name, message) values ('client_naissance_nn', 'Il est nécessaire de fournir une date de naissance.');
--insert into my_constraints (name, message) values ('client_pk', 'Il existe déjà un client portant ce nom et prénom.');
--commit;
