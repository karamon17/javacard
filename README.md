# ProjectJavaCard

## Описание проекта

ProjectJavaCard - это веб-приложение, разработанное с использованием Java и Spring Boot. Оно предоставляет функциональность для управления банковскими картами и клиентами.

## Технологии

- Java
- Spring Boot
- Hibernate
- JUnit5
- PostgreSQL
- Docker
- HTML
- JavaScript

## Установка и запуск

1. Клонируйте репозиторий на вашу локальную машину.
2. Создайте файл `.env` в корневом каталоге проекта и укажите в нем следующие переменные окружения:
   ```
   POSTGRES_USER=your_postgres_user
   POSTGRES_PASSWORD=your_postgres_password
   POSTGRES_DB=your_postgres_db
   SMTP_HOST=your_smtp_host
   SMTP_PORT=your_smtp_port
   SMTP_USER=your_smtp_user
   SMTP_PASSWORD=your_smtp_password
   ```
   Замените `your_postgres_user`, `your_postgres_password`, `your_postgres_db`, `your_smtp_host`, `your_smtp_port`, `your_smtp_user` и `your_smtp_password` на свои значения.
2. Установите Docker и Docker Compose, если они еще не установлены.
3. Запустите приложение с помощью Docker Compose, используя команду `docker-compose up` в корневом каталоге проекта.

## Использование

После запуска приложения вы можете взаимодействовать с ним через веб-интерфейс, доступный по адресу `http://localhost/html/index.html`.

## Функциональность

- Добавление нового клиента в базу данных
- Просмотр данных клиента и его банковских карт по его email
- Генерация новой банковской карты для клиента
- Соблюдение уникальности номера банковской карты
- Отправка email-уведомлений через SMTP-сервер: Оповещение клиента о выходе срока действия его банковской карты по email за 1 неделю до окончания срока действия и в день окончания срока действия
- Автоматическая генерация новой карты для клиента, если срок действия его карты истек
- Возможность деактивировать карту клиента
- Покрытие кода тестами с использованием JUnit5