package name.danilishe.dg.api;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Паттерн для генератора с возможностью параметризации.
 * Для реализации необходимо создать поле типа name.danilishe.dg.api.DataGeneratorFunction и указать
 * регулярное выражение(я) при совпадении с которым будет производиться генерация значения
 * Группы захвата из регулярного выражения попадут в виде списка параметров в функцию. Количество групп захвата
 * не ограничено
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface ParametrizedDataGenerator {
    /**
     * Регулярные выражения, при совпадении с которыми вызовется аннотированный генератор.
     * {@link !!! ВНИМАТЕЛЬНО ЭКРАНИРУЙТЕ СИМВОЛЫ!!!}
     */
    String[] value();
}
