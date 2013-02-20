package general;
import java.io.File;

/**
 *This class supports only XML file to upload for the application
 */
public class JavaFilter extends javax.swing.filechooser.FileFilter
{
  public boolean accept (File f) {
    return f.getName ().toLowerCase ().endsWith (".pz")
          || f.isDirectory ();
  }

  public String getDescription () {
    return "PZ Files (*.pz)";
  }
}
