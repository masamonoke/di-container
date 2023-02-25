create table kindergarten.public.child (
    id serial not null,
    firstName varchar(30),
    lastName varchar(30),
    groupNumber int
);

insert into kindergarten.public.child (firstName, lastName, groupNumber) values ('Aria', 'Williams', 4);

insert into kindergarten.public.child (firstName, lastName, groupNumber) values ('Charlotte', 'Davis', 4);

insert into kindergarten.public.child (firstName, lastName, groupNumber) values ('Arlo', 'Hall', 3);

create table kindergarten.public.educator (
    id serial not null,
    firstName varchar(30),
    lastName varchar(30),
    groupNumber int,
    rating float
);

insert into kindergarten.public.educator (firstName, lastName, groupNumber, rating)
    values ('Ethan', 'Wright', 4, 2.965259074699209);

insert into kindergarten.public.educator (firstName, lastName, groupNumber, rating)
values ('Louie', 'Baker', 8, 9.530936590496955);

insert into kindergarten.public.educator (firstName, lastName, groupNumber, rating)
values ('Edward', 'Miller', 1, 4.600386884804303);