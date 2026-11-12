# Lab 13 – Spring introduction (Products Web API)

Spring Boot 4.1.2, Maven, Java 21. Dependencies: Spring Web, Spring Data JDBC, H2 (in-memory database), Validation.

## Run

In Eclipse, right-click `pom.xml` → Run As → Maven build, and use the goal `clean spring-boot:run`. From a terminal, run `./mvnw clean spring-boot:run`.

## Endpoints (base URL `http://localhost:8080/api/v1/products`)

| Request | Result |
|---|---|
| `GET /api/v1/products` | all 17 products as JSON |
| `GET /api/v1/products/12` | 200 with product 12 |
| `GET /api/v1/products/999` | 404 Not Found |
| `GET /api/v1/products/-5` | 400 Bad Request (validation) |

## Structure

- `model/Product`: a record with an `@Id`. The price is a `BigDecimal`, matching `DECIMAL(10,2)` in `schema.sql` (step 13).
- `repository/ProductRepository`: extends `CrudRepository` and `PagingAndSortingRepository`.
- `service/ProductService`: the business layer.
- `controller/ProductController`: maps `/api/v1/products`, with `findAll` and `findById`.
- `schema.sql` and `data.sql` create the table and load the mock data at startup.

## Step 13: why BigDecimal?

`double` stores numbers in binary, so `0.1 + 0.2` gives `0.30000000000000004`. `BigDecimal` stores exact decimal values, which is what money needs, and the column is `DECIMAL(10,2)`.

## Step 14: validation

`spring-boot-starter-validation` adds Jakarta Bean Validation. The rules are:
- The `{id}` path variable has `@Positive`.
- In the `Product` record: `name` has `@NotBlank` and `@Size(max = 100)`, `description` has `@Size(max = 100)`, and `price` has `@NotNull`, `@DecimalMin("0.00", exclusive)` and `@Digits(8, 2)`.

**Which status code do you get when you send a negative id?**
`GET /api/v1/products/-5` returns **400 Bad Request**. Spring runs method validation on the controller parameter, the check fails, and it answers with a JSON problem detail containing the message "Product id must be a positive number". It does not return 500, and the database is never queried.
