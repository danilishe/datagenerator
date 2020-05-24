package example;

import name.danilishe.dg.api.DataGeneratorFunction;
import name.danilishe.dg.api.ParametrizedDataGenerator;

import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;


public interface Fixture {
    @ParametrizedDataGenerator("date:([YMdHms:\\s-]+)")
    DataGeneratorFunction dateGenerator = params -> DateTimeFormatter.ofPattern(params[0]).format(ZonedDateTime.now());

    @ParametrizedDataGenerator("date:\\+1m")
    DataGeneratorFunction datePlusMinute = params -> DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Z")).format(ZonedDateTime.now().plusMinutes(1)) + "new!";

    @ParametrizedDataGenerator("random.*")
    DataGeneratorFunction newRandom = params -> "New random " + new Random().nextInt(Integer.MAX_VALUE);

    DataGeneratorFunction datePlusMonth = params -> DateTimeFormatter.ISO_DATE_TIME.withZone(ZoneId.of("Z")).format(ZonedDateTime.now().plusMonths(1)) + "!!!";
}