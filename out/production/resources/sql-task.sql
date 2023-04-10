--Вывести к каждому самолету класс обслуживания и количество мест этого класса

select aircrafts_data.aircraft_code as aircraft, fare_conditions as type, count(seat_no) as seats
from aircrafts_data
         inner join seats on seats.aircraft_code = aircrafts_data.aircraft_code
group by aircraft, type
order by aircraft;

--Найти 3 самых вместительных самолета (модель + кол-во мест)
-- select model, seat_no from aircrafts_data
-- inner join seats on seats.aircraft_code = aircrafts_data.aircraft_code

select model, count(seat_no) as seats
from aircrafts_data
         inner join seats on seats.aircraft_code = aircrafts_data.aircraft_code
where aircrafts_data.aircraft_code IN (select seats.aircraft_code as code
                                       from seats
                                       group by code
                                       order by count(seat_no) desc
                                       limit 3)
group by model
order by count(seat_no) desc;

--Вывести код,модель самолета и места не эконом класса для самолета
-- 'Аэробус A321-200' с сортировкой по местам

select aircrafts_data.aircraft_code, model, seat_no
from aircrafts_data
         inner join seats on seats.aircraft_code = aircrafts_data.aircraft_code
where model ->> 'ru' = 'Аэробус A321-200'
  and fare_conditions <> 'Economy';

--Вывести города в которых больше 1 аэропорта ( код аэропорта, аэропорт, город)

select airport_code, airport_name, city
from airports_data
where city in (select city
               from airports_data
               group by city
               having count(airport_name) > 1);

-- Найти ближайший вылетающий рейс из Екатеринбурга в Москву,
-- на который еще не завершилась регистрация

select flight_no
from flights
where flight_id = (select flight_id
                   from flights
                            inner join airports_data on airports_data.airport_code = flights.arrival_airport
                   where arrival_airport in (select airport_code
                                             from airports_data
                                             where city ->> 'ru' = 'Екатеринбург')
                     and departure_airport in (select airport_code
                                               from airports_data
                                               where city ->> 'ru' = 'Москва')
                     and status in ('Scheduled', 'On Time', 'Delayed')
                   order by scheduled_departure desc
                   limit 1);

--Вывести самый дешевый и дорогой билет и стоимость ( в одном результирующем ответе)
select ticket_no, tickets.book_ref, passenger_id, passenger_name, contact_data, total_amount
from tickets
         inner join bookings on bookings.book_ref = tickets.book_ref
where tickets.book_ref in ((select book_ref
                            from bookings
                            order by total_amount desc
                            limit 1),
                           (select book_ref
                            from bookings
                            order by total_amount asc
                            limit 1));

-- Написать DDL таблицы Customers , должны быть поля id , firstName, LastName, email , phone.
-- Добавить ограничения на поля ( constraints) .

CREATE SCHEMA IF NOT EXISTS test;
CREATE TABLE IF NOT EXISTS test.Customers
(
    id           SERIAL PRIMARY KEY NOT NULL,
    first_name   VARCHAR(16)        NOT NULL,
    last_name    VARCHAR(24)        NOT NULL,
    email        VARCHAR            NOT NULL,
    phone_number VARCHAR(16)        NOT NULL
);

--  Написать DDL таблицы Orders , должен быть id, customerId,	quantity.
--  Должен быть внешний ключ на таблицу customers + ограничения

CREATE SCHEMA IF NOT EXISTS test;
CREATE TABLE IF NOT EXISTS test.Orders
(
    id          SERIAL PRIMARY KEY NOT NULL,
    customer_id INT                NOT NULL,
    quantity    INT                NOT NULL,
    FOREIGN KEY (customer_id) REFERENCES test.Customers (id)
        ON DELETE CASCADE ON UPDATE CASCADE
);

-- Написать 5 insert в эти таблицы

INSERT INTO test.Customers (first_name, last_name, email, phone_number)
VALUES ('Alexey', 'Vedger', 'aved@mail.ru', '+375335810571'),
       ('Maria', 'Fredosova', 'krak81test@gmail.com', '+37529101956'),
       ('Mark', 'Edingerd', 'edandmark@yahoo.com', '+3753310581543'),
       ('Halina', 'Tetskova', 'workhalte@gmail.com', '+375293451018'),
       ('Eva', 'Grinbergot', 'eevjoke@gmail.com', '++375440312385');

INSERT INTO test.Orders (customer_id, quantity)
VALUES (1, 54),
       (2, 2),
       (3, 242),
       (4, 430),
       (5, 0);

-- удалить таблицы

DROP TABLE IF EXISTS test.Orders;
DROP TABLE IF EXISTS test.Customers;

-- Написать свой кастомный запрос ( rus + sql)
-- Кастоманый запрос - вывести список имен и фамилий покупателей, которые приобрели более 300 товаров

select first_name, last_name, orders.id as order_id, quantity from test.customers
inner join test.orders on orders.id = customers.id
where quantity >= 300;



