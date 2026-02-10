# Changelog

All notable changes to the OceanView Reservation System project will be documented in this file.

The format is based on [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
and this project adheres to [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## Git Commit Conventions

This project follows conventional commit message format:

- **feat:** New feature implementation
- **chore:** Setup tasks, dependencies, configuration
- **docs:** Documentation updates
- **test:** Test additions or modifications
- **ci:** CI/CD configuration changes
- **fix:** Bug fixes
- **refactor:** Code refactoring without feature changes
- **style:** Code style/formatting changes
- **perf:** Performance improvements

### Commit Message Format

```
<type>: <subject>

<optional body>

<optional footer>
```

**Example:**
```
feat: add reservation cancellation endpoint

Implement DELETE /api/reservations/{id} with proper validation
and status updates.

Closes #123
```

---

## [Unreleased]

### Pending Implementation
- Complete pom.xml with Spring Boot dependencies
- Implement Java entity classes and repositories
- Implement REST API controllers
- Implement service layer with business logic
- Add authentication and authorization
- Implement frontend pages (HTML/CSS/JS)
- Complete unit and integration tests
- Finalize CI/CD workflow YAML

---

## [0.1.0] - 2026-02-10

### Initial Project Setup

All foundational work completed on **February 10, 2026** during development session.

### Added

#### Project Structure & Configuration
- **[e4f65d5]** `chore: init repo skeleton and folders`
  - Created project directory structure
  - Initialized Git repository on `dev` branch
  - Created directory tree: `src/main/java`, `src/main/resources`, `src/test`, `docs/`, `scripts/`
  - Added `.gitignore` for Maven, IDE files, logs

- **[61a536c]** `chore: add pom.xml placeholder (to be filled)`
  - Created Maven POM file with project metadata
  - Defined artifact: `com.oceanview:reservation-system:1.0.0-SNAPSHOT`
  - Added TODO comments for Spring Boot dependencies

- **[d097450]** `chore: create Java package structure and placeholders`
  - Created package structure: `com.oceanview.reservation.*`
  - Added placeholder files for:
    - `OceanviewReservationApplication.java` (main class)
    - Controllers: `ReservationController`, `AuthController`, `BillingController`
    - Service: `ReservationService`
    - Model: `Reservation`
    - Repository: `ReservationRepository`
    - Config: `DataLoader`

- **[98c3f1b]** `chore: add application.properties placeholder`
  - Created Spring Boot configuration file
  - Added TODO comments for H2 database config
  - Documented JPA/Hibernate settings needed
  - Noted server port and MySQL options

#### Frontend Structure
- **[ab5bfa4]** `chore: add frontend file placeholders and asset dirs`
  - Created static resource directories: `css/`, `js/`, `assets/`
  - Added 8 HTML page placeholders:
    - `index.html`, `login.html`, `dashboard.html`
    - `new-reservation.html`, `view-reservation.html`
    - `billing.html`, `reports.html`, `help.html`
  - Added JavaScript files: `api.js`, `app.js`
  - Added CSS file: `base.css`
  - Added logo placeholder: `assets/logo.png`
  - Updated README.md with frontend location

#### Documentation
- **[7fede57]** `docs: add documentation and UML placeholders`
  - Created comprehensive documentation structure:
    - `docs/architecture.md` - System architecture
    - `docs/api.md` - API documentation
    - `docs/requirements.md` - Functional/non-functional requirements
    - `docs/test-plan.md` - Testing strategy
    - `docs/deployment.md` - Deployment guide
  - Created UML diagram files:
    - `docs/uml/usecases.puml` - Use case diagram
    - `docs/uml/classes.puml` - Class diagram
    - `docs/uml/sequences/` - Sequence diagram directory

- **[6b5391d]** `docs: add API contract (endpoints + payloads + validation rules)`
  - Documented 9 complete REST API endpoints
  - Defined all request/response JSON formats
  - Specified validation rules for each field
  - Documented error responses (400, 401, 404, 409, 500)
  - Added data type specifications and format requirements
  - Created room types and status values reference tables
  - Total: 456 lines of comprehensive API specification

- **[3cf4818]** `docs: add UML diagrams and images`
  - Enhanced `usecases.puml` with actors (Guest, Staff, Manager)
  - Complete use cases: Login, Create/View/Modify/Cancel Reservation, Print Bill, Run Reports, Help
  - Expanded `classes.puml` with full class diagram:
    - Entities: Reservation, Guest, User, Bill
    - Enums: RoomType, ReservationStatus, UserRole
    - Services: ReservationService, BillingService, AuthService, ReportService
    - Utilities: BillingCalculator, AuthManager
    - Repositories and Controllers
  - Updated sequence diagrams (add-reservation, view-reservation, print-bill)
  - Generated PNG and SVG images for all diagrams (10 files total)
  - Installed PlantUML extension and Graphviz
  - Added `*.jar` to `.gitignore`

- **[25b8d8a]** `ci: add CI workflow placeholder and deployment notes`
  - Created `.github/workflows/maven.yml` with CI/CD documentation
  - Documented 8-step CI pipeline process
  - Specified Maven build command: `mvn -B -DskipTests=false clean test package`
  - Updated `docs/deployment.md` with 596 lines covering:
    - Build instructions (Maven commands)
    - Local running (Maven and JAR)
    - Database setup (H2 and MySQL)
    - CI/CD pipeline details
    - Production deployment (manual, SystemD, Docker)
    - Environment variables
    - Monitoring and maintenance
    - Rollback procedures
    - Security considerations

- **[3e20a1d]** `docs: add run/build checklist to README`
  - Expanded README.md to 294 lines
  - Added complete "Getting Started" section
  - Documented build commands: `mvn clean package`, `mvn test`
  - Documented run commands: `mvn spring-boot:run`, `java -jar`
  - Specified application access points:
    - Frontend: http://localhost:8080/
    - API: http://localhost:8080/api
    - H2 Console: http://localhost:8080/h2-console
  - Documented H2 console credentials (jdbc:h2:mem:oceanview, sa, blank password)
  - Explained server port configuration (default 8080, how to change)
  - Added troubleshooting section
  - Created project structure diagram
  - Added quick reference tables

#### Sample Data & Seeding
- **[329f871]** `chore: add sample data description and seed script placeholder`
  - Created `src/main/resources/db/sample-data.md` (130 lines)
  - Defined room types and rates:
    - SINGLE: $100/night, DOUBLE: $150/night, SUITE: $250/night, DELUXE: $400/night
  - Specified 2 sample reservations:
    - John Smith (DOUBLE, 3 nights, $517.50 total)
    - Sarah Johnson (SUITE, 4 nights, $1,150.00 total)
  - Defined 2 sample users (staff@oceanview.com, manager@oceanview.com)
  - Created `scripts/seed-data.sh` with implementation instructions (98 lines)
  - Documented DataLoader component approach

#### Testing
- **[786261c]** `test: add test plan and test placeholders`
  - Created comprehensive `docs/test-plan.md` (821 lines)
  - Documented 10 unit tests (UT-001 to UT-010):
    - Billing calculations (standard, single night, extended stay)
    - Date calculations
    - Validation tests (email, dates, data)
    - Authentication (password encryption, JWT tokens)
  - Documented 12 integration tests (IT-001 to IT-012):
    - Login flows (valid/invalid credentials)
    - Reservation CRUD operations
    - Validation error handling
    - Bill generation
    - Reports (occupancy, revenue)
  - Documented 5 manual UI tests (UI-001 to UI-005)
  - Created test placeholder files:
    - `BillingServiceTest.java` - 6 test stubs with Mockito setup
    - `ReservationControllerTest.java` - 10 test stubs with MockMvc setup
  - Specified 80% code coverage target
  - Added test execution commands and coverage reporting

### Project Statistics

**Total Commits:** 12  
**Files Created:** 50+  
**Documentation Lines:** 2,500+  
**Test Cases Documented:** 27  

**Directory Structure:**
```
oceanview-reservation-system/
├── .github/workflows/          # CI/CD configuration
├── docs/                       # Comprehensive documentation
│   ├── uml/images/            # 10 generated diagram images
│   └── *.md                   # 7 documentation files
├── scripts/                    # Utility scripts
├── src/
│   ├── main/
│   │   ├── java/              # 8 Java placeholder files
│   │   └── resources/
│   │       ├── static/        # 12 frontend files
│   │       ├── db/            # Sample data specification
│   │       └── application.properties
│   └── test/java/             # 2 test placeholder files
├── pom.xml                    # Maven configuration
├── README.md                  # 294 lines
└── .gitignore                 # Build/IDE exclusions
```

### Infrastructure

**Build System:** Maven 3.9.6  
**Java Version:** 17 (Eclipse Adoptium Temurin)  
**Version Control:** Git on `dev` branch  
**CI/CD:** GitHub Actions (placeholder)  

### Next Steps

1. **Code Implementation Phase:**
   - Fill `pom.xml` with complete Spring Boot dependencies
   - Implement entity classes with JPA annotations
   - Implement repository interfaces
   - Implement service layer with business logic
   - Implement REST controllers with proper error handling
   - Implement authentication with JWT
   - Implement frontend with API integration

2. **Testing Phase:**
   - Implement all unit tests (UT-001 to UT-010)
   - Implement integration tests (IT-001 to IT-012)
   - Achieve 80%+ code coverage
   - Execute manual UI tests

3. **Finalization:**
   - Complete GitHub Actions workflow YAML
   - Generate final test reports
   - Create deployment package
   - Merge `dev` → `main`
   - Tag release: `v1.0-submission`

---

## Release Process

### Version Tagging

```bash
# After merging dev to main
git checkout main
git merge dev
git tag -a v1.0-submission -m "Release v1.0 - Initial submission"
git push origin main --tags
```

### Branch Strategy

- **`dev`** - Active development branch (current)
- **`main`** - Production-ready code
- **`feature/*`** - Feature branches (as needed)
- **`docs`** - Documentation branch (future)

### Merge to Main Checklist

- [ ] All unit tests passing
- [ ] Integration tests passing
- [ ] Code coverage ≥ 80%
- [ ] Documentation complete and up-to-date
- [ ] README.md reflects current state
- [ ] CHANGELOG.md updated
- [ ] No open critical issues
- [ ] Peer review completed (if applicable)
- [ ] CI pipeline green

### Creating Pull Request

```bash
# Push dev branch
git push origin dev

# Create PR via GitHub UI
# Title: "Release v1.0 - Initial Submission"
# Body: Reference CHANGELOG.md changes
# Reviewers: Assign if applicable
# Merge after approval
```

### Final Submission Tag

```bash
git checkout main
git tag -a v1.0-submission -m "OceanView Reservation System v1.0 - Complete implementation with REST API, frontend, tests, and documentation"
git push origin v1.0-submission
```

---

## Maintainers

- Development Team
- Date: February 10, 2026

---

## Notes

This changelog documents the scaffolding and planning phase. Once code implementation begins, each feature will be logged with its corresponding commit and detailed changes.