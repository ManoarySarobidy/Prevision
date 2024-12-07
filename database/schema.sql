/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  sarobidy
 * Created: Nov 30, 2023
 */

drop database if exists prevision;
create database prevision;

\c prevision;

create sequence s_salle start with 1 increment by 1;
create table salle(
    id varchar(7) not null primary key,
    nom varchar(15) not null,
    ref varchar(7)
);

create sequence s_secteur start with 1 increment by 1;
create table secteur(
    id varchar(7) not null primary key,
    nom varchar(15) not null,
    ref varchar(7)
);

create table typeSource(
    ref integer not null unique,
    type varchar(20)
);

create sequence s_source start with 1 increment by 1;
create table sources(
    idSource varchar(7) not null primary key,
    reference varchar(20) not null,
    puissance double precision,
    type integer not null,
    foreign key(type) references typeSource(ref)
);

create sequence s_pointage start with 1 increment by 1;
create table pointage(
    
    idPointage varchar(7) not null primary key,
    dateJour date
);

-- create sequence s_detail_pointage start with 1 increment by 1;
create table detail_pointage(
    idDetail serial primary key,
    pointage varchar(7) not null,
    salle varchar(7) not null,
    temp timestamp,
    effectif integer,
    foreign key(pointage) references pointage(idPointage),
    foreign key(salle) references salle(id)

);

create table a_salle_secteur(
    id serial primary key,
    secteur varchar(7) not null,
    salle varchar(7) not null,
    foreign key(secteur) references secteur(id),
    foreign key(salle) references salle(id)
);

create table coupure(
    id serial primary key,
    secteur varchar(7) not null,
    temps timestamp,
    foreign key( secteur ) references secteur(id)
);

create table luminosite(
    id serial primary key,
    timerep integer,
    temp timestamp,
    valeur integer
);

create table a_source_secteur(
    id serial primary key,
    id_source varchar(7) not null,
    id_secteur varchar(7) not null,
    foreign key(id_source) references sources(idSource),
    foreign key(id_secteur) references secteur(id)
);

-- View section
create or replace view salle_secteur 
    as
    select
        s.id as id_secteur,
        s.nom as nom_secteur,
        s.ref as ref_secteur,
        sa.id as id_salle,
        sa.nom as nom_salle,
        sa.ref as ref_salle
     from a_salle_secteur as a_s_s
     join secteur as s
     on a_s_s.secteur = s.id
     join salle as sa
     on a_s_s.salle = sa.id;

create or replace view v_pointages
    as 
        select
            p.dateJour as date,
            d_p.idDetail,
            d_p.pointage as id_pointage,
            d_p.salle,
            d_p.temp,
            d_p.effectif
         from pointage as p
         join detail_pointage as d_p
            on p.idPointage = d_p.pointage;

-- Afaka reliena ny salle sy ny secteur

create or replace view v_pointages_secteur_salle
    as
        select
            v_p.date,
            v_p.iddetail,
            v_p.id_pointage,
            v_p.temp,
            v_p.effectif,
            s_s.*
        from v_pointages as v_p
        join salle_secteur as s_s
            on v_p.salle = s_s.id_salle;

create or replace view v_pointage_matin
    as
    select
        *
    from v_pointages where extract( hour from temp )  <= 12;


create or replace view v_pointage_apres
    as
    select
        *
    from v_pointages where extract( hour from temp ) > 12;

create or replace view v_secteur_source
    as
    select
        s.*,
        src.*
    from a_source_secteur as a_s_s
    join secteur as s on a_s_s.id_secteur = s.id
    join sources as src on a_s_s.id_source = src.idSource;

create or replace view source
    as
        select
            s.*,
            ts.type as name
         from
            typesource as ts
         join sources as s
         on  ts.ref = s.type;

create or replace view coupures
    as 
        select
            c.*, DATE(temps)
        from coupure as c;

create or replace view meteo
    as
        select
            *, DATE(temp)
         from luminosite;

create table pointage_2(
    idpointage varchar(7) not null primary key,
    salle varchar(7) not null,
    temps timestamp,
    effectif integer,
    foreign key( salle ) references salle(id)
);


create or replace view v_pointages_secteur_salle_2
    as
        select
            p_2.idpointage, p_2.temps as temp, p_2.effectif, DATE(temps),
            s_s.*
         from pointage_2 as p_2
         join salle_secteur as s_s
               on p_2.salle = s_s.id_salle;