import example.BasicDataGenerators;
import example.Fixture;
import name.danilishe.dg.DataGenerator;
import org.junit.Before;
import org.junit.Test;

public class DataGeneratorTest {
    DataGenerator dg;

    @Before
    public void setUp() throws Exception {
        dg = new DataGenerator();
        final int add = dg.addGenerators(BasicDataGenerators.class);
        System.out.println("Добавлено " + add + " генераторов");
    }

    @Test
    public void getValue() {
        System.out.println(dg.getValue("random"));
        System.out.println(dg.getValue("random1"));
        System.out.println(dg.getValue("random2"));
        dg.addGenerators(Fixture.class);
        System.out.println(dg.getValue("random"));
        System.out.println(dg.getValue("random1"));
        System.out.println(dg.getValue("random2"));


        System.out.println(dg.getValue("date:YYYY-MM-dd HH:mm"));
        System.out.println(dg.getValue("date:YYYY-MM-dd'T'HH:mm:ss z"));
        System.out.println(dg.getValue("date:Z YYYY-MM-dd'T'HH:mm:ss"));
        System.out.println(dg.getValue("date"));
        System.out.println(dg.getValue("date:+1H HH mm:ss"));
        System.out.println(dg.getValue("date:+1m-1w"));
        System.out.println(dg.getValue("date:-1m -1w"));
        System.out.println(dg.getValue("date:-1Y+2d"));
        System.out.println(dg.getValue("date:+1w"));
        System.out.println(dg.getValue("date:+1m"));
        System.out.println(dg.getValue("date:+1s"));
        System.out.println(dg.getValue("date:+1M +1Y"));
        System.out.println(dg.getValue("date:+1M +1Y"));
        System.out.println(dg.getValue("date:+1M +1Y YYYY-MM/dd"));
        System.out.println(dg.getValue("datePlusMonth"));
    }
}
