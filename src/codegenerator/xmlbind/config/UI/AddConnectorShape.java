package codegenerator.xmlbind.config.UI;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.DefaultListModel;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.JComboBox;

import codegenerator.xmlbind.config.BlockConnectorShape;
import codegenerator.xmlbind.config.BlockLangDef;

public class AddConnectorShape extends JDialog {

	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private BlockLangDef blockLangDef;
	private DefaultListModel model;
	private JComboBox comboBox;
	
	/**
	 * Create the dialog.
	 */
	public AddConnectorShape(final BlockLangDef blockLangDef, final DefaultListModel model) {
		setTitle("Adding a new shape");
		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setModal(true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(null);
		
		this.blockLangDef = blockLangDef;
		this.model = model;
		
		HashMap<Integer, BlockConnectorShape> usedShape = new HashMap<Integer, BlockConnectorShape>();
		for (BlockConnectorShape connectorShape : blockLangDef.getBlockConnectorShapes()) {
			usedShape.put(connectorShape.getShapeNumber(), connectorShape);
		}
		
		/**
		 * available shapes are 14
		 * so we can add shapes number between 1 to 14 those who are not already used
		 * 
		 * */
		List<Integer> availableShapes = new ArrayList<Integer>();
		for(int i = 1; i <= 14; i ++)
		{
			if(!usedShape.containsKey(i)){
				availableShapes.add(i);
			}
		}
		
		JLabel lblNewLabel = new JLabel("Shape Type");
		lblNewLabel.setBounds(55, 68, 91, 16);
		contentPanel.add(lblNewLabel);
		
		textField = new JTextField();
		textField.setBounds(172, 68, 195, 22);
		contentPanel.add(textField);
		textField.setColumns(10);
		
		JLabel lblNewLabel_1 = new JLabel("Shape Number");
		lblNewLabel_1.setBounds(55, 106, 91, 16);
		contentPanel.add(lblNewLabel_1);
		
		comboBox = new JComboBox(availableShapes.toArray());
		comboBox.setBounds(172, 106, 195, 22);
		contentPanel.add(comboBox);

		JPanel buttonPane = new JPanel();
		buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
		getContentPane().add(buttonPane, BorderLayout.SOUTH);

		JButton okButton = new JButton("OK");
		okButton.setActionCommand("OK");
		okButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				if(!textField.getText().equals("")  && !textField.getText().trim().equals(""))
				{
					BlockConnectorShape connectorShape = new BlockConnectorShape();
					connectorShape.setShapeNumber((int)comboBox.getSelectedItem());
					connectorShape.setShapeType(textField.getText());
					
					model.addElement(connectorShape);
					blockLangDef.getBlockConnectorShapes().add(connectorShape);
					
					AddConnectorShape.this.dispose();
				}
				else
				{
					JOptionPane.showMessageDialog(null, "Please put a valid name in the shape name field.");
				}
			}
		});
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		/**
		 * Close button
		 * */
		JButton cancelButton = new JButton("Cancel");
		cancelButton.setActionCommand("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddConnectorShape.this.dispose();
			}
		});
		buttonPane.add(cancelButton);


	}
}
