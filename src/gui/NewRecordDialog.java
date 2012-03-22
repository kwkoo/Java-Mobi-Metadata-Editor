package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JLabel;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import javax.swing.JComboBox;
import java.awt.Insets;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JTextField;

import mobimeta.EXTHRecord;

public class NewRecordDialog extends JDialog implements ActionListener
{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField tfValue;
	private JComboBox typeCombo;
	private EXTHAddRecordListener listener;
	private JButton addButton;
	private JButton cancelButton;


	/**
	 * Create the dialog.
	 */
	public NewRecordDialog(JFrame parent, EXTHAddRecordListener listener)
	{
		super(parent, true);
		
		this.listener = listener;
		String[] comboValues = new String[EXTHRecord.knownTypes.length];
		for (int i=0; i<comboValues.length; i++)
		{
			comboValues[i] = EXTHRecord.knownTypes[i] + " (" + EXTHRecord.knownDesc[i] + ")";
		}
		
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(10, 10, 10, 10));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		GridBagLayout gbl_contentPanel = new GridBagLayout();
		gbl_contentPanel.columnWidths = new int[]{0, 0};
		gbl_contentPanel.rowHeights = new int[]{0, 0, 0};
		gbl_contentPanel.columnWeights = new double[]{0.0, 1.0};
		gbl_contentPanel.rowWeights = new double[]{0.0, 0.0, Double.MIN_VALUE};
		contentPanel.setLayout(gbl_contentPanel);
		{
			JLabel lblType = new JLabel("Type");
			lblType.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			GridBagConstraints gbc_lblType = new GridBagConstraints();
			gbc_lblType.anchor = GridBagConstraints.EAST;
			gbc_lblType.insets = new Insets(0, 0, 5, 5);
			gbc_lblType.gridx = 0;
			gbc_lblType.gridy = 0;
			contentPanel.add(lblType, gbc_lblType);
		}
		{
			typeCombo = new JComboBox(comboValues);
			GridBagConstraints gbc_typeCombo = new GridBagConstraints();
			gbc_typeCombo.insets = new Insets(0, 0, 5, 0);
			gbc_typeCombo.fill = GridBagConstraints.HORIZONTAL;
			gbc_typeCombo.gridx = 1;
			gbc_typeCombo.gridy = 0;
			contentPanel.add(typeCombo, gbc_typeCombo);
		}
		{
			JLabel lblValue = new JLabel("Value");
			lblValue.setFont(new Font("Lucida Grande", Font.BOLD, 13));
			GridBagConstraints gbc_lblValue = new GridBagConstraints();
			gbc_lblValue.anchor = GridBagConstraints.EAST;
			gbc_lblValue.insets = new Insets(0, 0, 0, 5);
			gbc_lblValue.gridx = 0;
			gbc_lblValue.gridy = 1;
			contentPanel.add(lblValue, gbc_lblValue);
		}
		{
			tfValue = new JTextField();
			GridBagConstraints gbc_tfValue = new GridBagConstraints();
			gbc_tfValue.fill = GridBagConstraints.HORIZONTAL;
			gbc_tfValue.gridx = 1;
			gbc_tfValue.gridy = 1;
			contentPanel.add(tfValue, gbc_tfValue);
			tfValue.setColumns(20);
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				addButton = new JButton("Add");
				addButton.addActionListener(this);
				buttonPane.add(addButton);
				getRootPane().setDefaultButton(addButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
		
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
	}

	protected JComboBox getTypeCombo() {
		return typeCombo;
	}

	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();
		
		if (source == addButton)
		{
			String value = tfValue.getText();
			if (value.length() == 0) return;
			
			int typeIndex = typeCombo.getSelectedIndex();
			if (typeIndex == -1) return;
			
			int type = EXTHRecord.knownTypes[typeIndex];
			EXTHRecord rec = null;
			if (EXTHRecord.isBooleanType(type))	// this is an ugly hack - we really should present a checkbox to the user
			{
				boolean boolValue = false;
				if (value.equals("1")
					||
					value.toLowerCase().equals("true")
					||
					value.toLowerCase().equals("on")
					||
					value.toLowerCase().equals("yes"))
				{
					boolValue = true;
				}
				rec = new EXTHRecord(type, boolValue);
			}
			else
				rec = new EXTHRecord(type, value, GuiModel.getCharacterEncoding());
			listener.addEXTHRecord(rec);
			setVisible(false);
			dispose();
		}
		else if (source == cancelButton)
		{
			setVisible(false);
			dispose();
		}
	}
	protected JButton getAddButton() {
		return addButton;
	}
	protected JButton getCancelButton() {
		return cancelButton;
	}
	protected JTextField getTfValue() {
		return tfValue;
	}
}
