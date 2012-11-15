package general;
import java.io.File;

/**
 *This class supports only XML file to upload for the application
 */
public class JavaFilterText extends javax.swing.filechooser.FileFilter
{
  public boolean accept (File f) {
    return f.getName ().toLowerCase ().endsWith (".txt")
          || f.isDirectory ();
  }

  public String getDescription () {
    return "Text Files (*.txt)";
  }
}
