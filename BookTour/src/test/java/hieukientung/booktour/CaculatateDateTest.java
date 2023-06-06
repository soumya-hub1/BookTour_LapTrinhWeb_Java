package hieukientung.booktour;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class CaculatateDateTest {
    public static void main(String[] args) {
        LocalDate startDate = LocalDate.of(2023, 1, 1);
        LocalDate endDate = LocalDate.of(2023, 2, 1);

        // Tính số ngày giữa hai ngày
        long dayDistance = ChronoUnit.DAYS.between(startDate, endDate) + 1;

        System.out.println("Số ngày giữa hai ngày: " + dayDistance);
    }
}
