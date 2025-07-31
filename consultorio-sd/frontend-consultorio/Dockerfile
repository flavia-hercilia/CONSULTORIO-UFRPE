FROM node:20-alpine AS builder

#diretório de trabalho dentro do contêiner
WORKDIR /app

#copia os arquivos package.json e package-lock.json
COPY package.json ./
COPY package-lock.json ./ 

#dependências do Node.js usando npm
RUN npm install --frozen-lockfile

#copia o resto do código da aplicação
COPY . .

#constroi aplicativo React para produção 
RUN npm run build

#Nginx para arquivos estáticos
FROM nginx:alpine

#copia os arquivos estáticos 
COPY --from=builder /app/dist /usr/share/nginx/html

#a porta padrão do Nginx
EXPOSE 80

#comando Nginx para rodar o servidor
CMD ["nginx", "-g", "daemon off;"]