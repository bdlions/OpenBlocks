package codegenerator;

import general.DisplayMessage;
import general.JavaFilterText;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Hashtable;
import java.util.List;

import javax.swing.JDialog;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRootPane;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

import controller.WorkspaceController;

import language.LanguageEntry;
import javax.swing.JButton;
import java.awt.Dialog.ModalityType;
import java.awt.FlowLayout;

public class DisplayCode extends JDialog {

	private JPanel contentPane;
	JTextPane textPane;
	private static JavaFilterText javaFilterText;
	private JPanel buttonPanel;
	private JButton btnSaveAs;
	private JButton btnCopyToClipboard;
	private JButton btnClose;
	
	public static Hashtable syntaxMap;
	
	/**
	 * Create the frame.
	 */
	public DisplayCode(Hashtable syntaxMap) {
		this.syntaxMap = syntaxMap;
		setModal(true);
		
		String displayCodeWindowTitleString = "Displaying Code";
		if (syntaxMap.containsKey(displayCodeWindowTitleString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(displayCodeWindowTitleString);
			displayCodeWindowTitleString = titleEntry.getLabel();
		}
		
		setTitle(displayCodeWindowTitleString);
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		buttonPanel = new JPanel();
		contentPane.add(buttonPanel, BorderLayout.SOUTH);
		
		String saveAsString = "SaveAs";
		if (syntaxMap.containsKey(saveAsString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(saveAsString);
			saveAsString = titleEntry.getLabel();
		}
		btnSaveAs = new JButton(saveAsString);
		btnSaveAs.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				savePressed();
			}
		});
		buttonPanel.setLayout(new FlowLayout(FlowLayout.RIGHT, 5, 5));
		buttonPanel.add(btnSaveAs);
		
		String copyToClipBoardString = "Copy to Clipboard";
		if (syntaxMap.containsKey(copyToClipBoardString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(copyToClipBoardString);
			copyToClipBoardString = titleEntry.getLabel();
		}
		btnCopyToClipboard = new JButton(copyToClipBoardString);
		btnCopyToClipboard.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			    StringSelection selection = new StringSelection(textPane.getText());
			    Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
			    clipboard.setContents(selection, selection);
			    
			    DisplayMessage.printSuccessMessage("Code is saved into clipboard successfully");			    
			}
		});
		buttonPanel.add(btnCopyToClipboard);
		
		String closeString = "Close";
		if (syntaxMap.containsKey(closeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(closeString);
			closeString = titleEntry.getLabel();
		}
		btnClose = new JButton(closeString);
		btnClose.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DisplayCode.this.dispose();
			}
		});
		buttonPanel.add(btnClose);
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
		
		javaFilterText = new JavaFilterText();
		
		String saveString = "Save";
		
		String saveCodeString = "Save Code";
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
