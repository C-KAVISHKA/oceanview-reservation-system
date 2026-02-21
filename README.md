# OceanView Reservation System

A Spring Boot REST API backend with static HTML/CSS/vanilla-JS frontend for managing reservations.

## Project Status

🚧 Under Development

## Technology Stack

- **Backend**: Java 17, Spring Boot
- **Database**: MySQL
- **Frontend**: HTML, CSS, Vanilla JavaScript (located in `src/main/resources/static/`)
- **Build Tool**: Maven
- **Version Control**: Git

## Getting Started

### Prerequisites

- Java JDK 17 or higher
- Apache Maven 3.9+
- MySQL 5.7+ or 8.0+
- Git

Verify installations:
```bash
java -version
mvn -v
mysql --version
git --version
```

### Build the Project

Clean and build the project with tests:

```bash
mvn clean package
```

Build without running tests (faster):

```bash
mvn clean package -DskipTests
```

Run tests only:

```bash
mvn test
```

Generate test coverage report:

```bash
mvn test jacoco:report
```
Coverage report will be available at: `target/site/jacoco/index.html`

### Run the Application

**Using Maven (Development):**

```bash
mvn spring-boot:run
```

**Using Built JAR:**

```bash
# After building with mvn clean package
java -jar target/reservation-system-1.0.0-SNAPSHOT.jar
```

**Run with Specific Profile:**

```bash
# Development profile
mvn spring-boot:run -Dspring-boot.run.profiles=dev

# Or with JAR
java -jar target/reservation-system-1.0.0-SNAPSHOT.jar --spring.profiles.active=dev
```

### Access the Application

Once the application is running, you can access:

| Component | URL | Notes |
|-----------|-----|-------|
| **Frontend** | http://localhost:8080/ | Main landing page (index.html) |
| **API Base** | http://localhost:8080/api | REST API endpoints |
| **API Docs** | See [docs/api.md](docs/api.md) | Complete API specification |

#### Database Configuration

The application uses MySQL database.

**Default Connection Details:**
- **Database**: `oceanview`
- **Host**: `localhost:3306`
- **Username**: `root`
- **Password**: `root`

You can modify these settings in `src/main/resources/application.properties`:

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/oceanview?createDatabaseIfNotExist=true&useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true
spring.datasource.username=root
spring.datasource.password=root
```

The database will be created automatically on first run if it doesn't exist.

#### Server Port Configuration

**Default Port:** 8080

If port 8080 is already in use, you can change it in `src/main/resources/application.properties`:

```properties
server.port=8081
```

Or override via command line:

```bash
# Using Maven
mvn spring-boot:run -Dspring-boot.run.arguments=--server.port=8081

# Using JAR
java -jar target/reservation-system-1.0.0-SNAPSHOT.jar --server.port=8081
```

To check if port 8080 is in use:

**Windows:**
```powershell
netstat -ano | findstr :8080
```

**Linux/macOS:**
```bash
lsof -i :8080
# or
netstat -tulpn | grep 8080
```

### Quick Start Commands

```bash
# 1. Clone the repository (if not already done)
git clone <repository-url>
cd oceanview-reservation-system

# 2. Build the project
mvn clean package

# 3. Run the application
mvn spring-boot:run

# 4. Access the application
# Open your browser to: http://localhost:8080
```

### Development Workflow

1. **Make code changes** in your IDE
2. **Run tests** to verify: `mvn test`
3. **Build and run**: `mvn clean spring-boot:run`
4. **Test endpoints** using Postman or curl
5. **Check MySQL database** to inspect database state
6. **Commit changes**: `git add . && git commit -m "message"`

## Project Structure

```
oceanview-reservation-system/
├── .github/workflows/         # CI/CD configuration
├── docs/                      # Documentation
│   ├── api.md                # API specifications
│   ├── architecture.md       # System architecture
│   ├── test-plan.md         # Testing strategy
│   ├── deployment.md        # Deployment guide
│   └── uml/                 # UML diagrams
├── scripts/                  # Utility scripts
├── src/
│   ├── main/
│   │   ├── java/com/oceanview/reservation/
│   │   │   ├── controller/  # REST controllers
│   │   │   ├── service/     # Business logic
│   │   │   ├── model/       # JPA entities
│   │   │   ├── repository/  # Data access
│   │   │   └── config/      # Configuration
│   │   └── resources/
│   │       ├── static/      # Frontend files (HTML/CSS/JS)
│   │       └── application.properties
│   └── test/                # Unit and integration tests
├── pom.xml                  # Maven configuration
└── README.md               # This file
```

## API Endpoints

See [docs/api.md](docs/api.md) for complete API documentation.

**Key Endpoints:**
- `POST /api/auth/login` - User authentication
- `POST /api/reservations` - Create reservation
- `GET /api/reservations` - List all reservations
- `GET /api/reservations/{id}` - Get reservation details
- `PUT /api/reservations/{id}` - Update reservation
- `DELETE /api/reservations/{id}` - Cancel reservation
- `GET /api/billing/{id}` - Generate bill
- `GET /api/reports/occupancy` - Occupancy report
- `GET /api/reports/revenue` - Revenue report

## Testing

Run all tests:
```bash
mvn test
```

Run specific test class:
```bash
mvn test -Dtest=BillingServiceTest
```

Run tests with coverage:
```bash
mvn clean test jacoco:report
```

View coverage report:
- Open `target/site/jacoco/index.html` in your browser

Test plan and test cases: [docs/test-plan.md](docs/test-plan.md)

## Sample Data

The application loads sample data on startup in development mode:
- 2 sample reservations (John Smith, Sarah Johnson)
- 2 sample users (staff@oceanview.com, manager@oceanview.com)

See [src/main/resources/db/sample-data.md](src/main/resources/db/sample-data.md) for details.

## Documentation

- [UML Diagrams](docs/uml/) - Use cases, class diagram, sequence diagrams
- [API Documentation](docs/api.md) - REST API specifications
- [Test Plan](docs/test-plan.md) - Testing strategy and test cases
- [Architecture](docs/architecture.md) - System design and components
- [Deployment Guide](docs/deployment.md) - Build, run, and deploy instructions
- [Requirements](docs/requirements.md) - Functional and non-functional requirements

## Troubleshooting

### Port Already in Use

If you see "Port 8080 is already in use":
1. Change the port in `application.properties` to `8081` or higher
2. Or stop the process using port 8080

### Build Failures

If Maven build fails:
```bash
# Clean and retry
mvn clean install

# Update dependencies
mvn clean install -U
```

### MySQL Connection Issues

If you see "Public Key Retrieval is not allowed" error:
- Add `allowPublicKeyRetrieval=true` to the JDBC URL in `application.properties`

If MySQL is not running:
- **Windows**: Check MySQL service in Services
- **Linux/macOS**: `sudo systemctl start mysql` or `brew services start mysql`

### Tests Failing

Run tests with verbose output:
```bash
mvn test -X
```

## Contributing

1. Create a feature branch: `git checkout -b feature/your-feature`
2. Make your changes
3. Run tests: `mvn test`
4. Commit: `git commit -m "feat: add your feature"`
5. Push: `git push origin feature/your-feature`
6. Create a Pull Request

## License

[Specify License Here]

## Contact

For questions or support, please contact the development team.
