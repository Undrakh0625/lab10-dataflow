package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("gcd-ийн өгөгдлийн урсгалын хосууд")
class GcdTest {
    @Test
    @DisplayName("GCD-01 мөр 13→16, 13→20; 13(y)→15; 16(tmp)→18; 17(x)→16,20; 18(y)→15,16")
    void positiveInputsThenLoopExitAndCarryDefinitionsToUses() {
        // Arrange
        int x = 12;
        int y = 8;
        // Act
        int actual = DataFlowTargets.gcd(x, y);
        // Assert
        assertEquals(4, actual);
    }

    @Test
    @DisplayName("GCD-02 мөр 13(x)→20 болон 13(y)→15(false) def-clear хос")
    void zeroSecondArgumentUsesParameterDefinitionsAtExit() {
        // Arrange
        int x = 7;
        int y = 0;
        // Act
        int actual = DataFlowTargets.gcd(x, y);
        // Assert
        assertEquals(7, actual);
    }
}
