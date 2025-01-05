# Easy Menu Library

Easy Menu is a lightweight Java library for creating dynamic, navigable menu systems. It provides an intuitive way to define menu levels, actions, and navigation, enabling developers to build interactive console-based applications effortlessly.

---

## **Getting Started**

### **Installation**

Add the library to your project. If using Maven, include the following dependency:

```xml
<dependency>
    <groupId>org.easy.menu</groupId>
    <artifactId>easy-menu</artifactId>
    <version>1.0.0</version>
</dependency>
```

For Gradle, use:

```gradle
implementation 'org.easy.menu:easy-menu:1.0.0'
```

---

## **Usage**

### **1. Main Entry Point**

To start the menu system, use the `Menu.start(Class<?> clazz)` method, passing the main class of your application.

```java
public class Application {
    public static void main(String[] args) {
        Menu.start(Application.class);
    }
}
```

### **2. Defining Menu Levels**

Create classes that extend `MenuLevel` to define different levels of your menu.

**Example:**
```java
import org.easy.menu.domain.MenuLevel;
import org.easy.menu.annotation.Action;

public class MainMenu extends MenuLevel {

    @Override
    public String getLabel() {
        return "Main Menu";
    }

    @Action(label = "Start Game", order = 1)
    public void startGame() {
        System.out.println("Starting game...");
    }

    @Action(label = "View Scores", order = 2)
    public void viewScores() {
        System.out.println("Displaying scores...");
    }

    @Action(label = "Exit", order = 99)
    public void exit() {
        System.out.println("Exiting... Goodbye!");
        System.exit(0);
    }
}
```

### **3. Navigation**

Use the `navigate(Class<? extends MenuLevel> target)` method in your `MenuLevel` subclasses to transition between menu levels.

**Example:**
```java
@Action(label = "Go to Submenu", order = 3)
public void goToSubmenu() {
    try {
        navigate(SubMenu.class);
    } catch (UnknownLevelException e) {
        System.err.println("Error navigating to SubMenu: " + e.getMessage());
    }
}
```

### **4. Actions**

Annotate methods with `@Action` to define selectable options within a `MenuLevel`. Each action:
- Must be a `public` method with no arguments.
- Should specify a `label` and an `order` for display and navigation.

**Annotation Example:**
```java
@Action(label = "View Profile", order = 1)
public void viewProfile() {
    System.out.println("Profile details...");
}
```

### **5. Context Management**

The `Context` class is the core of the library, managing:
- The current menu level.
- Registered levels and their lifecycle.
- Home level (starting point).

Set the home level using the `@Home` annotation:
```java
import org.easy.menu.annotation.Home;

@Home
public class MainMenu extends MenuLevel {
    @Override
    public String getLabel() {
        return "Home Menu";
    }
}
```

---

## **Library Components**

### **1. Menu**

The main entry point for initializing the library:
```java
Menu.start(Class<?> clazz);
```
- `clazz` specifies the root package for scanning.

### **2. MenuLevel**

An abstract class representing a navigable menu level. Subclasses:
- Override `getLabel()` to define the level's name.
- Use `@Action` to specify menu options.
- Use `navigate(Class<? extends MenuLevel>)` for level transitions.

### **3. Action Annotation**

Defines menu options on methods. Attributes:
- `label`: The text displayed in the menu.
- `order`: The display order (lower values appear first).

### **4. Context**

Singleton class managing the application's menu state. Key methods:
- `addLevel(MenuLevel level)`: Registers a level.
- `setHome(MenuLevel home)`: Sets the starting level.
- `navigate(Class<? extends MenuLevel> target)`: Transitions to a specified level.

### **5. ClassScanner**

Automatically scans the specified package for:
- Subclasses of `MenuLevel`.
- Classes annotated with `@Injectable` for dependency injection.

---

## **Complete Example**

### **Structure**

```
com.example.menu
├── MainMenu.java
├── SubMenu.java
└── Application.java
```

### **Code**

**MainMenu.java:**
```java
import org.easy.menu.domain.MenuLevel;
import org.easy.menu.annotation.Action;
import org.easy.menu.annotation.Home;

@Home
public class MainMenu extends MenuLevel {

    @Override
    public String getLabel() {
        return "Main Menu";
    }

    @Action(label = "Go to Submenu", order = 1)
    public void goToSubmenu() {
        try {
            navigate(SubMenu.class);
        } catch (UnknownLevelException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @Action(label = "Exit", order = 99)
    public void exit() {
        System.out.println("Goodbye!");
        System.exit(0);
    }
}
```

**SubMenu.java:**
```java
import org.easy.menu.domain.MenuLevel;
import org.easy.menu.annotation.Action;

public class SubMenu extends MenuLevel {

    @Override
    public String getLabel() {
        return "Submenu";
    }

    @Action(label = "Return to Main Menu", order = 1)
    public void returnToMain() {
        try {
            navigate(MainMenu.class);
        } catch (UnknownLevelException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
```

**Application.java:**
```java
public class Application {
    public static void main(String[] args) {
        Menu.start(Application.class);
    }
}
```
---
## **Example**

To see how Easy Menu can be integrated into your project, check out the [easy-menu-example repository](https://github.com/afchamis21/easy-menu-example). It demonstrates how to create and navigate a simple menu system using Easy Menu.
---

## **Contributing**

Contributions are welcome! Feel free to submit issues or pull requests on the [GitHub repository](https://github.com/afchamis21/easy-menu).

---

## **License**

This library is licensed under the MIT License. See the [LICENSE](LICENSE) file for details.
