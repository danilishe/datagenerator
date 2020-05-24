package name.danilishe.dg;

import name.danilishe.dg.api.DataGeneratorFunction;
import name.danilishe.dg.api.ParametrizedDataGenerator;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DataGenerator {
    private final Map<String, DataGeneratorFunction> generators = new ConcurrentHashMap<>();

    /**
     * Находит в предоставленном классе поля, помеченные аннотацией name.danilishe.dg.api.ParametrizedDataGenerator,
     * проверяет что каждое поле имеет тип name.danilishe.dg.api.DataGeneratorFunction и является статическим.
     * затем добавляет в хранилище генераторов все предоставленные регулярные выражения и закрепляет за каждым генератор из поля
     * <p>
     * Предпочтительнее всего для хранения генераторов использовать интерфейсы (так короче)
     *
     * @param classes классы, содержащий статические поля с аннотациями name.danilishe.dg.api.ParametrizedDataGenerator
     * @return количество добавленных генераторов. Количество соответствует добавленным регулярным выражениям, а не количеству
     * реальных функций-генераторов
     */
    public int addGenerators(Class<?>... classes) {
        int gens = 0;
        for (Class<?> clazz : classes) {
            for (Field field : clazz.getDeclaredFields()) {
                if (field.isAnnotationPresent(ParametrizedDataGenerator.class)) {
                    if (field.getType().isAssignableFrom(DataGeneratorFunction.class) && Modifier.isStatic(field.getModifiers())) {
                        String[] regexps = field.getAnnotation(ParametrizedDataGenerator.class).value();
                        try {
                            field.setAccessible(true);
                            final DataGeneratorFunction generator = (DataGeneratorFunction) field.get(null);
                            for (String regexp : regexps) {
                                generators.put(regexp, generator);
                                gens++;
                            }
                        } catch (IllegalAccessException e) {
                            e.printStackTrace();
                        }
                    } else
                        throw new IllegalArgumentException("Поле " + field.getName() + " должно быть static с типом " + DataGeneratorFunction.class);
                } else if (field.getType().isAssignableFrom(DataGeneratorFunction.class) && Modifier.isStatic(field.getModifiers())) {
                    field.setAccessible(true);
                    try {
                        generators.put(field.getName(), (DataGeneratorFunction) field.get(null));
                        gens++;
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                } else if (field.getType().isAssignableFrom(DataGeneratorFunction.class)) {
                    throw new IllegalArgumentException("Поле " + field.getName() + " должно быть static с типом " + DataGeneratorFunction.class);
                }
            }
        }
        return gens;
    }

    /**
     * @param key строка для поиска генератора
     * @return значение, сгенерированное генератором из хранилища генераторов
     * @throws IllegalArgumentException если подходящий генератор не найден
     */
    public String getValue(String key) {
        for (String regexp : generators.keySet()) {
            if (key.matches(regexp)) {
                return generators.get(regexp).process(key, regexp);
            }
        }
        throw new IllegalArgumentException("Нет DataGenerator'а для '" + key + "' !!!\n Доступные генераторы:\n" + String.join("\n", generators.keySet()));
    }
}
