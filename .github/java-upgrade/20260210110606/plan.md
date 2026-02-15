
# Java 21 Upgrade Plan

## Project Overview
- **Project**: OceanView Reservation System
- **Location**: c:\Users\Enzo\oceanview-reservation-system-1
- **Working branch**: appmod/java-upgrade-20260210110606
- **Current Java version**: 17
- **Target Java version**: 21

## Development Setup
- **Source JDK**: Java 17 (Eclipse Adoptium JDK 17.0.18)
  - Path: C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot\bin
- **Target JDK**: Java 21 (to be installed)
- **Build Tool**: Maven 3.9.12
  - Path: C:\Users\Enzo\.maven\maven-3.9.12\bin

## Upgrade Objective
Migrate the project from Java 17 to Java 21 LTS while maintaining full functionality and test coverage.

## Implementation Steps

### 1. Environment Preparation
- Install Java 21 JDK
- Verify Maven installation and configuration
- Stash any uncommitted changes to ensure clean working directory
- Create dedicated upgrade branch for isolated development

### 2. Initial Assessment
- Build the project with current configuration
- Run full test suite to establish baseline (19 tests)
- Document current CVE status
- Verify all dependencies are compatible

### 3. Java Version Migration
- Update pom.xml Java version properties
  - Change `java.version` from 17 to 21
  - Update `maven.compiler.source` to 21
  - Update `maven.compiler.target` to 21
- Apply necessary code migrations for Java 21 compatibility
- Resolve any compilation errors
- Ensure project builds successfully

### 4. Validation & Testing
- Run CVE security scans on upgraded dependencies
  - Address any High or Critical severity issues
- Verify code behavior remains consistent
  - Review and fix any Critical or Major behavioral changes
- Execute full test suite
  - Ensure all 19 tests pass
  - Fix any test failures related to the upgrade

### 5. Documentation
- Document all changes made during the upgrade
- Record any issues encountered and their resolutions
- Update project documentation to reflect Java 21 requirement
