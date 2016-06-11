package net.terrocidepvp.treetwerk.configurations;

import java.util.List;

public class Messages {

    private List<String> success;
    private List<String> notEnough;

    Messages(List<String> success, List<String> notEnough) {
        this.success = success;
        this.notEnough = notEnough;
    }

    public List<String> getSuccess() {
        return success;
    }

    public List<String> getNotEnough() {
        return notEnough;
    }

}
