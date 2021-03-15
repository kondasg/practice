package graduation.y2020m10;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SeriesTest {

    Series series = new Series();

    @Test
    void isValidDate() {
        assertFalse(series.isValidDate("1234-1-4"));
        assertFalse(series.isValidDate("1234-11-44"));
        assertFalse(series.isValidDate("2020-11-11"));
        assertTrue(series.isValidDate("2020.11.11"));
    }

    @Test
    void dayOfWeek() {
        assertEquals("sze", series.dayOfWeek(2017, 10, 18));
        assertEquals("p", series.dayOfWeek(2017, 10, 20));

    }
}