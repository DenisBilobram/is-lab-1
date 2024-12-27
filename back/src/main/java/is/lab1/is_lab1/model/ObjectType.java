package is.lab1.is_lab1.model;

public enum ObjectType {
    CAR(Car.class),
    HUMAN_BEING(HumanBeing.class),
    COORDINATES(Coordinates.class);

    private final Class<?> type;

    ObjectType(Class<?> type) {
        this.type = type;
    }

    public Class<?> getType() {
        return type;
    }
    public static ObjectType fromClass(Class<?> clazz) {
        for (ObjectType actionType : ObjectType.values()) {
            if (actionType.getType().equals(clazz)) {
                return actionType;
            }
        }
        throw new IllegalArgumentException("Неизвестный класс: " + clazz.getName());
    }
}