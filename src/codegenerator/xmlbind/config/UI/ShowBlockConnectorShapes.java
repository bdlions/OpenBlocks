package codegenerator.xmlbind.config.UI;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.EventQueue;

import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.border.EmptyBorder;

import codegenerator.xmlbind.config.BlockConnectorShape;
import codegenerator.xmlbind.config.BlockLangDef;
import codegenerator.xmlbind.config.DefaultSettings;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

public class ShowBlockConnectorShapes extends JFrame{

	private JPanel contentPane;
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	private BlockLangDef blockLangDef;
	private JList<BlockConnectorShape> list;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShowBlockConnectorShapes frame = new ShowBlockConnectorShapes();
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
	public ShowBlockConnectorShapes() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		blockLangDef = DefaultSettings.getSettings();
		blockLangDef.toXML();
		
		list = new JList<BlockConnectorShape>();
		contentPane.add(list, BorderLayout.CENTER);
		list.setModel(new DefaultListModel());
		
		for (BlockConnectorShape element : blockLangDef.getBlockConnectorShapes()) {
			((DefaultListModel) list.getModel()).addElement(element);
		}
		
		list.setCellRenderer(new ListCellRenderer<BlockConnectorShape>() {
			@Override
			public Component getListCellRendererComponent(
					JList<? extends BlockConnectorShape> list,
					BlockConnectorShape value, int index, boolean isSelected,
					boolean cellHasFocus) {
				JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,isSelected, cellHasFocus);
				renderer.setText(value.getShapeType());
				return renderer;
			}
		});
		
		
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddConnectorShape dialog = new AddConnectorShape(blockLangDef, (DefaultListModel)list.getModel());
				dialog.setVisible(true);
			}
		});
		
		panel.add(btnAdd);
		
		JButton btnDelete = new JButton("Delete");
		btnDelete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				DefaultListModel model = (DefaultListModel) list.getModel();
			      if (model.size() > 0 && list.getSelectedIndex() >= 0 ) {
			    	 blockLangDef.getBlockConnectorShapes().remove(list.getSelectedValue());
			         model.remove(list.getSelectedIndex());
			      }
			      else{
			    	  JOptionPane.showMessageDialog(null, "Please select an item from the list.");
			      }
			}
		});
		
		panel.add(btnDelete);
		
	}


}
