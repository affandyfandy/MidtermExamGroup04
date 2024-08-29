### Midterm Exam Group 4 - FINDO

# POS (Point of Sale) System

This system is designed to manage basic shopping operations.

## Planning

### Project Overview

The objective of this project is to develop a Point of Sale (POS) system designed to handle basic shopping operations. The system will enable customers to make purchases and receive a detailed invoice summarizing their order.

### User Story

> **AS A CUSTOMER, I WANT TO BUY SOME PRODUCTS AND KNOW THEIR TOTAL PRICE SO THAT I NEED AN INVOICE TO SUMMARIZE THE ORDER LINES.**

### Business Flow

![business-flow.png](/img/business-flow.png)

### Project Requirements

**Project Objective:** Develop a Java Spring Boot project for a Point Of Sales (POS) system that allows customers to order products from the database, generates invoices, exports invoices to PDF files, and creates daily, monthly, and yearly reports on sales and total revenue.

**Implement Lombok, Hibernate, and Pagination:** Utilize Lombok for reducing boilerplate code, Hibernate for ORM (Object-Relational Mapping), and implement pagination for efficient handling of large datasets.

**Global Exception Handling:** Implement global exception handling to manage and respond to exceptions consistently across the application.

**Data Transfer Objects (DTOs):** Use DTOs to enhance security and encapsulate data when transferring between client and server.

**RESTful API Best Practices:** Follow best practices for designing and implementing RESTful APIs, ensuring clear, concise, and standardized communication.

**Git Workflow:** Adhere to best practices for Git workflow, including branching, committing, and merging strategies, to ensure smooth and organized collaboration.

### System Entities

1. Customer
Represents an individual or organization making a purchase. Attributes include: customerId, phone, firstName, lastName, createdDate, updatedDate, isActive.

2. Product
Represents an item available for purchase. Attributes include: productId,  name,  price,  isActive, quantity, createdTime, updatedTime

3. OrderItem
Represents a line item in an order, linking a product to a specific quantity. Attributes include: orderItemId, productId, invoiceId, quantity, amount, createdTime, updatedTime

4. Invoice
Represents a summary of the customer’s purchase, including all order lines and the total amount. Attributes include: invoiceId,  customerId, totalAmount, date, createdTime, updatedTime

### Entity Relationship

- **Customer** has many **Invoice**
- **Product** has many **OrderItem**
- **Invoice** has many **OrderItem**
- **OrderItem** references **Product** and **Invoice**

![class-diagram.png](/img/class-diagram.png)

## Designing

### DDL

```sql
-- Create Customer Table
CREATE TABLE customer (
    customer_id BINARY(16) NOT NULL,
    phone VARCHAR(12) NOT NULL,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    created_time TIMESTAMP NOT NULL,
    updated_time TIMESTAMP NOT NULL,
    is_active BOOLEAN NOT NULL,
    PRIMARY KEY (customer_id)
);

-- Create Product Table
CREATE TABLE product (
    product_id BINARY(16) NOT NULL,
    name VARCHAR(200) NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    quantity INT NOT NULL,
    is_active BOOLEAN NOT NULL,
    created_time TIMESTAMP NOT NULL,
    updated_time TIMESTAMP NOT NULL,
    PRIMARY KEY (product_id)
);

-- Create Invoice Table
CREATE TABLE invoice (
    invoice_id BINARY(16) NOT NULL,
    invoice_date TIMESTAMP NOT NULL,
    total_amount DECIMAL(10, 2) NOT NULL,
    created_time TIMESTAMP NOT NULL,
    updated_time TIMESTAMP NOT NULL,
    customer_id BINARY(16) NOT NULL,
    PRIMARY KEY (invoice_id),
    FOREIGN KEY (customer_id) REFERENCES customer(customer_id)
);

-- Create OrderItem Table
CREATE TABLE order_item (
    order_item_id BINARY(16) NOT NULL,
    quantity INT NOT NULL,
    price DECIMAL(10, 2) NOT NULL,
    created_time TIMESTAMP NOT NULL,
    updated_time TIMESTAMP NOT NULL,
    invoice_id BINARY(16) NOT NULL,
    product_id BINARY(16) NOT NULL,
    PRIMARY KEY (order_item_id),
    FOREIGN KEY (invoice_id) REFERENCES invoice(invoice_id),
    FOREIGN KEY (product_id) REFERENCES product(product_id)
);
```

## Implementation

### Project Structure (Backend)
```
group4/
│
├── src/main/                        
│   ├── java/com/example/group4                    
│   │   ├── controller/
│   │   ├── data/
│   │   ├── dto/
│   │   ├── repository/
│   │   ├── exception/
│   │   ├── service/
│   │   ├── utils/
│   │   └── config/
│   │
│   ├── resources/             
│   │   ├── application.properties
│   │   ├── static/
│   │   └── templates/
│   │   
│   ├── test/                          
├── pom.xml
```

### Project Structure (Frontend)
```
MidtermExamGroup04/
│
├── group4-client/                  
│   ├── api/                        
│   │   ├── server.js               
│   │   └── db.json                 
│   ├── src/                        
│   │   ├── app/                    
│   │   │   ├── config/             
│   │   │   ├── main/               
│   │   │   │   ├── components/    
│   │   │   │   │   ├── footer/    
│   │   │   │   │   ├── header/    
│   │   │   │   │   ├── main/      
│   │   │   │   │   └── sidebar/   
│   │   │   │   └── guards/        
│   │   │   ├── models/             
│   │   │   ├── pages/             
│   │   │   │   ├── customer/      
│   │   │   │   ├── invoice/       
│   │   │   │   ├── login/         
│   │   │   │   └── product/       
│   │   │   ├── services/           
│   │   ├── assets/                 
│   │   ├── styles/                
│   ├── angular.json                
│   ├── package.json                
│
├── .gitignore                      
├── README.md                       
└── package.json                    
```

### List of Endpoints

#### Customer

| Method | Endpoint                        | Description                              |
|--------|---------------------------------|------------------------------------------|
| `GET`  | `/api/v1/customer`               | Retrieve all customers                   |
| `GET`  | `/api/v1/customer/{id}`          | Retrieve customer by Id                  |
| `PUT`  | `/api/v1/customer/{id}`          | Update customer data by Id               |
| `POST` | `/api/v1/customer/{id}/activate` | Set customer status to "Active" by Id    |
| `POST` | `/api/v1/customer/{id}/deactivate` | Set customer status to "Deactive" by Id |
| `POST` | `/api/v1/customer`               | Create new customer                     |

#### Invoice

| Method | Endpoint                                    | Description                                         |
|--------|---------------------------------------------|-----------------------------------------------------|
| `GET`  | `/api/v1/invoice`                          | Retrieve all invoices                              |
| `GET`  | `/api/v1/invoice/{id}`                     | Retrieve invoice by Id                             |
| `PUT`  | `/api/v1/invoice/{id}`                     | Update invoice data by Id                          |
| `GET`  | `/api/v1/invoice/{id}/export`              | Export invoice to PDF file                         |
| `GET`  | `/api/v1/invoice/search?={params}`         | Retrieve all invoices containing Customer Name     |
| `GET`  | `/api/v1/invoice/filter/{params}`          | Retrieve all invoices filtered by date or month    |
| `GET`  | `/api/v1/invoice/report/{params}`          | Retrieve revenue report by day, month, or year     |
| `GET`  | `/api/v1/invoice/export/{params}`          | Export an Excel file filtered by Customer ID, month, or year |
| `POST` | `/api/v1/invoice`                          | Create new invoice                                |


#### Product

| Method | Endpoint                                  | Description                                    |
|--------|-------------------------------------------|------------------------------------------------|
| `GET`  | `/api/v1/product`                         | Retrieve all products                         |
| `POST` | `/api/v1/product`                         | Create new product                            |
| `GET`  | `/api/v1/product/{id}`                    | Retrieve product by Id                        |
| `PUT`  | `/api/v1/product/{id}`                    | Update product data by Id                     |
| `GET`  | `/api/v1/product/search?name={name}`      | Retrieve all products with a specific name    |
| `GET`  | `/api/v1/product/search?name={name}&status={status}` | Retrieve all products with a specific status and name |
| `GET`  | `/api/v1/product/search?status={status}`  | Retrieve all products with a specific status  |
| `POST` | `/api/v1/product/{id}/activate`           | Set product status to "Active" by Id          |
| `POST` | `/api/v1/product/{id}/deactivate`         | Set product status to "Inactive" by Id        |
| `POST` | `/api/v1/product/import`                  | Import a list of products from an Excel file  |

### Swagger Documentation

![swagger.png](/img/swagger.png)

### Exported Excel

![exported-excel.png](/img/exported-excel.png)

---

*Crafted with <span style="color: #e25555;">&#10084;</span> by Fahira Adindiah and Hamdan Azani*