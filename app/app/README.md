# Burger Builder Application

A full-stack web application for building and ordering custom burgers with a modern React frontend and Spring Boot backend API.

## Project Structure

```
capstone_project_ih/
├── frontend/                 # React + TypeScript + Vite frontend
│   ├── src/
│   │   ├── components/      # React components
│   │   ├── context/         # React Context providers
│   │   ├── services/        # API service layer
│   │   ├── types/           # TypeScript type definitions
│   │   └── utils/           # Utility functions
│   ├── public/              # Static assets
│   ├── package.json         # Frontend dependencies
│   ├── vite.config.ts       # Vite configuration
│   ├── nginx.conf           # Nginx configuration for production
│   └── README.md            # Frontend-specific documentation
├── backend/                 # Spring Boot REST API
│   ├── src/main/java/com/burgerbuilder/
│   │   ├── controller/      # REST controllers
│   │   ├── service/         # Business logic services
│   │   ├── repository/      # Data access layer
│   │   ├── entity/          # JPA entities
│   │   ├── dto/             # Data transfer objects
│   │   ├── exception/       # Custom exception handling
│   │   └── config/          # Configuration classes
│   ├── src/main/resources/
│   │   ├── application.properties          # Default configuration
│   │   ├── application-docker.properties   # Docker/PostgreSQL config
│   │   ├── application-azure.properties    # Azure SQL config
│   │   ├── schema.sql                      # Database schema
│   │   └── data.sql                        # Initial data
│   ├── pom.xml              # Maven dependencies and build config
│   └── TESTING.md           # Backend testing documentation
├── environment.env.example  # Environment variables template
└── environment.env          # Environment variables (create from example)
```

## Frontend Application

### Tech Stack

- **Framework**: React 19.1.1
- **Language**: TypeScript 5.8.3
- **Build Tool**: Vite 7.1.7
- **Routing**: React Router DOM 7.9.3
- **HTTP Client**: Axios 1.12.2
- **Testing**: Vitest 1.0.4 + Testing Library
- **Linting**: ESLint 9.36.0
- **CSS**: Vanilla CSS with CSS modules

### Key Features

- Interactive burger builder with drag-and-drop ingredients
- Shopping cart management with session persistence
- Order creation and tracking
- Order history viewing
- Responsive design with modern UI/UX
- Real-time API integration
- Comprehensive testing coverage

### Backend URL Configuration

The frontend connects to the backend API through the following configuration:

**Location**: `frontend/src/services/api.ts`

```typescript
const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080';
```

**Required Environment Variable**:
- `VITE_API_BASE_URL`: The base URL for the backend API (defaults to `http://localhost:8080`)

**Usage**:
1. Create a `.env` file in the frontend directory
2. Add: `VITE_API_BASE_URL=http://your-backend-url:8080`
3. For production: `VITE_API_BASE_URL=https://your-production-api.com`

### Frontend Compilation and Deployment

#### Development Setup

```bash
cd frontend
npm install
npm run dev          # Start development server (http://localhost:5173)
npm run test         # Run tests
npm run test:ui      # Run tests with UI
npm run test:coverage # Run tests with coverage
npm run lint         # Run ESLint
```

#### Production Build

```bash
cd frontend
npm run build        # Build for production
npm run preview      # Preview production build locally
```

The build process:
1. **TypeScript Compilation**: `tsc -b` compiles TypeScript to JavaScript
2. **Vite Build**: Bundles and optimizes assets
3. **Output**: Creates `dist/` folder with production-ready files

#### Deployment Options

**Option 1: Static Hosting (Recommended)**
- Build the application: `npm run build`
- Deploy the `dist/` folder to any static hosting service:
  - Vercel, Netlify, AWS S3, Azure Static Web Apps
  - Set `VITE_API_BASE_URL` environment variable in hosting platform

**Option 2: Docker with Nginx**
- The project includes `nginx.conf` for containerized deployment
- Nginx serves the built React app with optimizations:
  - Gzip compression
  - Static asset caching
  - Security headers
  - SPA routing support

**Option 3: Traditional Web Server**
- Upload built files to any web server (Apache, Nginx, IIS)
- Configure server to serve `index.html` for all routes (SPA support)

## Backend Application

### Tech Stack

- **Framework**: Spring Boot 3.2.0
- **Language**: Java 21
- **Build Tool**: Maven
- **Database**: 
  - PostgreSQL (Docker/Development)
  - Azure SQL Database (Production)
- **ORM**: Spring Data JPA + Hibernate
- **Validation**: Spring Boot Validation
- **Utilities**: Lombok
- **Testing**: Spring Boot Test + H2 Database

### Key Features

- RESTful API for burger ingredients, cart, and orders
- Session-based cart management
- Database initialization with sample data
- CORS configuration for frontend integration
- Comprehensive error handling
- Multi-environment configuration support

### Environment Variables Required

The backend requires the following environment variables (defined in `environment.env`):

#### Database Configuration
- `DB_HOST`: Database server hostname
- `DB_PORT`: Database port (1433 for SQL Server, 5432 for PostgreSQL)
- `DB_NAME`: Database name
- `DB_USERNAME`: Database username
- `DB_PASSWORD`: Database password
- `DB_DRIVER`: JDBC driver class name

#### Application Configuration
- `SPRING_PROFILES_ACTIVE`: Active Spring profile
  - `docker`: Uses PostgreSQL configuration
  - `azure`: Uses Azure SQL configuration
- `SERVER_PORT`: Server port (default: 8080)
- `CORS_ALLOWED_ORIGINS`: Comma-separated list of allowed CORS origins

#### Example Configuration

```bash
# For Docker/PostgreSQL Development
SPRING_PROFILES_ACTIVE=docker
DB_HOST=database
DB_PORT=5432
DB_NAME=burgerbuilder
DB_USERNAME=postgres
DB_PASSWORD=YourStrong!Passw0rd
DB_DRIVER=org.postgresql.Driver

# For Azure SQL Production
SPRING_PROFILES_ACTIVE=azure
DB_HOST=your-server.database.windows.net
DB_PORT=1433
DB_NAME=burgerbuilder
DB_USERNAME=your-username
DB_PASSWORD=your-password
DB_DRIVER=com.microsoft.sqlserver.jdbc.SQLServerDriver
```

### Backend Compilation and Deployment

#### Development Setup

```bash
cd backend
mvn clean install     # Download dependencies and compile
mvn spring-boot:run   # Start development server
```

#### Production Build

```bash
cd backend
mvn clean package     # Build JAR file
```

The build process:
1. **Dependency Resolution**: Downloads all Maven dependencies
2. **Compilation**: Compiles Java source code to bytecode
3. **Testing**: Runs unit and integration tests
4. **Packaging**: Creates executable JAR file in `target/` directory

#### Deployment Options

**Option 1: JAR File Execution**
```bash
java -jar target/burger-builder-backend-1.0.0.jar
```

**Option 2: Docker Deployment**
```bash
# Build Docker image
docker build -t burger-builder-backend .

# Run with environment variables
docker run -p 8080:8080 --env-file environment.env burger-builder-backend
```

**Option 3: Cloud Platform Deployment**
- **Azure App Service**: Deploy JAR file directly
- **AWS Elastic Beanstalk**: Upload JAR file
- **Google Cloud Run**: Containerized deployment
- **Heroku**: Git-based deployment

#### Environment-Specific Deployment

**Development (PostgreSQL)**:
1. Set `SPRING_PROFILES_ACTIVE=docker`
2. Configure PostgreSQL connection variables
3. Run with Docker Compose or local PostgreSQL

**Production (Azure SQL)**:
1. Set `SPRING_PROFILES_ACTIVE=azure`
2. Configure Azure SQL connection variables
3. Deploy to cloud platform with proper security configuration

## Getting Started

1. **Clone the repository**
2. **Set up environment variables**:
   ```bash
   cp environment.env.example environment.env
   # Edit environment.env with your database credentials
   ```
3. **Start the backend**:
   ```bash
   cd backend
   mvn spring-boot:run
   ```
4. **Start the frontend**:
   ```bash
   cd frontend
   npm install
   npm run dev
   ```
5. **Access the application**: http://localhost:5173

## API Endpoints

- `GET /api/ingredients` - Get all ingredients
- `GET /api/ingredients/{category}` - Get ingredients by category
- `POST /api/cart/items` - Add item to cart
- `GET /api/cart/{sessionId}` - Get cart items
- `DELETE /api/cart/items/{itemId}` - Remove cart item
- `POST /api/orders` - Create order
- `GET /api/orders/{orderId}` - Get order details
- `GET /api/orders/history` - Get order history

## License

This project is part of a capstone project for educational purposes.
