package name.danilishe.dg.api;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

@FunctionalInterface
public interface DataGeneratorFunction {
    String getData(String[] params);

    /**
     * Вспомогательный метод для формирования массива параметров из строки для замены и вызова лямбды
     * @param data строка, которая соответствует regexp. Из неё могут извлекаться дополнительные параметры
     *           для вызова лямбды.
     * @param regexp регулярное выражение полученное из аннотации name.danilishe.dg.api.ParametrizedDataGenerator
     *               может содержать неограниченное количество групп захвата, которые будут извлечены из data и переданы
     *               в getData в виде массива строк. Если регулярное выражение не содержало групп захвата, будет
     *               передан пустой массив
     * @return результат выполнения getData с извлечёнными параметрами
     */
    default String process(String data, String regexp) {
        final Matcher matcher = Pattern.compile(regexp).matcher(data);
        if (matcher.find()) {
            final String[] params = new String[matcher.groupCount()];
            for (int i = 0; i < matcher.groupCount(); i++) {
                params[i] = matcher.group(i + 1);
            }
            return getData(params);
        } else throw new IllegalArgumentException("'" + data + "' не соответствует regexp '" + regexp + "'");
    }
}
