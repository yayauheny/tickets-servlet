# Tickets - Clevertec
### Инструкция по запуску
- Запустить проект используя tomcat, проставив в war сгенерированный gradle war (exploded)
- Ввести в адресную строку http://localhost:8080/name - где name = {cards | products | ticket}
- Для get cards/products передать параметр id для получения позиции из БД, или не передавать параметр, если надо получить все товары (тогда передается page-size (по умолчанию 3) и page) 
- Для put cards передать id, discount-size (double) чтобы обновить данные в БД
- Для put products передать id, quantity, price (double), name (varchar), discount (boolean) чтобы обновить данные в БД
- Для post cards/products передать те же данные, что и в put (patch не добавлял)
- Для delete cards/products передать id удаляемой позиции
- Для get ticket передать cardId (в случае отсутствия создается новая), productId, quantity (в случае несоответствия товаров и их количества, возвращается чек по товарам, где количество есть)

