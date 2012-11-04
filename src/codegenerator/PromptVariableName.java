package codegenerator;
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


public class PromptVariableName extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JComboBox comboBox;
	private String[] types = {"integer", "double", "string", "boolean"};

	private int options = JOptionPane.CANCEL_OPTION;
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		try {
			PromptVariableName dialog = new PromptVariableName();
			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
			dialog.setVisible(true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Create the dialog.
	 */
	public PromptVariableName() {
		setBounds(100, 100, 450, 213);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWeights = new double[]{0.0, 0.0, 1.0};

		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblNewLabel = new JLabel("Name");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.EAST;
			gbc_lblNewLabel.insets = new Insets(0, 0, 5, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 0;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			JLabel blankLabel = new JLabel("        ");
			GridBagConstraints gbc_blankLabel = new GridBagConstraints();
			gbc_blankLabel.insets = new Insets(0, 0, 5, 5);
			gbc_blankLabel.gridx = 1;
			gbc_blankLabel.gridy = 0;
			contentPanel.add(blankLabel, gbc_blankLabel);
		}
		{
			textField = new JTextField();
			GridBagConstraints gbc_textField = new GridBagConstraints();
			gbc_textField.anchor = GridBagConstraints.WEST;
			gbc_textField.insets = new Insets(0, 0, 5, 0);
			gbc_textField.gridx = 2;
			gbc_textField.gridy = 0;
			contentPanel.add(textField, gbc_textField);
			textField.setColumns(20);
		}
		{
			JLabel lblNewLabel = new JLabel("Type");
			GridBagConstraints gbc_lblNewLabel = new GridBagConstraints();
			gbc_lblNewLabel.anchor = GridBagConstraints.WEST;
			gbc_lblNewLabel.insets = new Insets(0, 0, 0, 5);
			gbc_lblNewLabel.gridx = 0;
			gbc_lblNewLabel.gridy = 1;
			contentPanel.add(lblNewLabel, gbc_lblNewLabel);
		}
		{
			JLabel blankLabel = new JLabel("        ");
			GridBagConstraints gbc_blankLabel = new GridBagConstraints();
			gbc_blankLabel.insets = new Insets(0, 0, 0, 5);
			gbc_blankLabel.anchor = GridBagConstraints.EAST;
			gbc_blankLabel.gridx = 1;
			gbc_blankLabel.gridy = 1;
			contentPanel.add(blankLabel, gbc_blankLabel);
		}
		
		contentPanel.setLayout(gbl_contentPanel);
		{
			comboBox = new JComboBox(types);
			GridBagConstraints gbc_comboBox = new GridBagConstraints();
			gbc_comboBox.anchor = GridBagConstraints.WEST;
			gbc_comboBox.gridx = 2;
			gbc_comboBox.gridy = 1;
			contentPanel.add(comboBox, gbc_comboBox);
		}
		
		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);
		{
			JButton okButton = new JButton("OK");
			okButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					options = JOptionPane.OK_OPTION;
					dispose();
				}
			});
			okButton.setActionCommand("OK");
			buttonPane.add(okButton);
			getRootPane().setDefaultButton(okButton);
		}
		{
			JButton cancelButton = new JButton("Cancel");
			cancelButton.addActionListener(new ActionListener() {
				public void actionPerformed(ActionEvent e) {
					options = JOptionPane.CANCEL_OPTION;
					dispose();
				}
			});
			cancelButton.setActionCommand("Cancel");
			buttonPane.add(cancelButton);
		}
		
	}
	
	public String getValue()
	{
		return textField.getText();
	}
	public String getVariableType()
	{
		return comboBox.getSelectedItem().toString();
	}
	public int getOption()
	{
		return options;
	}

}
