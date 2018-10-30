create table if not exists statistics
(
   amount double not null,
   timestamp bigint not null,
   index (timestamp)
);