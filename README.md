# YouTube Timestamp Recorder

## Overview
YouTube Timestamp Recorder is a Java desktop application that helps you create and manage timestamps with notes for YouTube videos or any other media content. The application provides an intuitive GUI for recording important moments with descriptions, making it easy to organize and export your timestamps.

## Features
- ✅ **Add Timestamps**: Record timestamps in MM:SS or HH:MM:SS format
- ✅ **Add Notes**: Attach descriptive notes to each timestamp
- ✅ **Automatic Sorting**: Timestamps are automatically sorted chronologically
- ✅ **Duplicate Prevention**: Prevents adding duplicate timestamps
- ✅ **Edit & Delete**: Modify or remove existing timestamp entries
- ✅ **File Operations**: Save and load timestamp collections to/from files
- ✅ **Export Options**: Export timestamps in various formats
- ✅ **User-Friendly GUI**: Clean and intuitive Swing-based interface

## Project Structure
```
youtube-timestamp-recorder/
├── target/
│   └── classes/
│       ├── timestamp-recorder.jar
│       ├── timestamp-recorder-v2.jar
│       └── MANIFEST.MF
├── README.md
└── pom.xml
```

## How to Use

### Running the Application
1. **Download the JAR file**: Get `timestamp-recorder-v2.jar` from the `target/classes/` directory
2. **Run the application**: 
   ```bash
   java -jar timestamp-recorder-v2.jar
   ```
   Or simply double-click the JAR file if Java is properly configured on your system

### Using the Application
1. **Adding Timestamps**:
   - Enter a timestamp in MM:SS or HH:MM:SS format (e.g., `1:23` or `1:23:45`)
   - Add an optional note describing what happens at that time
   - Click "Add Timestamp" to save the entry

2. **Managing Timestamps**:
   - View all timestamps in the table below the input area
   - Timestamps are automatically sorted chronologically
   - Select a row and click "Remove Selected" to delete an entry
   - Double-click a note cell to edit it

3. **File Operations**:
   - **Save**: Use File → Save to save your timestamps to a file
   - **Load**: Use File → Load to load previously saved timestamps
   - **Export**: Use File → Export to export timestamps in different formats
   - **Clear**: Use File → Clear All to remove all timestamps

4. **Timestamp Format**:
   - Accepts both `MM:SS` (e.g., `5:30`) and `HH:MM:SS` (e.g., `1:05:30`) formats
   - Invalid formats will be rejected with an error message

## Installation Requirements
- **Java Runtime Environment (JRE) 8 or higher** - Required to run the application
- **Operating System**: Windows, macOS, or Linux with Java support

## Development Requirements (Optional)
If you want to build the project from source:
- Java Development Kit (JDK) 8 or higher
- Apache Maven 3.6 or higher

## Building from Source (Optional)
If you have the source code and want to build the project yourself:

1. **Build the project**:
   ```bash
   mvn clean install
   ```

2. **Run the application**:
   ```bash
   mvn exec:java -Dexec.mainClass="com.example.App"
   ```

## Troubleshooting

### Application Won't Start
- Ensure Java is properly installed: `java -version`
- Make sure you're using Java 8 or higher
- Try running from command line: `java -jar timestamp-recorder-v2.jar`

### Invalid Timestamp Format
- Use MM:SS format (e.g., `5:30` for 5 minutes 30 seconds)
- Use HH:MM:SS format (e.g., `1:05:30` for 1 hour 5 minutes 30 seconds)
- Ensure minutes and seconds are between 00-59

### File Operations Issues
- Ensure you have write permissions in the directory where you're saving files
- Check that the file path is accessible and not locked by another application

## Use Cases
- **YouTube Video Analysis**: Record important moments while watching educational content
- **Meeting Notes**: Track key discussion points with timestamps
- **Podcast Timestamps**: Mark interesting segments for later reference
- **Video Editing**: Plan cut points and highlight moments
- **Research**: Document specific moments in video/audio content

## License
This project is provided as-is for educational and personal use.

---
**Note**: This application runs entirely offline and does not connect to the internet or YouTube directly. It's a standalone tool for managing timestamp collections.