# 251-Assignment1-2026-Shuairan-Trisha

A java Swing-based text editor built for 159.251 Software Design and Construction, Assignment 1.

## Team

- Trisha Chand (ID: 25016426) - GitHub: tri-iisha
- Shuairan Bi (ID: 24021445) - GitHub: bioliver1105

## Features

- Full GUI text editor with  File, Search, View, Edit, Help, and About menus
- New, Open (.txt), Save, Exit
- Select, Copy, Paste, Cut
- Search (single word with highlighting)
- Time & Date Display
- Print
- Export to PDF (Apache PDFBox)
- Syntax highlighting for .java, .py, and .js files
- Custom start screen
- Configurable font and color theme via YAML config file

## Running the Project

### Option 1: Run directly with Maven

Requires Java 17 and Maven installed.
````
mvn clean package
java -jar target/Text-editor-1.0-SNAPSHOT.jar
````

### Option 2: Run with Docker

Requires Docker Desktop installed and running

Build the image:
````
docker build -t text-editor .
````

Run the container:
````
docker run --rm text-editor
````
**Note:** This is a GUI application, however, Docker containers do not have a display attached
by default. As a result, the container will build and run the application, but the GUI window
itself will not be visible.

## Configuration

The application reads default font and color settings from `config.yml` in the project root.

## Reports

Code quality and metrics reports are located in the `reports/` directory:
- `reports/spotbugs/` - SpotBugs bug analysis
- `reports/pmd/` - PMD code style/naming convention report
- `reports/metrics/` - LOC, NOM, Cyclomatic Complexity and CBO metrics

## Significant Commits

**Trisha:**
- `9e06ea4` - Added Dockerfile and Maven Shade plugin for containerized build
- `050f1d6` - Add YAML config file for font and color settings
- `4bab61e` - Implemented New, Open, Save, and Exit functions

**Shuairan:**
- `c1ce261` - Add JTextPane-based syntax highlighting for .java/.py/.js files
- `32039ec` - Add SpotBugs Maven plugin and generate report
- `faac01f` - Add searching function

## Repository

https://github.com/bioliver1105/251-Assignment1-2026-Shuairan-Trisha



