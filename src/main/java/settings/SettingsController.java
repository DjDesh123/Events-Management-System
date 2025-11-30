package settings;

public class SettingsController {

    private SettingsOption currentSettings;

    public SettingsController() {
        this.currentSettings = new SettingsOption();
    }

    public SettingsOption getSettings() {
        return currentSettings;
    }

    public void applySettings(boolean darkMode, double brightness, int fontSize) {
        currentSettings.setDarkMode(darkMode);
        currentSettings.setBrightness(brightness);
        currentSettings.setFontSize(fontSize);

        // Here you could trigger UI refresh if connected to JavaFX
        System.out.println("Settings applied: " + currentSettings.isDarkMode()
                + ", Brightness=" + currentSettings.getBrightness()
                + ", FontSize=" + currentSettings.getFontSize());
    }

    public void signOut() {
        // End user session, reset settings if desired
        currentSettings = new SettingsOption();
        System.out.println("User signed out, settings reset");
    }
}
