# payments-sample
Rest API for simple prepaid card, used tech stack

- Java
- Spring Boot
- JAXRS
- JPA (Hibernate)

# 1. How to start the application ?
It is simple just execute  ``mvn spring-boot:run``

# 2. Do I need a database to run it ?
No by default it uses in-memorry database but also it is easy just to change it in application.properties if you want the tables to created in real one.

# 3. How to use the API ?

## User create
curl -i -X POST \
   -H "Content-Type:application/json" \
   -d \
'{
  "name":"Coca-Cola",
  "city":"London",
  "address":"Kings Road 15",
  "country":"United Kingdom",
  "email":"support@cocacola.com",
  "phone":"+44 123456789"
}' \
 'http://localhost:8080/companies'
 
## Company update
curl -i -X PUT \
   -H "Content-Type:application/json" \
   -d \
'{
  "name":"Coca-Cola 1",
  "city":"London",
  "address":"Kings Road 15",
  "country":"United Kingdom",
  "email":"support@cocacola.com",
  "phone":"+44 123456789"
  
}' \
 'http://localhost:8080/companies/<COMPANY_UID>'
 
## Comapny details
curl -i -X GET \
 'http://localhost:8080/companies/<COMPANY_UID>'
 
## Company list all
curl -i -X GET \
 'http://localhost:8080/companies'

## Company add owner

curl -i -X POST \
   -H "Content-Type:application/json" \
   -d \
'{
  "name":"Elon Musk"
}' \
 'http://localhost:8080/companies/02f44f29-1a93-4b35-a049-87707501d1a9/owners'
