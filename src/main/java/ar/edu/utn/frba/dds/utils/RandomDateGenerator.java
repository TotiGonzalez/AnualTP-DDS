package ar.edu.utn.frba.dds.utils;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.concurrent.ThreadLocalRandom;

public class RandomDateGenerator {

    public static LocalDateTime get() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime oneYearAgo = now.minus(40, ChronoUnit.DAYS);

        long randomSeconds = ThreadLocalRandom.current().nextLong(
                oneYearAgo.toEpochSecond(java.time.ZoneOffset.UTC),
                now.toEpochSecond(java.time.ZoneOffset.UTC)
        );

        return LocalDateTime.ofEpochSecond(randomSeconds, 0, java.time.ZoneOffset.UTC);
    }

}
