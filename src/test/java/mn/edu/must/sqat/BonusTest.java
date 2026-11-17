package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("bonus-ийн өгөгдлийн урсгалын хосууд")
class BonusTest {
    @Test
    @DisplayName("B-AD-01 мөр 35,36,37,38,40,41,43,46,49,51,54 defs бүгдийг тодорхойлно")
    void allDefinitionsAreReachedByOneInput() {
        // Arrange
        int[] attendance = {1, 0, 1, 1, 1, 1, 1};
        int threshold = 6;
        // Act
        int actual = DataFlowTargets.bonus(attendance, threshold);
        // Assert
        assertEquals(8, actual);
    }
    @Test
    @DisplayName("B-DU-02 мөр 49→54 score-ийн эхний def-ээс нэмэлт онооны хэрэглээ")
    void scoreInitializationFlowsToStreakBonus() {
        // Arrange
        int[] attendance = {1, 1, 1, 1, 1};
        int threshold = 6;
        // Act
        int actual = DataFlowTargets.bonus(attendance, threshold);
        // Assert
        assertEquals(3, actual);
    }

    @Test
    @DisplayName("B-DU-03 мөр 49→56 score-ийн эхний def-ээс return use")
    void scoreInitializationFlowsDirectlyToReturn() {
        // Arrange
        int[] attendance = {};
        int threshold = 1;
        // Act
        int actual = DataFlowTargets.bonus(attendance, threshold);
        // Assert
        assertEquals(0, actual);
    }

    @Test
    @DisplayName("B-DU-04 мөр 51→56 score=5 def-ээс return; M2 wrong-variable fault-ыг барина")
    void fragmentedAttendanceDoesNotEarnFiveDayStreakBonus() {
        // Arrange
        int[] attendance = {1, 0, 1, 1, 1, 1, 0};
        int threshold = 5;
        // Act
        int actual = DataFlowTargets.bonus(attendance, threshold);
        // Assert
        assertEquals(5, actual);
    }

    @Test
    @DisplayName("B-INVALID-01 мөр 32–33 null attendance-г хориглох")
    void rejectsNullAttendance() {
        // Arrange
        int[] attendance = null;
        int threshold = 0;
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> DataFlowTargets.bonus(attendance, threshold));
    }

    @Test
    @DisplayName("B-INVALID-02 мөр 32–33 сөрөг threshold-г хориглох")
    void rejectsNegativeThreshold() {
        // Arrange
        int[] attendance = {};
        int threshold = -1;
        // Act & Assert
        assertThrows(IllegalArgumentException.class,
            () -> DataFlowTargets.bonus(attendance, threshold));
    }

}
