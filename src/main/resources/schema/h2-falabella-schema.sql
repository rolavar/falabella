drop table BEER if exists;
create table BEER (ID integer identity primary key, NAME varchar(255), BREWERY varchar(255), COUNTRY varchar(255), price decimal);
