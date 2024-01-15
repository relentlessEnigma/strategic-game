package org.example;

public enum EraAge {
    STONE_AGE("Idade da Pedra", 1),
    BRONZE_AGE("Idade do Bronze", 2),
    IRON_AGE("Idade do Ferro", 3),
    MEDIEVAL_AGE("Idade Medával", 4),
    RENAISSANCE("Idade do Renascimento", 5),
    INDUSTRIAL_AGE("Idade Industrial", 6),
    MODERN_AGE("Idade Moderna", 7),
    INFORMATION_AGE("Idade da Informação", 8),
    FUTURE_AGE("Idade Futura", 9);

    private final String eraName;
    private final int level;

    EraAge(String eraName, int level) {
        this.eraName = eraName;
        this.level = level;
    }

    public static EraAge setByLevel(int level) {
        for (EraAge eraAge : EraAge.values()) {
            if (eraAge.level == level) {
                return eraAge;
            }
        }
        return null;
    }

    public String getEraName() {
        return eraName;
    }

    public int getLevel() {
        return this.level;
    }

    public EraAge getNextLevel() {
        int nextLevel = this.level + 1;

        for (EraAge eraAge : EraAge.values()) {
            if (eraAge.level == nextLevel) {
                return eraAge;
            }
        }
        return this;
    }
}
