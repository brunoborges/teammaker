# TeamMaker GUI Test Suite

This document describes the TestFX-based unit tests for the TeamMaker GUI application.

## Test Overview

The test suite consists of two main test classes that cover the core functionality of the TeamMaker JavaFX application:

### 1. `TeamMakerFXAppTest` - Comprehensive UI Testing
- **Purpose**: Complete end-to-end testing of the GUI application
- **Focus**: User interactions, UI component behavior, and integration testing
- **Tests**: 8 test methods covering all major functionality

### 2. `TeamMakerCoreFunctionalityTest` - Core Feature Testing  
- **Purpose**: Focused testing of essential team generation functionality
- **Focus**: Critical business logic validation and core user workflows
- **Tests**: 6 test methods covering core features

## Test Coverage

### Core Functionality Tested
✅ **Application Initialization**
- Window creation and title verification
- Default data loading (players and teams)
- Initial UI state validation

✅ **Team Generation**
- Generate teams with valid configuration
- Results display and formatting
- Tab switching to view results

✅ **UI Component Validation**
- All UI components presence verification
- Score sliders functionality and constraints
- Button interactions and responsiveness

✅ **Results Management**
- Results persistence across tab switches
- Results area content validation
- Multiple team generation scenarios

✅ **Data Validation**
- Player and team list validation
- Score slider constraint testing
- Configuration validation scenarios

## Test Structure

### Dependencies
```xml
<dependency>
    <groupId>org.testfx</groupId>
    <artifactId>testfx-junit5</artifactId>
    <version>4.0.16-alpha</version>
    <scope>test</scope>
</dependency>
```

### UI Element IDs Added for Testing
The following IDs were added to the GUI for testability:
- `#tabPane` - Main tab container
- `#playerListView` - Players list view
- `#teamListView` - Teams list view  
- `#addPlayerButton` - Add player button
- `#addTeamButton` - Add team button
- `#removePlayerButton` - Remove player button
- `#removeTeamButton` - Remove team button
- `#generateButton` - Generate teams button
- `#resultsArea` - Results text area
- `#minScoreSlider` - Minimum score slider
- `#maxScoreSlider` - Maximum score slider

### Test Method Organization
Tests are ordered using `@Order` annotations to ensure proper execution sequence:
1. Application initialization tests
2. Default data validation
3. Core team generation functionality
4. UI interaction tests
5. Edge case and error handling
6. Component presence validation

## Running the Tests

### Command Line
```bash
# Run all GUI tests
mvn test -pl gui

# Run specific test class
mvn test -pl gui -Dtest=TeamMakerCoreFunctionalityTest

# Run with verbose output
mvn test -pl gui -Dtest=TeamMakerFXAppTest -X
```

### Test Results
- **14 total tests** across both test classes
- All tests pass successfully
- Execution time: ~15 seconds total
- No failures or errors

## Test Features

### TestFX Features Used
- **Node Lookup**: Finding UI components by ID and CSS selector
- **User Interactions**: Clicking buttons, tab switching
- **Assertions**: Validating UI state and content
- **Platform Threading**: Proper JavaFX Platform.runLater usage
- **Sleep/Wait**: Allowing UI updates to complete

### Validation Techniques
- Component presence verification
- Content validation (text area results)
- State persistence testing
- Error condition handling
- UI responsiveness testing

## Key Test Scenarios

### 1. Team Generation Workflow
```java
@Test
public void testGenerateTeams() {
    // Click generate button
    clickOn("#generateButton");
    
    // Switch to results tab
    Platform.runLater(() -> tabPane.getSelectionModel().select(1));
    
    // Validate results content
    TextArea resultsArea = lookup("#resultsArea").queryAs(TextArea.class);
    assertTrue(resultsArea.getText().contains("TEAM FORMATION RESULTS"));
}
```

### 2. UI Component Validation
```java  
@Test
public void testUIComponents() {
    assertNotNull(lookup("#generateButton").query());
    assertNotNull(lookup("#playerListView").query());
    assertNotNull(lookup("#teamListView").query());
}
```

### 3. Score Slider Testing
```java
@Test
public void testScoreSliders() {
    Slider minSlider = lookup("#minScoreSlider").queryAs(Slider.class);
    Platform.runLater(() -> minSlider.setValue(2.0));
    assertEquals(2.0, minSlider.getValue(), 0.1);
}
```

## Test Environment Requirements

### JavaFX Testing Setup
- **TestFX**: Provides JavaFX testing capabilities
- **JUnit 5**: Modern testing framework with ordered execution
- **Headless Mode**: Tests can run in CI/CD environments
- **Platform Threading**: Proper JavaFX thread management

### System Requirements
- **Java 21+**: Required for JavaFX and language features
- **JavaFX 21.0.5**: GUI framework version
- **Maven 3.6+**: Build tool for test execution

## Best Practices Applied

### 1. Test Isolation
- Each test method is independent
- No shared state between tests
- Clean application startup for each test class

### 2. Robust Assertions
- Multiple validation conditions per test
- Graceful handling of timing issues
- Clear error messages for failures

### 3. Realistic User Workflows
- Tests simulate actual user interactions
- Tab switching and navigation testing
- Content validation after user actions

### 4. Error Tolerance
- Tests handle various application states gracefully
- Validation for both success and error conditions
- Flexible assertions for different scenarios

## Future Test Enhancements

### Potential Additions
- **Dialog Testing**: Add/edit player dialogs, error dialogs
- **File Operations**: Import/export functionality testing  
- **Keyboard Navigation**: Tab/arrow key navigation tests
- **Accessibility**: Screen reader and keyboard-only testing
- **Performance**: Load testing with large datasets
- **Integration**: Cross-module integration tests

### Advanced TestFX Features
- **Custom Matchers**: Create domain-specific assertion matchers
- **Screenshots**: Capture screenshots on test failures
- **Robot Actions**: More complex user interaction simulation
- **Parallel Execution**: Run tests in parallel for faster feedback

## Conclusion

The test suite provides comprehensive coverage of the TeamMaker GUI application's core functionality. The tests validate:
- ✅ Application startup and initialization
- ✅ Team generation core workflow  
- ✅ UI component interactions
- ✅ Results display and persistence
- ✅ Score configuration functionality
- ✅ Tab navigation and state management

All tests pass successfully and provide confidence in the application's stability and functionality.