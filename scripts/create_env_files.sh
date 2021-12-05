echo "PORT=8080" > docker.env
echo "DATABASE_URL=postgresql://db:5432/postgres" >> docker.env
echo "JDBC_DATABASE_URL=postgresql://db:5432/postgres" >> docker.env
echo "DATABASE_USERNAME=postgres" >> docker.env
echo "DATABASE_PASSWORD=postgres" >> docker.env

echo "PORT=8081" > .env
echo "DATABASE_URL=postgresql://db:5433/postgres" >> .env
echo "JDBC_DATABASE_URL=postgresql://db:5433/postgres" >> .env
echo "DATABASE_USERNAME=postgres" >> .env
echo "DATABASE_PASSWORD=postgres" >> .env

