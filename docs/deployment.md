# Deployment Guide

## Prerequisites

### Required Software

- **Java JDK 17+** (Eclipse Adoptium Temurin recommended)
- **Apache Maven 3.9+**
- **Git** for version control
- **H2 Database** (embedded, included in dependencies)
- **MySQL 8.0+** (optional, for production)

### Development Tools (Optional)

- IntelliJ IDEA / Eclipse / VS Code
- Postman or similar API testing tool
- Docker (for containerized deployment)

---

## Build Instructions

### Standard Build

Build the project and run all tests:

```bash
mvn clean package
```

### Skip Tests (Faster Build)

```bash
mvn clean package -DskipTests
```

### Run Tests Only

```bash
mvn test
```

### Run Tests with Coverage Report

```bash
mvn clean test jacoco:report
```

Coverage report will be available at: `target/site/jacoco/index.html`

### Build Specific Profiles

```bash
# Development profile
mvn clean package -Pdev

# Production profile
mvn clean package -Pprod
```

---

## Running Locally

### Using Maven

```bash
mvn spring-boot:run
```

With specific profile:

```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

### Using Built JAR

```bash
# Build first
mvn clean package

# Run the JAR
java -jar target/reservation-system-1.0.0-SNAPSHOT.jar
```

With profile:

```bash
java -jar target/reservation-system-1.0.0-SNAPSHOT.jar --spring.profiles.active=dev
```

### Application Access

Once running, the application will be available at:

- **API Base URL**: http://localhost:8080/api
- **H2 Console**: http://localhost:8080/h2-console
- **Frontend**: http://localhost:8080/index.html

---

## Database Setup

### H2 In-Memory Database (Development)

H2 is configured by default for development. Configuration in `application.properties`:

```properties
spring.datasource.url=jdbc:h2:mem:oceanview
spring.datasource.username=sa
spring.datasource.password=
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.jpa.hibernate.ddl-auto=update
```

**Accessing H2 Console:**
1. Navigate to http://localhost:8080/h2-console
2. JDBC URL: `jdbc:h2:mem:oceanview`
3. Username: `sa`
4. Password: (leave blank)

### MySQL Database (Production)

Create a separate `application-prod.properties` file:

```properties
# MySQL Configuration
spring.datasource.url=jdbc:mysql://localhost:3306/oceanview_db
spring.datasource.username=oceanview_user
spring.datasource.password=${DB_PASSWORD}
spring.datasource.driver-class-name=com.mysql.cj.jdbc.Driver

# JPA Configuration
spring.jpa.hibernate.ddl-auto=validate
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.dialect=org.hibernate.dialect.MySQL8Dialect

# Disable H2 Console
spring.h2.console.enabled=false
```

**MySQL Setup Commands:**

```sql
-- Create database
CREATE DATABASE oceanview_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

-- Create user
CREATE USER 'oceanview_user'@'localhost' IDENTIFIED BY 'secure_password_here';

-- Grant privileges
GRANT ALL PRIVILEGES ON oceanview_db.* TO 'oceanview_user'@'localhost';
FLUSH PRIVILEGES;
```

Run with production profile:

```bash
java -jar target/reservation-system-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod
```

---

## Continuous Integration (CI)

### GitHub Actions Workflow

The project uses GitHub Actions for automated CI/CD. Configuration file: `.github/workflows/maven.yml`

**Workflow Triggers:**
- Push to `main` or `dev` branches
- Pull requests targeting `main`

**CI Pipeline Steps:**

1. **Environment Setup**
   - OS: Ubuntu Latest
   - Java: JDK 17 (Eclipse Temurin)
   - Maven: Latest 3.9.x

2. **Dependency Caching**
   ```yaml
   # Cache Maven dependencies to speed up builds
   - uses: actions/cache@v3
     with:
       path: ~/.m2/repository
       key: ${{ runner.os }}-maven-${{ hashFiles('**/pom.xml') }}
   ```

3. **Build and Test**
   ```bash
   mvn -B -DskipTests=false clean test package
   ```
   
   **Flags Explained:**
   - `-B` : Batch mode (non-interactive, suitable for CI)
   - `-DskipTests=false` : Explicitly run tests (default, but clear)
   - `clean` : Remove previous build artifacts
   - `test` : Execute unit tests
   - `package` : Build JAR file

4. **Integration Tests (Optional)**
   ```bash
   mvn -B verify
   ```
   Runs integration tests in addition to unit tests.

5. **Code Quality Checks**
   ```bash
   # Checkstyle for code style compliance
   mvn checkstyle:check
   
   # SpotBugs for potential bugs
   mvn spotbugs:check
   
   # JaCoCo for code coverage
   mvn jacoco:check
   ```

6. **Test Reports**
   - Unit test results: `target/surefire-reports/`
   - Integration test results: `target/failsafe-reports/`
   - Coverage report: `target/site/jacoco/`

7. **Artifact Upload**
   ```yaml
   # Upload JAR for deployment
   - uses: actions/upload-artifact@v3
     with:
       name: reservation-system-jar
       path: target/*.jar
       retention-days: 30
   ```

**Coverage Threshold:**
- Minimum required: 80%
- Build fails if coverage drops below threshold

**Lint Checks:**
- Code style: Google Java Style Guide
- Static analysis: SpotBugs, PMD
- Dependency vulnerabilities: OWASP Dependency-Check

---

## Production Deployment

### Manual Deployment

1. **Build Production JAR**
   ```bash
   mvn clean package -Pprod -DskipTests=false
   ```

2. **Transfer to Server**
   ```bash
   scp target/reservation-system-1.0.0-SNAPSHOT.jar user@server:/opt/oceanview/
   ```

3. **Run on Server**
   ```bash
   ssh user@server
   cd /opt/oceanview
   java -jar reservation-system-1.0.0-SNAPSHOT.jar \
     --spring.profiles.active=prod \
     --server.port=8080
   ```

4. **Run as Service (SystemD)**
   
   Create `/etc/systemd/system/oceanview.service`:
   
   ```ini
   [Unit]
   Description=OceanView Reservation System
   After=syslog.target network.target
   
   [Service]
   User=oceanview
   ExecStart=/usr/bin/java -jar /opt/oceanview/reservation-system-1.0.0-SNAPSHOT.jar --spring.profiles.active=prod
   SuccessExitStatus=143
   Restart=always
   RestartSec=10
   
   [Install]
   WantedBy=multi-user.target
   ```
   
   Enable and start:
   ```bash
   sudo systemctl enable oceanview
   sudo systemctl start oceanview
   sudo systemctl status oceanview
   ```

### Docker Deployment

1. **Create Dockerfile**
   
   ```dockerfile
   FROM eclipse-temurin:17-jre-alpine
   
   WORKDIR /app
   COPY target/reservation-system-1.0.0-SNAPSHOT.jar app.jar
   
   EXPOSE 8080
   
   ENTRYPOINT ["java", "-jar", "app.jar"]
   CMD ["--spring.profiles.active=prod"]
   ```

2. **Build Docker Image**
   ```bash
   docker build -t oceanview-reservation:1.0.0 .
   ```

3. **Run Container**
   ```bash
   docker run -d \
     --name oceanview-app \
     -p 8080:8080 \
     -e SPRING_PROFILES_ACTIVE=prod \
     -e DB_PASSWORD=secure_password \
     oceanview-reservation:1.0.0
   ```

4. **Docker Compose (with MySQL)**
   
   Create `docker-compose.yml`:
   
   ```yaml
   version: '3.8'
   
   services:
     mysql:
       image: mysql:8.0
       environment:
         MYSQL_ROOT_PASSWORD: root_password
         MYSQL_DATABASE: oceanview_db
         MYSQL_USER: oceanview_user
         MYSQL_PASSWORD: secure_password
       ports:
         - "3306:3306"
       volumes:
         - mysql_data:/var/lib/mysql
     
     app:
       build: .
       ports:
         - "8080:8080"
       environment:
         SPRING_PROFILES_ACTIVE: prod
         DB_PASSWORD: secure_password
       depends_on:
         - mysql
   
   volumes:
     mysql_data:
   ```
   
   Start services:
   ```bash
   docker-compose up -d
   ```

---

## Environment Variables

### Required for Production

| Variable | Description | Example |
|----------|-------------|---------|
| `SPRING_PROFILES_ACTIVE` | Active Spring profile | `prod` |
| `DB_PASSWORD` | Database password | `secure_password_123` |
| `JWT_SECRET` | JWT signing secret | `your-256-bit-secret` |
| `SERVER_PORT` | Application port | `8080` |

### Optional Configuration

| Variable | Description | Default |
|----------|-------------|---------|
| `DB_URL` | Database JDBC URL | `jdbc:h2:mem:oceanview` |
| `DB_USERNAME` | Database username | `sa` |
| `LOG_LEVEL` | Logging level | `INFO` |
| `MAX_UPLOAD_SIZE` | Max file upload size | `10MB` |

### Setting Environment Variables

**Linux/macOS:**
```bash
export DB_PASSWORD="secure_password"
export JWT_SECRET="your-secret-key"
```

**Windows:**
```powershell
$env:DB_PASSWORD="secure_password"
$env:JWT_SECRET="your-secret-key"
```

**Docker:**
```bash
docker run -e DB_PASSWORD="secure_password" -e JWT_SECRET="key" ...
```

---

## Monitoring and Maintenance

### Health Checks

Spring Boot Actuator endpoints (add to `pom.xml`):

- **Health**: `GET /actuator/health`
- **Info**: `GET /actuator/info`
- **Metrics**: `GET /actuator/metrics`

### Logging

**Log Levels:**
```properties
logging.level.root=INFO
logging.level.com.oceanview.reservation=DEBUG
logging.file.name=logs/oceanview.log
logging.file.max-size=10MB
logging.file.max-history=30
```

**Log Locations:**
- Development: Console output
- Production: `/var/log/oceanview/application.log`
- Docker: Use `docker logs oceanview-app`

### Database Backup

**MySQL Backup:**
```bash
# Backup
mysqldump -u oceanview_user -p oceanview_db > backup_$(date +%Y%m%d).sql

# Restore
mysql -u oceanview_user -p oceanview_db < backup_20260210.sql
```

**Automated Backup (Cron):**
```bash
# Add to crontab
0 2 * * * /usr/bin/mysqldump -u oceanview_user -p'password' oceanview_db > /backups/db_$(date +\%Y\%m\%d).sql
```

### Performance Monitoring

- **Application Metrics**: Micrometer + Prometheus
- **Database Monitoring**: MySQL Workbench, Percona Monitoring
- **APM Tools**: New Relic, Datadog, Elastic APM
- **Uptime Monitoring**: Pingdom, UptimeRobot

### Troubleshooting

**Application won't start:**
```bash
# Check logs
tail -f logs/oceanview.log

# Verify Java version
java -version

# Check port availability
netstat -tulpn | grep 8080
```

**Database connection issues:**
```bash
# Test MySQL connection
mysql -h localhost -u oceanview_user -p

# Verify credentials in application.properties
# Check firewall rules
```

**High CPU/Memory usage:**
```bash
# Monitor JVM
jps -lvm
jstat -gc <pid> 1000

# Adjust JVM heap size
java -Xmx2g -Xms512m -jar app.jar
```

---

## Rollback Procedure

1. **Stop Current Version**
   ```bash
   sudo systemctl stop oceanview
   ```

2. **Replace JAR with Previous Version**
   ```bash
   cp /opt/oceanview/backup/reservation-system-previous.jar \
      /opt/oceanview/reservation-system-1.0.0-SNAPSHOT.jar
   ```

3. **Restart Service**
   ```bash
   sudo systemctl start oceanview
   ```

4. **Verify**
   ```bash
   curl http://localhost:8080/actuator/health
   ```

---

## Security Considerations

- ✅ Use HTTPS in production (configure SSL certificates)
- ✅ Store secrets in environment variables, not in code
- ✅ Regular security updates for dependencies
- ✅ Enable CORS only for trusted origins
- ✅ Implement rate limiting for API endpoints
- ✅ Use strong JWT secrets (minimum 256 bits)
- ✅ Regular database backups
- ✅ Monitor application logs for suspicious activity

---

## CI/CD Pipeline Summary

```
┌─────────────┐
│  Git Push   │
└──────┬──────┘
       │
       ▼
┌─────────────────────────────────────┐
│  GitHub Actions Workflow            │
├─────────────────────────────────────┤
│  1. Checkout code                   │
│  2. Setup Java 17 + Maven           │
│  3. Cache dependencies              │
│  4. Run: mvn clean test package     │
│  5. Generate coverage report        │
│  6. Run code quality checks         │
│  7. Upload artifacts                │
└──────┬──────────────────────────────┘
       │
       ▼
┌─────────────────┐
│  Tests Pass?    │◄─ 80% coverage required
└──────┬──────────┘
       │ Yes
       ▼
┌─────────────────┐
│  Artifact Ready │──► JAR file available for deployment
└─────────────────┘
```

**Build Command Breakdown:**

```bash
mvn -B -DskipTests=false clean test package
```

- **mvn**: Maven command
- **-B**: Batch mode (non-interactive, no color output)
- **-DskipTests=false**: Ensure tests are run (explicit)
- **clean**: Delete target/ directory
- **test**: Run all unit tests (@Test methods)
- **package**: Compile, test, and create JAR in target/

**Optional Phases:**
- `verify`: Run integration tests
- `install`: Install JAR to local Maven repo
- `deploy`: Deploy to remote Maven repository

---

## Quick Reference

| Task | Command |
|------|---------|
| Build project | `mvn clean package` |
| Run locally | `mvn spring-boot:run` |
| Run tests | `mvn test` |
| Run with coverage | `mvn test jacoco:report` |
| Build for production | `mvn clean package -Pprod` |
| Run JAR | `java -jar target/*.jar` |
| Check style | `mvn checkstyle:check` |
| Find bugs | `mvn spotbugs:check` |
