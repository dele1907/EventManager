package de.eventmanager.core.presentation.PresentationHelpers;

import java.util.Map;
import java.util.Optional;

public class EnumHelper {
    public static <E extends Enum<E>> Optional<E> getMapOfStringToEnumConstant(
            String userInput, Class<E> enumClass, Map<String, E> mappings
    ) {
        return Optional.ofNullable(mappings.get(userInput));
    }
}