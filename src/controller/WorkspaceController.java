package controller;


import general.JavaFilter;
import general.JavaFilterText;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

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
import javax.swing.text.JTextComponent;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import language.*;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.sun.org.apache.bcel.internal.generic.GETSTATIC;
import com.sun.xml.internal.ws.util.xml.XmlUtil;


import renderable.BlockUtilities;
import renderable.RenderableBlock;
import sun.rmi.runtime.NewThreadAction;

import workspace.Page;
import workspace.SearchBar;
import workspace.SearchableContainer;
import workspace.TrashCan;
import workspace.Workspace;
import workspace.WorkspaceWidget;
import codeblocks.BlockConnectorShape;
import codeblocks.BlockGenus;
import codeblocks.BlockLinkChecker;
import codeblocks.CommandRule;
import codeblocks.SocketRule;
import codegenerator.BlockValidator;
import codegenerator.CodeGen;
import codegenerator.DisplayCode;
import codegenerator.ExternalOption;
import codegenerator.PrintUtilities;
import codegenerator.PromptVariableName;
import codegenerator.StructureCode;
import codegenerator.ValidationErrorDisplayer;
import codegenerator.Variable;
import codegenerator.VariableMaker;
import codegenerator.XMLToBlockGenerator;
import codegenerator.xmlbind.Block;

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
    
	//JavaFilter class, which supports only text file to upload
    private static JavaFilter fJavaFilter;
    private static JavaFilterText fJavaFilterText;
	private static String fileSavePath = ""; 
	private static boolean isFileSaved = false;
	
	private static int externalVariableCounter = 1;
	private static String projectInitialXmlContent = "";
	private static LanguageGenerator languageGenerator;
	private static Hashtable syntaxMap;
	
	public static JMenu menuFile;
	public static JMenuItem menuItemNew;
	public static JMenuItem menuItemOpen;
	public static JMenuItem menuItemSave;
	public static JMenuItem menuItemSaveAs;
	public static JMenuItem menuItemPrint;
	public static JMenuItem menuItemExternal;
	public static JMenuItem menuItemExit;
	
	public static JMenu menuCodeGeneration;
	public static JMenuItem menuItemValidate;
	public static JCheckBoxMenuItem checkBoxMenuItemGenerateCCode;
	public static JCheckBoxMenuItem checkBoxMenuItemGenerateJavaCode;
	
	public static JMenu menuConfiguration;
	public static JCheckBoxMenuItem checkBoxMenuItemEnglish;
	public static JCheckBoxMenuItem checkBoxMenuItemFrancis;
	public static JCheckBoxMenuItem checkBoxMenuItemSpanish;
	
	public static JMenu menuHelp;
	public static JMenuItem menuItemOnlineHelp;
	
	public static JMenu menuAbout;
	public static JMenuItem menuItemSnap;
	
	public static JButton newButton;
	public static JButton openButton;
	public static JButton saveBbutton;
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
	
	private static String LANG_DEF_FILEPATH;
    
    private static Element langDefRoot;
    
    //flags 
    private boolean isWorkspacePanelInitialized = false;
    
    /** The single instance of the Workspace Controller **/
    protected static Workspace workspace;
    
    protected JPanel workspacePanel;
    protected SearchBar searchBar;
    
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
    	fJavaFilter = new JavaFilter();
    	fJavaFilterText = new JavaFilterText();
    	System.out.println("Creating GUI...");
        syntaxMap = languageGenerator.getSyntaxMapLanguage();       
        //Create and set up the window.
        JFrame frame = new JFrame("OpenBlocks Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        int inset = 50;
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        frame.setBounds(100, 100, 800, 500);
        
        JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		String fileString = "File";
		if (syntaxMap.containsKey(fileString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(fileString);
			fileString = titleEntry.getLabel();
		}
		menuFile = new JMenu(fileString);
		menuBar.add(menuFile);
		projectInitialXmlContent = workspace.getSaveString();
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
				if(!projectInitialXmlContent.equals(workspace.getSaveString()))
				{
					int reply = JOptionPane.showConfirmDialog(null, "Do you want to save your current work?", "Save?",  JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION)
					{
					   wc.savePressed(wc);
					}
					else
					{
						//initialize block panel here
					}
				}
				workspace.clearCodeInEditor();
				
				//String saveString = workspace.getSaveString();
				//workspace.LoadProjectWithVariable();
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
				//wc.langDefDirty = true;
				//VariableMaker.addVariable(langDefRoot.getOwnerDocument(), "string_var", "string");
				//wc.loadProject(wc.getSaveString());
				//JOptionPane.showMessageDialog(null, "Open Clicked.");			
				if(!projectInitialXmlContent.equals(workspace.getSaveString()))
				{
					int reply = JOptionPane.showConfirmDialog(null, "Do you want to save your current work?", "Save?",  JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION)
					{
					   wc.savePressed(wc);
					}
				}
				workspace.clearCodeInEditor();
				wc.openPressed();
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
		String saveAsString = "Save As";
		if (syntaxMap.containsKey(saveAsString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(saveAsString);
			saveAsString = titleEntry.getLabel();
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
				JOptionPane.showMessageDialog(null, "Print Clicked.");
				PrintUtilities.printComponent(workspace.getPageNamed("Blocks").getJComponent());
			}
		});
		String uploadExternalString = "External";
		if (syntaxMap.containsKey(printString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(printString);
			printString = titleEntry.getLabel();
		}
		menuItemExternal = new JMenuItem(uploadExternalString);
		menuFile.add(menuItemExternal);
		menuItemExternal.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				String externalParameterList = "";
				externalParameterList = wc.uploadExternalFilePressed();
				wc.langDefDirty = true;
        		ExternalOption.addCustombyUser(langDefRoot.getOwnerDocument(), "externalvariable"+externalVariableCounter,externalParameterList, "string");
        		wc.loadProject(wc.getSaveString());
        		PrintUtilities.printComponent(workspace.getPageNamed("Blocks").getJComponent());
        		
        		ExternalOption.addCustombyUser(langDefRoot.getOwnerDocument(), "externalvar"+externalVariableCounter,externalParameterList, "string");
        		wc.loadProject(wc.getSaveString());        		
        		externalVariableCounter++;
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
				if(!projectInitialXmlContent.equals(workspace.getSaveString()))
				{
					int reply = JOptionPane.showConfirmDialog(null, "Do you want to save your current work?", "Save?",  JOptionPane.YES_NO_OPTION);
					if (reply == JOptionPane.YES_OPTION)
					{
					   wc.savePressed(wc);
					}
				}
				System.exit(0);
			}
		});
		String codeGenerationString = "Code Generation";
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
				List<String> errors = BlockValidator.validateAll(XMLToBlockGenerator.generateBlocks(wc.getSaveString()));
            	if(errors.size() > 0){
            		ValidationErrorDisplayer displayer = new ValidationErrorDisplayer();
                	displayer.displayError(errors);
                	displayer.setVisible(true);
            	}else{
            		JOptionPane.showMessageDialog(null, "No error found");
            	}			
			}
		});
		
		final ButtonGroup generateCodeButtonGroup = new ButtonGroup();
		String generateCCodeString = "Generate C Code";
		if (syntaxMap.containsKey(generateCCodeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(generateCCodeString);
			generateCCodeString = titleEntry.getLabel();
		}
		checkBoxMenuItemGenerateCCode = new JCheckBoxMenuItem(generateCCodeString,true);
		workspace.setSelectedLanguage("c");
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
				workspace.setSelectedLanguage("c");
			}
		});
		
		generateCodeButtonGroup.add(checkBoxMenuItemGenerateJavaCode);
		menuCodeGeneration.add(checkBoxMenuItemGenerateJavaCode);
		checkBoxMenuItemGenerateJavaCode.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				workspace.setSelectedLanguage("java");
			}
		});
		
		
		/*JMenuItem menuItemGenerateCCode = new JMenuItem("Generate C Code");
		menuCodeGeneration.add(menuItemGenerateCCode);
		menuItemGenerateCCode.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "Generate C Code Clicked.");				
			}
		});
		JMenuItem menuItemGenerateJavaCode = new JMenuItem("Generate Java Code");
		menuCodeGeneration.add(menuItemGenerateJavaCode);
		menuItemGenerateJavaCode.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				workspace.loadProjectWithVariable(wc.getSaveString());
				workspace.setCodeInEditor();
			}
		});*/
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
				//JOptionPane.showMessageDialog(null, "English Clicked.");		
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
				//JOptionPane.showMessageDialog(null, "Francis Clicked.");		
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
				//JOptionPane.showMessageDialog(null, "Francis Clicked.");		
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
				JOptionPane.showMessageDialog(null, "Name:Snap\nVersion:1.0\nSnap Java");				
			}
		});
		
		
        //create search bar
        SearchBar searchBar = new SearchBar("Search blocks", "Search for blocks in the drawers and workspace", workspace);
        for(SearchableContainer con : wc.getAllSearchableContainers()){
            searchBar.addSearchableContainer(con);
        }
        
        // create save button
        JButton saveButton = new JButton("Save");
        saveButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	//JOptionPane.showMessageDialog(null, "Generate Java Code Clicked.");	
				//System.out.println(wc.getSaveString());
				//workspace.loadProjectWithVariable(wc.getSaveString());
				workspace.setCodeInEditor();
                //CodeGen codeGen = new CodeGen();
                //editor.setText(codeGen.getCode(workspace.getSaveString()));
                
                //CodeGen manageCode = new CodeGen();
                //StructureCode sCode = new StructureCode();                
                //editor.setText(sCode.indentMyCode(manageCode.getGenerateCode(wc.getSaveString())));
            }
        });
        
        JPanel topPane = new JPanel();
        
        JToolBar toolbar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
        toolbar.setEnabled(false);
        //JButton cutbutton = new JButton(new ImageIcon("cut.gif"));
        newButton = new JButton(newString);
        newButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "New button clicked toolbar.");
            }
        });
        toolbar.add(newButton);
        
        openButton = new JButton(openString);
        openButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Open button clicked toolbar.");
            }
        });
        toolbar.add(openButton);
        
        saveBbutton = new JButton(saveString);
        saveBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Save button clicked toolbar.");
            }
        });
        toolbar.add(saveBbutton);
        
        printBbutton = new JButton(printString);
        printBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Print button clicked toolbar.");
            	PrintUtilities.printComponent(workspace.getPageNamed("Blocks").getJComponent());
            }
        });
        toolbar.add(printBbutton);
        
        String undoString = "Undo";
		if (syntaxMap.containsKey(undoString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(undoString);
			undoString = titleEntry.getLabel();
		}
        undoButton = new JButton(undoString);
        undoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Undo button clicked toolbar.");
            }
        });
        toolbar.add(undoButton);
        String redoString = "Redo";
		if (syntaxMap.containsKey(redoString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(redoString);
			redoString = titleEntry.getLabel();
		}        
        redoButton = new JButton(redoString);
        redoButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Redo button clicked toolbar.");
            }
        });
        toolbar.add(redoButton);
        String cutString = "Cut";
		if (syntaxMap.containsKey(cutString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(cutString);
			cutString = titleEntry.getLabel();
		}
        cutButton = new JButton(cutString);
        cutButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Cut button clicked toolbar.");
            }
        });
        toolbar.add(cutButton);
        String copyString = "Copy";
		if (syntaxMap.containsKey(copyString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(copyString);
			copyString = titleEntry.getLabel();
		}
        copyButton = new JButton(copyString);
        copyButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Copy button clicked toolbar.");
            }
        });
        toolbar.add(copyButton);
        String pasteString = "Paste";
		if (syntaxMap.containsKey(pasteString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(pasteString);
			pasteString = titleEntry.getLabel();
		}
        pasteButton = new JButton(pasteString);
        pasteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Paste button clicked toolbar.");
            }
        });
        toolbar.add(pasteButton);
        String deleteString = "Delete";
		if (syntaxMap.containsKey(deleteString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(deleteString);
			deleteString = titleEntry.getLabel();
		}
        deleteButton = new JButton(deleteString);
        deleteButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Delete button clicked toolbar.");
            }
        });
        toolbar.add(deleteButton);
        
        validateBbutton = new JButton(validateString);
        validateBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	//System.out.println(BlockValidator.validateAll(XMLToBlockGenerator.generateBlocks(wc.getSaveString())));
            	List<String> errors = BlockValidator.validateAll(XMLToBlockGenerator.generateBlocks(wc.getSaveString()));
            	if(errors.size() > 0){
            		ValidationErrorDisplayer displayer = new ValidationErrorDisplayer();
                	displayer.displayError(errors);
                	displayer.setVisible(true);
            	}else{
            		JOptionPane.showMessageDialog(null, "No error found");
            	}
            	
            }
        });
        toolbar.add(validateBbutton);        
        /*generateCCodeButton = new JButton(generateCCodeString);
        generateCCodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	workspace.setSelectedLanguage("c");               
                String code = workspace.getCode();            	
            	DisplayCode displayCode = new DisplayCode();
            	displayCode.setCode(code);
            	displayCode.setVisible(true);
            	
            	workspace.setCodeInEditor(); 
            }
        });
        toolbar.add(generateCCodeButton);
        generateJavaCodeButton = new JButton(generateJavaCodeString);
        generateJavaCodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	workspace.setSelectedLanguage("java");               
                String code = workspace.getCode();            	
            	DisplayCode displayCode = new DisplayCode();
            	displayCode.setCode(code);
            	displayCode.setVisible(true);
            	
            	//workspace.setSelectedLanguage("java");
            	workspace.setCodeInEditor();
            }
        });
        toolbar.add(generateJavaCodeButton);*/
        
        String viewCodeString = "View Code";
		if (syntaxMap.containsKey(viewCodeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(viewCodeString);
			viewCodeString = titleEntry.getLabel();
		}
		viewCodeButton = new JButton(viewCodeString);
		viewCodeButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	List<String> errors = BlockValidator.validateAll(XMLToBlockGenerator.generateBlocks(wc.getSaveString()));
            	if(errors.size() > 0){
            		ValidationErrorDisplayer displayer = new ValidationErrorDisplayer();
                	displayer.displayError(errors);
                	displayer.setVisible(true);
                	
                	workspace.clearCodeInEditor();
            	}else{
            		String code = workspace.getCode();            	
                	DisplayCode displayCode = new DisplayCode();
                	displayCode.setCode(code);
                	displayCode.setVisible(true);
                	
                	workspace.setCodeInEditor();
            	}
            }
        });
        toolbar.add(viewCodeButton);
        
        JButton buttonAddVariable = new JButton("Add Variable");
        buttonAddVariable.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	PromptVariableName variableName = new PromptVariableName();
            	variableName.setModal(true);
            	variableName.setVisible(true);
            	
            	
            	if(variableName.getOption() == JOptionPane.OK_OPTION)
            	{
                	if( BlockGenus.getGenusWithName(Variable.getValidVariableName(variableName.getValue())) != null){
                		JOptionPane.showMessageDialog(null, "Variable exists with the same name.");
                	}
                	else{
	            		wc.langDefDirty = true;
	            		VariableMaker.addVariable(langDefRoot.getOwnerDocument(), variableName.getValue(), variableName.getVariableType());
	            		wc.loadProject(wc.getSaveString());
	            		JOptionPane.showMessageDialog(null, "A "+ variableName.getVariableType() +" variable named \""+ variableName.getValue() +"\" has been added");

	            		codeblocks.Block block = new codeblocks.Block(variableName.getValue()+"_decl");
	            		Page blockPage = workspace.getPageNamed("Blocks");
	            		WorkspaceWidget ww = workspace.getWidgetAt(new Point(blockPage.getJComponent().getX(), blockPage.getJComponent().getY()));
	            		blockPage.blockDropped(new RenderableBlock(ww, block.getBlockID()));
	            		
                	}
            	}
            	
            }
        });
        toolbar.add(buttonAddVariable);
        
        //frame.getContentPane().add(toolbar,BorderLayout.NORTH);
        
        
        searchBar.getComponent().setPreferredSize(new Dimension(130, 23));
        JPanel searchBarPanel = new JPanel();
        searchBarPanel.add(searchBar.getComponent());
        topPane.setLayout(new BorderLayout());
        topPane.add(toolbar,BorderLayout.NORTH);
        topPane.add(searchBarPanel, BorderLayout.CENTER);
        //topPane.add(saveButton);
        frame.add(topPane, BorderLayout.PAGE_START);        
        frame.add(wc.getWorkspacePanel(), BorderLayout.CENTER);
        workspace.addCodeEditor();
        frame.setVisible(true);
        
    }

    public static void main(String[] args) {
        //Schedule a job for the event-dispatching thread:
        //creating and showing this application's GUI.
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() { 
                //TODO grab file path from args array
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
		
		String saveAsString = "Save As";
		if (syntaxMap.containsKey(saveAsString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(saveAsString);
			saveAsString = titleEntry.getLabel();
		}
		menuItemSaveAs.setText(saveAsString);
		
		String printString = "Print";
		if (syntaxMap.containsKey(printString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(printString);
			printString = titleEntry.getLabel();
			PrintUtilities.printComponent(workspace.getPageNamed("Blocks").getJComponent());
		}
		menuItemPrint.setText(printString);
		
		String exitString = "Exit";
		if (syntaxMap.containsKey(exitString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(exitString);
			exitString = titleEntry.getLabel();
		}
		menuItemExit.setText(exitString);
		
		String codeGenerationString = "Code Generation";
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
		
		String francisString = "Francis";
		if (syntaxMap.containsKey(francisString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(francisString);
			francisString = titleEntry.getLabel();
		}
		checkBoxMenuItemFrancis.setText(francisString);
		
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
		
		String viewCodeString = "View code";
		if (syntaxMap.containsKey(viewCodeString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(viewCodeString);
			viewCodeString = titleEntry.getLabel();
		}
		
		newButton.setText(newString);
		openButton.setText(openString);
		saveBbutton.setText(saveString);
		printBbutton.setText(printString);
		validateBbutton.setText(validateString);
		//generateCCodeButton.setText(generateCCodeString);
		//generateJavaCodeButton.setText(generateJavaCodeString);
		viewCodeButton.setText(viewCodeString);
		
		String undoString = "Undo";
		if (syntaxMap.containsKey(undoString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(undoString);
			undoString = titleEntry.getLabel();
		}
		undoButton.setText(undoString);
		String redoString = "Redo";
		if (syntaxMap.containsKey(redoString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(redoString);
			redoString = titleEntry.getLabel();
		}
		redoButton.setText(redoString);
		String cutString = "Cut";
		if (syntaxMap.containsKey(cutString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(cutString);
			cutString = titleEntry.getLabel();
		}
		cutButton.setText(cutString);
		String copyString = "Copy";
		if (syntaxMap.containsKey(copyString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(copyString);
			copyString = titleEntry.getLabel();
		}
		copyButton.setText(copyString);
		String pasteString = "Paste";
		if (syntaxMap.containsKey(pasteString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(pasteString);
			pasteString = titleEntry.getLabel();
		}
		pasteButton.setText(pasteString);
		String deleteString = "Delete";
		if (syntaxMap.containsKey(deleteString)) {
			LanguageEntry titleEntry = (LanguageEntry) syntaxMap.get(deleteString);
			deleteString = titleEntry.getLabel();
		}
		deleteButton.setText(deleteString);
	}
	
	public void openPressed(){
		String uploadedFileContent = "";
		File openedFile = new File("samle.xml");
		JFileChooser fc = new JFileChooser();
        fc.setDialogTitle("Open XML File");

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
        	openedFile = fc.getSelectedFile();
            fileSavePath = fc.getSelectedFile().getPath();
            //retrieving text content of the uploaded file
            uploadedFileContent = readFile(fc.getSelectedFile().getPath());

            if (uploadedFileContent != null) {
                //upload xml file here
            	//JOptionPane.showMessageDialog(null, uploadedFileContent);
            	loadProject(uploadedFileContent);
            }            
        }

	}
	/*
	 * This method takes action when Save is selected from menu
	 * and saves block content
	 * */
	public void savePressed(final WorkspaceController wc)
	{
		//saving the block content into a xml file
		File file = new File(fileSavePath);
        //saving this block content for the first time
		if (!isFileSaved) {
            JFileChooser fc = new JFileChooser();
            // Start in current directory
            fc.setCurrentDirectory(new File("."));
            // Set filter for text files.
            fc.setFileFilter(fJavaFilter);
            // Set to a specific name for saving
            fc.setSelectedFile(file);
            // Open chooser dialog
            int result = fc.showSaveDialog(fc);

            if (result == JFileChooser.APPROVE_OPTION) {
                file = fc.getSelectedFile();
                if (file.exists()) {
                    int response = JOptionPane.showConfirmDialog(null,
                            "Overwrite existing file?", "Confirm Overwrite",
                            JOptionPane.OK_CANCEL_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (response == JOptionPane.CANCEL_OPTION) {
                        return;
                    }
                }
                //writing text content into file
                writeFile(file, wc.getSaveString());
                //updating changed file path
                fileSavePath = fc.getSelectedFile().getPath();	                
                isFileSaved = true;
            }
        }
        else{
            try {
                FileWriter fstream = new FileWriter(fileSavePath);
                BufferedWriter out = new BufferedWriter(fstream);
                out.write(wc.getSaveString());
                out.close();
            }
            catch(Exception ex){

            }
        }
	}
	
	public void saveAsPressed(final WorkspaceController wc)
	{
		File file = new File(fileSavePath);
        JFileChooser fc = new JFileChooser();
        // Start in current directory
        fc.setCurrentDirectory(new File("."));
        // Set filter for text files.
        fc.setFileFilter(fJavaFilter);
        // Set to a specific name for save.
        fc.setSelectedFile(file);
        // Open chooser dialog
        int result = fc.showSaveDialog(fc);
        if (result == JFileChooser.APPROVE_OPTION) {
            file = fc.getSelectedFile();
            if (file.exists()) {
                int response = JOptionPane.showConfirmDialog(null,
                        "Overwrite existing file?", "Confirm Overwrite",
                        JOptionPane.OK_CANCEL_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (response == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
            writeFile(file,wc.getSaveString());
            fileSavePath = fc.getSelectedFile().getPath();
            isFileSaved = true;
        }
	}
	/*
	 * This method takes a file url and returns content of that file
	 * */
	public String readFile(String fileUrl) {
        File aFile = new File(fileUrl);
        StringBuilder contents = new StringBuilder();
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(new FileInputStream(aFile)));
            try {
                String line = null;
                while ((line = input.readLine()) != null) {
                    contents.append(line+"\n");
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
    	dataString = dataString.replaceAll("\n", newLine);
        try {
            PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(file)));
            out.print(dataString);
            out.flush();
            out.close();
        } catch (IOException e) {
            return false;
        }
        return true;
    }
    
    public String uploadExternalFilePressed(){
		//String uploadedFileContent = "";
		String parsedFileContent = "";
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
                        System.out.println("each line:"+line);
                        line = line.trim();
                        if(line.length() >8)
                        {
                        	firstWord = line.substring(0, 8);
                        	if(firstWord.equals("external"))
                            {
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
                            				return "Error";
                            			}
                            		}
                            		
                            	}
                            	//check duplicate external variable here
                            	variableDeclarationLine = tempString[0];
                            	tempString = variableDeclarationLine.split("=");
                            	parsedFileContent = parsedFileContent+","+tempString[1].trim();
                            	System.out.println("variable vlaue:"+tempString[1]);
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
        return parsedFileContent;

	}
}
