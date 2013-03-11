package codegenerator.xmlbind.config.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JComboBox;
import javax.swing.JCheckBox;
import javax.swing.JButton;
import javax.swing.ListCellRenderer;

import codegenerator.xmlbind.config.BlockConnector;
import codegenerator.xmlbind.config.BlockGenus;
import codegenerator.xmlbind.config.BlockLangDef;

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JList;
import javax.swing.border.TitledBorder;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

public class AddBlockGenus extends JDialog {

	private JPanel contentPane;
	private final String[] kindValues = {"variable", "data", "command", "function"};
	private BlockLangDef blockLangDef;
	private DefaultListModel listModel;
	private JTextField textFieldIsLabelSuffix;
	private JTextField textFieldIsLabelPrefix;
	private JTextField textFieldName;
	private JTextField textFieldInitLabel;
	private JButton buttonColorChooser;
	private JCheckBox checkBoxIsTerminator;
	private JCheckBox checkBoxIsStarter;
	private JCheckBox checkBoxPageLabelEnabled;
	private JComboBox comboBoxVariable;
	private JCheckBox checkBoxEditableLabel;
	private JCheckBox checkBoxLabelUnique;
	private BlockGenus blockGenus = new BlockGenus();
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	private JList listBlockConnector;
	
	/**
	 * Create the frame.
	 */
	public AddBlockGenus(final BlockLangDef blockLangDef, final DefaultListModel listModel) {
		setTitle("Add block genus");
		setModal(true);
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 727, 405);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(new BorderLayout());
		
		this.blockLangDef = blockLangDef;
		this.listModel = listModel;
		
		JPanel panelComponent = new JPanel();
		panelComponent.setBounds(12, 13, 685, 312);
		contentPane.add(panelComponent, BorderLayout.CENTER);
		panelComponent.setLayout(null);
		
		JLabel label = new JLabel("Is Terminator");
		label.setBounds(429, 86, 119, 16);
		panelComponent.add(label);
		
		checkBoxIsTerminator = new JCheckBox();
		checkBoxIsTerminator.setBounds(556, 77, 113, 25);
		panelComponent.add(checkBoxIsTerminator);
		
		checkBoxIsStarter = new JCheckBox();
		checkBoxIsStarter.setBounds(556, 50, 113, 25);
		panelComponent.add(checkBoxIsStarter);
		
		JLabel label_1 = new JLabel("Is Starter");
		label_1.setBounds(429, 50, 119, 16);
		panelComponent.add(label_1);
		
		JLabel label_2 = new JLabel("Page Label Enabled");
		label_2.setBounds(429, 15, 119, 16);
		panelComponent.add(label_2);
		
		checkBoxPageLabelEnabled = new JCheckBox();
		checkBoxPageLabelEnabled.setBounds(556, 15, 113, 25);
		panelComponent.add(checkBoxPageLabelEnabled);
		
		JLabel label_3 = new JLabel("Is Label Sufix");
		label_3.setBounds(12, 272, 90, 16);
		panelComponent.add(label_3);
		
		textFieldIsLabelSuffix = new JTextField();
		textFieldIsLabelSuffix.setColumns(10);
		textFieldIsLabelSuffix.setBounds(139, 272, 205, 22);
		panelComponent.add(textFieldIsLabelSuffix);
		
		textFieldIsLabelPrefix = new JTextField();
		textFieldIsLabelPrefix.setColumns(10);
		textFieldIsLabelPrefix.setBounds(139, 243, 205, 22);
		panelComponent.add(textFieldIsLabelPrefix);
		
		JLabel label_4 = new JLabel("Is Label Prefix");
		label_4.setBounds(12, 243, 90, 16);
		panelComponent.add(label_4);
		
		textFieldName = new JTextField();
		textFieldName.setColumns(10);
		textFieldName.setBounds(114, 13, 205, 22);
		panelComponent.add(textFieldName);
		
		JLabel label_5 = new JLabel("Name");
		label_5.setBounds(12, 13, 56, 16);
		panelComponent.add(label_5);
		
		JLabel label_6 = new JLabel("Init Label");
		label_6.setBounds(12, 48, 56, 16);
		panelComponent.add(label_6);
		
		textFieldInitLabel = new JTextField();
		textFieldInitLabel.setColumns(10);
		textFieldInitLabel.setBounds(114, 48, 205, 22);
		panelComponent.add(textFieldInitLabel);
		
		comboBoxVariable = new JComboBox(kindValues);
		comboBoxVariable.setBounds(114, 83, 205, 19);
		panelComponent.add(comboBoxVariable);
		
		JLabel label_7 = new JLabel("Kind");
		label_7.setBounds(12, 83, 56, 16);
		panelComponent.add(label_7);
		
		JLabel label_8 = new JLabel("Color");
		label_8.setBounds(12, 115, 56, 16);
		panelComponent.add(label_8);
		
		buttonColorChooser = new JButton();
		buttonColorChooser.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				Color selectedColor = JColorChooser.showDialog(AddBlockGenus.this,
						"Pick a Color", Color.BLACK);
				if(selectedColor!=null)
				{
					buttonColorChooser.setBackground(selectedColor);
				}
			}
		});
		buttonColorChooser.setBackground(Color.BLACK);
		buttonColorChooser.setBounds(114, 115, 22, 25);
		panelComponent.add(buttonColorChooser);
		
		JLabel label_9 = new JLabel("Editable Label");
		label_9.setBounds(12, 147, 90, 16);
		panelComponent.add(label_9);
		
		checkBoxEditableLabel = new JCheckBox();
		checkBoxEditableLabel.setBounds(114, 147, 113, 25);
		panelComponent.add(checkBoxEditableLabel);
		
		JLabel label_10 = new JLabel("Label Unique");
		label_10.setBounds(12, 180, 90, 16);
		panelComponent.add(label_10);
		
		checkBoxLabelUnique = new JCheckBox();
		checkBoxLabelUnique.setBounds(114, 180, 113, 25);
		panelComponent.add(checkBoxLabelUnique);
		
		JLabel label_11 = new JLabel("Is Label Value");
		label_11.setBounds(12, 214, 90, 16);
		panelComponent.add(label_11);
		
		JCheckBox checkBoxIsLabelValue = new JCheckBox();
		checkBoxIsLabelValue.setBounds(114, 210, 113, 25);
		panelComponent.add(checkBoxIsLabelValue);
		
		JPanel panelBlockConnector = new JPanel();
		panelBlockConnector.setBounds(429, 115, 258, 185);
		panelBlockConnector.setBorder(new TitledBorder(new LineBorder(new Color(0, 0, 0)), "Block Connectors", TitledBorder.LEADING, TitledBorder.ABOVE_TOP, null, Color.GRAY));
		
		panelComponent.add(panelBlockConnector);
		panelBlockConnector.setLayout(new BorderLayout(0, 0));
		
		JPanel panel = new JPanel();
		panelBlockConnector.add(panel, BorderLayout.SOUTH);
		
		listBlockConnector = new JList();
		listBlockConnector.setModel(new DefaultListModel());
		panelBlockConnector.add(listBlockConnector, BorderLayout.CENTER);
		listBlockConnector.setCellRenderer(new ListCellRenderer<BlockConnector>() {
			@Override
			public Component getListCellRendererComponent(
					JList<? extends BlockConnector> list, BlockConnector value,
					int index, boolean isSelected, boolean cellHasFocus) {
				JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,isSelected, cellHasFocus);
				renderer.setText(value.getConnectorType());
				return renderer;
			}
		});
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				if(blockGenus.getBlockConnectors() == null){
					blockGenus.setBlockConnectors(new ArrayList<BlockConnector>());
				}
				AddBlockConnector blockConnector = new AddBlockConnector(blockLangDef, blockGenus, (DefaultListModel)listBlockConnector.getModel());
				blockConnector.setVisible(true);
			}
		});
		panel.add(btnAdd);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DefaultListModel model = (DefaultListModel) listBlockConnector.getModel();
			      if (model.size() > 0 && listBlockConnector.getSelectedIndex() >= 0 ) {
			    	 blockLangDef.getBlockGenuses().remove(listBlockConnector.getSelectedValue());
			         model.remove(listBlockConnector.getSelectedIndex());
			      }
			      else{
			    	  JOptionPane.showMessageDialog(null, "Please select an item from the list.");
			      }
			}
		});
		panel.add(btnDelete);
		

		
		JPanel panelButton = new JPanel();
		contentPane.add(panelButton, BorderLayout.SOUTH);
		
		JButton btnOk = new JButton("Ok");
		btnOk.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				blockGenus.setName(textFieldName.getText());
				blockGenus.setInitLabel(textFieldInitLabel.getText());
				blockGenus.setKind(comboBoxVariable.getSelectedItem().toString());
				Color selectedColor = buttonColorChooser.getBackground();
				
				blockGenus.setColor(selectedColor.getRed() +" " + selectedColor.getGreen() + " " + selectedColor.getBlue());
				
				listModel.addElement(blockGenus);
				blockLangDef.getBlockGenuses().add(blockGenus);
				AddBlockGenus.this.dispose();
				
				blockLangDef.toXML();
			}
		});
		panelButton.add(btnOk);
		
		JButton btnCancel = new JButton("Cancel");
		btnCancel.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddBlockGenus.this.dispose();
			}
		});
		panelButton.add(btnCancel);
	}
}
