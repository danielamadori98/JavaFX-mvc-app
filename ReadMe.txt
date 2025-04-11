Complete File Structure

project/
|--JavaEnvironment <----------- Downloade form OneDrive
├── src/
│   ├── com/
│   │   └── dashapp/
│   │       ├── Main.java
│   │       ├── config/
│   │       │   └── AppConfig.java
│   │       ├── model/
│   │       │   ├── User.java
│   │       │   └── UserRepository.java
│   │       ├── view/
│   │       │   ├── ViewNavigator.java
│   │       │   └── components/
│   │       │       └── NavBar.java
│   │       ├── controller/
│   │           ├── MainController.java
│   │           ├── HomeController.java
│   │           ├── LoginController.java
│   │           ├── RegisterController.java
│   │           ├── DashboardController.java
│   │           └── ProfileController.java
│   │            
│   └── resources/
│       ├── css/
│       │   └── styles.css
│       ├── fxml/
│       │   ├── MainView.fxml
│       │   ├── HomeView.fxml
│       │   ├── LoginView.fxml
│       │   ├── RegisterView.fxml
│       │   ├── DashboardView.fxml
│       │   └── ProfileView.fxml
│       └── data/
│           └── users.csv  (will be created if it doesn't exist)
├── bin/  (created by the build script)
├── run.sh  (for Linux/Mac)
└── run.bat  (for Windows) <----------- insert the path like in build_and_run.bat >path to your JFX installation like in build_and_run.sh<


