package gui;

import java.io.*;
import java.util.HashSet;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import java.awt.GridBagLayout;
import javax.swing.JLabel;

import java.awt.FileDialog;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.JSeparator;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import java.awt.Component;
import javax.swing.Box;
import javax.swing.SwingConstants;
import java.awt.Font;
import javax.swing.JTextField;
import java.awt.Color;
import javax.swing.JScrollPane;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JTable;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JTextArea;

import mobimeta.MobiCommon;
import javax.swing.JMenuBar;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

public class Main implements ListSelectionListener, ActionListener,
		TableModelListener, LanguageModel
{
	private FileDialog	openFileChooser	= null;
	private FileDialog	saveFileChooser	= null;
	private JFrame		frame;
	private JTextArea	lblInputFilename;
	private JTextArea	lblOutputFilename;
	private JTextField	tfFullName;
	private JTable		table;
	private JButton		buttonRemove;
	private JButton		buttonAdd;
	private JButton		buttonSave;
	private	JButton		btnLanguage;
	private	JButton		btnHeaderInfo;
	private GuiModel	model;
	private File		outputFile;
	private	boolean		packHeader	= false;
	private JMenuItem mntmOpen;
	private JMenuItem mntmSave;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		System.setProperty("apple.laf.useScreenMenuBar", "true");
		System.setProperty("com.apple.mrj.application.apple.menu.about.name",
				"Mobi Meta Editor");
		
		HashSet<String> optionsSet = new HashSet<String>();
		File inputFile = null;
		for (int i=0; i<args.length; i++)
		{
			String arg = args[i];
			if (arg.startsWith("-"))
				optionsSet.add(arg);
			else if (inputFile == null)
				inputFile = new File(arg);
			else
				printUsage();
		}

		if ((inputFile != null) && (!inputFile.exists() || !inputFile.isFile()))
		{
			System.err.println("Input file " + inputFile.getAbsolutePath()
					+ " does not exist or is not a file.");
			System.exit(1);
		}
		
		if (optionsSet.contains("-h") || optionsSet.contains("--help"))
			printUsage();

		if (optionsSet.contains("-s"))
			MobiCommon.safeMode = true;

		if (optionsSet.contains("-d"))
			MobiCommon.debug = true;

		// get around inner class mumbo-jumbo
		//
		final File f = inputFile;

		EventQueue.invokeLater(new Runnable()
		{
			public void run()
			{
				try
				{
					Main window = new Main(f);
					window.frame.setVisible(true);
				}
				catch (Exception e)
				{
					e.printStackTrace();
				}
			}
		});
	}

	private static void printUsage()
	{
		System.err.println("Usage: mobimeta [switches] [input file]");
		System.err.println("  -h\tthis message");
		System.err
				.println("  -s\tsafe mode - does not change the size of the mobi header record");
		System.err.println("  -d\tdebug mode");
		System.exit(0);
	}

	/**
	 * Create the application.
	 */
	public Main(File inputFile)
	{
		model = new GuiModel();
		model.addTableModelListener(this);
		initialize();
		if (inputFile == null)
			inputFile = getSourceFile();

		if (inputFile == null)
		{
			showAlert("No mobi file selected.");
			System.exit(0);
		}
	}


	public void valueChanged(ListSelectionEvent e)
	{
		if (!MobiCommon.safeMode)
			buttonRemove.setEnabled(table.getSelectedRow() != -1);
	}
	
	public void pickSaveTarget()
	{
		if (saveFileChooser == null)
		{
			saveFileChooser = new FileDialog(frame, "Select mobi file", FileDialog.SAVE);
			saveFileChooser.setFilenameFilter(new MobiFileFilter());
		}
		
		if (outputFile != null)
		{
			saveFileChooser.setDirectory(outputFile.getParent());
			saveFileChooser.setFile(outputFile.getName());
		}

		saveFileChooser.setVisible(true);
		
		if (saveFileChooser.getFile() != null)
		{
			outputFile = new File(saveFileChooser.getDirectory(), saveFileChooser.getFile());
			lblOutputFilename.setText(outputFile.getAbsolutePath());
		}
	}

	public void actionPerformed(ActionEvent event)
	{
		Object source = event.getSource();

		if (source == buttonAdd)
		{
			NewRecordDialog dialog = new NewRecordDialog(frame, model);
			dialog.setVisible(true);
		}
		else if (source == buttonRemove)
		{
			int row = table.getSelectedRow();
			if (row == -1) return;
			model.removeRecordAtRow(row);
		}
		else if ((source == buttonSave) || (source == mntmSave))
		{
			if (packHeader) model.setFullName(tfFullName.getText());
			try
			{
				File tmpOutput = null;
				if (lblInputFilename.getText().equals(lblOutputFilename.getText()))
					tmpOutput = File.createTempFile("mobimeta", ".mobi");
				else
					tmpOutput = outputFile;
				
				model.save(tmpOutput, packHeader);
				if (!tmpOutput.equals(outputFile))
				{
					if (!tmpOutput.renameTo(outputFile))
					{
						showAlert("Error renaming temp file to " + outputFile.getAbsolutePath());
						return;
					}
				}
				
				setWindowChangedStatus(false);
				showAlert("File saved.");
			}
			catch (GuiException e)
			{
				showAlert(e.getMessage());
			}
			catch (IOException e)
			{
				showAlert("Could not create temp file for writing: " + e.getMessage());
			}
		}
		else if (source == btnLanguage)
		{
			LanguageDialog dialog = new LanguageDialog(frame, this);
			dialog.setVisible(true);
		}
		else if (source == btnHeaderInfo)
		{
			HeaderInfoDialog dialog = new HeaderInfoDialog(frame, model);
			dialog.setVisible(true);
		}
		else if (source == mntmOpen)
		{
			getSourceFile();
		}
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize()
	{
		frame = new JFrame();
		frame.setTitle("Mobi Meta Editor");
		frame.setBounds(50, 50, 800, 700);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(new BorderLayout(0, 0));

		Component horizontalStrut = Box.createHorizontalStrut(20);
		frame.getContentPane().add(horizontalStrut, BorderLayout.WEST);

		Component horizontalStrut_1 = Box.createHorizontalStrut(20);
		frame.getContentPane().add(horizontalStrut_1, BorderLayout.EAST);

		Component verticalStrut = Box.createVerticalStrut(20);
		frame.getContentPane().add(verticalStrut, BorderLayout.NORTH);

		Component verticalStrut_1 = Box.createVerticalStrut(20);
		frame.getContentPane().add(verticalStrut_1, BorderLayout.SOUTH);

		JPanel panel = new JPanel();
		frame.getContentPane().add(panel, BorderLayout.CENTER);
		GridBagLayout gbl_panel = new GridBagLayout();
		gbl_panel.columnWidths = new int[]
		{ 0, 0, 0, 0, 0 };
		gbl_panel.rowHeights = new int[]
		{ 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
		gbl_panel.columnWeights = new double[]
		{ 1.0, 0.0, 1.0, 0.0, Double.MIN_VALUE };
		gbl_panel.rowWeights = new double[]
		{ 0.0, 0.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, Double.MIN_VALUE };
		panel.setLayout(gbl_panel);

		JLabel lblInput = new JLabel("Input Filename");
		lblInput.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblInput.setVerticalAlignment(SwingConstants.TOP);
		GridBagConstraints gbc_lblInput = new GridBagConstraints();
		gbc_lblInput.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblInput.insets = new Insets(0, 0, 5, 5);
		gbc_lblInput.gridx = 0;
		gbc_lblInput.gridy = 0;
		panel.add(lblInput, gbc_lblInput);

		Component horizontalStrut_2 = Box.createHorizontalStrut(20);
		GridBagConstraints gbc_horizontalStrut_2 = new GridBagConstraints();
		gbc_horizontalStrut_2.insets = new Insets(0, 0, 5, 5);
		gbc_horizontalStrut_2.gridx = 1;
		gbc_horizontalStrut_2.gridy = 0;
		panel.add(horizontalStrut_2, gbc_horizontalStrut_2);

		lblInputFilename = new JTextArea(0, 40);
		lblInputFilename.setEditable(false);
		lblInputFilename.setLineWrap(true);
		GridBagConstraints gbc_lblInputFilename = new GridBagConstraints();
		gbc_lblInputFilename.fill = GridBagConstraints.BOTH;
		gbc_lblInputFilename.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblInputFilename.insets = new Insets(0, 0, 5, 5);
		gbc_lblInputFilename.gridx = 2;
		gbc_lblInputFilename.gridy = 0;
		panel.add(lblInputFilename, gbc_lblInputFilename);

		JLabel lblOutput = new JLabel("Output Filename");
		lblOutput.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		lblOutput.setVerticalAlignment(SwingConstants.TOP);
		lblOutput.setHorizontalAlignment(SwingConstants.RIGHT);
		GridBagConstraints gbc_lblOutput = new GridBagConstraints();
		gbc_lblOutput.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblOutput.insets = new Insets(0, 0, 5, 5);
		gbc_lblOutput.gridx = 0;
		gbc_lblOutput.gridy = 1;
		panel.add(lblOutput, gbc_lblOutput);

		lblOutputFilename = new JTextArea(0, 40);
		lblOutputFilename.setEditable(false);
		lblOutputFilename.setLineWrap(true);
		lblOutputFilename.addMouseListener(new MouseAdapter()
		{
            public void mouseClicked(MouseEvent e)
            {
            	pickSaveTarget();
            }
		});
		GridBagConstraints gbc_lblOutputFilename = new GridBagConstraints();
		gbc_lblOutputFilename.fill = GridBagConstraints.BOTH;
		gbc_lblOutputFilename.anchor = GridBagConstraints.NORTHWEST;
		gbc_lblOutputFilename.insets = new Insets(0, 0, 5, 5);
		gbc_lblOutputFilename.gridx = 2;
		gbc_lblOutputFilename.gridy = 1;
		panel.add(lblOutputFilename, gbc_lblOutputFilename);

		JSeparator separator = new JSeparator();
		separator.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_separator = new GridBagConstraints();
		gbc_separator.fill = GridBagConstraints.BOTH;
		gbc_separator.gridwidth = 3;
		gbc_separator.insets = new Insets(0, 0, 5, 5);
		gbc_separator.gridx = 0;
		gbc_separator.gridy = 2;
		panel.add(separator, gbc_separator);

		Component separatorStrut1 = Box.createVerticalStrut(10);
		GridBagConstraints gbc_strut1 = new GridBagConstraints();
		gbc_strut1.fill = GridBagConstraints.VERTICAL;
		gbc_strut1.gridwidth = 1;
		gbc_strut1.insets = new Insets(0, 0, 5, 0);
		gbc_strut1.gridx = 3;
		gbc_strut1.gridy = 2;
		panel.add(separatorStrut1, gbc_strut1);

		JLabel lblTitle = new JLabel("Full Name");
		lblTitle.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		GridBagConstraints gbc_lblTitle = new GridBagConstraints();
		gbc_lblTitle.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblTitle.insets = new Insets(0, 0, 5, 5);
		gbc_lblTitle.gridx = 0;
		gbc_lblTitle.gridy = 3;
		panel.add(lblTitle, gbc_lblTitle);

		tfFullName = new JTextField(15);
		if (MobiCommon.safeMode)
			tfFullName.setEditable(false);
		else
		{
			tfFullName.getDocument().addDocumentListener(new DocumentListener()
			{
				public void changedUpdate(DocumentEvent arg0)
				{
					packHeader = true;
					setWindowChangedStatus(true);
				}

				public void insertUpdate(DocumentEvent arg0)
				{
					packHeader = true;
					setWindowChangedStatus(true);
				}

				public void removeUpdate(DocumentEvent arg0)
				{
					packHeader = true;
					setWindowChangedStatus(true);
				}
			});
		}
		GridBagConstraints gbc_tfFullName = new GridBagConstraints();
		gbc_tfFullName.fill = GridBagConstraints.HORIZONTAL;
		gbc_tfFullName.anchor = GridBagConstraints.NORTHWEST;
		gbc_tfFullName.insets = new Insets(0, 0, 5, 5);
		gbc_tfFullName.gridx = 2;
		gbc_tfFullName.gridy = 3;
		panel.add(tfFullName, gbc_tfFullName);
		
		JPanel panel_3 = new JPanel();
		GridBagConstraints gbc_panel_3 = new GridBagConstraints();
		gbc_panel_3.insets = new Insets(0, 0, 5, 5);
		gbc_panel_3.fill = GridBagConstraints.BOTH;
		gbc_panel_3.gridx = 2;
		gbc_panel_3.gridy = 4;
		panel.add(panel_3, gbc_panel_3);
		
		btnLanguage = new JButton("Language...");
		btnLanguage.addActionListener(this);
		btnHeaderInfo = new JButton("Header Info...");
		btnHeaderInfo.addActionListener(this);
		panel_3.setLayout(new FlowLayout(FlowLayout.LEFT, 5, 5));
		panel_3.add(btnLanguage);
		panel_3.add(btnHeaderInfo);

		JLabel lblExthRecords = new JLabel("EXTH Records");
		lblExthRecords.setFont(new Font("Lucida Grande", Font.BOLD, 13));
		GridBagConstraints gbc_lblExthRecords = new GridBagConstraints();
		gbc_lblExthRecords.anchor = GridBagConstraints.NORTHEAST;
		gbc_lblExthRecords.insets = new Insets(0, 0, 5, 5);
		gbc_lblExthRecords.gridx = 0;
		gbc_lblExthRecords.gridy = 5;
		panel.add(lblExthRecords, gbc_lblExthRecords);

		JScrollPane scrollPane = new JScrollPane();
		GridBagConstraints gbc_scrollPane = new GridBagConstraints();
		gbc_scrollPane.fill = GridBagConstraints.BOTH;
		gbc_scrollPane.insets = new Insets(0, 0, 5, 5);
		gbc_scrollPane.gridx = 2;
		gbc_scrollPane.gridy = 5;
		panel.add(scrollPane, gbc_scrollPane);

		table = new CustomJTable(model);
		table.getColumnModel().getColumn(0).setPreferredWidth(100);
		table.getSelectionModel().addListSelectionListener(this);
		scrollPane.setViewportView(table);
		
		JPanel panel_1 = new JPanel();
		GridBagConstraints gbc_panel_1 = new GridBagConstraints();
		gbc_panel_1.insets = new Insets(0, 0, 5, 5);
		gbc_panel_1.anchor = GridBagConstraints.EAST;
		gbc_panel_1.gridx = 2;
		gbc_panel_1.gridy = 6;
		panel.add(panel_1, gbc_panel_1);
		panel_1.setLayout(new FlowLayout(FlowLayout.CENTER, 5, 5));

		buttonAdd = new JButton("+");
		buttonAdd.addActionListener(this);
		panel_1.add(buttonAdd);

		buttonRemove = new JButton("-");
		buttonRemove.addActionListener(this);
		buttonRemove.setEnabled(false);
		panel_1.add(buttonRemove);
		
		if (MobiCommon.safeMode)
		{
			buttonAdd.setEnabled(false);
			buttonRemove.setEnabled(false);
		}

		JSeparator separator_1 = new JSeparator();
		separator_1.setForeground(Color.DARK_GRAY);
		GridBagConstraints gbc_separator_1 = new GridBagConstraints();
		gbc_separator_1.insets = new Insets(0, 0, 5, 5);
		gbc_separator_1.fill = GridBagConstraints.HORIZONTAL;
		gbc_separator_1.gridwidth = 3;
		gbc_separator_1.gridx = 0;
		gbc_separator_1.gridy = 7;
		panel.add(separator_1, gbc_separator_1);

		Component separatorStrut2 = Box.createVerticalStrut(10);
		GridBagConstraints gbc_strut2 = new GridBagConstraints();
		gbc_strut2.fill = GridBagConstraints.VERTICAL;
		gbc_strut2.gridwidth = 1;
		gbc_strut2.insets = new Insets(0, 0, 5, 0);
		gbc_strut2.gridx = 3;
		gbc_strut2.gridy = 7;
		panel.add(separatorStrut2, gbc_strut2);

		JPanel panel_2 = new JPanel();
		GridBagConstraints gbc_panel_2 = new GridBagConstraints();
		gbc_panel_2.insets = new Insets(0, 0, 0, 5);
		gbc_panel_2.anchor = GridBagConstraints.WEST;
		gbc_panel_2.gridwidth = 3;
		gbc_panel_2.fill = GridBagConstraints.VERTICAL;
		gbc_panel_2.gridx = 0;
		gbc_panel_2.gridy = 8;
		panel.add(panel_2, gbc_panel_2);

		buttonSave = new JButton("Save");
		buttonSave.addActionListener(this);
		panel_2.add(buttonSave);
		
		JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu mnFile = new JMenu("File");
		menuBar.add(mnFile);
		
		mntmOpen = new JMenuItem("Open...");
		mntmOpen.addActionListener(this);
		mnFile.add(mntmOpen);
		
		mntmSave = new JMenuItem("Save");
		mntmSave.addActionListener(this);
		mnFile.add(mntmSave);
	}

	private File getSourceFile()
	{
		if (openFileChooser == null)
		{
			openFileChooser = new FileDialog(frame, "Select mobi file", FileDialog.LOAD);
			openFileChooser.setFilenameFilter(new MobiFileFilter());
		}

		openFileChooser.setVisible(true);
		
		File source = null;
		String dir	= openFileChooser.getDirectory();
		String file	= openFileChooser.getFile();
		if ((dir != null) && (file != null))
			source = new File(dir, file);

		if (source != null)
		{
			try
			{
				model.setModel(source);
			}
			catch (GuiException e)
			{
				showAlert("Could not parse mobi file: " + e.getMessage());
				System.exit(0);
			}

			lblInputFilename.setText(source.getAbsolutePath());
			tfFullName.setText(model.getFullName());

			outputFile = getOutputFile(source);
			lblOutputFilename.setText(outputFile.getAbsolutePath());

			// we trigger the window modified indicator when we set tfFullName
			//
			setWindowChangedStatus(false);
			packHeader = false;
		}
		
		return source;
	}

	private File getOutputFile(File inputFile)
	{
		File parent = inputFile.getParentFile();
		String inputName = inputFile.getName();
		int dot = inputName.lastIndexOf('.');
		String outputName;

		if (dot == -1)
			outputName = inputName + "_new";
		else
			outputName = inputName.substring(0, dot) + "_new"
					+ inputName.substring(dot);

		return new File(parent, outputName);
	}

	private void showAlert(String message)
	{
		JOptionPane.showMessageDialog(frame, message);
	}

	// scroll to the newly added/deleted row
	//
	public void tableChanged(TableModelEvent event)
	{
		int eventType = event.getType();
		int row = event.getLastRow();
		if (eventType == TableModelEvent.INSERT)
		{
			table.getSelectionModel().setSelectionInterval(row, row);
			table.scrollRectToVisible(new Rectangle(table.getCellRect(row, 0,
					true)));
			packHeader = true;
			setWindowChangedStatus(true);
		}
		else if (eventType == TableModelEvent.DELETE)
		{
			boolean select = false;
			int numRows = model.getRowCount();
			if (numRows > row)
			{
				select = true;
			}
			else if (numRows > 0)
			{
				select = true;
				row = numRows - 1;
			}
			if (select)
			{
				table.getSelectionModel().setSelectionInterval(row, row);
				table.scrollRectToVisible(new Rectangle(table.getCellRect(row,
						0, true)));
			}
			packHeader = true;
			setWindowChangedStatus(true);
		}
		else if (eventType == TableModelEvent.UPDATE)
		{
			packHeader = true;
			setWindowChangedStatus(true);
		}
	}
	
	protected void setWindowChangedStatus(boolean status)
	{
		frame.getRootPane().putClientProperty("Window.documentModified",
				Boolean.valueOf(status));
	}

	// we implement the LanguageModel interface because we want to intercept the
	// setLanguages() call so that we can set the window status changed flag
	//
	public int getLocale()
	{
		return model.getLocale();
	}

	// we implement the LanguageModel interface because we want to intercept the
	// setLanguages() call so that we can set the window status changed flag
	//
	public int getDictInput()
	{
		return model.getDictInput();
	}

	// we implement the LanguageModel interface because we want to intercept the
	// setLanguages() call so that we can set the window status changed flag
	//
	public int getDictOutput()
	{
		return model.getDictOutput();
	}

	// we implement the LanguageModel interface because we want to intercept the
	// setLanguages() call so that we can set the window status changed flag
	//
	public void setLanguages(int locale, int dictInput, int dictOutput)
	{
		model.setLanguages(locale, dictInput, dictOutput);
		setWindowChangedStatus(true);
	}
	protected JMenuItem getMntmOpen() {
		return mntmOpen;
	}
	protected JMenuItem getMntmSave() {
		return mntmSave;
	}
}
