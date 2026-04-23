# Prerequisites for Office Knowledge Map

This document lists all the technologies and tools required to run the Office Knowledge Map application (both backend and frontend).

## Required Technologies

### 1. Java Development Kit (JDK)
- **Version Required:** Java 17 or higher
- **Purpose:** Required for running the Spring Boot backend application
- **Download:** [Oracle JDK](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html) or [OpenJDK](https://openjdk.org/projects/jdk/17/)

**Verify Installation:**
```bash
java -version
javac -version
```
Expected output should show version 17.x.x or higher.

---

### 2. Apache Maven
- **Version Required:** Maven 3.6+ (3.8+ recommended)
- **Purpose:** Dependency management and build tool for the backend
- **Download:** [Apache Maven](https://maven.apache.org/download.cgi)

**Verify Installation:**
```bash
mvn -version
```

**Installation Instructions:**
- **Linux/Mac:**
  ```bash
  # Using package manager (Ubuntu/Debian)
  sudo apt update
  sudo apt install maven
  
  # Using package manager (Mac)
  brew install maven
  ```

- **Windows:**
  1. Download Maven binary zip file
  2. Extract to C:\Program Files\Apache\maven
  3. Add Maven bin directory to PATH environment variable
  4. Set MAVEN_HOME environment variable

---

### 3. PostgreSQL Database
- **Version Required:** PostgreSQL 12+ (14+ recommended)
- **Purpose:** Primary database for storing application data
- **Download:** [PostgreSQL](https://www.postgresql.org/download/)

**Default Configuration (as per application.properties):**
- **Database Name:** `office_knowledge_map`
- **Username:** `postgres`
- **Password:** `postgres`
- **Port:** `5432`

**Verify Installation:**
```bash
psql --version
```

**Setup Database:**
```bash
# Login to PostgreSQL
psql -U postgres

# Create database
CREATE DATABASE office_knowledge_map;

# Exit
\q
```

**Installation Instructions:**
- **Linux (Ubuntu/Debian):**
  ```bash
  sudo apt update
  sudo apt install postgresql postgresql-contrib
  sudo systemctl start postgresql
  sudo systemctl enable postgresql
  ```

- **Mac:**
  ```bash
  brew install postgresql@14
  brew services start postgresql@14
  ```

- **Windows:**
  Download and run the installer from [PostgreSQL Downloads](https://www.postgresql.org/download/windows/)

---

### 4. Node.js
- **Version Required:** Node.js 18.x or 20.x LTS
- **Purpose:** JavaScript runtime for running the Angular frontend
- **Download:** [Node.js](https://nodejs.org/)

**Verify Installation:**
```bash
node -v
```
Expected output: v18.x.x or v20.x.x

**Installation Instructions:**
- **Linux (Ubuntu/Debian):**
  ```bash
  curl -fsSL https://deb.nodesource.com/setup_20.x | sudo -E bash -
  sudo apt-get install -y nodejs
  ```

- **Mac:**
  ```bash
  brew install node@20
  ```

- **Windows:**
  Download and run the installer from [Node.js Downloads](https://nodejs.org/)

---

### 5. npm (Node Package Manager)
- **Version Required:** npm 10.8.2 or higher
- **Purpose:** Package manager for frontend dependencies
- **Note:** npm is included with Node.js installation

**Verify Installation:**
```bash
npm -v
```
Expected output: 10.8.2 or higher

**Update npm (if needed):**
```bash
npm install -g npm@latest
```

---

### 6. Angular CLI
- **Version Required:** Angular CLI 21.2.6
- **Purpose:** Command-line interface for Angular development
- **Installation:**
```bash
npm install -g @angular/cli@21.2.6
```

**Verify Installation:**
```bash
ng version
```
Expected output should show Angular CLI version 21.2.6

---

## Optional but Recommended

### 7. Git
- **Version:** Latest stable version
- **Purpose:** Version control system
- **Download:** [Git](https://git-scm.com/downloads)

**Verify Installation:**
```bash
git --version
```

---

### 8. IDE (Integrated Development Environment)

**For Backend Development:**
- **IntelliJ IDEA** (Community or Ultimate Edition)
  - Download: [IntelliJ IDEA](https://www.jetbrains.com/idea/download/)
- **Eclipse IDE for Java Developers**
  - Download: [Eclipse](https://www.eclipse.org/downloads/)
- **Visual Studio Code** with Java extensions
  - Download: [VS Code](https://code.visualstudio.com/)

**For Frontend Development:**
- **Visual Studio Code** (Recommended)
  - Download: [VS Code](https://code.visualstudio.com/)
  - Recommended Extensions:
    - Angular Language Service
    - ESLint
    - Prettier
    - TypeScript and JavaScript Language Features

---

### 9. API Testing Tools (Optional)
- **Postman** - [Download](https://www.postman.com/downloads/)
- **Insomnia** - [Download](https://insomnia.rest/download)
- **cURL** - Command-line tool (usually pre-installed on Linux/Mac)

---

## Environment Variables

### Backend (Java/Maven)
Ensure the following environment variables are set:

- **JAVA_HOME**: Path to JDK installation
  ```bash
  # Example (Linux/Mac)
  export JAVA_HOME=/usr/lib/jvm/java-17-openjdk
  
  # Example (Windows)
  set JAVA_HOME=C:\Program Files\Java\jdk-17
  ```

- **MAVEN_HOME**: Path to Maven installation (optional, but recommended)
  ```bash
  # Example (Linux/Mac)
  export MAVEN_HOME=/usr/share/maven
  
  # Example (Windows)
  set MAVEN_HOME=C:\Program Files\Apache\maven
  ```

- **PATH**: Include Java and Maven bin directories
  ```bash
  # Linux/Mac
  export PATH=$JAVA_HOME/bin:$MAVEN_HOME/bin:$PATH
  
  # Windows
  set PATH=%JAVA_HOME%\bin;%MAVEN_HOME%\bin;%PATH%
  ```

---

## Port Requirements

Ensure the following ports are available:

- **Backend Server:** Port `8080`
- **Frontend Dev Server:** Port `4200`
- **PostgreSQL Database:** Port `5432`

**Check if ports are in use:**
```bash
# Linux/Mac
lsof -i :8080
lsof -i :4200
lsof -i :5432

# Windows
netstat -ano | findstr :8080
netstat -ano | findstr :4200
netstat -ano | findstr :5432
```

---

## System Requirements

### Minimum Hardware Requirements:
- **RAM:** 8 GB (16 GB recommended)
- **Disk Space:** 5 GB free space
- **Processor:** Dual-core processor (Quad-core recommended)

### Supported Operating Systems:
- **Linux:** Ubuntu 20.04+, Debian 11+, Fedora 35+, or any modern Linux distribution
- **macOS:** macOS 11 (Big Sur) or later
- **Windows:** Windows 10/11 (64-bit)

---

## Verification Checklist

Before running the application, verify all installations:

```bash
# Java
java -version          # Should show 17.x.x or higher
javac -version         # Should show 17.x.x or higher

# Maven
mvn -version           # Should show 3.6+ or higher

# PostgreSQL
psql --version         # Should show 12.x or higher

# Node.js
node -v                # Should show v18.x.x or v20.x.x

# npm
npm -v                 # Should show 10.8.2 or higher

# Angular CLI
ng version             # Should show 21.2.6

# Git (Optional)
git --version          # Any recent version
```

---

## Quick Setup Summary

1. **Install Java 17** → Set JAVA_HOME
2. **Install Maven 3.8+** → Set MAVEN_HOME
3. **Install PostgreSQL 14+** → Create database `office_knowledge_map`
4. **Install Node.js 20.x** → Includes npm
5. **Install Angular CLI 21.2.6** → `npm install -g @angular/cli@21.2.6`
6. **Install Git** (Optional but recommended)
7. **Install IDE** (VS Code, IntelliJ IDEA, etc.)

---

## Next Steps

After installing all prerequisites:

1. Navigate to the project directory
2. Follow the setup instructions in the respective README files:
   - Backend: `backend/README.md`
   - Frontend: `frontend/README.md` or `frontend/FRONTEND_README.md`

---

## Troubleshooting

### Common Issues:

**Java not found:**
- Ensure JAVA_HOME is set correctly
- Verify Java is in your PATH

**Maven not found:**
- Ensure Maven is installed and in your PATH
- Try running with full path to mvn executable

**PostgreSQL connection refused:**
- Verify PostgreSQL service is running
- Check database credentials in `backend/src/main/resources/application.properties`
- Ensure database `office_knowledge_map` exists

**Angular CLI not found:**
- Install globally: `npm install -g @angular/cli@21.2.6`
- Try with npx: `npx ng serve`

**Port already in use:**
- Kill process using the port
- Change port in configuration files

---

## Contact

For additional support or questions, refer to the project documentation or contact the development team.
