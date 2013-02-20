package codegenerator;

import general.JavaFilterText;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

import controller.WorkspaceController;

import language.LanguageEntry;

public class DisplayCode extends JFrame {

	private JPanel contentPane;
	JTextPane textPane;
	public static JMenu menuSave;
	public static JMenuItem menuItemSaveCode;
	private static JavaFilterText javaFilterText;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					DisplayCode frame = new DisplayCode();
					frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public DisplayCode() {
		setTitle("Displaying Code.");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		JMenuBar menuBar = new JMenuBar();
		setJMenuBar(menuBar);
		
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		
		javaFilterText = new JavaFilterText();
		
		String saveString = "Save";		
		menuSave = new JMenu(saveString);
		menuBar.add(menuSave);
		
		String saveCodeString = "Save Code";		
		menuItemSaveCode = new JMenuItem(saveCodeString);
		menuSave.add(menuItemSaveCode);
		menuItemSaveCode.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				savePressed();
			}
		});
	}
	
	public void savePressed()
	{
		File file = new File("");        
        JFileChooser fc = new JFileChooser();
        // Start in current directory
        fc.setCurrentDirectory(new File("."));
        // Set filter for text files.
        fc.setFileFilter(javaFilterText);
        // Set to a specific name for saving
        fc.setSelectedFile(file);
        // Open chooser dialog
        int result = fc.showSaveDialog(fc);

        if (result == JFileChooser.APPROVE_OPTION) {
            if(!fc.getSelectedFile().getName().endsWith(".txt")) 
            {
            	file = new File(fc.getSelectedFile()+".txt");
            }
            else
            {
            	file = fc.getSelectedFile();
            }
            if (file.exists()) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Overwrite existing file?", "Confirm Overwrite",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            //writing text content into file
            writeFile(file, this.getCode());
        } 
	}
	
	public static boolean writeFile(File file, String dataString) {
    	String newLine = System.getProperty("line.separator");
    	dataString = dataString.replaceAll("\n", newLine);
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.print(dataString);
            out.flush();
            out.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
	
	public void setCode(String code)
	{
		textPane.setText(code);
	}
	
	public String getCode()
	{
		return textPane.getText();
	}
	
}
