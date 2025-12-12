package settings;

public class SettingsOption {
    private boolean darkMode;
    private double brightness;
    private int fontSize;

    //constructor
    public SettingsOption() {
        this.darkMode = false;
        this.brightness = 1.0;
        this.fontSize = 14;
    }

    // Getters and setters
    public boolean isDarkMode() { return darkMode; }
    public void setDarkMode(boolean darkMode) { this.darkMode = darkMode; }

    public double getBrightness() { return brightness; }
    public void setBrightness(double brightness) {
        this.brightness = Math.max(0.0, Math.min(1.0, brightness));
    }

    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) {
        this.fontSize = Math.max(8, Math.min(48, fontSize));
    }
}
