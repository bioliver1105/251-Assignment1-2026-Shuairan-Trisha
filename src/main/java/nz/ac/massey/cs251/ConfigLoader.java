package nz.ac.massey.cs251;

import org.yaml.snakeyaml.Yaml;
import java.awt.*;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Map;

public class ConfigLoader {

    public String fontFamily = "Monospaced";
    public int fontSize = 14;
    public Color textBackground = new Color(235,245,255);
    public Color textForeground = new Color(20,30,50);
    public Color menuBackground = new Color(235,245,235);

    public ConfigLoader(String filePath) {
        try (FileInputStream input = new FileInputStream(filePath)) {
            Yaml yaml = new Yaml();
            Map<String, Object> data = yaml.load(input);

            Map<String, Object> editor = (Map<String, Object>) data.get("editor");
            Map<String, Object> font = (Map<String, Object>) editor.get("font");
            Map<String, Object> colors = (Map<String, Object>) editor.get("colors");

            fontFamily = (String) font.get("family");
            fontSize = (Integer) font.get("size");

            textBackground = parseColor((String) colors.get("textBackground"));
            textForeground = parseColor((String) colors.get("textForeground"));
            menuBackground = parseColor((String) colors.get("menuBackground"));

        } catch (IOException e) {
            System.out.println("Could not load config.yml, using default settings.");
        }
    }

 private Color parseColor(String rgb) {
        String[] parts = rgb.split(",");
        int r = Integer.parseInt(parts[0].trim());
        int g = Integer.parseInt(parts[1].trim());
        int b = Integer.parseInt(parts[2].trim());
        return new Color(r, g, b);
 }

}
