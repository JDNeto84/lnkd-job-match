#!/bin/bash

# Carregar variáveis do arquivo .env
if [ -f .env ]; then
  echo "Carregando variáveis do arquivo .env..."
  export $(grep -v '^#' .env | xargs)
else
  echo "Arquivo .env não encontrado!"
  exit 1
fi

# Verificar se as variáveis foram carregadas
echo "Configurações carregadas:"
echo "POSTGRES_USER: $POSTGRES_USER"
echo "POSTGRES_DBMJ: $POSTGRES_DBMJ"

# Subir container do Postgres
echo "Iniciando Postgres..."
docker compose up -d postgres

# Aguardar o Postgres ficar pronto
echo "Aguardando Postgres iniciar..."
RETRIES=10
until docker compose exec -T postgres pg_isready -U "$POSTGRES_USER" 2>/dev/null; do
  RETRIES=$((RETRIES - 1))
  if [ $RETRIES -eq 0 ]; then
    echo "Erro: Postgres não iniciou após 10 tentativas"
    exit 1
  fi
  echo "Aguardando Postgres... ($RETRIES tentativas restantes)"
  sleep 2
done

echo "Postgres pronto!"

# Rodar aplicação com as variáveis de ambiente
echo "Iniciando aplicação..."
cd matchjob
./gradlew bootRun