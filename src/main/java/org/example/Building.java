package org.example;

import java.util.List;

public class Building {

    private Boolean isBuilded;
    private int level;
    private int maxLevel;
    private String constructionTypeName;
    private int amountConstructionsAllowed;
    private int constructionMinutes;
    private List<ResourceAmount> resourceCost;
    private List<ResourceAmount> resourceProduction;

    public Building(Boolean isBuilded, ConstructionType constructionType) {
        this.isBuilded = isBuilded;
        this.level = 0;
        this.constructionTypeName = constructionType.getName();
        this.constructionMinutes = constructionType.getBaseConstructionTime();
        this.resourceProduction = constructionType.getBaseResourcesProduction();
        this.amountConstructionsAllowed = constructionType.getAmountConstructionsAllowed();
        this.resourceCost = constructionType.getBaseResourcesCost();
        this.maxLevel = constructionType.getMaxLevel();
    }

    public void build() {
        if(amountConstructionsAllowed > 0) {
            System.out.println("A retirar recursos necessários do teu inventário...");
            try {
                Thread.sleep(getConstructionTimeInMils());
            } catch (InterruptedException e) {
                System.out.println("Construção Falhou: " + getConstructionTypeName());
                throw new RuntimeException(e);
            }

            level = 1;
            isBuilded = true;
            increaseResourceCost();
        } else {
            System.out.println("Já construiste o máximo deste tipo de edificio");
        }
    }

    public void upgrade() {
        if(level < maxLevel) {

            try {
                Thread.sleep(getConstructionTimeInMils());
            } catch (InterruptedException e) {
                System.out.println("Atualização Falhou: " + getConstructionTypeName());
                throw new RuntimeException(e);
            }

            level++;
            constructionMinutes += level * 2;
            increaseResourceCost();
            increaseResourceProductions();
        }
    }

    private void increaseResourceCost() {
        for (ResourceAmount resourceAmount : resourceCost) {
            int baseAmount = resourceAmount.getAmount();
            resourceAmount.setAmount(baseAmount * (level+1));
        }
    }

    private void increaseResourceProductions() {
        for (ResourceAmount resourceAmount : resourceProduction) {
            int baseAmount = resourceAmount.getAmount();
            resourceAmount.setAmount(baseAmount * level);
        }
    }

    public void showDetails() {
        System.out.println(getConstructionTypeName() + " Nivel: " + getLevel());
        System.out.println("-----------------------------------------------");
        System.out.println("Custo de recursos para construção:");
        for (ResourceAmount resourceAmount : resourceCost) {
            System.out.printf("%-10s: %d%n", resourceAmount.getResource(), resourceAmount.getAmount());
        }

        System.out.println("Tempo de construção: " + constructionMinutes + " minutos");

        System.out.println("Produção de recursos por minuto:");
        if(resourceProduction.isEmpty()) {
            System.out.println("Sem Produção por agora");
        } else {
            for (ResourceAmount resourceAmount : resourceProduction) {
                System.out.println(resourceAmount.getResource() + ": " + resourceAmount.getAmount());
            }
        }
        System.out.println("-----------------------------------------------");
    }


    public Boolean getBuilded() {
        return isBuilded;
    }

    public int getConstructionMinutes() {
        return constructionMinutes;
    }

    public int getConstructionTimeInMils() {
        return constructionMinutes * 60000;
    }

    public void setConstructionMinutes(int constructionMinutes) {
        this.constructionMinutes = constructionMinutes;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(int maxLevel) {
        this.maxLevel = maxLevel;
    }

    public void setResourceCost(List<ResourceAmount> resourceCost) {
        this.resourceCost = resourceCost;
    }

    public List<ResourceAmount> getResourceProduction() {
        return resourceProduction;
    }

    public void setResourceProduction(List<ResourceAmount> resourceProduction) {
        this.resourceProduction = resourceProduction;
    }

    public String getConstructionTypeName() {
        return constructionTypeName;
    }

    public void setConstructionTypeName(String constructionTypeName) {
        this.constructionTypeName = constructionTypeName;
    }

    public List<ResourceAmount> getResourceCost() {
        return resourceCost;
    }

    public int getAmountConstructionsAllowed() {
        return amountConstructionsAllowed;
    }

    public void setAmountConstructionsAllowed(int amountConstructionsAllowed) {
        this.amountConstructionsAllowed = amountConstructionsAllowed;
    }
}

