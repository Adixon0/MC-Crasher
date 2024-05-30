package cb.sektory.mccrasher;

public enum CrusherType {
    EXPLOSION,
    POSITION;

    public static CrusherType getFromString(String s) {

        for (CrusherType type : values()) {
            if (type.name().toLowerCase().contains(s.toLowerCase())) {
                return type;
            }
        }

        return null;

    }

}