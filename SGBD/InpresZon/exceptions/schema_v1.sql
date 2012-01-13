drop table errlog;

drop table client;

create table errlog (
    created_on date,
    errcode integer,
    errmsg varchar2(4000),    
    created_by varchar2(100)
);

create table client (
   nom varchar2(50),
   prenom varchar2(50),
   naissance date not null,
   constraint client_pk primary key (nom, prenom)
);
