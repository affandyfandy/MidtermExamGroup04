# Midterm Exam Group 4 - FINDO

Division of tasks as of July 24th, 2024

| Name   | Task                          | Status |
|--------|-------------------------------|--------|
| Hamdan | • Sequence diagram            | Done   |
|        | • Features CRUD Customer      |        |
|        | • Features CRUD Order         |        |
| Fahira | • ERD & class diagram         | Done   |
|        | • Features CRUD Invoice       |        |
|        | • Features CRUD Product       |        |

---

# POS (Point of Sale) System
This system is designed to manage basic shopping operations.

## Business Flow

We divided our work into three phases: **Planning, Designing, Implementation**.

### Planning

In the first phase, we defined the scope of the system. We began by defining the user story:

> **AS A CUSTOMER, I WANT TO BUY SOME PRODUCTS AND KNOW THEIR TOTAL PRICE SO THAT I NEED AN INVOICE TO SUMMARIZE THE ORDER LINES.**

From this user story, we identified four main objects: **Customer, Product, Invoice, and OrderLine**.

#### Customer
The Customer object is expected to manage customer operations, from basic queries (CRUD) to complex operations, such as placing an order.

#### Product
The Product object is expected to manage product operations, from basic queries (CRUD) to complex operations, such as purchasing a product.

#### OrderLine
The OrderLine object is expected to manage order operations, from basic queries (CRUD) to complex operations, such as checking out an order into an invoice.

#### Invoice
The Invoice object is expected to manage invoice operations, from basic queries (CRUD) to complex operations, such as printing an invoice.

### Designing

To help define the system's structure, we started with low-level diagram and expanded to high-level diagram. Firstly, we created an entity relationship diagram (ERD), which covers the relationships between each object.

![erd.png](/diagram/erd.png)

Next, we developed the ERD into a class diagram, illustrating the attributes and keys of each object.

![classdiagram.png](/diagram/classdiagram.png)

Finally, in terms of the MVC model we wanted to implement, we merged the class diagram into a sequence diagram. The sequence diagram displays the activities that occur in each component during query operations.

![sequence.png](/diagram/sequence.png)

### Implementation

In this phase, we began coding the backend of the system. We started by building the basic CRUD operations for Customer, Product, OrderLine, and Invoice.




