package codegenerator.xmlbind.config.UI;

import java.awt.BorderLayout;
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

import codegenerator.xmlbind.config.BlockGenus;
import codegenerator.xmlbind.config.BlockLangDef;
import codegenerator.xmlbind.config.DefaultSettings;
import javax.swing.JList;
import javax.swing.JButton;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JScrollBar;

public class ShowBlockGenuses extends JFrame{

	private JPanel contentPane;
	protected DefaultListCellRenderer defaultRenderer = new DefaultListCellRenderer();
	private BlockLangDef blockLangDef;
	private JList<BlockGenus> list;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ShowBlockGenuses frame = new ShowBlockGenuses();
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
	public ShowBlockGenuses() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		blockLangDef = DefaultSettings.getSettings();
		blockLangDef.toXML();
		
		list = new JList<BlockGenus>();
		contentPane.add(list, BorderLayout.CENTER);
		list.setModel(new DefaultListModel());
		
		for (BlockGenus element : blockLangDef.getBlockGenuses()) {
			((DefaultListModel) list.getModel()).addElement(element);
		}
		
		list.setCellRenderer(new ListCellRenderer<BlockGenus>() {
			@Override
			public Component getListCellRendererComponent(
					JList<? extends BlockGenus> list, BlockGenus value,
					int index, boolean isSelected, boolean cellHasFocus) {
				// TODO Auto-generated method stub
				JLabel renderer = (JLabel) defaultRenderer.getListCellRendererComponent(list, value, index,isSelected, cellHasFocus);
				renderer.setText(value.getInitLabel());
				return renderer;
			}
		});
		
		
		
		JPanel panel = new JPanel();
		contentPane.add(panel, BorderLayout.SOUTH);
		
		JButton btnAdd = new JButton("Add");
		btnAdd.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				AddBlockGenus dialog = new AddBlockGenus(blockLangDef, (DefaultListModel)list.getModel());
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
			    	 blockLangDef.getBlockGenuses().remove(list.getSelectedValue());
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
