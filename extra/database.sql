use devsu;

CREATE TABLE person (
    documentType varchar(1),
    document     varchar(20),
    firstName     varchar(18) not null,   
    middleName    varchar(18),
    firstSurname  varchar(18) not null, 
    secondSurname varchar(18),
    gender        varchar(1) not null,
    direction     varchar(250) not null,
    internationaCallingCode varchar(6),
    phoneNumber   varchar(15), 
    primary key(documentType, document)
);

CREATE TABLE agency(
    agencyId smallint unsigned AUTO_INCREMENT,
    agencyName varchar(100) not null,
    direction varchar(200),
    primary key (agencyId)
);

CREATE TABLE employee(
    documentType varchar(1),
    document     varchar(20),
    employeeCode int unsigned AUTO_INCREMENT,
    currentAgency smallint unsigned not null,
    employeeStatus bit not null,
    creationDate datetime,
    foreign key (currentAgency) references agency(agencyId),
    foreign key (documentType, document) references person(documentType, document),
    unique (employeeCode),
    primary key(documentType, document)
);

CREATE TABLE customer(
    documentType varchar(1),
    document     varchar(20),
    customerCode bigint unsigned AUTO_INCREMENT,
    passwordCustomer varchar(300) not null,
    customerStatus varchar(1) not  null,
    creationEmployee int unsigned,
    creationDate datetime,
    lastUpdateEmployee int unsigned,
    lastUpdateDate datetime,
    foreign key (documentType, document) references person(documentType, document),
    unique (customerCode),
    primary key(documentType, document),
    foreign key (creationEmployee) references employee(employeeCode)
);

CREATE TABLE account(
    creationAgency smallint unsigned not null,
    correlative    int unsigned not null,
    accountType    tinyint not null,
    formatedIdentifier varchar(13),
    openingBalance  decimal(13, 2) not null,
    currentBalance decimal(13, 2) not null,
    accountStatus varchar(1) not null,
    openingDate datetime not null,
    openingEmployee int unsigned,
    dailyLimit decimal(13, 2) not null,
    currency varchar(3) not null,
    customerCode bigint unsigned,
    lastUpdateEmployee int unsigned,
    lastUpdateDate datetime,
    primary key (creationAgency, correlative, accountType),
    foreign key (customerCode) references customer(customerCode),
    foreign key (openingEmployee) references employee(employeeCode)
);


create TABLE movement(
    movementId bigint unsigned AUTO_INCREMENT not null,
    creationAgency smallint unsigned not null,
    correlative    int unsigned not null,
    accountType    tinyint not null,
    applicationDate datetime not null,
    movementType bit not null,
    movementDescription varchar(255),
    amount decimal(13, 2) not null,
    balance decimal(13, 2) not null,
    reverseMovement bigint unsigned,
    movementStatus varchar(1) not null,
    primary key (movementId),
    foreign key (creationAgency, correlative, accountType) references 
        account(creationAgency, correlative, accountType)
);

alter table movement add constraint `fk_reverseMovement` foreign key
    (reverseMovement) references movement(movementId);

alter table customer add column lastUpdateEmployee int unsigned;
alter table customer add column lastUpdateDate datetime;
alter table account add column lastUpdateEmployee int unsigned;
alter table account add column lastUpdateDate datetime;
alter table movement add column movementStatus varchar(1);
