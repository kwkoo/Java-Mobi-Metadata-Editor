package gui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import java.awt.GridBagLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.JLabel;
import java.awt.GridBagConstraints;
import java.awt.Font;
import javax.swing.SwingConstants;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JComboBox;

public class LanguageDialog extends JDialog implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private final JPanel contentPanel = new JPanel();

	private JButton okButton;
	private JButton cancelButton;
	private JComboBox cbLanguage;
	private JComboBox cbDictInput;
	private JComboBox cbDictOutput;

	// store original values here
	private int locale;
	private int dictInput;
	private int dictOutput;

	// these values indicate if they are known values
	private boolean localeExists;
	private boolean dictInputExists;
	private boolean dictOutputExists;

	private LanguageModel model;

	/**
	 * Create the dialog.
	 */
	public LanguageDialog(JFrame parent, LanguageModel model)
	{
		super(parent, true);
		this.model = model;
		locale = model.getLocale();
		dictInput = model.getDictInput();
		dictOutput = model.getDictOutput();
		localeExists = (LanguageCodes.codeToIndex(locale) != -1);
		dictInputExists = (LanguageCodes.codeToIndex(dictInput) != -1);
		dictOutputExists = (LanguageCodes.codeToIndex(dictOutput) != -1);

		String[] localeChoices;
		String[] dictInputChoices;
		String[] dictOutputChoices;

		// assemble choices for combo boxes
		//
		if (localeExists)
			localeChoices = LanguageCodes.description;
		else
		{
			localeChoices = new String[LanguageCodes.description.length + 1];
			System.arraycopy(LanguageCodes.description, 0, localeChoices, 0,
					LanguageCodes.description.length);
			localeChoices[localeChoices.length - 1] = "Unknown (" + locale
					+ ")";
		}

		if (dictInputExists)
			dictInputChoices = LanguageCodes.description;
		else
		{
			dictInputChoices = new String[LanguageCodes.description.length + 1];
			System.arraycopy(LanguageCodes.description, 0, dictInputChoices, 0,
					LanguageCodes.description.length);
			dictInputChoices[dictInputChoices.length - 1] = "Unknown ("
					+ dictInput + ")";
		}

		if (dictOutputExists)
			dictOutputChoices = LanguageCodes.description;
		else
		{
			dictOutputChoices = new String[LanguageCodes.description.length + 1];
			System.arraycopy(LanguageCodes.description, 0, dictOutputChoices,
					0, LanguageCodes.description.length);
			dictOutputChoices[dictOutputChoices.length - 1] = "Unknown ("
					+ dictOutput + ")";
		}

		setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(new BorderLayout());
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel, BorderLayout.CENTER);
		contentPanel.setLayout(new BorderLayout(0, 0));
		{
			Component verticalStrut = Box.createVerticalStrut(20);
			contentPanel.add(verticalStrut, BorderLayout.NORTH);
		}
		{
			Component verticalStrut = Box.createVerticalStrut(20);
			contentPanel.add(verticalStrut, BorderLayout.SOUTH);
		}
		{
			Component horizontalStrut = Box.createHorizontalStrut(20);
			contentPanel.add(horizontalStrut, BorderLayout.WEST);
		}
		{
			Component horizontalStrut = Box.createHorizontalStrut(20);
			contentPanel.add(horizontalStrut, BorderLayout.EAST);
		}
		{
			JPanel panel = new JPanel();
			contentPanel.add(panel, BorderLayout.CENTER);
			GridBagLayout gbl_panel = new GridBagLayout();
			gbl_panel.columnWidths = new int[]
			{ 0, 0, 0 };
			gbl_panel.rowHeights = new int[]
			{ 0, 0, 0, 0 };
			gbl_panel.columnWeights = new double[]
			{ 0.0, 1.0, Double.MIN_VALUE };
			gbl_panel.rowWeights = new double[]
			{ 0.0, 0.0, 0.0, Double.MIN_VALUE };
			panel.setLayout(gbl_panel);
			{
				JLabel lblLanguage = new JLabel("Language");
				lblLanguage.setHorizontalAlignment(SwingConstants.RIGHT);
				lblLanguage.setFont(new Font("Lucida Grande", Font.BOLD, 13));
				GridBagConstraints gbc_lblLanguage = new GridBagConstraints();
				gbc_lblLanguage.anchor = GridBagConstraints.EAST;
				gbc_lblLanguage.insets = new Insets(0, 0, 5, 5);
				gbc_lblLanguage.gridx = 0;
				gbc_lblLanguage.gridy = 0;
				panel.add(lblLanguage, gbc_lblLanguage);
			}
			{
				cbLanguage = new JComboBox(localeChoices);
				cbLanguage.setSelectedIndex(localeExists ? LanguageCodes
						.codeToIndex(locale) : (localeChoices.length - 1));
				GridBagConstraints gbc_cbLanguage = new GridBagConstraints();
				gbc_cbLanguage.insets = new Insets(0, 0, 5, 0);
				gbc_cbLanguage.fill = GridBagConstraints.HORIZONTAL;
				gbc_cbLanguage.gridx = 1;
				gbc_cbLanguage.gridy = 0;
				panel.add(cbLanguage, gbc_cbLanguage);
			}
			{
				JLabel lblDictionaryInput = new JLabel("Dictionary Input");
				lblDictionaryInput.setHorizontalAlignment(SwingConstants.RIGHT);
				lblDictionaryInput.setFont(new Font("Lucida Grande", Font.BOLD,
						13));
				GridBagConstraints gbc_lblDictionaryInput = new GridBagConstraints();
				gbc_lblDictionaryInput.anchor = GridBagConstraints.EAST;
				gbc_lblDictionaryInput.insets = new Insets(0, 0, 5, 5);
				gbc_lblDictionaryInput.gridx = 0;
				gbc_lblDictionaryInput.gridy = 1;
				panel.add(lblDictionaryInput, gbc_lblDictionaryInput);
			}
			{
				cbDictInput = new JComboBox(dictInputChoices);
				cbDictInput
						.setSelectedIndex(dictInputExists ? LanguageCodes
								.codeToIndex(dictInput)
								: (dictInputChoices.length - 1));
				GridBagConstraints gbc_cbDictInput = new GridBagConstraints();
				gbc_cbDictInput.insets = new Insets(0, 0, 5, 0);
				gbc_cbDictInput.fill = GridBagConstraints.HORIZONTAL;
				gbc_cbDictInput.gridx = 1;
				gbc_cbDictInput.gridy = 1;
				panel.add(cbDictInput, gbc_cbDictInput);
			}
			{
				JLabel lblDictionaryOutput = new JLabel("Dictionary Output");
				lblDictionaryOutput
						.setHorizontalAlignment(SwingConstants.RIGHT);
				lblDictionaryOutput.setFont(new Font("Lucida Grande",
						Font.BOLD, 13));
				GridBagConstraints gbc_lblDictionaryOutput = new GridBagConstraints();
				gbc_lblDictionaryOutput.anchor = GridBagConstraints.EAST;
				gbc_lblDictionaryOutput.insets = new Insets(0, 0, 0, 5);
				gbc_lblDictionaryOutput.gridx = 0;
				gbc_lblDictionaryOutput.gridy = 2;
				panel.add(lblDictionaryOutput, gbc_lblDictionaryOutput);
			}
			{
				cbDictOutput = new JComboBox(dictOutputChoices);
				cbDictOutput.setSelectedIndex(dictOutputExists ? LanguageCodes
						.codeToIndex(dictOutput)
						: (dictOutputChoices.length - 1));
				GridBagConstraints gbc_cbDictOutput = new GridBagConstraints();
				gbc_cbDictOutput.fill = GridBagConstraints.HORIZONTAL;
				gbc_cbDictOutput.gridx = 1;
				gbc_cbDictOutput.gridy = 2;
				panel.add(cbDictOutput, gbc_cbDictOutput);
			}
		}
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane, BorderLayout.SOUTH);
			{
				okButton = new JButton("OK");
				okButton.addActionListener(this);
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(this);
				buttonPane.add(cancelButton);
			}
		}
	}

	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();

		if (source == okButton)
		{
			int localeIndex = cbLanguage.getSelectedIndex();
			if ((localeIndex >= 0) && (localeIndex < LanguageCodes.code.length))
				locale = LanguageCodes.code[localeIndex];

			int dictInputIndex = cbDictInput.getSelectedIndex();
			if ((dictInputIndex >= 0) && (dictInputIndex < LanguageCodes.code.length))
				dictInput = LanguageCodes.code[dictInputIndex];

			int dictOutputIndex = cbDictOutput.getSelectedIndex();
			if ((dictOutputIndex >= 0) && (dictOutputIndex < LanguageCodes.code.length))
				dictOutput = LanguageCodes.code[dictOutputIndex];

			model.setLanguages(locale, dictInput, dictOutput);

			setVisible(false);
			dispose();
		}
		else if (source == cancelButton)
		{
			setVisible(false);
			dispose();
		}
	}
}
