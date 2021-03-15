package graduation.y2020m10;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DayHourMinTest {

    @Test
    void minTest() {
        DayHourMin dayHourMin = new DayHourMin(59);

        assertEquals(0, dayHourMin.getDay());
        assertEquals(0, dayHourMin.getHour());
        assertEquals(59, dayHourMin.getMin());
    }

    @Test
    void hourTest() {
        DayHourMin dayHourMin = new DayHourMin(71);

        assertEquals(0, dayHourMin.getDay());
        assertEquals(1, dayHourMin.getHour());
        assertEquals(11, dayHourMin.getMin());
    }

    @Test
    void daxTest() {
        DayHourMin dayHourMin = new DayHourMin(1571);

        assertEquals(1, dayHourMin.getDay());
        assertEquals(2, dayHourMin.getHour());
        assertEquals(11, dayHourMin.getMin());
    }

}