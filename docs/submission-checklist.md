# Submission Checklist - OceanView Reservation System v1.0

**Date:** 2026-02-10  
**Project:** OceanView Reservation System  
**Version:** 1.0.0  
**Submission Tag:** v1.0-submission

---

## Overview

This checklist ensures all required deliverables are complete, verified, and packaged for final submission.

---

## 📄 1. Project Report (report.pdf)

**Status:** ⏳ Pending Generation

### Required Components

- [ ] **Cover Page**
  - Project title: "OceanView Reservation System"
  - Subtitle: "Spring Boot REST API with HTML/CSS/JS Frontend"
  - Student/Team name
  - Course/Module information
  - Submission date
  - Version: 1.0.0

- [ ] **Abstract (½ - 1 page)**
  - Project overview and purpose
  - Technology stack summary
  - Key features implemented
  - Challenges and solutions

- [ ] **Table of Contents**
  - All sections numbered
  - Page numbers

- [ ] **1. Introduction (1-2 pages)**
  - Project background
  - Objectives and scope
  - Target users
  - System overview

- [ ] **2. Requirements (2-3 pages)**
  - Functional requirements
  - Non-functional requirements
  - Use cases
  - User stories
  - Source: `docs/requirements.md`

- [ ] **3. System Architecture (2-3 pages)**
  - High-level architecture diagram
  - Component descriptions
  - Technology stack details
  - Design patterns used
  - Source: `docs/architecture.md`

- [ ] **4. UML Diagrams (3-4 pages)**
  - [ ] Use Case Diagram (`docs/uml/images/usecases.png`)
    - Caption: "Figure 1: System Use Cases"
  - [ ] Class Diagram (`docs/uml/images/classes.png`)
    - Caption: "Figure 2: System Class Diagram"
  - [ ] Sequence Diagram - Add Reservation (`docs/uml/images/add-reservation.png`)
    - Caption: "Figure 3: Add Reservation Sequence"
  - [ ] Sequence Diagram - View Reservation (`docs/uml/images/view-reservation.png`)
    - Caption: "Figure 4: View Reservation Sequence"
  - [ ] Sequence Diagram - Print Bill (`docs/uml/images/print-bill.png`)
    - Caption: "Figure 5: Print Bill Sequence"

- [ ] **5. API Specification (3-4 pages)**
  - API endpoints table
  - Request/response examples (2-3 key endpoints)
  - Authentication flow
  - Error handling
  - Source: `docs/api.md`

- [ ] **6. Database Design (1-2 pages)**
  - Entity-Relationship diagram
  - Table schemas
  - Sample data description
  - Source: `src/main/resources/db/sample-data.md`

- [ ] **7. Implementation Details (2-3 pages)**
  - Key algorithms (billing calculation)
  - Security implementation (JWT)
  - Data validation approach
  - Code structure overview

- [ ] **8. Testing (3-4 pages)**
  - [ ] Test strategy overview
  - [ ] Test cases summary table
  - [ ] Unit test results with screenshots
  - [ ] Integration test results with screenshots
  - [ ] Code coverage report screenshot
  - [ ] Manual UI test evidence (screenshots)
  - Source: `docs/test-plan.md`

- [ ] **9. Test Evidence (Screenshots)**
  - [ ] Application running (console output)
  - [ ] H2 Console with data
  - [ ] API test results (Postman/curl)
  - [ ] Frontend pages (all 8 pages)
  - [ ] Test execution output
  - [ ] Code coverage report
  - [ ] CI pipeline success
  - **Location:** `docs/screenshots/`

- [ ] **10. Deployment (1-2 pages)**
  - Build instructions
  - Run instructions
  - Environment setup
  - CI/CD pipeline overview
  - Source: `docs/deployment.md`

- [ ] **11. User Guide (1-2 pages)**
  - How to access the application
  - Basic workflows
  - Screenshots of key features

- [ ] **12. Conclusion (1 page)**
  - Summary of achievements
  - Lessons learned
  - Future enhancements
  - Reflection

- [ ] **13. References**
  - Spring Boot documentation
  - Technical resources used
  - Third-party libraries

- [ ] **14. Appendices**
  - [ ] Appendix A: Source code snippets (key classes)
  - [ ] Appendix B: Complete API documentation
  - [ ] Appendix C: Test case details
  - [ ] Appendix D: GitHub repository information

- [ ] **GitHub Repository Link**
  - Include prominently on cover page or in introduction
  - Format: `https://github.com/username/oceanview-reservation-system`
  - Tag reference: `v1.0-submission`

### Report Generation Commands

```bash
# Option 1: Generate PDF from Markdown (using pandoc)
pandoc docs/submission-report.md -o report.pdf \
  --pdf-engine=xelatex \
  --toc \
  --number-sections \
  --highlight-style=tango

# Option 2: Use VS Code Markdown PDF extension
# Install extension: yzane.markdown-pdf
# Right-click on markdown file → Markdown PDF: Export (pdf)

# Option 3: Create in Google Docs/Word and export to PDF
# Then save as report.pdf in root directory
```

---

## 📘 2. README.md

**Status:** ✅ Complete  
**Location:** `README.md`  
**Lines:** 294

### Verification Checklist

- [x] **Project title and description**
- [x] **Technology stack listed**
- [x] **Prerequisites documented**
- [x] **Build instructions** (`mvn clean package`)
- [x] **Run instructions** (`mvn spring-boot:run`)
- [x] **Application access URLs**
  - [x] Frontend: http://localhost:8080/
  - [x] API: http://localhost:8080/api
  - [x] H2 Console: http://localhost:8080/h2-console
- [x] **H2 Console credentials**
- [x] **Server port configuration**
- [x] **API endpoints summary**
- [x] **Testing instructions**
- [x] **Sample data information**
- [x] **Documentation links**
- [x] **Troubleshooting section**
- [x] **Project structure diagram**

### Final README Review

```bash
# Check README length and content
wc -l README.md
cat README.md | grep -E "^##" # List all sections

# Verify all links work
# Manually test each documentation link
```

---

## 🎨 3. UML Documentation

**Status:** ✅ Complete

### UML Source Files

**Location:** `docs/uml/`

- [x] `usecases.puml` - Use case diagram source
- [x] `classes.puml` - Class diagram source
- [x] `sequences/add-reservation.puml` - Add reservation flow
- [x] `sequences/view-reservation.puml` - View reservation flow
- [x] `sequences/print-bill.puml` - Print bill flow

**Total PlantUML Files:** 5

### UML Images (PNG Format)

**Location:** `docs/uml/images/`

- [x] `usecases.png` - Use case diagram
- [x] `classes.png` - Class diagram
- [x] `add-reservation.png` - Add reservation sequence
- [x] `view-reservation.png` - View reservation sequence
- [x] `print-bill.png` - Print bill sequence

**Total PNG Images:** 5

### UML Images (SVG Format)

**Location:** `docs/uml/images/`

- [x] `usecases.svg` - Scalable use case diagram
- [x] `classes.svg` - Scalable class diagram
- [x] `add-reservation.svg` - Scalable sequence diagram
- [x] `view-reservation.svg` - Scalable sequence diagram
- [x] `print-bill.svg` - Scalable sequence diagram

**Total SVG Images:** 5

### Verification Commands

```bash
# List all UML files
ls -lh docs/uml/*.puml docs/uml/sequences/*.puml

# List all generated images
ls -lh docs/uml/images/

# Verify image sizes (should be reasonable, not 0 bytes)
du -h docs/uml/images/*
```

---

## 🧪 4. Test Documentation & Evidence

### Test Plan Documentation

**Status:** ✅ Complete  
**Location:** `docs/test-plan.md`  
**Lines:** 821

- [x] Test strategy documented
- [x] 10 unit tests specified (UT-001 to UT-010)
- [x] 12 integration tests specified (IT-001 to IT-012)
- [x] 5 manual UI tests specified (UI-001 to UI-005)
- [x] Test data defined
- [x] Expected results documented
- [x] Acceptance criteria listed

### Test Implementation

**Status:** ⏳ Pending Implementation

**Location:** `src/test/java/`

- [ ] `BillingServiceTest.java` - Implement all test methods
- [ ] `ReservationControllerTest.java` - Implement all test methods
- [ ] Additional test classes as needed

### Test Execution Results

**Status:** ⏳ Pending Execution

- [ ] **Run all tests**
  ```bash
  mvn clean test
  ```

- [ ] **Generate test reports**
  - Surefire reports: `target/surefire-reports/`
  - Copy to: `docs/tests/`

- [ ] **Generate coverage report**
  ```bash
  mvn test jacoco:report
  ```
  - Coverage report: `target/site/jacoco/index.html`
  - Screenshot and save to: `docs/screenshots/coverage-report.png`

- [ ] **Verify coverage threshold**
  - Target: ≥80% code coverage
  - Actual: _____% (to be filled)

### Test Evidence Screenshots

**Status:** ⏳ Pending Capture  
**Location:** `docs/screenshots/`

Required screenshots:

- [ ] `01-application-running.png` - Console showing app started on port 8080
- [ ] `02-h2-console-login.png` - H2 console login page
- [ ] `03-h2-console-data.png` - H2 console showing reservation table data
- [ ] `04-test-execution.png` - Maven test execution output
- [ ] `05-test-results-summary.png` - Test results summary (passed/failed/skipped)
- [ ] `06-coverage-report.png` - JaCoCo coverage report summary
- [ ] `07-coverage-details.png` - Package-level coverage details
- [ ] `08-api-test-login.png` - POST /api/auth/login test (Postman/curl)
- [ ] `09-api-test-create-reservation.png` - POST /api/reservations test
- [ ] `10-api-test-get-reservations.png` - GET /api/reservations test
- [ ] `11-api-test-billing.png` - GET /api/billing/{id} test
- [ ] `12-frontend-index.png` - Landing page (index.html)
- [ ] `13-frontend-login.png` - Login page
- [ ] `14-frontend-dashboard.png` - Dashboard page
- [ ] `15-frontend-new-reservation.png` - New reservation form
- [ ] `16-frontend-view-reservation.png` - View reservation page
- [ ] `17-frontend-billing.png` - Billing page
- [ ] `18-frontend-reports.png` - Reports page
- [ ] `19-ci-pipeline-success.png` - GitHub Actions workflow success
- [ ] `20-git-commit-history.png` - Git log showing commits

### Screenshot Capture Commands

```bash
# Create screenshots directory
mkdir -p docs/screenshots

# Windows: Use Snipping Tool or Snip & Sketch (Win + Shift + S)
# macOS: Use Command + Shift + 4
# Linux: Use gnome-screenshot or scrot

# For terminal output screenshots, use:
# 1. Run command
# 2. Screenshot terminal
# 3. Save with descriptive name in docs/screenshots/
```

---

## 📦 5. GitHub Repository

**Status:** ✅ Repository Ready  
**Current Branch:** `dev`  
**Total Commits:** 14

### Repository Requirements

- [x] **Repository created and initialized**
- [x] **At least 6 meaningful commits** (Currently: 14 ✅)
- [x] **Descriptive commit messages**
- [x] **Git conventions followed** (feat, chore, docs, test, ci)
- [x] **README.md complete**
- [x] **Proper .gitignore**

### Commit History Verification

**Current Commits:**

1. ✅ `e4f65d5` - chore: init repo skeleton and folders
2. ✅ `61a536c` - chore: add pom.xml placeholder (to be filled)
3. ✅ `d097450` - chore: create Java package structure and placeholders
4. ✅ `98c3f1b` - chore: add application.properties placeholder
5. ✅ `ab5bfa4` - chore: add frontend file placeholders and asset dirs
6. ✅ `7fede57` - docs: add documentation and UML placeholders
7. ✅ `6b5391d` - docs: add API contract (endpoints + payloads + validation rules)
8. ✅ `3cf4818` - docs: add UML diagrams and images
9. ✅ `329f871` - chore: add sample data description and seed script placeholder
10. ✅ `786261c` - test: add test plan and test placeholders
11. ✅ `25b8d8a` - ci: add CI workflow placeholder and deployment notes
12. ✅ `3e20a1d` - docs: add run/build checklist to README
13. ✅ `9ba5400` - docs: add comprehensive changelog and git conventions
14. ✅ `cd9a930` - docs: add release instructions and merge process

**Requirement Met:** ✅ 14 commits (exceeds minimum of 6)

### GitHub Repository URL

**Status:** ⏳ To Be Added to Report

- [ ] Repository URL recorded
- [ ] URL included in report.pdf cover page
- [ ] URL included in report.pdf introduction
- [ ] Repository set to public (if submission requires)
- [ ] Tag `v1.0-submission` created and pushed

### Repository Commands

```bash
# Verify commit history
git log --oneline --all

# Count commits
git rev-list --count HEAD

# Verify remote URL
git remote -v

# Create and push submission tag (after merge to main)
git tag -a v1.0-submission -m "Final submission release"
git push origin v1.0-submission

# Get repository URL
git config --get remote.origin.url
```

---

## ⚙️ 6. CI/CD Workflow

**Status:** ✅ Configuration Present (Placeholder)  
**Location:** `.github/workflows/maven.yml`

### Verification Checklist

- [x] **Workflow file exists**
- [ ] **Workflow file contains actual YAML** (Currently placeholder)
- [ ] **Workflow tested and passing** (Green checkmark on GitHub)
- [ ] **Build succeeds:** `mvn clean package`
- [ ] **Tests execute in CI**
- [ ] **Artifacts uploaded**

### CI Workflow Requirements

Documented steps in workflow:
- [x] Checkout code
- [x] Setup Java 17
- [x] Cache Maven dependencies
- [x] Run tests and build: `mvn -B -DskipTests=false clean test package`
- [x] Upload test reports
- [x] Upload JAR artifact

### CI Completion Tasks

- [ ] Replace placeholder with actual YAML configuration
- [ ] Trigger workflow by pushing to GitHub
- [ ] Verify workflow runs successfully
- [ ] Screenshot workflow success
- [ ] Save screenshot: `docs/screenshots/19-ci-pipeline-success.png`

### Sample CI Trigger

```bash
# Push changes to trigger CI
git push origin dev

# Monitor workflow on GitHub:
# https://github.com/<username>/<repo>/actions
```

---

## 🗄️ 7. Database Export

**Status:** ⏳ Pending (After Implementation)

### H2 Database Export (Development)

**When to Export:** After running application and loading sample data

**Option 1: SQL Script Export (Recommended)**

```sql
-- Connect to H2 Console: http://localhost:8080/h2-console
-- Execute:
SCRIPT TO 'docs/database/oceanview-h2-dump.sql';

-- This creates a complete SQL dump including schema and data
```

**Option 2: CSV Export (Data Only)**

```sql
-- Export individual tables
CALL CSVWRITE('docs/database/reservations.csv', 
  'SELECT * FROM reservation');
CALL CSVWRITE('docs/database/users.csv', 
  'SELECT * FROM user');
```

**Tasks:**

- [ ] Create directory: `docs/database/`
- [ ] Run application with sample data loaded
- [ ] Connect to H2 console
- [ ] Export database using SCRIPT command
- [ ] Save as: `docs/database/oceanview-h2-dump.sql`
- [ ] Verify export file is not empty
- [ ] Include in submission package

### MySQL Migration Script (Optional - Production)

**Location:** `docs/database/migration-mysql.sql`

- [ ] Create MySQL-compatible schema
- [ ] Include CREATE TABLE statements
- [ ] Include sample INSERT statements
- [ ] Test against actual MySQL instance
- [ ] Document any MySQL-specific changes

**Template:**

```sql
-- docs/database/migration-mysql.sql
-- OceanView Reservation System - MySQL Schema

CREATE DATABASE IF NOT EXISTS oceanview_db 
  CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;

USE oceanview_db;

-- Drop tables if they exist (for clean migration)
DROP TABLE IF EXISTS billing;
DROP TABLE IF EXISTS reservation;
DROP TABLE IF EXISTS user;

-- Create tables
CREATE TABLE user (
  id BIGINT AUTO_INCREMENT PRIMARY KEY,
  email VARCHAR(255) NOT NULL UNIQUE,
  password VARCHAR(255) NOT NULL,
  full_name VARCHAR(255) NOT NULL,
  role VARCHAR(50) NOT NULL,
  active BOOLEAN DEFAULT TRUE,
  created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  INDEX idx_email (email)
) ENGINE=InnoDB;

-- (Add remaining tables...)

-- Insert sample data
INSERT INTO user (email, password, full_name, role, active) VALUES
('staff@oceanview.com', '$2a$10$...', 'Alice Manager', 'STAFF', TRUE),
('manager@oceanview.com', '$2a$10$...', 'Bob Director', 'MANAGER', TRUE);

-- (Add remaining sample data...)
```

---

## 📦 8. Final Packaging

### ZIP Archive Creation

**Status:** ⏳ Pending  
**Filename:** `oceanview-reservation-v1.0-submission.zip`

**Contents to Include:**

- [ ] Source code (entire repository)
- [ ] Built JAR file: `target/reservation-system-1.0.0-SNAPSHOT.jar`
- [ ] Documentation (`docs/` directory)
- [ ] UML diagrams and images
- [ ] Test reports
- [ ] Screenshots
- [ ] Database exports
- [ ] README.md
- [ ] CHANGELOG.md
- [ ] Report PDF (report.pdf)

**Excluded (via .gitignore):**
- `target/` directory (except JAR)
- `.idea/`, `.vscode/`
- `*.log`
- `*.jar` (PlantUML)
- `.git/` (optionally exclude, or include for full history)

### Packaging Commands

**Option 1: Windows PowerShell**

```powershell
# Build project first
mvn clean package

# Create deliverables directory
New-Item -ItemType Directory -Force -Path "deliverables"

# Copy JAR
Copy-Item target/reservation-system-1.0.0-SNAPSHOT.jar deliverables/

# Copy documentation
Copy-Item -Recurse docs deliverables/
Copy-Item README.md deliverables/
Copy-Item CHANGELOG.md deliverables/ -ErrorAction SilentlyContinue

# Copy report.pdf (once created)
Copy-Item report.pdf deliverables/ -ErrorAction SilentlyContinue

# Create ZIP (excluding .git for smaller size)
Compress-Archive -Path src,docs,scripts,pom.xml,README.md,.gitignore,.github,deliverables/reservation-system-1.0.0-SNAPSHOT.jar,report.pdf -DestinationPath oceanview-reservation-v1.0-submission.zip -Force

# OR include entire directory with git history
Compress-Archive -Path * -DestinationPath oceanview-reservation-v1.0-submission.zip -Force
```

**Option 2: Linux/macOS**

```bash
# Build project
mvn clean package

# Create ZIP excluding .git, target, etc.
zip -r oceanview-reservation-v1.0-submission.zip . \
  -x "*.git/*" "target/*" ".idea/*" ".vscode/*" "*.log" ".DS_Store"

# OR use tar.gz
tar -czf oceanview-reservation-v1.0-submission.tar.gz \
  --exclude='.git' \
  --exclude='target' \
  --exclude='.idea' \
  --exclude='.vscode' \
  --exclude='*.log' \
  .
```

### Package Verification

- [ ] **ZIP file created successfully**
- [ ] **File size reasonable** (should be < 50 MB without .git, < 200 MB with .git)
- [ ] **Extract and verify contents**
  ```bash
  # Create test directory
  mkdir test-extract
  cd test-extract
  
  # Extract archive
  unzip ../oceanview-reservation-v1.0-submission.zip
  # or
  tar -xzf ../oceanview-reservation-v1.0-submission.tar.gz
  
  # Verify structure
  ls -la
  
  # Try to build
  mvn clean package
  
  # Try to run
  java -jar deliverables/reservation-system-1.0.0-SNAPSHOT.jar
  ```

- [ ] **All required files present in archive**
- [ ] **JAR file can be executed**
- [ ] **Application starts without errors**

---

## ✅ 9. Final Verification

### Pre-Submission Checklist

#### Documentation

- [ ] Report PDF complete (all sections)
- [ ] Report PDF page count: 20-30 pages
- [ ] All UML diagrams included in report
- [ ] All test evidence screenshots included
- [ ] README.md accurate and complete
- [ ] All documentation links work

#### Code

- [ ] Project compiles: `mvn clean compile`
- [ ] All tests pass: `mvn test`
- [ ] Application builds: `mvn clean package`
- [ ] JAR file created successfully
- [ ] Application runs: `mvn spring-boot:run`
- [ ] Application accessible at http://localhost:8080
- [ ] H2 console accessible
- [ ] All API endpoints functional

#### Testing

- [ ] Unit tests implemented
- [ ] Integration tests implemented
- [ ] Test coverage ≥ 80%
- [ ] Test reports generated
- [ ] Screenshots captured for all tests

#### Git & GitHub

- [ ] All changes committed
- [ ] Meaningful commit messages (14+ commits)
- [ ] dev branch pushed to remote
- [ ] dev merged to main
- [ ] Tag v1.0-submission created
- [ ] Tag pushed to remote
- [ ] Repository URL accessible
- [ ] CI workflow passing

#### Package

- [ ] ZIP archive created
- [ ] ZIP archive tested (extract and build)
- [ ] All deliverables included
- [ ] File size acceptable

### Submission Deliverables Summary

**Physical/Upload Deliverables:**

1. ✅ report.pdf (20-30 pages)
2. ✅ Source code ZIP (oceanview-reservation-v1.0-submission.zip)
3. ✅ GitHub repository URL
4. ✅ Executable JAR file (inside ZIP or separate)

**Online Deliverables:**

1. ✅ GitHub repository (public with tag v1.0-submission)
2. ✅ CI/CD pipeline (visible on GitHub Actions)

---

## 📊 Submission Statistics

### Code Metrics

- **Total Java Files:** 10 (placeholders + implementations)
- **Total Lines of Java Code:** _____ (to be counted)
- **Test Files:** 2+
- **Test Cases:** 27 documented (10 unit, 12 integration, 5 manual)
- **Code Coverage:** _____% (target: ≥80%)

### Documentation Metrics

- **README:** 294 lines ✅
- **CHANGELOG:** 329 lines ✅
- **API Documentation:** 456 lines ✅
- **Test Plan:** 821 lines ✅
- **Deployment Guide:** 596 lines ✅
- **Release Guide:** 358 lines ✅
- **Total Documentation:** 2,854+ lines ✅

### Git Metrics

- **Total Commits:** 14 ✅ (exceeds requirement of 6)
- **Branches:** dev, main
- **Tags:** v1.0-submission
- **Contributors:** 1

### Project Timeline

- **Start Date:** February 10, 2026
- **Submission Date:** February 10, 2026
- **Duration:** 1 day (scaffolding complete, implementation pending)

---

## 🎯 Final Actions

### Day of Submission

1. [ ] Run final build: `mvn clean package`
2. [ ] Run all tests: `mvn test`
3. [ ] Capture final screenshots
4. [ ] Update CHANGELOG.md with final commits
5. [ ] Merge dev to main
6. [ ] Tag v1.0-submission
7. [ ] Push all changes and tags
8. [ ] Generate/finalize report.pdf
9. [ ] Create final ZIP package
10. [ ] Verify ZIP contents
11. [ ] Upload to submission portal
12. [ ] Submit GitHub URL
13. [ ] Send confirmation email (if required)
14. [ ] Keep local backup

### Post-Submission

- [ ] Create GitHub release for v1.0-submission
- [ ] Archive project locally
- [ ] Document lessons learned
- [ ] Celebrate completion! 🎉

---

## 📧 Submission Contacts

**Instructor/TA:** [To be filled]  
**Email:** [To be filled]  
**Submission Portal:** [To be filled]  
**Deadline:** [To be filled]

---

## 📝 Notes

Use this space to track any issues, reminders, or important notes during submission preparation:

```
[Add notes here as needed]
```

---

**Last Updated:** 2026-02-10  
**Checklist Version:** 1.0  
**Status:** Scaffolding Complete - Implementation Pending

---

## ⚠️ IMPORTANT REMINDERS

1. **Test before submitting** - Extract and run from the ZIP file
2. **Check file sizes** - Large files may fail to upload
3. **Verify GitHub URL** - Make sure repository is accessible
4. **Tag correctly** - Use exact tag name: `v1.0-submission`
5. **PDF quality** - Ensure images are clear and readable
6. **Screenshots** - Include timestamps where possible
7. **Backup everything** - Keep multiple copies
8. **Submit early** - Don't wait until the last minute
9. **Read submission requirements** - Follow all specific instructions
10. **Confirmation** - Save submission confirmation/receipt

---

**END OF CHECKLIST**