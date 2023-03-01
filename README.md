# Tickets - Clevertec
### Используемый стек
Java 17, Gradle 7.5, PostgreSQL 15.1.1, JUnit5, Паттерны [Builder, Singleton]
### Инструкция по запуску
- Запустить TicketRunner.class
- Ввести данные для формирования чека в формате X-Y X-Y card-Z, где X - id товаров, Y - их количество, Z - номер карты покупателя.
- Если требуется, повторно ввести данные для формирования чека (В этом случае будет создан еще один чек с порядковым номером в папке tickets).
- Ввести путь к файлу (работает с файлами .txt) для вывода содержимого чека в консоль.
- Ввести 'exit' (без кавычек) для выхода из приложения
### Реализация
- Проект написан с использованием БД PostgreSQL. По дефолту создается 2 таблицы, product и discount_card, которые заполняются данными
- Паттерн Builder реализован в классе Product, который со связанными классами находится в папке [product]
- Паттерн Singleton для класса DataBase (не успел вынести и реализовать 2 отдельные сущности CardDB и ProductDB)
- Написано 4 теста, 2 из которых проверяют корректность работы БД с данными, и еще 2, которые отвечают за тест расчета стоимости и скидок, а также вывода ошибок при неправильном создании обьектов или ошибок SQL
### Особенности реализации
- Возможность создавать безграничное количество чеков (информация о порядковом номере будет отображаться в пункте чека [Cashier] и в созданном файле tickets/)
- При вводе несуществующей карты, пользователю присваивается новая карта со скидкой 0%, и порядковым номером чека.
- Возможность выхода из программы путем ввода команды exit
- Возможность задавать новую компанию, где указывается ее название, адрес, процент скидки товарной позиции (если кол-во товара больше Constants.DISCOUNT_AFTER и данная позиция является акционной), валюта (BYN, USD, EUR)
- Чек предусматривает информировании о скидке по конкретным позициям товара, а также общий размер скидки, включая скидку по карте (от общей стоимости чека)
### To do
- Корректно мигрировать БД, поэтому запросы на создание таблиц по умолчанию хранятся в Constants.CREATE_TABLES и в файле resourses/sql.sql
- Дописать тесты
- Проверить считываемый с консоли файл на принадлежность к чеку с помощью RegEx
