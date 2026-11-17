package mn.edu.must.sqat;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
