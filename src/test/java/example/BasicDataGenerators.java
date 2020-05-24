package example;

import com.github.javafaker.Faker;
import name.danilishe.dg.api.DataGeneratorFunction;
import name.danilishe.dg.api.ParametrizedDataGenerator;

import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalUnit;


public interface BasicDataGenerators {
    @ParametrizedDataGenerator("ИНН организации")
    DataGeneratorFunction orgINN = params -> Faker.instance().regexify("\\d{10}");

    @ParametrizedDataGenerator("random.*")
    DataGeneratorFunction random = params -> new Faker().regexify("\\w{3,25}");

    /**
     * at 31/12/2000
     * date:dd/MM/YYYY -> 31/12/2000
     * date: dd/MM/YYYY HH:mm -> '31/12/2000 13:53'
     * <p>
     * Вы водит текущую дату в дефолтном или указанном формате
     * Особенность! Все пробелы в начале и конце формата обрезаются
     */
    @ParametrizedDataGenerator("date:?([GyYMwWDdFEuaHkKhmsSzZXT\\s:';_/\\.,-]+|)")
    DataGeneratorFunction CURRENT_DATE_FORMATTED = params -> {
        DateTimeFormatter dtf = DateTimeFormatter.ISO_INSTANT;
        final String formatPattern = params[params.length - 1];
        if (!formatPattern.isEmpty())
            dtf = DateTimeFormatter.ofPattern(formatPattern.trim());
        return dtf.format(ZonedDateTime.now());
    };

    /**
     * at 31/12/2000
     * date: +7d YYYY-MM-dd -> 2001-01-07
     * date: -1w YYYY-MM-dd -> 2000-12-24
     * date: -1w +1M -> 2001-01-24T13:53:22 +0200
     * date:-1w+1M-1d+1d -> 2001-01-24T13:53:22 +0200
     * date:+1Y -> 2001-12-31T13:53:22 +0200 (default is ISO_INSTANT format)
     * <p>
     * Вы водит текущую дату в дефолтном или указанном формате с неограниченным количеством смещений даты
     * Требования:
     * знак всегда рядом с цифрой,
     * буква периода может быть рядом с цифрой, но не более 1 пробела между ними
     */
    @ParametrizedDataGenerator("date:(?:\\s?([+-]\\d+)([YMwdHms]))+(\\s[GyYMwWDdFEuaHkKhmsSzZXT\\s:';_/\\.,-]+|)")
    DataGeneratorFunction SHIFTED_DATE = params -> {
        DateTimeFormatter dtf = DateTimeFormatter.ISO_INSTANT;
        final String formatPattern = params[params.length - 1];
        if (!formatPattern.isEmpty())
            dtf = DateTimeFormatter.ofPattern(formatPattern.trim()); // в передней части вылазит пробел
        ZonedDateTime time = ZonedDateTime.now();
        for (int i = 0; i < params.length - 1; i += 2) {
            if (params[i] == null) continue;
            final int amount = Integer.parseInt(params[i]);
            time = time.plus(amount, chronoUnitByChar(params[i + 1]));
        }
        return dtf.format(time);
    };

    static TemporalUnit chronoUnitByChar(String code) {
        switch (code) {
            case "Y":
                return ChronoUnit.YEARS;
            case "M":
                return ChronoUnit.MONTHS;
            case "w":
                return ChronoUnit.WEEKS;
            case "d":
                return ChronoUnit.DAYS;
            case "H":
                return ChronoUnit.HOURS;
            case "m":
                return ChronoUnit.MINUTES;
            case "s":
            default:
                return ChronoUnit.SECONDS;
        }
    }
}
