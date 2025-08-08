# YouTube Timestamp Recorder

## Overview
YouTube Timestamp Recorder is a Java desktop application that helps you create and manage timestamps with notes for YouTube videos or any other media content. The application provides an intuitive GUI for recording important moments with descriptions, featuring hierarchical timestamps, dynamic note management, and automatic file opening capabilities.

## Features
- ✅ **Add Timestamps**: Record timestamps in MM:SS or HH:MM:SS format
- ✅ **Add Notes**: Attach descriptive notes to each timestamp
- ✅ **Automatic Sorting**: Timestamps are automatically sorted chronologically
- ✅ **Duplicate Prevention**: Prevents adding duplicate timestamps
- ✅ **Edit & Delete**: Modify or remove existing timestamp entries
- ✅ **File Operations**: Save and load timestamp collections to/from files
- ✅ **Export Options**: Export timestamps to text format
- ✅ **User-Friendly GUI**: Clean and intuitive Swing-based interface

## Project Structure
```
YoutubeTimestampRecorder/
├── src/
│   └── main/
│       └── java/
│           └── com/
│               └── example/
│                   ├── App.java                      # Main application entry point
│                   ├── TimestampRecorderGUI.java     # Main GUI with modern styling
│                   ├── TimestampService.java         # Core timestamp business logic
│                   ├── TimestampEntry.java           # Data model for timestamps
│                   ├── TimestampType.java            # Enum for main/sub classification
│                   ├── FileService.java              # File operations and auto-open
│                   ├── controller/
│                   │   └── TimestampController.java  # MVC controller layer
│                   ├── model/
│                   │   └── TimestampModel.java       # MVC model layer
│                   └── view/
│                       ├── SwingTimestampView.java   # Modern UI view implementation
│                       ├── TimestampView.java        # View interface
│                       └── components/
│                           ├── UIComponents.java     # Modern UI component factory
│                           ├── DynamicNotesPanel.java       # Dynamic notes management
│                           └── TimeModificationPanel.java   # Time adjustment controls
├── target/
│   └── classes/                                      # Compiled bytecode (build artifacts)
├── pom.xml                                           # Maven build configuration
├── README.md
└── .gitignore                                        # Git ignore rules
```

## How to Use

### Running the Application

#### Option 1: Quick Start (Recommended)
1. **Download**: Get the latest `timestamp-recorder-v3.jar` from the releases
2. **Run**: Double-click the JAR file or use command line:
   ```bash
   java -jar timestamp-recorder-v3.jar
   ```

#### Option 2: Build from Source
1. **Prerequisites**: Ensure Java 8+ is installed (`java -version`)
2. **Build using provided script**:
   ```bash
   # Windows
   build.bat
   
   # Or manually compile:
   javac -d target\classes -cp target\classes src\main\java\com\example\*.java src\main\java\com\example\controller\*.java src\main\java\com\example\model\*.java src\main\java\com\example\view\*.java src\main\java\com\example\view\components\*.java
   ```
3. **Run the application**: 
   ```bash
   java -cp target\classes com.example.App
   # Or run the generated JAR:
   java -jar target\timestamp-recorder-v3.jar
   ```

### Using the Application

#### Adding Timestamps
1. **Enter Timestamp**: Use MM:SS format (e.g., `01:30`) in the timestamp field
2. **Add Notes**: 
   - Enter notes in the dynamic notes section
   - Use **+** button to add multiple notes per timestamp
   - Use **-** button to remove note fields
3. **Choose Type**: 
   - Click **"Add Timestamp"** for main entries
   - Click **"Add Sub-timestamp"** for indented sub-entries (creates hierarchical structure)

#### Time Adjustment Features
- **Fine-tune timestamps** using the time adjustment buttons:
  - **Subtract**: -30s, -15s, -10s, -5s (red buttons)
  - **Add**: +5s, +10s, +15s, +30s (green buttons)
- **Duplicate Detection**: Timestamp field highlights in red if timestamp already exists

#### Managing Timestamps
- **Automatic Sorting**: All timestamps display chronologically regardless of input order
- **Edit Notes**: Click directly in the note column to edit
- **Delete Entries**: Select row and click "Delete Selected"
- **Clear All**: Remove all timestamps with confirmation dialog

#### File Operations
- **Save**: Use File → Save to save your collection in native format
- **Load**: Use File → Load to load previously saved timestamps  
- **Export to Text**: Exports with hierarchical formatting and **automatically opens** the file

## Advanced Features

### Hierarchical Timestamps
Create organized, indented timestamp structures:
```
00:30  Introduction to the topic
    01:15  Key concept explanation  
    02:45  Important example
05:00  Next major section
    05:30  Sub-topic details
    07:20  Related information
```

### Multiple Notes Per Timestamp
- Add multiple related notes to a single timestamp
- Notes are combined with " | " separator in exports
- Dynamic +/- interface for adding/removing note fields

### Smart Formatting
- **Input**: Accepts various formats (`1:30`, `01:30`, `1:30:45`)
- **Display**: Standardizes to MM:SS with leading zeros (`01:30`, `90:45`)
- **Export**: Maintains consistent formatting across all outputs

### Modern UI Design
The application features a contemporary interface with:
- **Professional Color Scheme**: Blue accent colors (#3B82F6) for buttons and borders
- **Enhanced Typography**: Improved font rendering and customizable text sizes
- **Styled Tables**: Professional table appearance with alternating row colors and improved readability
- **Visual Feedback**: Modern button styling with hover effects and visual state indicators
- **Consistent Theming**: Unified design language across all UI components
## Installation Requirements
- **Java Runtime Environment (JRE) 8 or higher** - Required to run the application
- **Operating System**: Windows, macOS, or Linux with Java support

## Development & Building

### From Source Code
1. **Clone the repository**:
   ```bash
   git clone https://github.com/DerekL112211/YoutubeTimestampRecorder.git
   cd YoutubeTimestampRecorder
   ```

2. **Build the project**:
   ```bash
   # Use build script (recommended)
   build.bat
   
   # Or compile manually
   javac -d target\classes -cp target\classes src\main\java\com\example\*.java src\main\java\com\example\controller\*.java src\main\java\com\example\model\*.java src\main\java\com\example\view\*.java src\main\java\com\example\view\components\*.java
   ```

3. **Run the application**:
   ```bash
   java -jar target\timestamp-recorder.jar
   ```

### Using Maven (Optional)
If you prefer Maven for building:
```bash
mvn clean compile exec:java -Dexec.mainClass="com.example.App"
```

## Troubleshooting

### Application Won't Start
- **Check Java Version**: Run `java -version` (needs Java 8+)
- **Compilation Issues**: Ensure UTF-8 encoding with `-encoding UTF-8` flag
- **Missing Classes**: Verify all `.java` files are in `src/main/java/com/example/`

### Timestamp Format Issues
- **Accepted Formats**: `1:30`, `01:30`, `1:30:45`, `01:30:45`
- **Display Format**: Always shows as MM:SS with leading zeros
- **Time Limits**: Minutes and seconds must be 00-59

## License
This project is provided as-is for educational and personal use.

---
**Note**: This application runs entirely offline and does not connect to the internet or YouTube directly. It's a standalone tool for managing timestamp collections with advanced organizational features.