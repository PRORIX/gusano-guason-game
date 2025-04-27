# GusanoGuasón

**GusanoGuasón** es un divertido y visualmente atractivo juego del gusano (Snake) hecho en **JavaFX**, donde el jugador controla un gusano que debe comer manzanas para crecer sin chocar contra sí mismo ni los bordes. El proyecto incorpora elementos modernos como login, registro, base de datos, revivir aleatorio, y un sistema de puntuación con persistencia.

---

## Características

- **Interfaz moderna**: Estética oscura, bordes redondeados, botones animados y diseño limpio.
- **Registro e inicio de sesión**: Sistema seguro con validación, gestión de usuarios y base de datos SQLite.
- **Juego clásico con mejoras**:
  - Manzanas generadas aleatoriamente.
  - Movimiento fluido y controles responsivos.
  - Sistema de puntuación en tiempo real.
- **Pantalla de perfil**: Muestra tu usuario y puntuación máxima.
- **Controladores separados** para una mejor organización.
- **Diseño responsive** en las ventanas de juego y menús.
- **Persistencia de datos** con `Usuarios.db`.

---

## Tecnologías usadas

- **Java 17**
- **JavaFX**
- **FXML** para vistas
- **SQLite** como base de datos embebida
- **CSS personalizado** para diseño y estilo visual
- **Maven** (opcionalmente) para gestión de dependencias

---

## Estructura del proyecto

```
GusanoGuasón/
│
├── src/
│   ├── main/
│   │   ├── java/es/prorix/gusanoguason/
│   │   │   ├── Main.java
│   │   │   ├── models/Usuario.java
│   │   │   ├── database/ConexionBD.java
│   │   │   └── controllers/
│   │   │       ├── LoginController.java
│   │   │       ├── RegistroController.java
│   │   │       ├── PerfilController.java
│   │   │       └── JugarController.java
│   │   └── resources/
│   │       ├── fxml/
│   │       │   ├── login.fxml
│   │       │   ├── registro.fxml
│   │       │   ├── perfil.fxml
│   │       │   └── jugar.fxml
│   │       ├── css/
│   │       │   └── estilos.css
│   │       └── Usuarios.db
```

---

## Cómo ejecutar el proyecto

1. **Clona el repositorio**:
   ```bash
   git clone https://github.com/PRORIX/gusano-guason-game
   cd GusanoGuason
   ```

2. **Abre el proyecto en IntelliJ, Eclipse o tu IDE favorito**.

3. Asegúrate de tener Java 17 y JavaFX configurados en tu entorno.

4. Ejecuta `Main.java`.

---

## Controles del juego

- **Flechas del teclado**: Mover el gusano.

---

## Créditos

- Desarrollado con pasión por PRORIX.
- Recomendaciones de diseño por CARCOCHE_39
- Inspirado en el clásico juego Snake y adaptado para entornos educativos y lúdicos.

---

## Licencia

Este proyecto está bajo la Licencia MIT.  
Eres libre de modificar, distribuir y usar este código con fines educativos o personales.

---
