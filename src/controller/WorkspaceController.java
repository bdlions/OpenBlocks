package controller;


import general.DisplayMessage;
import general.JavaFilter;
import general.JavaFilterText;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;


import javax.swing.*;

import javax.swing.ButtonGroup;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextPane;
import javax.swing.JToolBar;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import language.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.xerces.internal.parsers.DOMParser;

import renderable.RenderableBlock;


import workspace.*;
import codeblocks.*;
import codegenerator.*;



/**
 * 
 * The WorkspaceController is the starting point for any program using Open Blocks.
 * It contains a Workspace (the block programming area) as well as the Factories
 * (the palettes of blocks), and is responsible for setting up and laying out
 * the overall window including loading some WorkspaceWidgets like the TrashCan.
 * 
 * @author Ricarose Roque
 */
public class WorkspaceController {
    
	public static JFrame frame;
	//JavaFilter class, which supports only text file to upload
    private static JavaFilter fJavaFilter;
    private static JavaFilterText fJavaFilterText;
	private static String fileSavePath = ""; 
	private static boolean isFileSaved = false;
	private static String savedFileContent = "";
	
	private static int externalVariableCounter = 1;
	private static String projectInitialXmlContent = "";
	private static LanguageGenerator languageGenerator;
	private static Hashtable<String, LanguageEntry> syntaxMap;
	
	public static SearchBar searchBar;
	
	public static JMenu menuFile;
	public static JMenuItem menuItemNew;
	public static JMenuItem menuItemOpen;
	public static JMenuItem menuItemSave;
	public static JMenuItem menuItemSaveAs;
	public static JMenuItem menuItemClose;
	public static JMenuItem menuItemPrint;
	public static JMenuItem menuItemExit;
	
	public static JMenu menuCodeGeneration;
	public static JMenuItem menuItemValidate;
	public static JCheckBoxMenuItem checkBoxMenuItemGenerateCCode;
	public static JCheckBoxMenuItem checkBoxMenuItemGenerateJavaCode;
	public static JMenuItem menuItemViewCode;
	
	public static JMenu menuConfiguration;
	public static JCheckBoxMenuItem checkBoxMenuItemEnglish;
	public static JCheckBoxMenuItem checkBoxMenuItemFrancis;
	public static JCheckBoxMenuItem checkBoxMenuItemSpanish;
	
	public static JMenu menuHelp;
	public static JMenuItem menuItemOnlineHelp;
	
	public static JMenu menuAbout;
	public static JMenuItem menuItemSnap;
	
	public static JMenu menuExternal;
	public static JMenuItem menuItemExternal;
	
	public static JButton newButton;
	public static JButton openButton;
	public static JButton saveButton;
	public static JButton printBbutton;
	public static JButton validateBbutton;
	public static JButton generateCCodeButton;
	public static JButton generateJavaCodeButton;
	public static JButton viewCodeButton;
	public static JButton undoButton;
	public static JButton redoButton;
	public static JButton cutButton;
	public static JButton copyButton;
	public static JButton pasteButton;
	public static JButton deleteButton;
	public static JButton buttonAddVariable;
	
	private static String LANG_DEF_FILEPATH;
    
    private static Element langDefRoot;
    
    public static HashMap<String, String> variableListMap = new HashMap<String, String>();
    
    //flags 
    private boolean isWorkspacePanelInitialized = false;
    
    /** The single instance of the Workspace Controller **/
    protected static Workspace workspace;
    
    protected JPanel workspacePanel;
    //protected SearchBar searchBar;
    
    //flag to indicate if a new lang definition file has been set
    private boolean langDefDirty = true;
    
    //flag to indicate if a workspace has been loaded/initialized 
    private boolean workspaceLoaded = false;
    /**
     * Constructs a WorkspaceController instance that manages the
     * interaction with the codeblocks.Workspace
     *
     */
    public WorkspaceController(){
        workspace = Workspace.getInstance();
    }
    
    
/*    *//**
     * Returns the single instance of this
     * @return the single instance of this
     *//*
    public static WorkspaceController getInstance(){
        return wc;
    }*/
    
    ////////////////////
    //  LANG DEF FILE //
    ////////////////////
    
    /**
     * Sets the file path for the language definition file, if the 
     * language definition file is located in 
     */
    public void setLangDefFilePath(String filePath){
        
        LANG_DEF_FILEPATH = filePath; //TODO do we really need to save the file path?
        
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            
            String langDefLocation = /*workingDirectory +*/ LANG_DEF_FILEPATH;
            doc = builder.parse(new File(langDefLocation));
            
            langDefRoot = doc.getDocumentElement();
           
            //set the dirty flag for the language definition file 
            //to true now that a new file has been set
            langDefDirty = true;
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
public void setLangDefFilePathTest(String content){
        
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        DOMParser parser = new DOMParser();
        
        try {
            builder = factory.newDocumentBuilder();
            
            parser.parse(new InputSource(new StringReader(content)));
             doc = parser.getDocument();
            
            langDefRoot = doc.getDocumentElement();
           
            //set the dirty flag for the language definition file 
            //to true now that a new file has been set
            langDefDirty = true;
            workspaceLoaded = true;
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }
    
    /**
     * Sets the contents of the Lang Def File to the specified 
     * String langDefContents
     * @param langDefContents String contains the specification of a language
     * definition file
     */
    public void setLangDefFileString(String langDefContents){
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(langDefContents)));
            langDefRoot = doc.getDocumentElement();
            
            //set the dirty flag for the language definition file 
            //to true now that a new file has been set
            langDefDirty = true;
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    /**
     * Sets the Lang Def File to the specified File langDefFile.  
     * @param langDefFile File contains the specification of the a language 
     * definition file.
     */
    public void setLangDefFile(File langDefFile){
        //LANG_DEF_FILEPATH = langDefFile.getCanonicalPath();
        
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
          
            doc = builder.parse(langDefFile);
            
            langDefRoot = doc.getDocumentElement();
            
            //set the dirty flag for the language definition file 
            //to true now that a new file has been set
            langDefDirty = true;
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Loads all the block genuses, properties, and link rules of 
     * a language specified in the pre-defined language def file.
     * @param root Loads the language specified in the Element root
     */
    public void loadBlockLanguage(Element root){
        //load connector shapes
        //MUST load shapes before genuses in order to initialize connectors within
        //each block correctly
        BlockConnectorShape.loadBlockConnectorShapes(root);
        
        //load genuses
        BlockGenus.loadBlockGenera(root);
        
        
        //load rules
        BlockLinkChecker.addRule(new CommandRule());
        BlockLinkChecker.addRule(new SocketRule());
        BlockLinkChecker.addRule(new PolyRule());
        BlockLinkChecker.addRule(new ExceptionRule(root));
        
        //set the dirty flag for the language definition file 
        //to false now that the lang file has been loaded
        langDefDirty = false;
    }
    
    /**
     * Resets the current language within the active
     * Workspace.
     *
     */
    public void resetLanguage(){
        //clear shape mappings
        BlockConnectorShape.resetConnectorShapeMappings();
        //clear block genuses
        BlockGenus.resetAllGenuses();
        //clear all link rules
        BlockLinkChecker.reset();
    }
    
    
    ////////////////////////
    // SAVING AND LOADING //
    ////////////////////////
    
    /**
     * Returns the save string for the entire workspace.  This includes the block workspace, any 
     * custom factories, canvas view state and position, pages
     * @return the save string for the entire workspace.
     */
    public String getSaveString(){
        StringBuffer saveString = new StringBuffer();
        //append the save data
        saveString.append("<?xml version=\"1.0\"?>");
        saveString.append("\r\n");
        //dtd file path may not be correct...
        //saveString.append("<!DOCTYPE StarLogo-TNG SYSTEM \""+SAVE_FORMAT_DTD_FILEPATH+"\">");
        //append root node
        saveString.append("<CODEBLOCKS>");
        saveString.append(workspace.getSaveString());
        saveString.append("</CODEBLOCKS>");
        return saveString.toString();
    }
    
    /**
     * Loads a fresh workspace based on the default specifications in the language 
     * definition file.  The block canvas will have no live blocks.   
     */
    public void loadFreshWorkspace(){
        //need to just reset workspace (no need to reset language) unless
        //language was never loaded
    	BlockGenus.setNameToGenus(new HashMap<String, BlockGenus>());    	
    	//reset only if workspace actually exists
        if(workspaceLoaded)
            resetWorkspace();
        
        if(langDefDirty)
            loadBlockLanguage(langDefRoot);
        
        workspace.loadWorkspaceFrom(null, langDefRoot);
        
        workspaceLoaded = true;
    }
    
    /**
     * Loads the programming project from the specified file path.  
     * This method assumes that a Language Definition File has already 
     * been specified for this programming project.
     * @param path String file path of the programming project to load
     */ 
    public void loadProjectFromPath(String path){
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            
            doc = builder.parse(new File(path));
            
            Element projectRoot = doc.getDocumentElement();

            //load the canvas (or pages and page blocks if any) blocks from the save file
            //also load drawers, or any custom drawers from file.  if no custom drawers
            //are present in root, then the default set of drawers is loaded from 
            //langDefRoot
            workspace.loadWorkspaceFrom(projectRoot, langDefRoot);

            workspaceLoaded = true;
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    /**
     * Loads the programming project specified in the projectContents.   
     * This method assumes that a Language Definition File has already been 
     * specified for this programming project.
     * @param projectContents
     */
    public void loadProject(String projectContents){
        //need to reset workspace and language (only if new language has been set)
        
        //reset only if workspace actually exists
        if(workspaceLoaded)
            resetWorkspace();
        
        if(langDefDirty)
            loadBlockLanguage(langDefRoot);
        
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document doc;
        try {
            builder = factory.newDocumentBuilder();
            doc = builder.parse(new InputSource(new StringReader(projectContents)));
            Element root = doc.getDocumentElement();
            //load the canvas (or pages and page blocks if any) blocks from the save file
            //also load drawers, or any custom drawers from file.  if no custom drawers
            //are present in root, then the default set of drawers is loaded from 
            //langDefRoot
            workspace.loadWorkspaceFrom(root, langDefRoot);

            workspaceLoaded = true;
            workspace.addCodeEditor();
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
        
    }
    
    /**
     * Loads the programming project specified in the projectContents String, 
     * which is associated with the language definition file contained in the 
     * specified langDefContents.  All the blocks contained in projectContents
     * must have an associted block genus defined in langDefContents.
     * 
     * If the langDefContents have any workspace settings such as pages or 
     * drawers and projectContents has workspace settings as well, the 
     * workspace settings within the projectContents will override the 
     * workspace settings in langDefContents.  
     * 
     * NOTE: The language definition contained in langDefContents does 
     * not replace the default language definition file set by: setLangDefFilePath() or 
     * setLangDefFile().
     * 
     * @param projectContents
     * @param langDefContents String XML that defines the language of
     * projectContents
     */
    public void loadProject(String projectContents, String langDefContents){
        
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        Document projectDoc;
        Document langDoc;
        try {
            builder = factory.newDocumentBuilder();
            projectDoc = builder.parse(new InputSource(new StringReader(projectContents)));
            Element projectRoot = projectDoc.getDocumentElement();
            langDoc = builder.parse(new InputSource(new StringReader(projectContents)));
            Element langRoot = langDoc.getDocumentElement();
            
            //need to reset workspace and language (if langDefContents != null)
            //reset only if workspace actually exists
            if(workspaceLoaded)
                resetWorkspace();
            
            if(langDefContents == null)
                loadBlockLanguage(langDefRoot);
            else
                loadBlockLanguage(langRoot);
            //TODO should verify that the roots of the two XML strings are valid
            workspace.loadWorkspaceFrom(projectRoot, langRoot);
            
            workspaceLoaded = true;
            
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } 
    }
    
    /**
     * Resets the entire workspace.  This includes all blocks, pages, drawers, and trashed blocks.  
     * Also resets the undo/redo stack.  The language (i.e. genuses and shapes) is not reset.
     */
    public void resetWorkspace(){
        //clear all pages and their drawers
        //clear all drawers and their content
        //clear all block and renderable block instances
        workspace.reset();
        //clear action history
        //rum.reset();
        //clear runblock manager data
        //rbm.reset();
    }
    
    
    
    /**
     * This method creates and lays out the entire workspace panel with its 
     * different components.  Workspace and language data not loaded in 
     * this function.
     * Should be call only once at application startup.
     */
    private void initWorkspacePanel(){
       
        //add trashcan and prepare trashcan images
        ImageIcon tc = new ImageIcon("support/images/trash.png");
        ImageIcon openedtc = new ImageIcon("support/images/trash_open.png");
        TrashCan trash = new TrashCan(tc.getImage(), openedtc.getImage());

        workspace.addWidget(trash, true, true);
       
        
        workspacePanel = new JPanel();      
        workspacePanel.setLayout(new BorderLayout());
        workspacePanel.add(workspace, BorderLayout.CENTER);
        
        isWorkspacePanelInitialized = true;
    }
    
    /**
     * Returns the JComponent of the entire workspace. 
     * @return the JComponent of the entire workspace. 
     */
    public JComponent getWorkspacePanel(){
        if(!isWorkspacePanelInitialized)
            initWorkspacePanel();
        return workspacePanel;
    }
    
    /**
     * Returns a SearchBar instance capable of searching for blocks 
     * within the BlockCanvas and block drawers
     */
    public JComponent getSearchBar(){
        SearchBar searchBar = new SearchBar("Search blocks", "Search for blocks in the drawers and workspace", workspace);
        for(SearchableContainer con : getAllSearchableContainers()){
            searchBar.addSearchableContainer(con);
        }
        
        return searchBar.getComponent();
    }
    
    /**
     * Returns an unmodifiable Iterable of SearchableContainers
     * @return an unmodifiable Iterable of SearchableContainers
     */
    public Iterable<SearchableContainer> getAllSearchableContainers(){
        return workspace.getAllSearchableContainers();
    }
    
    /////////////////////////////////////
    // TESTING CODEBLOCKS SEPARATELY //
    /////////////////////////////////////
    /**
     * Create the GUI and show it.  For thread safety,
     * this method should be invoked from the
     * event-dispatching thread.
     */
    private static void createAndShowGUI(final WorkspaceController wc) {
    	try {
    		UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    	fJavaFilter = new JavaFilter();
    	fJavaFilterText = new JavaFilterText();
    	System.out.println("Creating GUI...");
        syntaxMap = languageGenerator.getSyntaxMapLanguage();       
        //Create and set up the window.
        
		String frameTitle = "OpenBlocksDemo";
		if (syntaxMap.containsKey(frameTitle)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(frameTitle);
			frameTitle = titleEntry.getLabel();
		}        
        frame = new JFrame(frameTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);   
        frame.setExtendedState(JFrame.MAXIMIZED_BOTH); 
        //frame.setBounds(100, 100, 800, 500);
        
        //adding menu bar and menu items
        JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		String fileString = "File";
		if (syntaxMap.containsKey(fileString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(fileString);
			fileString = titleEntry.getLabel();
		}
		menuFile = new JMenu(fileString);
		menuBar.add(menuFile);
		
		projectInitialXmlContent = wc.getSaveString();
		String newString = "New";
		if (syntaxMap.containsKey(newString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(newString);
			newString = titleEntry.getLabel();
		}
		menuItemNew = new JMenuItem(newString);
		menuFile.add(menuItemNew);
		menuItemNew.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				wc.newSelected(wc);
			}
		});
		
	    String openString = "Open";
		if (syntaxMap.containsKey(openString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(openString);
			openString = titleEntry.getLabel();
		}
		menuItemOpen = new JMenuItem(openString);
		menuFile.add(menuItemOpen);
		menuItemOpen.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				wc.openSelected(wc);				
			}
		});
		String saveString = "Save";
		if (syntaxMap.containsKey(saveString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(saveString);
			saveString = titleEntry.getLabel();
		}
		menuItemSave = new JMenuItem(saveString);
		menuFile.add(menuItemSave);
		menuItemSave.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				wc.savePressed(wc);			
			}
		});
		String saveAsString = "SaveAs";
		if (syntaxMap.containsKey(saveAsString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(saveAsString);
			saveAsString = titleEntry.getLabel();
		}
		else
		{
			saveAsString = "Save As";
		}
		menuItemSaveAs = new JMenuItem(saveAsString);
		menuFile.add(menuItemSaveAs);
		menuItemSaveAs.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				wc.saveAsPressed(wc);
			}
		});
		
		String closeString = "Close";
		if (syntaxMap.containsKey(closeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(closeString);
			closeString = titleEntry.getLabel();
		}
		menuItemClose = new JMenuItem(closeString);
		menuFile.add(menuItemClose);
		menuItemClose.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				wc.closeSelected(wc);
			}
		});
		
		String printString = "Print";
		if (syntaxMap.containsKey(printString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(printString);
			printString = titleEntry.getLabel();
		}
		menuItemPrint = new JMenuItem(printString);
		menuFile.add(menuItemPrint);
		menuItemPrint.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				wc.printSelected(wc);
			}
		});

		String exitString = "Exit";
		if (syntaxMap.containsKey(exitString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(exitString);
			exitString = titleEntry.getLabel();
		}
		menuItemExit = new JMenuItem(exitString);
		menuFile.add(menuItemExit);
		menuItemExit.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				wc.exitSelected(wc);
			}
		});
		String codeGenerationString = "Code generated on the fly";
		if (syntaxMap.containsKey(codeGenerationString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(codeGenerationString);
			codeGenerationString = titleEntry.getLabel();
		}
		menuCodeGeneration = new JMenu(codeGenerationString);
		menuBar.add(menuCodeGeneration);
		
		String validateString = "Validate";
		if (syntaxMap.containsKey(validateString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(validateString);
			validateString = titleEntry.getLabel();
		}
		menuItemValidate = new JMenuItem(validateString);
		menuCodeGeneration.add(menuItemValidate);
		menuItemValidate.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				wc.validateSelected(wc);						
			}
		});
		
		final ButtonGroup generateCodeButtonGroup = new ButtonGroup();
		String generateCCodeString = "Generate C Code";
		if (syntaxMap.containsKey(generateCCodeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(generateCCodeString);
			generateCCodeString = titleEntry.getLabel();
		}
		checkBoxMenuItemGenerateCCode = new JCheckBoxMenuItem(generateCCodeString,true);
		Workspace.setSelectedLanguage("c");
		String generateJavaCodeString = "Generate Java Code";
		if (syntaxMap.containsKey(generateJavaCodeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(generateJavaCodeString);
			generateJavaCodeString = titleEntry.getLabel();
		}
		checkBoxMenuItemGenerateJavaCode = new JCheckBoxMenuItem(generateJavaCodeString);
		
		generateCodeButtonGroup.add(checkBoxMenuItemGenerateCCode);
		menuCodeGeneration.add(checkBoxMenuItemGenerateCCode);
		checkBoxMenuItemGenerateCCode.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				List<String> errors = BlockValidator.validateAll(XMLToBlockGenerator.generateBlocks(wc.getSaveString()));
            	if(errors.size() > 0){
            		ValidationErrorDisplayer displayer = new ValidationErrorDisplayer();
                	displayer.displayError(errors);
                	displayer.setVisible(true);
                	Workspace.clearCodeInEditor();
            	}
            	else
            	{
            		Workspace.setSelectedLanguage("c");
            		Workspace.setCodeInEditor();     		
            	}
			}
		});
		
		generateCodeButtonGroup.add(checkBoxMenuItemGenerateJavaCode);
		menuCodeGeneration.add(checkBoxMenuItemGenerateJavaCode);
		checkBoxMenuItemGenerateJavaCode.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				
				List<String> errors = BlockValidator.validateAll(XMLToBlockGenerator.generateBlocks(wc.getSaveString()));
            	if(errors.size() > 0){
            		ValidationErrorDisplayer displayer = new ValidationErrorDisplayer();
                	displayer.displayError(errors);
                	displayer.setVisible(true);
                	Workspace.clearCodeInEditor();
            	}
            	else
            	{
            		Workspace.setSelectedLanguage("java");
            		Workspace.setCodeInEditor();       		
            	}				
			}
		});
		
		String viewCodeString = "View Code";
		if (syntaxMap.containsKey(viewCodeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(viewCodeString);
			viewCodeString = titleEntry.getLabel();
		}
		menuItemViewCode = new JMenuItem(viewCodeString);
		menuCodeGeneration.add(menuItemViewCode);
		menuItemViewCode.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				List<String> errors = BlockValidator.validateAll(XMLToBlockGenerator.generateBlocks(wc.getSaveString()));
            	if(errors.size() > 0)
            	{
            		ValidationErrorDisplayer displayer = new ValidationErrorDisplayer();
                	displayer.displayError(errors);
                	displayer.setVisible(true);
                	Workspace.clearCodeInEditor();
            	}
            	else
            	{
            		Workspace.setCodeInEditor();
            		String code = Workspace.getCode();            	
                	DisplayCode displayCode = new DisplayCode(syntaxMap);
                	displayCode.setCode(code);
                	displayCode.setLocationRelativeTo(null);
                	displayCode.setVisible(true);                	
            	}			
			}
		});
		
		String configurationString = "Configuration";
		if (syntaxMap.containsKey(configurationString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(configurationString);
			configurationString = titleEntry.getLabel();
		}
		menuConfiguration = new JMenu(configurationString);
		menuBar.add(menuConfiguration);
		
		final ButtonGroup languageButtonGroup = new ButtonGroup();
		String englishString = "English";
		if (syntaxMap.containsKey(englishString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(englishString);
			englishString = titleEntry.getLabel();
		}
		checkBoxMenuItemEnglish = new JCheckBoxMenuItem(englishString,true);		
		
		String spanishString = "Spanish";
		if (syntaxMap.containsKey(spanishString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(spanishString);
			spanishString = titleEntry.getLabel();
		}
		checkBoxMenuItemSpanish = new JCheckBoxMenuItem(spanishString);
		
		String francisString = "Francis";
		if (syntaxMap.containsKey(francisString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(francisString);
			francisString = titleEntry.getLabel();
		}
		checkBoxMenuItemFrancis = new JCheckBoxMenuItem(francisString);
		
		checkBoxMenuItemEnglish.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				DisplayMessage.setLanguage("English");
				languageGenerator.setLanguage("English");
				languageGenerator.updateLanguageMapLoadLanguage();
				syntaxMap = languageGenerator.getSyntaxMapLanguage();			
				
				updateLabelText();
			}
		});
		checkBoxMenuItemSpanish.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				DisplayMessage.setLanguage("Spanish");
				languageGenerator.setLanguage("Spanish");
				languageGenerator.updateLanguageMapLoadLanguage();
				syntaxMap = languageGenerator.getSyntaxMapLanguage();
				
				updateLabelText();
			}
		});
		checkBoxMenuItemFrancis.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				DisplayMessage.setLanguage("Francis");
				languageGenerator.setLanguage("Francis");
				languageGenerator.updateLanguageMapLoadLanguage();
				syntaxMap = languageGenerator.getSyntaxMapLanguage();
				
				updateLabelText();				
			}
		});
		
		
		languageButtonGroup.add(checkBoxMenuItemEnglish);
		menuConfiguration.add(checkBoxMenuItemEnglish);
		
		languageButtonGroup.add(checkBoxMenuItemSpanish);
		menuConfiguration.add(checkBoxMenuItemSpanish);
		
		languageButtonGroup.add(checkBoxMenuItemFrancis);
		menuConfiguration.add(checkBoxMenuItemFrancis);
		
		String externalMenuString = "External";
		if (syntaxMap.containsKey(externalMenuString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(externalMenuString);
			externalMenuString = titleEntry.getLabel();
		}
		menuExternal= new JMenu(externalMenuString);
		menuBar.add(menuExternal);
		
		String uploadExternalString = "External";
		if (syntaxMap.containsKey(printString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(printString);
			printString = titleEntry.getLabel();
		}
		menuItemExternal = new JMenuItem(uploadExternalString);
		menuExternal.add(menuItemExternal);
		menuItemExternal.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				wc.uploadExternalFilePressed(wc);
			}
		});
		
		String helpString = "Help";
		if (syntaxMap.containsKey(helpString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(helpString);
			helpString = titleEntry.getLabel();
		}
		menuHelp= new JMenu(helpString);
		menuBar.add(menuHelp);
		
		String onlineHelpString = "Online Help";
		if (syntaxMap.containsKey(onlineHelpString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(onlineHelpString);
			onlineHelpString = titleEntry.getLabel();
		}
		menuItemOnlineHelp = new JMenuItem(onlineHelpString);
		menuHelp.add(menuItemOnlineHelp);
		menuItemOnlineHelp.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				try {
		            Runtime.getRuntime().exec("\"C:\\Program Files\\Internet Explorer\\IEXPLORE.EXE\" " + "www.help.com");
		        } 
				catch (Exception ex) {
		        }
			}
		});
		
		
		String aboutString = "About";
		if (syntaxMap.containsKey(aboutString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(aboutString);
			aboutString = titleEntry.getLabel();
		}
		menuAbout= new JMenu(aboutString);
		menuBar.add(menuAbout);
		
		String snapString = "Snap";
		if (syntaxMap.containsKey(snapString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(snapString);
			snapString = titleEntry.getLabel();
		}
		menuItemSnap = new JMenuItem(snapString);
		menuAbout.add(menuItemSnap);
		menuItemSnap.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				DisplayMessage.printMessage("SnapAbout");						
			}
		});		
		
        //create search bar
		String searchBarTitleString = "Search blocks";
		if (syntaxMap.containsKey(searchBarTitleString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(searchBarTitleString);
			searchBarTitleString = titleEntry.getLabel();
		}
		String searchBarToolTipString = "Search for blocks in the drawers and workspace";
		if (syntaxMap.containsKey(searchBarToolTipString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(searchBarToolTipString);
			searchBarToolTipString = titleEntry.getLabel();
		}
        searchBar = new SearchBar(searchBarTitleString, searchBarToolTipString, workspace);
        for(SearchableContainer con : wc.getAllSearchableContainers()){
            searchBar.addSearchableContainer(con);
        }
        
        JPanel topPane = new JPanel();
        String toolbarImageLocation = "support/images/toolbarImage/";
        JToolBar toolbar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
        toolbar.setEnabled(false);
        newButton = new JButton();
        newButton.setIcon(new ImageIcon(toolbarImageLocation + "new.png", newString));
        newButton.setToolTipText(newString);
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	wc.newSelected(wc);
            }
        });
        toolbar.add(newButton);
        
        openButton = new JButton();
        openButton.setIcon(new ImageIcon(toolbarImageLocation + "open.png", openString));
        openButton.setToolTipText(openString);
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	wc.openSelected(wc);            	
            }
        });
        toolbar.add(openButton);
        
        saveButton = new JButton();
        saveButton.setIcon(new ImageIcon(toolbarImageLocation + "save.png", saveString));
        saveButton.setToolTipText(saveString);
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	wc.savePressed(wc);	
            }
        });
        toolbar.add(saveButton);
        
        printBbutton = new JButton();
        printBbutton.setIcon(new ImageIcon(toolbarImageLocation + "print.png", printString));
        printBbutton.setToolTipText(printString);
        printBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	wc.printSelected(wc);
            }
        });
        toolbar.add(printBbutton);
        
        validateBbutton = new JButton();
        validateBbutton.setIcon(new ImageIcon(toolbarImageLocation + "validation.png", validateString));
        validateBbutton.setToolTipText(validateString);
        validateBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	wc.validateSelected(wc);            	
            }
        });
        toolbar.add(validateBbutton);        
        
        viewCodeString = "View Code";
		if (syntaxMap.containsKey(viewCodeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(viewCodeString);
			viewCodeString = titleEntry.getLabel();
		}
		viewCodeButton = new JButton();
		viewCodeButton.setIcon(new ImageIcon(toolbarImageLocation + "view_code.png", viewCodeString));
		viewCodeButton.setToolTipText(viewCodeString);
		viewCodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	List<String> errors = BlockValidator.validateAll(XMLToBlockGenerator.generateBlocks(wc.getSaveString()));
            	if(errors.size() > 0)
            	{
            		ValidationErrorDisplayer displayer = new ValidationErrorDisplayer();
                	displayer.displayError(errors);
                	displayer.setVisible(true);
                	Workspace.clearCodeInEditor();
            	}
            	else
            	{
            		Workspace.setCodeInEditor();
            		String code = Workspace.getCode();            	
                	DisplayCode displayCode = new DisplayCode(syntaxMap);
                	displayCode.setCode(code);
                	displayCode.setLocationRelativeTo(null);
                	displayCode.setVisible(true);                	
            	}
            }
        });
        toolbar.add(viewCodeButton);
        
        String addVariableString = "Add Variable";
		if (syntaxMap.containsKey(addVariableString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(addVariableString);
			addVariableString = titleEntry.getLabel();
		}
        buttonAddVariable = new JButton();
        buttonAddVariable.setIcon(new ImageIcon(toolbarImageLocation + "add.png", addVariableString));
        buttonAddVariable.setToolTipText(addVariableString);
        buttonAddVariable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	PromptVariableName variableName = new PromptVariableName(syntaxMap);
            	variableName.setModal(true);
            	variableName.setLocationRelativeTo(null);
            	variableName.setVisible(true);
            	
            	if(variableName.getOption() == JOptionPane.OK_OPTION)
            	{
            		if( BlockGenus.getGenusWithName(Variable.getValidVariableName(variableName.getValue())) != null)
            		{
                		DisplayMessage.printErrorMessage("Variable exists with the same name");            			                		
                	}
                	else{	            		
                		DisplayMessage.printSuccessMessage("Variable is added successfully");                		
                		
                		WorkspaceController.variableListMap.put(variableName.getValue(), variableName.getVariableType());
                		wc.langDefDirty = true;
	            		VariableMaker.addVariable(langDefRoot.getOwnerDocument(), variableName.getValue(), variableName.getVariableType());
	            		wc.loadProject(wc.getSaveString());
	            		codeblocks.Block block = new codeblocks.Block(variableName.getValue()+"_decl");
	            		Page blockPage = workspace.getPageNamed("Blocks");
	            		WorkspaceWidget ww = workspace.getWidgetAt(new Point(blockPage.getJComponent().getX(), blockPage.getJComponent().getY()));
	            		blockPage.blockDropped(new RenderableBlock(ww, block.getBlockID()));
	            		workspace.cleanUpAllBlocks();
	            		List<String> errors = BlockValidator.validateAll(XMLToBlockGenerator.generateBlocks(wc.getSaveString()));
	                	if(errors.size() > 0)
	                	{
	                		Workspace.clearCodeInEditor();
	                	}
	                	else
	                	{
	                		Workspace.setCodeInEditor();	                    	
	                	}
	                    wc.updateSearchableComponents(wc);
                	}
            	}
            	
            }
        });
        toolbar.add(buttonAddVariable);
        
        searchBar.getComponent().setPreferredSize(new Dimension(130, 23));
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.add(searchBar.getComponent());
        topPane.setLayout(new BorderLayout());
        topPane.add(toolbar,BorderLayout.NORTH);
        topPane.add(searchBarPanel, BorderLayout.CENTER);
        //topPane.add(saveButton);
        frame.add(topPane, BorderLayout.PAGE_START);        
        frame.add(wc.getWorkspacePanel(), BorderLayout.CENTER);
        //adding code editor
        Workspace.addCodeEditor();
        frame.setVisible(true);
        wc.resetFileReference();
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
                //setting look and feel
            	try {
        		    // Set cross-platform Java L&F (also called "Metal")
        	        UIManager.setLookAndFeel(
        	            UIManager.getCrossPlatformLookAndFeelClassName());        	        
        	    } 
        	    catch (UnsupportedLookAndFeelException e) {
        	       // handle exception
        	    }
        	    catch (ClassNotFoundException e) {
        	       // handle exception
        	    }
        	    catch (InstantiationException e) {
        	       // handle exception
        	    }
        	    catch (IllegalAccessException e) {
        	       // handle exception
        	    }
            	DisplayMessage.generateLanguage();
            	DisplayMessage.setLanguage("English");
                LANG_DEF_FILEPATH = "support/lang_def.xml";
                      
                //Create a new WorkspaceController 
                WorkspaceController wc = new WorkspaceController();
                wc.loadLanguage("English");    
                wc.setLangDefFilePath(LANG_DEF_FILEPATH);
                wc.loadFreshWorkspace();
                createAndShowGUI(wc);
                      
            }
        });
    }
	
	public static void initWithLangDefFilePath(final String langDefFilePath) {
	
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
                               
                //Create a new WorkspaceController 
                WorkspaceController wc = new WorkspaceController();
                
				wc.setLangDefFilePath(langDefFilePath);

                wc.loadFreshWorkspace();
                createAndShowGUI(wc);
            }
        });
	}
	
	public void loadLanguage(String language)
	{
		languageGenerator = new LanguageGenerator();
		languageGenerator.generateLoadLanguage();
	}
	
	public static void updateLabelText()
	{
		String frameTitle = "OpenBlocksDemo";
		if (syntaxMap.containsKey(frameTitle)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(frameTitle);
			frameTitle = titleEntry.getLabel();
		}
		frame.setTitle(frameTitle);
		
		String fileString = "File";
		if (syntaxMap.containsKey(fileString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(fileString);
			fileString = titleEntry.getLabel();
		}
		menuFile.setText(fileString);
		
		String newString = "New";
		if (syntaxMap.containsKey(newString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(newString);
			newString = titleEntry.getLabel();
		}
		menuItemNew.setText(newString);
		
		String openString = "Open";
		if (syntaxMap.containsKey(openString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(openString);
			openString = titleEntry.getLabel();
		}
		menuItemOpen.setText(openString);
		
		String saveString = "Save";
		if (syntaxMap.containsKey(saveString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(saveString);
			saveString = titleEntry.getLabel();
		}
		menuItemSave.setText(saveString);
		
		String saveAsString = "SaveAs";
		if (syntaxMap.containsKey(saveAsString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(saveAsString);
			saveAsString = titleEntry.getLabel();
		}
		else
		{
			saveAsString = "Save As";
		}
		menuItemSaveAs.setText(saveAsString);
		
		String closeString = "Close";
		if (syntaxMap.containsKey(closeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(closeString);
			closeString = titleEntry.getLabel();
		}
		menuItemClose.setText(closeString);
		
		String printString = "Print";
		if (syntaxMap.containsKey(printString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(printString);
			printString = titleEntry.getLabel();
		}
		menuItemPrint.setText(printString);
		
		String exitString = "Exit";
		if (syntaxMap.containsKey(exitString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(exitString);
			exitString = titleEntry.getLabel();
		}
		menuItemExit.setText(exitString);
		
		String codeGenerationString = "Code generated on the fly";
		if (syntaxMap.containsKey(codeGenerationString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(codeGenerationString);
			codeGenerationString = titleEntry.getLabel();
		}
		menuCodeGeneration.setText(codeGenerationString);
		
		String validateString = "Validate";
		if (syntaxMap.containsKey(validateString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(validateString);
			validateString = titleEntry.getLabel();
		}
		menuItemValidate.setText(validateString);
		
		String generateCCodeString = "Generate C Code";
		if (syntaxMap.containsKey(generateCCodeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(generateCCodeString);
			generateCCodeString = titleEntry.getLabel();
		}
		checkBoxMenuItemGenerateCCode.setText(generateCCodeString);
		
		String generateJavaCodeString = "Generate Java Code";
		if (syntaxMap.containsKey(generateJavaCodeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(generateJavaCodeString);
			generateJavaCodeString = titleEntry.getLabel();
		}
		checkBoxMenuItemGenerateJavaCode.setText(generateJavaCodeString);
		
		String viewCodeString = "View Code";
		if (syntaxMap.containsKey(viewCodeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(viewCodeString);
			viewCodeString = titleEntry.getLabel();
		}
		menuItemViewCode.setText(viewCodeString);
		
		String configurationString = "Configuration";
		if (syntaxMap.containsKey(configurationString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(configurationString);
			configurationString = titleEntry.getLabel();
		}
		menuConfiguration.setText(configurationString);
		
		String englishString = "English";
		if (syntaxMap.containsKey(englishString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(englishString);
			englishString = titleEntry.getLabel();
		}
		checkBoxMenuItemEnglish.setText(englishString);
		
		String spanishString = "Spanish";
		if (syntaxMap.containsKey(spanishString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(spanishString);
			spanishString = titleEntry.getLabel();
		}
		checkBoxMenuItemSpanish.setText(spanishString);
		
		String francisString = "Francis";
		if (syntaxMap.containsKey(francisString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(francisString);
			francisString = titleEntry.getLabel();
		}
		checkBoxMenuItemFrancis.setText(francisString);
		
		String externalMenuString = "External";
		if (syntaxMap.containsKey(externalMenuString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(externalMenuString);
			externalMenuString = titleEntry.getLabel();
		}
		menuExternal.setText(externalMenuString);
		
		String uploadExternalString = "External";
		if (syntaxMap.containsKey(uploadExternalString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(uploadExternalString);
			uploadExternalString = titleEntry.getLabel();
		}
		menuItemExternal.setText(uploadExternalString);
		
		String helpString = "Help";
		if (syntaxMap.containsKey(helpString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(helpString);
			helpString = titleEntry.getLabel();
		}
		menuHelp.setText(helpString);
		
		String onlineHelpString = "Online Help";
		if (syntaxMap.containsKey(onlineHelpString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(onlineHelpString);
			onlineHelpString = titleEntry.getLabel();
		}
		menuItemOnlineHelp.setText(onlineHelpString);
		
		String aboutString = "About";
		if (syntaxMap.containsKey(aboutString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(aboutString);
			aboutString = titleEntry.getLabel();
		}
		menuAbout.setText(aboutString);
		
		String snapString = "Snap";
		if (syntaxMap.containsKey(snapString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(snapString);
			snapString = titleEntry.getLabel();
		}
		menuItemSnap.setText(snapString);
		
		newButton.setToolTipText(newString);
		openButton.setToolTipText(openString);
		saveButton.setToolTipText(saveString);
		printBbutton.setToolTipText(printString);
		validateBbutton.setToolTipText(validateString);
		viewCodeButton.setToolTipText(viewCodeString);
		
		String addVariableString = "Add Variable";
		if (syntaxMap.containsKey(addVariableString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(addVariableString);
			addVariableString = titleEntry.getLabel();
		}
		buttonAddVariable.setToolTipText(addVariableString);
		
		String searchBlocksNameString = "Search blocks";
		if (syntaxMap.containsKey(searchBlocksNameString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(searchBlocksNameString);
			searchBlocksNameString = titleEntry.getLabel();
		}
		String searchBlocksNameTooltipString = "Search for blocks in the drawers and workspace";
		if (syntaxMap.containsKey(searchBlocksNameTooltipString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(searchBlocksNameTooltipString);
			searchBlocksNameTooltipString = titleEntry.getLabel();
		}
		
		searchBar.searchBar.setText(searchBlocksNameString);
		searchBar.searchBar.setToolTipText(searchBlocksNameTooltipString);
	}
	
	/*
     * User selects New either from menu item or tool bar
     * */
    public void newSelected(WorkspaceController wc)
    {    	
    	//if(!projectInitialXmlContent.equals(wc.getSaveString()))
    	if(isProjectModified(wc.getSaveString()))
		{
			int reply = DisplayMessage.saveMessageUserInput("Do you want to save your current work");    		
			//before initializing the window, we are saving current project content
    		if (reply == JOptionPane.YES_OPTION)
			{
			   wc.saveAsPressed(wc);
			}
    		else if(reply == JOptionPane.NO_OPTION)
    		{
    			//do nothing
    		}
    		else
    		{
    			//user didn't select yes/no option
    			return;            			
    		}
		}
		
		//initializing frame title
		String frameTitle = "OpenBlocksDemo";
		if (syntaxMap.containsKey(frameTitle)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(frameTitle);
			frameTitle = titleEntry.getLabel();
		}
		frame.setTitle(frameTitle);
		
		wc.setLangDefFilePath(LANG_DEF_FILEPATH);
        //loading fresh workspace
		wc.loadFreshWorkspace();
		//adding code editor
        Workspace.addCodeEditor();
        Workspace.clearCodeInEditor();
        //updating search option
        wc.updateSearchableComponents(wc);
        wc.resetFileReference();        
    }
    
    /*
     * User selects Open either from menu item or tool bar
     * */
    public void openSelected(WorkspaceController wc)
    {    	
    	//if(!projectInitialXmlContent.equals(wc.getSaveString()))
    	if(isProjectModified(wc.getSaveString()))
		{
    		int reply = DisplayMessage.saveMessageUserInput("Do you want to save your current work");
    		if (reply == JOptionPane.YES_OPTION)
			{
			   wc.savePressed(wc);
			}
    		else if(reply == JOptionPane.NO_OPTION)
    		{
    			//do nothing
    		}
    		else
    		{
    			//user didn't select yes/no option
    			return;
    		}
		}
    	
    	//initializing frame title
		String frameTitle = "OpenBlocksDemo";
		if (syntaxMap.containsKey(frameTitle)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(frameTitle);
			frameTitle = titleEntry.getLabel();
		}
		frame.setTitle(frameTitle);
		
		wc.setLangDefFilePath(LANG_DEF_FILEPATH);
        wc.loadFreshWorkspace(); 
        Workspace.addCodeEditor();
        Workspace.clearCodeInEditor();        
        wc.updateSearchableComponents(wc);        
        wc.resetFileReference();
    	
    	wc.openPostProcessing(wc);
    }
	
	/*
	 * User uploads a file after selecting Open either from menu item or tool bar
	 * */
	public void openPostProcessing(WorkspaceController wc)
	{
		String uploadedFileContent = "";
		String fileName = "";
		JFileChooser fc = new JFileChooser();
        
		String fileChooserDialogTitle = "Open pz File";
		if (syntaxMap.containsKey(fileChooserDialogTitle)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(fileChooserDialogTitle);
			fileChooserDialogTitle = titleEntry.getLabel();
		}
		fc.setDialogTitle(fileChooserDialogTitle);
		String openString = "Open";
		if (syntaxMap.containsKey(openString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(openString);
			openString = titleEntry.getLabel();
		}
		UIManager.put("FileChooser.openButtonText", openString);
		String cancelString = "Cancel";
		if (syntaxMap.containsKey(cancelString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(cancelString);
			cancelString = titleEntry.getLabel();
		}
		UIManager.put("FileChooser.cancelButtonText", cancelString);		
		String openButtonToolTipString = "Open selected file";
		if (syntaxMap.containsKey(openButtonToolTipString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(openButtonToolTipString);
			openButtonToolTipString = titleEntry.getLabel();
		}
		UIManager.put("FileChooser.openButtonToolTipText", openButtonToolTipString);
		String ocancelButtonToolTipString = "Abort file chooser dialog";
		if (syntaxMap.containsKey(ocancelButtonToolTipString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(ocancelButtonToolTipString);
			ocancelButtonToolTipString = titleEntry.getLabel();
		}
        UIManager.put("FileChooser.cancelButtonToolTipText",ocancelButtonToolTipString);
		
		String fileNameString = "File name";
		if (syntaxMap.containsKey(fileNameString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(fileNameString);
			fileNameString = titleEntry.getLabel();
		}
		UIManager.put("FileChooser.fileNameLabelText", fileNameString);		
		String fileTypeString = "Files of type";
		if (syntaxMap.containsKey(fileTypeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(fileTypeString);
			fileTypeString = titleEntry.getLabel();
		}
		UIManager.put("FileChooser.filesOfTypeLabelText", fileTypeString);
		
		SwingUtilities.updateComponentTreeUI(fc);
		
        // Choose only files, not directories
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Start in current directory
        fc.setCurrentDirectory(new File("."));

        // Set filter for text file only.
        fc.setFileFilter(fJavaFilter);

        // Now open chooser
        int result = fc.showOpenDialog(fc);

        //user selects a file
        if (result == JFileChooser.APPROVE_OPTION) {
        	if(!fc.getSelectedFile().getName().endsWith(".pz"))
        	{
        		fileSavePath = fc.getSelectedFile().getPath()+".pz";
                //retrieving text content of the uploaded file
                uploadedFileContent = readFile(fc.getSelectedFile().getPath()+".pz");
                fileName = fc.getSelectedFile().getName()+".pz";
        	}
        	else
        	{
        		fileSavePath = fc.getSelectedFile().getPath();
                //retrieving text content of the uploaded file
                uploadedFileContent = readFile(fc.getSelectedFile().getPath());
                fileName = fc.getSelectedFile().getName();
        	}
        	isFileSaved = true;

            if (uploadedFileContent != null) 
            {
            	String codePart = "";
            	String leftPanelPart = "";
            	if(uploadedFileContent.indexOf("<BlockLangDef>") > 0 && uploadedFileContent.indexOf("<CODEBLOCKS>") >= 0)
            	{
            		codePart = uploadedFileContent.substring(0, uploadedFileContent.indexOf("<BlockLangDef>"));
            		leftPanelPart = uploadedFileContent.substring(uploadedFileContent.indexOf("<BlockLangDef>"));
            	}
            	else
            	{
            		resetFileReference();
            		DisplayMessage.printErrorMessage("Please upload correct file");
            		return;
            	}
            	wc.workspaceLoaded = false;
            	wc.setLangDefFilePathTest(leftPanelPart);
            	wc.loadFreshWorkspace();
         	
            	//updating frame title including file name
                String frameTitle = "OpenBlocksDemo";
        		if (syntaxMap.containsKey(frameTitle)) {
        			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(frameTitle);
        			frameTitle = titleEntry.getLabel();
        		}
        		frame.setTitle(frameTitle+" "+fileName);
        		
            	loadProject(codePart);
            	
            	List<String> errors = BlockValidator.validateAll(XMLToBlockGenerator.generateBlocks(wc.getSaveString()));
            	if(errors.size() > 0)
            	{
            		Workspace.clearCodeInEditor();
            	}
            	else
            	{
            		Workspace.setCodeInEditor();	                    	
            	}
            	
            	//updating search tool bar
            	wc.updateSearchableComponents(wc);
            	
            	String leftPanelXml = "";    		
        		try
        		{
        			TransformerFactory tf = TransformerFactory.newInstance();
            		Transformer transformer = tf.newTransformer();
            		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            		StringWriter writer = new StringWriter();
            		transformer.transform(new DOMSource(WorkspaceController.langDefRoot), new StreamResult(writer));
            		leftPanelXml = writer.getBuffer().toString();        		
        		}
        		catch(Exception ex)
        		{
        			System.out.println("Error while generating left panel xml");
        		}
        		WorkspaceController.savedFileContent = wc.getSaveString()+leftPanelXml;
            } 
            else
            {
            	resetFileReference();
            }
        }
	}
	/*
	 * This method takes action when Save is selected from menu
	 * and saves block content
	 * */
	public void savePressed(final WorkspaceController wc)
	{
		//saving this block content for the first time
		if (!isFileSaved) 
		{
			saveAsPressed(wc);			
        }
        else
        {
            try 
            {
            	String leftPanelXml = "";    		
        		try
        		{
        			TransformerFactory tf = TransformerFactory.newInstance();
            		Transformer transformer = tf.newTransformer();
            		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
            		StringWriter writer = new StringWriter();
            		transformer.transform(new DOMSource(WorkspaceController.langDefRoot), new StreamResult(writer));
            		leftPanelXml = writer.getBuffer().toString();        		
        		}
        		catch(Exception ex)
        		{
        			System.out.println("Error while generating left panel xml");
        		}
            	WorkspaceController.savedFileContent = wc.getSaveString()+leftPanelXml;
        		writeFile(new File(fileSavePath),wc.getSaveString()+leftPanelXml);  
        		
        		DisplayMessage.printSuccessMessage("File is saved successfully");
            }
            catch(Exception ex)
            {
            	System.out.println("Error while saving file.");
            }
        }
	}
	
	/*
	 * This method takes action when SaveAs is selected from menu
	 * and saves block content
	 * */
	public void saveAsPressed(final WorkspaceController wc)
	{
		File file = new File(fileSavePath);
		JFileChooser fc = new JFileChooser();
		
		String saveString = "Save";
		if (syntaxMap.containsKey(saveString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(saveString);
			saveString = titleEntry.getLabel();
		}
		fc.setDialogTitle(saveString);
		UIManager.put("FileChooser.saveButtonText", saveString);
		String cancelString = "Cancel";
		if (syntaxMap.containsKey(cancelString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(cancelString);
			cancelString = titleEntry.getLabel();
		}
		UIManager.put("FileChooser.cancelButtonText", cancelString);		
		String saveButtonToolTipString = "Save selected file";
		if (syntaxMap.containsKey(saveButtonToolTipString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(saveButtonToolTipString);
			saveButtonToolTipString = titleEntry.getLabel();
		}
		UIManager.put("FileChooser.saveButtonToolTipText", saveButtonToolTipString);
		String ocancelButtonToolTipString = "Abort file chooser dialog";
		if (syntaxMap.containsKey(ocancelButtonToolTipString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(ocancelButtonToolTipString);
			ocancelButtonToolTipString = titleEntry.getLabel();
		}
        UIManager.put("FileChooser.cancelButtonToolTipText",ocancelButtonToolTipString);
		
		String fileNameString = "File name";
		if (syntaxMap.containsKey(fileNameString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(fileNameString);
			fileNameString = titleEntry.getLabel();
		}
		UIManager.put("FileChooser.fileNameLabelText", fileNameString);		
		String fileTypeString = "Files of type";
		if (syntaxMap.containsKey(fileTypeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(fileTypeString);
			fileTypeString = titleEntry.getLabel();
		}
		UIManager.put("FileChooser.filesOfTypeLabelText", fileTypeString);
		
		SwingUtilities.updateComponentTreeUI(fc);
		
        // Start in current directory
        fc.setCurrentDirectory(new File("."));
        // Set filter for text files.
        fc.setFileFilter(fJavaFilter);
        // Set to a specific name for save.
        fc.setSelectedFile(file);
        // Open chooser dialog
        int result = fc.showSaveDialog(fc);
        if (result == JFileChooser.APPROVE_OPTION) 
        {
            //adding xml extension after file name if there is no file name extension
        	if(!fc.getSelectedFile().getName().endsWith(".pz")) 
            {
            	file = new File(fc.getSelectedFile()+".pz");
            }
            else
            {
            	file = fc.getSelectedFile();
            }
            
            if (file.exists()) {
            	String overWriteMessage = "Overwrite existing file";
            	if (syntaxMap.containsKey(overWriteMessage)) {
        			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(overWriteMessage);
        			overWriteMessage = titleEntry.getLabel();
        		}
        		String overWriteMessageTitle = "Overwrite";
        		if (syntaxMap.containsKey(overWriteMessageTitle)) {
        			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(overWriteMessageTitle);
        			overWriteMessageTitle = titleEntry.getLabel();
        		}
        		String yesString = "Yes";
        		if (syntaxMap.containsKey(yesString)) {
        			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(yesString);
        			yesString = titleEntry.getLabel();
        		}
        		String noString = "No";
        		if (syntaxMap.containsKey(noString)) {
        			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(noString);
        			noString = titleEntry.getLabel();
        		}
        		int response = JOptionPane.showOptionDialog(null, 
        				overWriteMessage+"?", 
        				overWriteMessageTitle+"?", 
        		        JOptionPane.OK_CANCEL_OPTION, 
        		        JOptionPane.INFORMATION_MESSAGE, 
        		        null, 
        		        new String[]{yesString, noString},
        		        "default");            	
            	
            	/*int response = JOptionPane.showConfirmDialog(null,
                        "Overwrite existing file?", "Confirm Overwrite",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);*/
                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            //updating frame title including file name
            String frameTitle = "OpenBlocksDemo";
    		if (syntaxMap.containsKey(frameTitle)) {
    			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(frameTitle);
    			frameTitle = titleEntry.getLabel();
    		}
    		frame.setTitle(frameTitle+" "+file.getName());
    		
    		String leftPanelXml = "";    		
    		try
    		{
    			TransformerFactory tf = TransformerFactory.newInstance();
        		Transformer transformer = tf.newTransformer();
        		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
        		StringWriter writer = new StringWriter();
        		transformer.transform(new DOMSource(WorkspaceController.langDefRoot), new StreamResult(writer));
        		leftPanelXml = writer.getBuffer().toString();        		
    		}
    		catch(Exception ex)
    		{
    			System.out.println("Error while generating left panel xml");
    		}
    		WorkspaceController.savedFileContent = wc.getSaveString()+leftPanelXml;
            writeFile(file,wc.getSaveString()+leftPanelXml);
            //storing this file path to use later if user wants to save
            fileSavePath = file.getPath();
            isFileSaved = true;
            DisplayMessage.printSuccessMessage("File is saved successfully");
        }
	}
	
	/*
     * User selects Close from menu item
     * */
    public void closeSelected(WorkspaceController wc)
    {
    	//if(!projectInitialXmlContent.equals(wc.getSaveString()))
    	if(isProjectModified(wc.getSaveString()))
		{
    		int reply = DisplayMessage.saveMessageUserInput("Do you want to save your current work");
			//before initializing the window, we are saving current project
    		if (reply == JOptionPane.YES_OPTION)
			{
			   wc.savePressed(wc);
			}
    		else if(reply == JOptionPane.NO_OPTION)
    		{
    			//no action
    		}
    		else
    		{
    			//user didn't select yes/no option
    			return;
    		}
		}
    	//initializing frame title
		String frameTitle = "OpenBlocksDemo";
		if (syntaxMap.containsKey(frameTitle)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(frameTitle);
			frameTitle = titleEntry.getLabel();
		}
		frame.setTitle(frameTitle);
		
		wc.setLangDefFilePath(LANG_DEF_FILEPATH);
        wc.loadFreshWorkspace(); 
        Workspace.addCodeEditor();
        Workspace.clearCodeInEditor();        
        wc.updateSearchableComponents(wc);        
        wc.resetFileReference();
    }
    
    /*
     * User wants to print block content
     * */
    public void printSelected(WorkspaceController wc)
    {
    	Page blockPage = workspace.getPageNamed("Blocks");
    	try 
    	{
    		Color defaultColor = blockPage.getPageColor();
        	
    		blockPage.setPageColor(Color.WHITE);
			JComponent blockComponent = blockPage.getJComponentCloned();
        	blockPage.setPageColor(defaultColor);
        	
        	PrintUtilities.printComponent(blockComponent);
		} 
    	catch (CloneNotSupportedException e) 
    	{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*
     * User selects exit from menu item
     * */
    public void exitSelected(WorkspaceController wc)
    {    	
    	//if(!projectInitialXmlContent.equals(wc.getSaveString()))
    	if(isProjectModified(wc.getSaveString()))	
		{
    		int reply = DisplayMessage.saveMessageUserInput("Do you want to save your current work");
    		if (reply == JOptionPane.YES_OPTION)
			{
			   wc.savePressed(wc);
			}
    		else if(reply == JOptionPane.NO_OPTION)
    		{				
				System.exit(0);
    		}
    		else
    		{
    			//user didn't select yes/no option
    			return;
    		}
		}
    	else
    	{
    		System.exit(0);
    	}
    }
    
    /*
     * User wants to validate block panel
     * */
    public void validateSelected(WorkspaceController wc)
    {
    	List<String> errors = BlockValidator.validateAll(XMLToBlockGenerator.generateBlocks(wc.getSaveString()));
    	if(errors.size() > 0)
    	{
    		ValidationErrorDisplayer displayer = new ValidationErrorDisplayer();
        	displayer.displayError(errors);
        	displayer.setVisible(true);
    	}
    	else
    	{
    		DisplayMessage.printSuccessMessage("No Error");    		   		
    	}
    }
    
    /*
     * User selects External from menu item
     * */
    public void uploadExternalFilePressed(final WorkspaceController wc){
		//String uploadedFileContent = "";
		String parsedFileContent = "";
		String externalVariableList = "";
		File openedFile = new File("external.txt");
		JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open Text File");

        // Choose only files, not directories
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);

        // Start in current directory
        fc.setCurrentDirectory(new File("."));

        // Set filter for text file only.
        fc.setFileFilter(fJavaFilterText);

        // Now open chooser
        int result = fc.showOpenDialog(fc);

        //user selects a file
        if (result == JFileChooser.APPROVE_OPTION) {
        	openedFile = fc.getSelectedFile();
            parsedFileContent = "\""+fc.getSelectedFile().getName().substring(0, fc.getSelectedFile().getName().length()-4)+"\"";
            File aFile = new File(fc.getSelectedFile().getPath());
            try {
                BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(aFile)));
                try {
                    String line = null;
                    String firstWord = "";
                    String variableDeclarationLine = "";
                    String commentString = "";
                    String commentFirstWord = "";
                    String[] tempString;
                    while ((line = input.readLine()) != null) {
                        line = line.trim();
                        if(line.length() >8)
                        {
                        	firstWord = line.substring(0, 8);
                        	if(firstWord.equals("external"))
                            {
                        		externalVariableList = externalVariableList + line + "\n";
                        		tempString = line.split(";");
                            	if(tempString.length > 1)
                            	{
                            		commentString = tempString[1].trim();
                            		if(commentString.length() > 8)
                            		{
                            			commentFirstWord = commentString.substring(0,8);
                            			if(commentFirstWord.equals("external"))
                            			{
                            				//two variables in one line..so error.
                            				DisplayMessage.printErrorMessage("Each variable must be in separate line");
                            				return;
                            			}
                            		}                            		
                            	}
                            	//check duplicate external variable here
                            	variableDeclarationLine = tempString[0];
                            	tempString = variableDeclarationLine.split("=");
                            	parsedFileContent = parsedFileContent+","+tempString[1].trim();
                            }
                        }
                        
                    }
                } finally {
                    input.close();
                }
            } catch (IOException ex) {
                System.out.println("Error while loading file.");
            }          
        }
        else
        {
        	return;
        }
        String uploadContent = externalVariableList+parsedFileContent;
		String uploadString = "Upload";
		if (syntaxMap.containsKey(uploadString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(uploadString);
			uploadString = titleEntry.getLabel();
		}
		String yesString = "Yes";
		if (syntaxMap.containsKey(yesString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(yesString);
			yesString = titleEntry.getLabel();
		}
		String noString = "No";
		if (syntaxMap.containsKey(noString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(noString);
			noString = titleEntry.getLabel();
		}
		int reply = JOptionPane.showOptionDialog(null, 
				uploadContent, 
				uploadString+"?", 
		        JOptionPane.OK_CANCEL_OPTION, 
		        JOptionPane.INFORMATION_MESSAGE, 
		        null, 
		        new String[]{yesString, noString},
		        "default");
        if (reply == JOptionPane.YES_OPTION)
		{
			wc.langDefDirty = true;		
			ExternalOption.addCustombyUser(langDefRoot.getOwnerDocument(), "externalvar"+externalVariableCounter,parsedFileContent, "string");
			wc.loadProject(wc.getSaveString());        		
			externalVariableCounter++;
			updateSearchableComponents(wc);
		}
	}
	
	/*
	 * This method takes a file url and returns content of that file
	 * */
	public String readFile(String fileUrl) {
        File aFile = new File(fileUrl);
        StringBuilder contents = new StringBuilder();
        try {
        	BufferedReader input = new BufferedReader( new InputStreamReader( new FileInputStream(new File(fileUrl)), "UTF-16"));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    contents.append(line);
                }
            } finally {
                input.close();
            }
        } catch (IOException ex) {
            System.out.println("Error while loading file.");
        }
        return contents.toString();
    }
	
	/*
     * This method takes file and file content and store the file content
     * */
    public static boolean writeFile(File file, String dataString) {
    	String newLine = System.getProperty("line.separator");
    	//dataString = dataString.replaceAll("\n", newLine);
        try {
            BufferedWriter bw = new BufferedWriter( new OutputStreamWriter( new FileOutputStream(file), "UTF-16"));
            bw.write(dataString);
            
            bw.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    /*
     * Updating search Bar
     * */
    public void updateSearchableComponents(WorkspaceController wc)
    {
    	searchBar.clearSearchableContainer();
    	for(SearchableContainer con : wc.getAllSearchableContainers()){
            searchBar.addSearchableContainer(con);
        }
    }
    
    /*
     * Returning workspace
     * */
    public static Workspace getWorkspace()
    {	
    	return workspace;
    }
    
    /*
     * Resetting current active project information
     * */
    public void resetFileReference()
    {
    	WorkspaceController.isFileSaved = false;
        WorkspaceController.fileSavePath = "";
        //WorkspaceController.savedFileContent = "";
        
        String leftPanelXml = "";    		
		try
		{
			TransformerFactory tf = TransformerFactory.newInstance();
    		Transformer transformer = tf.newTransformer();
    		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    		StringWriter writer = new StringWriter();
    		transformer.transform(new DOMSource(WorkspaceController.langDefRoot), new StreamResult(writer));
    		leftPanelXml = writer.getBuffer().toString();        		
		}
		catch(Exception ex)
		{
			System.out.println("Error while generating left panel xml");
		}
    	WorkspaceController.savedFileContent = projectInitialXmlContent+leftPanelXml;
    }
    
    /*
     * This method checks whether project content is modified or not
     * */
    public boolean isProjectModified(String projectContent)
    {
    	boolean isModified = false;
    	String leftPanelXml = "";    		
 		try
 		{
 			//generating left panel xml content
 			TransformerFactory tf = TransformerFactory.newInstance();
     		Transformer transformer = tf.newTransformer();
     		transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
     		StringWriter writer = new StringWriter();
     		transformer.transform(new DOMSource(WorkspaceController.langDefRoot), new StreamResult(writer));
     		leftPanelXml = writer.getBuffer().toString();        		
 		}
 		catch(Exception ex)
 		{
 			System.out.println("Error while generating left panel xml");
 		}
 		//comparing current project content with last saved content
 		if(!(WorkspaceController.savedFileContent.equals(projectContent+leftPanelXml)))
 		{
 			isModified = true; 			
 		}
    	return isModified;
    }
}
