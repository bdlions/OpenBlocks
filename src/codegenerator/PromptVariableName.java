package codegenerator;
import general.DisplayMessage;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.Insets;
import java.util.Hashtable;

import language.LanguageEntry;


public class PromptVariableName extends JDialog {

	private String[] types = {"integer", "double", "string", "boolean"};
	private int options = JOptionPane.CANCEL_OPTION;
	
	private JTextField textFieldVariableName = new JTextField();
	private JLabel labelVariableName;
	private JLabel labelVariableType = new JLabel();
	private JComboBox comboBoxVariableType;
	private JButton buttonOk;
	private JButton buttonCancel;
	
	public static Hashtable syntaxMap;
	
		/**
	 * Create the dialog.
	 */
	public PromptVariableName(Hashtable syntaxMap) 
	{
		this.syntaxMap = syntaxMap;
		String addVariableTitleString = "New Variable";
		if (syntaxMap.containsKey(addVariableTitleString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(addVariableTitleString);
			addVariableTitleString = titleEntry.getLabel();
		}
		setTitle(addVariableTitleString);
		
		setBounds(100, 100, 350, 190);
		getContentPane().setLayout(null);
		
		String nameString = "Name";
		if (syntaxMap.containsKey(nameString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(nameString);
			nameString = titleEntry.getLabel();
		}
		labelVariableName = new JLabel(nameString);
		getContentPane().add(labelVariableName);
		labelVariableName.setBounds(20,20,100,26);
		
		textFieldVariableName.setText("");
		getContentPane().add(textFieldVariableName);
		textFieldVariableName.setBounds(130,20,150,26);
		
		String typeString = "Type";
		if (syntaxMap.containsKey(typeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(typeString);
			typeString = titleEntry.getLabel();
		}
		labelVariableType = new JLabel(typeString);
		getContentPane().add(labelVariableType);
		labelVariableType.setBounds(20,60,100,26);
		
		comboBoxVariableType = new JComboBox(types);
		getContentPane().add(comboBoxVariableType);
		comboBoxVariableType.setBounds(130,60,150,26);
		
		String okString = "Ok";
		if (syntaxMap.containsKey(okString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(okString);
			okString = titleEntry.getLabel();
		}
		buttonOk = new JButton(okString);
		getContentPane().add(buttonOk);
		buttonOk.setBounds(130,110,80,26);
		buttonOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(!Variable.isValidVariableName(textFieldVariableName.getText())){
					DisplayMessage.printErrorMessage("This name is invalid. Please add a vaild variable name");												
				}
				else{
					options = JOptionPane.OK_OPTION;
					dispose();
				}
			}
		});
		
		String cancelString = "Cancel";
		if (syntaxMap.containsKey(cancelString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(cancelString);
			cancelString = titleEntry.getLabel();
		}
		buttonCancel = new JButton(cancelString);
		getContentPane().add(buttonCancel);
		buttonCancel.setBounds(230,110,80,26);
		buttonCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				options = JOptionPane.CANCEL_OPTION;
				dispose();
			}
		});		
	}
	
	public String getValue()
	{
		return textFieldVariableName.getText();
	}
	public String getVariableType()
	{
		return comboBoxVariableType.getSelectedItem().toString();
	}
	public int getOption()
	{
		return options;
	}	
}
