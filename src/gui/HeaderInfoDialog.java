package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;

public class HeaderInfoDialog extends JDialog implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JButton closeButton;
	private JScrollPane scrollPane;
	private JTextArea textArea;

	/**
	 * Create the dialog.
	 */
	public HeaderInfoDialog(JFrame parent, MetaInfoProvider infoProvider)
	{
		super(parent, "Header Info", true);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			scrollPane = new JScrollPane();
			contentPanel.add(scrollPane, BorderLayout.CENTER);
			{
				textArea = new JTextArea(infoProvider.getMetaInfo());
				textArea.setEditable(false);
				scrollPane.setViewportView(textArea);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				closeButton = new JButton("Close");
				closeButton.addActionListener(this);
				closeButton.setActionCommand("Cancel");
				buttonPane.add(closeButton);
			}
		}
	}

	protected JButton getCloseButton() {
		return closeButton;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (e.getSource() == closeButton)
		{
			setVisible(false);
			dispose();
		}
	}
}
