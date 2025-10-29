# Testing Documentation

This document provides information about the unit testing strategy for the Burger Builder Backend API.

## Test Structure

The test suite consists of **9 unit test classes** covering all layers of the application:

### Unit Tests
- **Controller Tests (3)**: Test REST API endpoints with mocked dependencies
- **Service Tests (3)**: Test business logic with mocked repositories  
- **Repository Tests (3)**: Test repository method calls with mocked repositories

## Running Tests

### Run All Tests
```bash
mvn test
```

### Run Specific Test Categories
```bash
# Controller tests only
mvn test -Dtest="*ControllerTest"

# Service tests only
mvn test -Dtest="*ServiceTest"

# Repository tests only
mvn test -Dtest="*RepositoryTest"
```

### Run Individual Test Classes
```bash
mvn test -Dtest=IngredientControllerTest
mvn test -Dtest=CartServiceTest
mvn test -Dtest=OrderRepositoryTest
```

## Test Coverage

### Controllers (3 test classes)
- `IngredientControllerTest` - Tests all ingredient endpoints
- `CartControllerTest` - Tests all cart management endpoints
- `OrderControllerTest` - Tests all order management endpoints

**Coverage**: All REST endpoints with various scenarios including:
- Successful requests
- Validation errors
- Resource not found errors
- Invalid input data

### Services (3 test classes)
- `IngredientServiceTest` - Tests ingredient business logic
- `CartServiceTest` - Tests cart management logic
- `OrderServiceTest` - Tests order processing logic

**Coverage**: All business logic methods with:
- Happy path scenarios
- Exception handling
- Edge cases
- Data validation

### Repositories (3 test classes)
- `IngredientRepositoryTest` - Tests repository method calls
- `CartItemRepositoryTest` - Tests cart item repository calls
- `OrderRepositoryTest` - Tests order repository calls

**Coverage**: All repository method invocations with:
- Method call verification
- Return value validation
- Parameter passing verification

## Test Configuration

### Mocking Strategy
- **Service Layer**: Mock repositories to isolate business logic
- **Controller Layer**: Mock services to isolate API layer
- **Repository Layer**: Mock repositories to verify method calls

### Dependencies
- **JUnit 5**: Testing framework
- **Mockito**: Mocking and verification
- **AssertJ**: Fluent assertions
- **Hamcrest**: JSON response matchers

## Test Data Management

### Setup and Teardown
- Each test class uses `@BeforeEach` to set up test data
- Mock objects created using `@Mock` annotation
- Test data created using helper factory methods

### Test Data Factory Methods
Each test class includes helper methods to create test entities:
```java
private Ingredient createIngredient(String name, String category, BigDecimal price, Integer sortOrder)
private CartItem createCartItem(String sessionId, Ingredient ingredient, Integer quantity, LocalDateTime createdAt)
private Order createOrder(String orderNumber, String email, LocalDateTime createdAt)
```

## Assertions and Matchers

### AssertJ
Used for fluent assertions in service and repository tests:
```java
assertThat(result).isNotNull();
assertThat(result).hasSize(2);
assertThat(result.get(0).getName()).isEqualTo("Beef Patty");
```

### Hamcrest Matchers
Used for JSON response validation in controller tests:
```java
.andExpect(jsonPath("$", hasSize(2)))
.andExpect(jsonPath("$[*].name", containsInAnyOrder("Beef Patty", "Chicken Breast")))
.andExpect(jsonPath("$[*].isAvailable", everyItem(is(true))))
```

## Test Structure

```
src/test/java/com/burgerbuilder/
├── controller/
│   ├── IngredientControllerTest.java
│   ├── CartControllerTest.java
│   └── OrderControllerTest.java
├── service/
│   ├── IngredientServiceTest.java
│   ├── CartServiceTest.java
│   └── OrderServiceTest.java
└── repository/
    ├── IngredientRepositoryTest.java
    ├── CartItemRepositoryTest.java
    └── OrderRepositoryTest.java
```

## Best Practices

### Test Naming
- Test methods use descriptive names: `getAllIngredients_ShouldReturnListOfIngredients()`
- Test classes follow pattern: `{ClassUnderTest}Test`

### Test Organization
- Arrange-Act-Assert pattern used consistently
- Helper methods for common setup tasks
- Clear separation between test categories

### Error Testing
- Both positive and negative test cases
- Exception scenarios thoroughly tested
- Validation error responses verified

### Mocking
- Mock external dependencies only
- Verify interactions with mocked objects
- Use meaningful mock return values

## Performance Considerations

### Test Execution Speed
- Pure unit tests with mocked dependencies
- No database or external service calls
- Fast execution and immediate feedback

### Test Isolation
- Each test is completely independent
- No shared state between tests
- Mock objects reset between tests

## Continuous Integration

### Maven Test Goals
- `mvn test` - Runs all tests
- `mvn test-compile` - Compiles test classes
- `mvn surefire:test` - Runs tests with detailed reporting

### Test Reports
- Surefire reports generated in `target/surefire-reports/`
- Test results integrated with CI/CD pipeline

## Debugging Tests

### IDE Integration
- Run individual tests from IDE
- Debug tests with breakpoints
- View test execution results

### Logging
- Mockito provides detailed interaction logging
- AssertJ provides clear assertion failure messages
- Test execution logs available for troubleshooting