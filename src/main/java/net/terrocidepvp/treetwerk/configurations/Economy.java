package net.terrocidepvp.treetwerk.configurations;

public class Economy {

    private boolean enabled;
    private String mode;
    private int amount;

    Economy(boolean enabled, String mode, int amount) {
        this.enabled = enabled;
        this.mode = mode;
        this.amount = amount;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public String getMode() {
        return mode;
    }

    public int getAmount() {
        return amount;
    }

}
