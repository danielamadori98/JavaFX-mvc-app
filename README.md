
# JavaFX Admin Dashboard with SQLite (MVC)

A modern desktop application using **JavaFX**, **SQLite**, and **MVC pattern** for user and project management.

---

## Project Structure

```
JavaFX_MVC_admin_sqlite_jdbc/
│
├── bin/                        # Compiled classes
│   └── com/dashapp/           # All app packages
│
├── src/                       
│   └── com/dashapp/
│       ├── controller/        # JavaFX controllers
│       ├── model/             # Database & data classes
│       ├── view/              # View navigation utils
│       └── Main.java          # Main entry point
│
├── resources/
│   ├── css/                   # Styling
│   ├── data/                  # SQLite DB
│   └── fxml/                  # All views
│
├── lib/                       # External libraries
│   └── sqlite-jdbc-*.jar
│
├── run.bat / run.sh           # Launch scripts
└── README.md
```

---

## Features

- User login & registration
- Dashboard with navigation bar
- Project management:
    - Create, assign, delete projects
    - Project details with user assignment
    - Role-based controls
- SQLite persistence
- Responsive JavaFX GUI (FXML)
- MVC separation of concerns

---

## How to Run

### Requirements

- Java 11+ (JDK)
- JavaFX SDK
- sqlite-jdbc in `lib/`

### Compile and Run

```bash
# On Windows
run.bat

# On Unix/macOS
sh run.sh
```

---

## Notes

- Default database: `resources/data/dashapp.db`
- Admin user is predefined (`admin`).
- Manager can assign/unassign users to projects.
