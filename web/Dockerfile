# Wybierz obraz bazowy
FROM node:14

# Skopiuj pliki aplikacji
COPY . /web

# Ustaw aplikację jako katalog roboczy
WORKDIR /web

# Zainstaluj zależności
RUN npm install

# Ustaw dane wyjściowe
EXPOSE 8881

# Uruchom aplikację
CMD [ "npm", "start" ]
