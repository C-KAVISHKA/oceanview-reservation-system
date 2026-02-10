# Final Release Instructions

## Pre-Merge Checklist

Before merging `dev` into `main` and creating the `v1.0-submission` tag, ensure all requirements are met:

### Code Completion
- [ ] `pom.xml` filled with all Spring Boot dependencies
- [ ] All Java entity classes implemented
- [ ] All repository interfaces implemented
- [ ] All service classes with business logic implemented
- [ ] All REST controllers implemented
- [ ] Authentication and JWT implementation complete
- [ ] Data loader component implemented
- [ ] Frontend HTML/CSS/JS pages functional
- [ ] API integration working in frontend

### Testing
- [ ] All unit tests implemented and passing
- [ ] All integration tests implemented and passing
- [ ] Code coverage ≥ 80%
- [ ] Manual UI tests executed and documented
- [ ] No failing tests

### Documentation
- [ ] README.md is accurate and complete
- [ ] API documentation matches implementation
- [ ] CHANGELOG.md is up to date
- [ ] All UML diagrams reflect actual design
- [ ] Deployment guide is accurate

### Quality
- [ ] No compiler warnings
- [ ] Code follows style guidelines
- [ ] All TODOs addressed
- [ ] No debug/console.log statements in production code
- [ ] Error handling implemented

### CI/CD
- [ ] GitHub Actions workflow YAML completed
- [ ] CI pipeline passing
- [ ] Build artifacts generated successfully

### Build & Run
- [ ] `mvn clean package` succeeds
- [ ] `mvn spring-boot:run` starts application
- [ ] Application accessible at http://localhost:8080
- [ ] H2 console accessible at /h2-console
- [ ] All API endpoints responding correctly
- [ ] Frontend pages loading and functional

---

## Merge Process: dev → main

### Step 1: Final Commit on dev

Ensure all changes are committed on the `dev` branch:

```bash
# Check status
git status

# If there are uncommitted changes
git add .
git commit -m "feat: final implementation complete"

# Push to remote
git push origin dev
```

### Step 2: Create main Branch

If `main` branch doesn't exist yet:

```bash
# Create main branch from dev
git checkout -b main
git push origin main

# Switch back to dev
git checkout dev
```

### Step 3: Merge dev into main (Option A - Direct Merge)

```bash
# Switch to main branch
git checkout main

# Merge dev into main
git merge dev -m "Release v1.0 - Merge dev to main for submission"

# Push to remote
git push origin main
```

### Step 3 Alternative: Create Pull Request (Option B - Recommended)

```bash
# Ensure dev is pushed
git push origin dev

# Go to GitHub repository
# Click "Pull Requests" → "New Pull Request"
# Base: main ← Compare: dev
# Title: "Release v1.0 - Initial Submission"
# Description: See template below
# Create Pull Request → Merge Pull Request
```

**Pull Request Template:**

```markdown
## Release v1.0 - Initial Submission

### Summary
Complete implementation of OceanView Reservation System with REST API backend, 
HTML/CSS/JS frontend, comprehensive testing, and documentation.

### Changes
See [CHANGELOG.md](docs/CHANGELOG.md) for detailed list of all changes.

### Key Features Implemented
- ✅ REST API with 9 endpoints (auth, reservations, billing, reports)
- ✅ Spring Boot backend with H2/MySQL support
- ✅ Vanilla JavaScript frontend (8 pages)
- ✅ JWT authentication
- ✅ Billing calculation engine
- ✅ Sample data seeding
- ✅ Comprehensive test suite (27 test cases)
- ✅ CI/CD pipeline (GitHub Actions)
- ✅ Complete documentation

### Test Results
- Unit Tests: ✅ All passing
- Integration Tests: ✅ All passing
- Code Coverage: XX% (≥80% target)
- Manual UI Tests: ✅ Completed

### Documentation
- API Documentation: docs/api.md
- Architecture: docs/architecture.md
- Test Plan: docs/test-plan.md
- Deployment Guide: docs/deployment.md
- README: Complete with getting started guide

### Checklist
- [x] All code implemented
- [x] All tests passing
- [x] Code coverage target met
- [x] Documentation complete
- [x] CI pipeline green
- [x] Build succeeds: `mvn clean package`
- [x] Application runs: `mvn spring-boot:run`
- [x] Ready for submission

### Reviewers
@reviewer-name (if applicable)

### Next Steps
After merge:
1. Tag release: `v1.0-submission`
2. Generate final test reports
3. Package deliverables
4. Submit project
```

### Step 4: Tag the Release

After merging to main:

```bash
# Ensure you're on main with latest changes
git checkout main
git pull origin main

# Create annotated tag
git tag -a v1.0-submission -m "OceanView Reservation System v1.0 - Complete implementation

Features:
- REST API with 9 endpoints
- Spring Boot + H2/MySQL backend
- HTML/CSS/JS frontend
- JWT authentication
- Comprehensive testing (80%+ coverage)
- Full documentation
- CI/CD pipeline

Deliverables:
- Runnable JAR
- Source code
- Test reports
- UML diagrams
- API documentation
- Deployment guide

Submission Date: 2026-02-10"

# Push tag to remote
git push origin v1.0-submission

# Verify tag
git tag -l -n9 v1.0-submission
```

### Step 5: Verify Release

```bash
# Check tags
git tag

# View tag details
git show v1.0-submission

# List branches
git branch -a

# Verify commit history on main
git log --oneline -n 10
```

---

## Post-Release Actions

### Create GitHub Release

1. Go to GitHub repository → "Releases"
2. Click "Draft a new release"
3. Choose tag: `v1.0-submission`
4. Release title: "OceanView Reservation System v1.0 - Initial Submission"
5. Description: Use the summary from CHANGELOG.md
6. Attach files:
   - JAR file: `target/reservation-system-1.0.0-SNAPSHOT.jar`
   - Test reports (ZIP)
   - UML diagrams (ZIP)
   - README.pdf (generated from README.md)
7. Click "Publish release"

### Generate Deliverables Package

```bash
# Create deliverables directory
mkdir -p deliverables

# Build final JAR
mvn clean package
cp target/reservation-system-1.0.0-SNAPSHOT.jar deliverables/

# Copy documentation
cp -r docs deliverables/
cp README.md deliverables/
cp docs/CHANGELOG.md deliverables/

# Copy test reports
cp -r target/surefire-reports deliverables/test-reports
cp -r target/site/jacoco deliverables/coverage-report

# Create ZIP archive
# Windows PowerShell:
Compress-Archive -Path deliverables/* -DestinationPath oceanview-reservation-v1.0-submission.zip

# Linux/macOS:
# zip -r oceanview-reservation-v1.0-submission.zip deliverables/
```

### Final Verification Commands

```bash
# Clone fresh repository
git clone <repo-url> oceanview-test
cd oceanview-test

# Checkout release tag
git checkout v1.0-submission

# Build and run
mvn clean package
mvn spring-boot:run

# In another terminal, test endpoints
curl http://localhost:8080/actuator/health
curl http://localhost:8080/api/reservations

# Open browser
# http://localhost:8080/
# http://localhost:8080/h2-console
```

---

## Rollback (If Needed)

If issues are found after tagging:

```bash
# Delete tag locally
git tag -d v1.0-submission

# Delete tag remotely
git push origin :refs/tags/v1.0-submission

# Fix issues on dev
git checkout dev
# Make fixes
git commit -m "fix: address issue"

# Re-merge and re-tag
git checkout main
git merge dev
git tag -a v1.0-submission -m "..."
git push origin main --tags
```

---

## Branch Protection (Optional)

For team projects, consider setting up branch protection on `main`:

**GitHub Settings → Branches → Add Rule:**
- Branch name pattern: `main`
- ✅ Require pull request reviews before merging
- ✅ Require status checks to pass before merging
- ✅ Require branches to be up to date before merging
- ✅ Include administrators

---

## Summary of Git Workflow

```
Initial Setup (Completed)
├── Step 0: Environment setup ✅
├── Step 1: Initialize repository on dev ✅
├── Steps 2-12: Implementation & documentation ✅
└── Step 13: Create CHANGELOG ✅

Release Process (When Ready)
├── Pre-merge: Verify all checklist items
├── Merge: dev → main (via PR or direct)
├── Tag: v1.0-submission
├── Release: Create GitHub release
├── Package: Generate deliverables ZIP
└── Verify: Fresh clone test
```

---

## Contact

For questions about the release process, contact the development team.

**Date:** February 10, 2026  
**Current Branch:** dev  
**Total Commits:** 13  
**Ready for Code Implementation:** ✅
