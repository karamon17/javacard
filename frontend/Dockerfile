# Используем официальный образ nginx
FROM nginx:1.21.1-alpine

# Удаляем стандартную страницу приветствия nginx
RUN rm /usr/share/nginx/html/*

# Копируем нашу статическую веб-страницу в контейнер nginx
COPY . /usr/share/nginx/html

# Открываем порт 80
EXPOSE 80

# Запускаем nginx
CMD ["nginx", "-g", "daemon off;"]