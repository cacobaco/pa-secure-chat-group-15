package g15.pas.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ConstantsTest {

    @Test
    void testConfigFilePathConstant() {
        // Arrange
        String expectedFilePath = "project.config";

        // Act
        String actualFilePath = Constants.CONFIG_FILE_PATH;

        // Assert
        assertEquals(expectedFilePath, actualFilePath);
    }
}

