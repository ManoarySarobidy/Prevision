/* 
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Other/SQLTemplate.sql to edit this template
 */
/**
 * Author:  sarobidy
 * Created: Nov 30, 2023
 */

insert into salle values ( CONCAT( 'SAL', nextval( 's_salle' ) ), 'Salle 1', 'S1' );
insert into salle values ( CONCAT( 'SAL', nextval( 's_salle' ) ), 'Salle 2', 'S2' );
insert into salle values ( CONCAT( 'SAL', nextval( 's_salle' ) ), 'Salle 3', 'S3' );
insert into salle values ( CONCAT( 'SAL', nextval( 's_salle' ) ), 'Salle 4', 'S4' );
insert into salle values ( CONCAT( 'SAL', nextval( 's_salle' ) ), 'Salle 5', 'S5' );
insert into salle values ( CONCAT( 'SAL', nextval( 's_salle' ) ), 'Salle 6', 'S6' );


insert into secteur values ( CONCAT( 'SCT', nextval( 's_secteur' ) ), 'Secteur 1', 'ST1'  );
insert into secteur values ( CONCAT( 'SCT', nextval( 's_secteur' ) ), 'Secteur 2', 'ST2'  );
insert into secteur values ( CONCAT( 'SCT', nextval( 's_secteur' ) ), 'Secteur 3', 'ST3'  );

insert into typeSource values ( 10 , 'Batterie' );
insert into typeSource values ( 20 , 'Panneau Solaire' );

insert into sources values ( CONCAT('SRC', nextval('s_source')), 'SC1' , 15000, 20 );
insert into sources values ( CONCAT('SRC', nextval('s_source')), 'SC2' , 40000, 10 );

insert into a_salle_secteur values ( default, 'SCT1' , 'SAL1' );
insert into a_salle_secteur values ( default, 'SCT1' , 'SAL2' );
insert into a_salle_secteur values ( default, 'SCT2' , 'SAL3' );
insert into a_salle_secteur values ( default, 'SCT2' , 'SAL4' );
insert into a_salle_secteur values ( default, 'SCT3' , 'SAL5' );
insert into a_salle_secteur values ( default, 'SCT3' , 'SAL6' );

insert into luminosite values (default, 8, '2023-01-05 08:00:00', 9);
insert into luminosite values (default, 9, '2023-01-05 09:00:00', 5);
insert into luminosite values (default, 10, '2023-01-05 10:00:00', 4);
insert into luminosite values (default, 11, '2023-01-05 11:00:00', 8);
insert into luminosite values (default, 12, '2023-01-05 12:00:00', 3);
insert into luminosite values (default, 13, '2023-01-05 13:00:00', 10);
insert into luminosite values (default, 14, '2023-01-05 14:00:00', 10);
insert into luminosite values (default, 15, '2023-01-05 15:00:00', 7);
insert into luminosite values (default, 16, '2023-01-05 16:00:00', 6);
insert into luminosite values (default, 17, '2023-01-05 17:00:00', 8);

insert into coupure values (default, 'SCT1', '2023-01-05 12:56:00');

insert into pointage values ( CONCAT( 'PNT', nextval( 's_pointage' ) ) , '2023-01-05' );

insert into detail_pointage values ( default, 'PNT1' ,'SAL1', '2023-01-05 11:59:00', 150);
insert into detail_pointage values ( default, 'PNT1' ,'SAL1', '2023-01-05 17:59:00', 100);

insert into a_source_secteur values (default, 'SRC1', 'SCT1');
insert into a_source_secteur values (default, 'SRC2', 'SCT1');



-- Insert into data test dylan

insert into sources values('SRC10' , 'SC3' , 40000, 20);
insert into sources values('SRC11' , 'SC3' , 30000, 10);

insert into a_source_secteur values ( default, 'SRC10' , 'SCT3' );
insert into a_source_secteur values ( default, 'SRC11' , 'SCT3' );


insert into luminosite values (default, 8, '2023-10-04 08:00:00', 5);
insert into luminosite values (default, 9, '2023-10-04 09:00:00', 4);
insert into luminosite values (default, 10, '2023-10-04 10:00:00', 8);
insert into luminosite values (default, 11, '2023-10-04 11:00:00', 8);
insert into luminosite values (default, 12, '2023-10-04 12:00:00', 9);
insert into luminosite values (default, 13, '2023-10-04 13:00:00', 7);
insert into luminosite values (default, 14, '2023-10-04 14:00:00', 3);
insert into luminosite values (default, 15, '2023-10-04 15:00:00', 7);
insert into luminosite values (default, 16, '2023-10-04 16:00:00', 6);
insert into luminosite values (default, 17, '2023-10-04 17:00:00', 5);



insert into coupure values (default, 'SCT3', '2023-10-04 14:36:00');

delete from pointage_2 where idpointage = 'POIN500';
delete from pointage_2 where idpointage = 'POIN501';

INSERT INTO pointage_2 (idpointage, salle, temps, effectif) values ( 'POIN500' , 'SAL6', '2023-10-04 08:00:00', 450 );
INSERT INTO pointage_2 (idpointage, salle, temps, effectif) values ( 'POIN501' , 'SAL6', '2023-10-04 12:00:00', 430 );