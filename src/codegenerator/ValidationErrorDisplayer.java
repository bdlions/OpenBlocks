package codegenerator;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTextPane;
import javax.swing.JScrollPane;

public class ValidationErrorDisplayer extends JFrame {

	private JPanel contentPane;
	JTextPane textPane;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					ValidationErrorDisplayer frame = new ValidationErrorDisplayer();
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
	public ValidationErrorDisplayer() {
		setTitle("All Validation Errors");
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 600, 400);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		
		JScrollPane scrollPane = new JScrollPane();
		contentPane.add(scrollPane, BorderLayout.CENTER);
		
		textPane = new JTextPane();
		scrollPane.setViewportView(textPane);
	}
	
	public void displayError(List<String> errors)
	{
		int count = 0;
		String fullText = "";
		for (String string : errors) {
			if(count > 0 ){
				fullText += "\n";
			}
			fullText += string;
			count ++;
		}
		
		textPane.setText(fullText);
	}

	
}
