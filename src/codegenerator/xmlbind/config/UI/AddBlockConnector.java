package codegenerator.xmlbind.config.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JTextField;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.DefaultComboBoxModel;
import javax.swing.ListCellRenderer;

import codegenerator.xmlbind.config.BlockConnector;
import codegenerator.xmlbind.config.BlockConnectorShape;
import codegenerator.xmlbind.config.BlockGenus;
import codegenerator.xmlbind.config.BlockLangDef;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class AddBlockConnector extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textFieldLabel;
	private BlockGenus blockGenus;
	private DefaultListModel listModel;
	private BlockLangDef blockLangDef;
	private JComboBox comboBoxConnectorKind;
	private JComboBox comboBoxConnectorType;
	
	/**
	 * Create the dialog.
	 */
	public AddBlockConnector(BlockLangDef blockLangDef, BlockGenus blockGenus, DefaultListModel listModel) {
		setBounds(100, 100, 311, 304);
		setModal(true);
		
		this.blockLangDef = blockLangDef;
		this.blockGenus = blockGenus;
		this.listModel = listModel;
		
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		JLabel lblLabel = new JLabel("label");
		lblLabel.setBounds(12, 27, 56, 16);
		contentPanel.add(lblLabel);
		
		textFieldLabel = new JTextField();
		textFieldLabel.setBounds(124, 24, 161, 22);
		contentPanel.add(textFieldLabel);
		textFieldLabel.setColumns(10);
		
		JLabel lblLabelEditable = new JLabel("label Editable");
		lblLabelEditable.setBounds(12, 59, 104, 16);
		contentPanel.add(lblLabelEditable);
		
		JCheckBox chckbxNewCheckBox = new JCheckBox();
		chckbxNewCheckBox.setBounds(124, 55, 113, 25);
		contentPanel.add(chckbxNewCheckBox);
		
		JLabel lblConnectorType = new JLabel("Connector Type");
		lblConnectorType.setBounds(12, 88, 104, 16);
		contentPanel.add(lblConnectorType);
		
		
		
		comboBoxConnectorType = new JComboBox(blockLangDef.getBlockConnectorShapes().toArray());
		comboBoxConnectorType.setBounds(124, 89, 161, 22);
		comboBoxConnectorType.setRenderer(new ListCellRenderer<BlockConnectorShape>() {
			@Override
			public Component getListCellRendererComponent(
					JList<? extends BlockConnectorShape> list,
					BlockConnectorShape value, int index, boolean isSelected,
					boolean cellHasFocus) {
				// TODO Auto-generated method stub
				DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
				JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,isSelected, cellHasFocus);
				renderer.setText(value.getShapeType());
				return renderer;
			}
		});
		
		contentPanel.add(comboBoxConnectorType);
		
		
		JLabel lblNewLabel = new JLabel("Position Type");
		lblNewLabel.setBounds(12, 127, 104, 16);
		contentPanel.add(lblNewLabel);
		
		JComboBox comboBoxPositionType = new JComboBox(new String[] {"single", "mirror", "bottom"});
		comboBoxPositionType.setBounds(124, 124, 161, 22);
		contentPanel.add(comboBoxPositionType);
		
		JLabel lblConnectoryKind = new JLabel("Connectory Kind");
		lblConnectoryKind.setBounds(12, 156, 104, 16);
		contentPanel.add(lblConnectoryKind);
		
		comboBoxConnectorKind = new JComboBox(new String[] {"plug", "socket"});
		comboBoxConnectorKind.setBounds(124, 159, 161, 22);
		contentPanel.add(comboBoxConnectorKind);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
				BlockConnector blockConnector = new BlockConnector();
				blockConnector.setLabel(textFieldLabel.getText());
				blockConnector.setConnectorKind(comboBoxConnectorKind.getSelectedItem().toString());
				blockConnector.setConnectorType(((BlockConnectorShape)comboBoxConnectorType.getSelectedItem()).getShapeType());
				
				AddBlockConnector.this.listModel.addElement(blockConnector);
				AddBlockConnector.this.blockGenus.getBlockConnectors().add(blockConnector);
				AddBlockConnector.this.dispose();
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);
	
		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				AddBlockConnector.this.dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
	
	}
}
