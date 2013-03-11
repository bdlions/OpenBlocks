package codegenerator.xmlbind.config.UI;

import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JButton;
import javax.swing.JTextArea;
import java.awt.EventQueue;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JColorChooser;

public class ColorChooserProgram {

	JFrame guiFrame;
	JTextArea tracker;
	JPanel optPanel;

	// Note: Typically the main method will be in a
	// separate class. As this is a simple one class
	// example it's all in the one class.
	public static void main(String[] args) {

		// Use the event dispatch thread for Swing components
		EventQueue.invokeLater(new Runnable() {

			@Override
			public void run() {

				new ColorChooserProgram();
			}
		});

	}

	public ColorChooserProgram() {
		guiFrame = new JFrame();
		// make sure the program exits when the frame closes
		guiFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		guiFrame.setTitle("Dialog Box Example");
		guiFrame.setSize(500, 300);

		// This will center the JFrame in the middle of the screen
		guiFrame.setLocationRelativeTo(null);

		guiFrame.setLayout(new BorderLayout());

		// Using a JTextArea to diplay feedback
		tracker = new JTextArea("File Tracker:");
		tracker.setVisible(true);
		guiFrame.add(tracker, BorderLayout.NORTH);

		optPanel = new JPanel();
		optPanel.setLayout(new GridLayout(1, 2));

		guiFrame.add(optPanel, BorderLayout.SOUTH);

		// button for the show dialog method
		JButton showButton = new JButton("Show Color Dialog");
		showButton.setActionCommand("Show Color Dialog");
		showButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				Color selectedColor = JColorChooser.showDialog(guiFrame,
						"Pick a Color", Color.GREEN);

				if (selectedColor != null) {
					tracker.append("\nThe selected color is make up of Red: "
							+ selectedColor.getRed() + " Blue: "
							+ selectedColor.getBlue() + " Green: "
							+ selectedColor.getGreen());
				}
			}
		});

		optPanel.add(showButton);

		// button for the create dialog method
		JButton createButton = new JButton("Create Color Dialog");
		createButton.setActionCommand("Create Color Dialog");
		createButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent event) {
				// this users a JColorchooser instance in combination
				// with a JDialog to create a color chooser dialog. It's
				// modeless
				// and the OK and Cancel buttons can be listened to.
				final JColorChooser colorChooser = new JColorChooser();
				JDialog dialog = JColorChooser.createDialog(guiFrame,
						"Set Text Area color", false, colorChooser,
						new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent event) {
								// this actionListenerr is for the OK button
								tracker.append("\nI can feel my color being changed to "
										+ colorChooser.getColor());
								tracker.setBackground(colorChooser.getColor());
							}
						}, new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent event) {
								// this actionListener is for the cancel button
								tracker.append("\nCancel button clicked..");
							}
						});
				dialog.setVisible(true);
			}
		});

		optPanel.add(createButton);

		guiFrame.setVisible(true);

	}
}