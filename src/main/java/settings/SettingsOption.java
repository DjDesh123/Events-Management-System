package settings;

public class SettingsOption {
    private boolean darkMode;   // true = dark mode, false = light mode
    private double brightness;  // 0.0 to 1.0, default 1.0
    private int fontSize;       // e.g., 12, 14, 16

    public SettingsOption() {
        this.darkMode = false;    // default light mode
        this.brightness = 1.0;    // max brightness
        this.fontSize = 14;       // default font size
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
        this.fontSize = Math.max(8, Math.min(48, fontSize)); // constrain reasonable sizes
    }
}
