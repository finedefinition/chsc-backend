package ua.dlc.chscbackend.model;


public enum Ticker {
    AAPL("Apple"),
    MSFT("Microsoft"),
    GOOGL("Alphabet"),
    AMZN("Amazon"),
    FB("Facebook"),
    TSLA("Tesla"),
    JPM("JPMorgan"),
    V("Visa"),
    JNJ("Johnson & Johnson"),
    WMT("Walmart"),
    PG("Procter & Gamble"),
    UNH("UnitedHealth"),
    HD("Home Depot"),
    BAC("Bank of America"),
    PFE("Pfizer"),
    DIS("Walt Disney"),
    VZ("Verizon");

    private final String fullName;

    Ticker(String fullName) {
        this.fullName = fullName;
    }

    public String getFullName() {
        return fullName;
    }
}
