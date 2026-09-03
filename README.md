# BookCatalog - Spring Boot E-Commerce Application

A complete Spring Boot e-commerce application for managing a book catalog with shopping cart functionality, inventory management, and weather integration.

## Project Overview

BookCatalog is a full-stack web application built with Spring Boot 3.2.0 that provides:

- **Book Catalog**: Browse books across 5 categories (Fiction, Non-Fiction, Science, History, Biography)
- **Shopping Cart**: Add books to cart, update quantities, and checkout
- **Real-time Inventory**: Automatic stock deduction on purchase, out-of-stock status tracking
- **Role-Based Access Control**: USER (customer), EMPLOYEE (inventory manager), ADMIN (full access)
- **Inventory Management**: Dedicated admin interface for employees to add, edit, and delete books
- **Weather Integration**: Real-time weather display across all pages
- **Authentication & Security**: Spring Security with BCrypt password encoding and session management

## Technology Stack

| Component | Technology | Version |
|-----------|-----------|---------|
| Framework | Spring Boot | 3.2.0 |
| Security | Spring Security | 6.x |
| Database | H2 (Development) | Latest |
| ORM | Spring Data JPA / Hibernate | Latest |
| Template Engine | Thymeleaf | 3.x |
| Frontend | Bootstrap | 5.3.0 |
| Build Tool | Maven | 3.x |
| Java | OpenJDK | 17+ |

## Project Structure

```
book-catalog-app/
├── src/
│   ├── main/
│   │   ├── java/com/bookcatalog/
│   │   │   ├── BookCatalogApplication.java      # Main entry point
│   │   │   ├── config/
│   │   │   │   └── SecurityConfig.java          # Spring Security configuration
│   │   │   ├── controller/
│   │   │   │   ├── HomeController.java          # Homepage & browsing
│   │   │   │   ├── CartController.java          # Shopping cart operations
│   │   │   │   ├── EmployeeController.java      # Inventory management
│   │   │   │   └── AuthController.java          # Login/registration
│   │   │   ├── dto/
│   │   │   │   ├── BookDTO.java                 # Book transfer object
│   │   │   │   ├── CartItemDTO.java             # Cart item transfer object
│   │   │   │   ├── UserRegistrationDTO.java     # Registration form data
│   │   │   │   └── WeatherDTO.java              # Weather data
│   │   │   ├── entity/
│   │   │   │   ├── Book.java                    # Book entity
│   │   │   │   ├── User.java                    # User entity (Spring UserDetails)
│   │   │   │   ├── CartItem.java                # Shopping cart item
│   │   │   │   └── Order.java                   # Purchase order record
│   │   │   ├── exception/
│   │   │   │   └── ResourceNotFoundException.java
│   │   │   ├── repository/
│   │   │   │   ├── BookRepository.java
│   │   │   │   ├── UserRepository.java
│   │   │   │   ├── CartItemRepository.java
│   │   │   │   └── OrderRepository.java
│   │   │   ├── service/
│   │   │   │   ├── BookService.java             # Book business logic
│   │   │   │   ├── CartService.java             # Cart business logic
│   │   │   │   ├── UserService.java             # User/auth business logic
│   │   │   │   └── WeatherService.java          # Weather API integration
│   │   │   └── enums/
│   │   │       └── UserRole.java                # USER, EMPLOYEE, ADMIN roles
│   │   └── resources/
│   │       ├── application.properties           # Configuration
│   │       ├── templates/
│   │       │   ├── index.html                   # Homepage
│   │       │   ├── books.html                   # Browse books
│   │       │   ├── book-detail.html             # Single book page
│   │       │   ├── cart.html                    # Shopping cart
│   │       │   ├── checkout.html                # Checkout page
│   │       │   ├── orders.html                  # Order history
│   │       │   ├── login.html                   # Login page
│   │       │   ├── register.html                # Registration page
│   │       │   ├── access-denied.html           # Access denied page
│   │       │   └── employee/
│   │       │       ├── dashboard.html           # Employee dashboard
│   │       │       ├── inventory.html           # Inventory management
│   │       │       ├── add-book.html            # Add book form
│   │       │       └── edit-book.html           # Edit book form
│   │       └── static/
│   │           ├── css/
│   │           ├── js/
│   │           └── images/
│   └── test/
│       └── java/com/bookcatalog/               # Unit tests
├── pom.xml                                      # Maven configuration
├── README.md                                    # This file
└── target/
    └── book-catalog-app-1.0.0.jar              # Packaged JAR
```

## Key Features

### 1. **Book Catalog Management**
- Browse 5 categories: Fiction, Non-Fiction, Science, History, Biography
- Search books by title or author
- Pagination support (12 books per page)
- View detailed book information with ISBN, description, price

### 2. **Shopping Cart**
- Add books to cart with quantity selection
- Update quantities or remove items
- View total price with tax calculation (10%)
- Real-time inventory validation
- Out-of-stock prevention

### 3. **Inventory System**
- Automatic stock deduction on purchase
- Out-of-stock status display (when inventory ≤ 0)
- Low stock warning (when inventory ≤ 5)
- Stock restoration on order cancellation (if implemented)

### 4. **User Management**
- **Customer**: Browse, search, shop, view orders
- **Employee**: Manage inventory, add/edit/delete books
- **Admin**: Full system access

### 5. **Authentication & Security**
- User registration with email validation
- Login with "Remember Me" (7-day tokens)
- Password encryption with BCrypt
- Session-based authentication (30-minute timeout)
- CSRF protection (disabled for development)
- Role-based access control via Spring Security

### 6. **Weather Integration**
- Real-time weather display on all pages
- Shows temperature, description, humidity, wind speed
- Fallback to demo data if API unavailable
- Supports multiple cities (configurable)

### 7. **Order Management**
- Order history per user
- Order status tracking (PENDING, COMPLETED, CANCELLED)
- Order details with items and totals
- Automatic tax calculation (10%)

## Setup & Installation

### Prerequisites
- Java 17 or higher
- Maven 3.6+
- Internet connection (for Maven dependencies)

### Build Steps

```bash
# Clone or navigate to project
cd book-catalog-app

# Clean and package the application
mvn clean package -DskipTests=true

# Build is complete when you see "BUILD SUCCESS"
```

### Run the Application

```bash
# Run from JAR file
java -jar target/book-catalog-app-1.0.0.jar

# Or run from Maven
mvn spring-boot:run
```

### Access the Application

- **Homepage**: http://localhost:8080/
- **Browse Books**: http://localhost:8080/books
- **Login**: http://localhost:8080/login
- **H2 Database Console**: http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:testdb`
  - User: `sa`
  - Password: (leave blank)

## Demo Credentials

### Customer Account
- **Username**: customer1
- **Password**: password123
- **Role**: USER (customer)

### Employee Account
- **Username**: employee1
- **Password**: employee123
- **Role**: EMPLOYEE (inventory management)

### Admin Account
- **Username**: admin
- **Password**: admin123
- **Role**: ADMIN (full access)

## Sample Data

The application initializes with 6 sample books:

| Title | Author | Category | Price | Stock |
|-------|--------|----------|-------|-------|
| The Great Gatsby | F. Scott Fitzgerald | Fiction | $12.99 | 20 |
| Sapiens | Yuval Noah Harari | Non-Fiction | $18.99 | 15 |
| A Brief History of Time | Stephen Hawking | Science | $15.99 | 10 |
| The Fall of the Roman Empire | Edward Gibbon | History | $14.99 | 12 |
| Steve Jobs | Walter Isaacson | Biography | $16.99 | 18 |
| To Kill a Mockingbird | Harper Lee | Fiction | $11.99 | 25 |

## Core API Endpoints

### Public Endpoints
- `GET /` - Homepage
- `GET /books` - Browse books (with pagination and category filter)
- `GET /book-detail/{id}` - Book details
- `GET /search?q={term}` - Search books
- `GET /login` - Login page
- `POST /register` - Register new user
- `GET /register` - Registration form

### Customer Endpoints (Authenticated)
- `GET /cart` - View shopping cart
- `POST /cart/add` - Add book to cart
- `POST /cart/update` - Update cart item quantity
- `POST /cart/remove` - Remove item from cart
- `POST /cart/clear` - Clear entire cart
- `GET /cart/checkout` - Checkout page
- `POST /cart/checkout` - Complete order
- `GET /orders` - View order history

### Employee Endpoints (EMPLOYEE/ADMIN role)
- `GET /employee` - Employee dashboard
- `GET /employee/inventory` - Inventory management page
- `GET /employee/add-book` - Add book form
- `POST /employee/add-book` - Create new book
- `GET /employee/edit-book/{id}` - Edit book form
- `POST /employee/edit-book/{id}` - Update book
- `POST /employee/delete-book/{id}` - Delete book

### Admin Endpoints (ADMIN role)
- All employee endpoints
- User management (if implemented)
- System configuration (if implemented)

## Configuration

### Database
Edit `src/main/resources/application.properties`:
```properties
# H2 Database (Default)
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# To switch to MySQL:
# spring.datasource.url=jdbc:mysql://localhost:3306/bookcatalog
# spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver
# spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
```

### Weather API
```properties
# OpenWeatherMap configuration
weather.api.key=demo
weather.api.url=https://api.openweathermap.org/data/2.5/weather
```

To use real weather data:
1. Get free API key from https://openweathermap.org/api
2. Update `application.properties` with your API key

### Session & Security
```properties
# Session timeout
server.servlet.session.timeout=30m

# CSRF (disabled for development)
spring.security.csrf.enabled=false

# Remember-me token validity
spring.security.remember-me.token-validity-seconds=604800  # 7 days
```

## Database Schema

### book_table
```sql
CREATE TABLE book_table (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  title VARCHAR(255) NOT NULL,
  author VARCHAR(255) NOT NULL,
  isbn VARCHAR(255) UNIQUE NOT NULL,
  category VARCHAR(50) NOT NULL,
  price DECIMAL(10,2) NOT NULL,
  inventory INTEGER NOT NULL,
  description TEXT,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
```

### user_table
```sql
CREATE TABLE user_table (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  username VARCHAR(255) UNIQUE NOT NULL,
  password VARCHAR(255) NOT NULL,
  email VARCHAR(255) UNIQUE NOT NULL,
  full_name VARCHAR(255),
  role VARCHAR(50) NOT NULL,
  enabled BOOLEAN,
  created_at TIMESTAMP,
  updated_at TIMESTAMP
);
```

### cart_item_table
```sql
CREATE TABLE cart_item_table (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  book_id BIGINT NOT NULL,
  quantity INTEGER NOT NULL,
  added_at TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user_table(id),
  FOREIGN KEY (book_id) REFERENCES book_table(id)
);
```

### order_table
```sql
CREATE TABLE order_table (
  id BIGINT PRIMARY KEY AUTO_INCREMENT,
  user_id BIGINT NOT NULL,
  order_items TEXT,
  total_amount DECIMAL(10,2),
  status VARCHAR(50),
  order_date TIMESTAMP,
  updated_at TIMESTAMP,
  FOREIGN KEY (user_id) REFERENCES user_table(id)
);
```

## Workflow Examples

### Customer Shopping Flow
1. Browse books: `/books`
2. View book details: `/book-detail/{id}`
3. Add to cart: `POST /cart/add`
4. View cart: `/cart`
5. Checkout: `GET /cart/checkout` → `POST /cart/checkout`
6. View orders: `/orders`

### Employee Inventory Management
1. Login as employee: `/login` (use employee1/employee123)
2. Dashboard: `/employee`
3. Manage inventory: `/employee/inventory`
4. Add book: `/employee/add-book`
5. Edit/Delete books from inventory table

### Out-of-Stock Behavior
1. User adds last book to cart → Inventory reduced to 0
2. Book marked as "Out of Stock" on browsing pages
3. Add-to-cart button disabled for out-of-stock books
4. Employee can restore stock via edit book functionality

## Testing

### Manual Testing Checklist

**Authentication**
- [ ] Register new user
- [ ] Login with credentials
- [ ] Remember me functionality
- [ ] Access denied for protected pages
- [ ] Logout works correctly

**Shopping**
- [ ] Add book to cart
- [ ] Update cart quantity
- [ ] Remove item from cart
- [ ] Clear entire cart
- [ ] Tax calculated correctly (10%)

**Inventory**
- [ ] Stock deducts after purchase
- [ ] Out-of-stock books cannot be purchased
- [ ] Out-of-stock status displays
- [ ] Employee can add new books
- [ ] Employee can edit books
- [ ] Employee can delete books

**Search & Browse**
- [ ] Category filter works
- [ ] Pagination works
- [ ] Search by title works
- [ ] Search by author works

**Weather**
- [ ] Weather displays on all pages
- [ ] Weather updates correctly
- [ ] Fallback works when API unavailable

## Troubleshooting

### Build Issues
```bash
# Clean rebuild
mvn clean install -DskipTests

# If dependencies fail, check Maven settings
mvn help:describe -Dplugin=clean
```

### Runtime Issues
```bash
# Check logs for errors
# Application logs are written to console

# Check database
# Visit http://localhost:8080/h2-console

# Clear browser cache
# Ctrl+Shift+Delete (Windows) or Cmd+Shift+Delete (Mac)
```

### Common Problems

| Issue | Solution |
|-------|----------|
| Port 8080 already in use | Change port in `application.properties`: `server.port=8081` |
| H2 console won't load | Enable in `application.properties`: `spring.h2.console.enabled=true` |
| Weather not displaying | Check API key and internet connection |
| Login fails | Check browser cookies, try incognito mode |
| Out-of-stock still allows purchase | Refresh browser and retry |

## Performance Optimization

### Current Optimizations
- H2 in-memory database (fast for development)
- JPA query optimization with proper eager/lazy loading
- Spring Data pagination (limit query results)
- Thymeleaf template caching (disabled for development)
- Bootstrap CDN for static assets

### Production Recommendations
1. Switch to MySQL/PostgreSQL
2. Enable Thymeleaf caching
3. Add Redis for session storage
4. Implement database indexing on frequently searched columns
5. Use CDN for static assets
6. Enable GZIP compression
7. Add query optimization and logging

## Security Considerations

### Implemented
- ✅ BCrypt password encoding
- ✅ CSRF protection (can be enabled)
- ✅ SQL injection prevention (JPA parameterized queries)
- ✅ XSS prevention (Thymeleaf auto-escaping)
- ✅ Role-based access control
- ✅ Session management
- ✅ HTTPS ready (can be configured)

### For Production
- [ ] Enable HTTPS/SSL
- [ ] Use environment variables for secrets
- [ ] Implement rate limiting
- [ ] Add Web Application Firewall (WAF)
- [ ] Regular security audits
- [ ] Keep dependencies updated
- [ ] Implement logging and monitoring
- [ ] Add two-factor authentication

## Development Notes

### Adding a New Role
1. Add role to `UserRole` enum
2. Update `SecurityConfig.java` endpoints
3. Add role-specific templates
4. Add role-specific controllers

### Adding a New Book Category
1. Update the category list in `EmployeeController` and `CartController`
2. Update `Book` entity constraints if needed
3. Add filter option to templates

### Extending Functionality
- Implement order status updates
- Add email notifications
- Implement wishlist feature
- Add book reviews/ratings
- Implement discount codes
- Add multiple shipping options

## Future Enhancements

1. **Payment Integration**: Stripe, PayPal
2. **Email Notifications**: Order confirmations, shipping updates
3. **Advanced Search**: Filters by price range, rating
4. **Wishlist**: Save books for later
5. **Book Reviews**: User ratings and comments
6. **Recommendations**: Based on purchase history
7. **Multiple Addresses**: Shipping address management
8. **Analytics Dashboard**: Sales, popular books, user behavior

## Deployment

### Local JAR Execution
```bash
java -jar target/book-catalog-app-1.0.0.jar
```

### Docker (if Dockerfile exists)
```bash
docker build -t bookcatalog .
docker run -p 8080:8080 bookcatalog
```

### Cloud Deployment
- **AWS Elastic Beanstalk**: Spring Boot compatible
- **Google Cloud App Engine**: Java 17 runtime
- **Microsoft Azure App Service**: Built-in Spring support
- **Heroku**: Procfile configuration available

## Support & Resources

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Security Reference](https://docs.spring.io/spring-security/reference/)
- [Thymeleaf Documentation](https://www.thymeleaf.org/documentation.html)
- [Bootstrap Documentation](https://getbootstrap.com/docs/)
- [JPA/Hibernate Guide](https://docs.jboss.org/hibernate/orm/6.1/userguide/html_single/)

## License

This project is provided as-is for educational purposes.

## Author

Created with Spring Boot 3.2.0

---

**Last Updated**: August 2024
**Version**: 1.0.0
