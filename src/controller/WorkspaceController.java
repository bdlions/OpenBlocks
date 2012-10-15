package controller;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
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
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import workspace.Page;
import workspace.SearchBar;
import workspace.SearchableContainer;
import workspace.TrashCan;
import workspace.Workspace;
import codeblocks.BlockConnectorShape;
import codeblocks.BlockGenus;
import codeblocks.BlockLinkChecker;
import codeblocks.CommandRule;
import codeblocks.SocketRule;
import codegenerator.CodeGen;

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
    
    private static JTextComponent editor;
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
        
        DocumentBuilderFactory factory=DocumentBuilderFactory.newInstance();
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
        saveString.append("<?xml version=\"1.0\" encoding=\"ISO-8859\"?>");
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
    private static void createAndShowGUI(WorkspaceController wc) {
        System.out.println("Creating GUI...");
        
        //Create and set up the window.
        JFrame frame = new JFrame("OpenBlocks Demo");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
//        int inset = 50;
//        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        
        frame.setBounds(100, 100, 800, 500);
        
        JMenuBar menuBar = new JMenuBar();
		frame.setJMenuBar(menuBar);
		
		JMenu menuFile = new JMenu("File");
		menuBar.add(menuFile);
		
		JMenuItem menuItemNew = new JMenuItem("New");
		menuFile.add(menuItemNew);
		menuItemNew.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "New Clicked.");				
			}
		});
		JMenuItem menuItemOpen = new JMenuItem("Open");
		menuFile.add(menuItemOpen);
		menuItemOpen.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "Open Clicked.");				
			}
		});
		JMenuItem menuItemSave = new JMenuItem("Save");
		menuFile.add(menuItemSave);
		menuItemSave.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "Save Clicked.");				
			}
		});
		JMenuItem menuItemSaveAs = new JMenuItem("Save As");
		menuFile.add(menuItemSaveAs);
		menuItemSaveAs.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "Save As Clicked.");				
			}
		});
		JMenuItem menuItemPrint = new JMenuItem("Print");
		menuFile.add(menuItemPrint);
		menuItemPrint.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "Print Clicked.");				
			}
		});
		JMenuItem menuItemExit = new JMenuItem("Exit");
		menuFile.add(menuItemExit);
		menuItemExit.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "Exit Clicked.");				
			}
		});
		
		JMenu menuCodeGeneration = new JMenu("Code Generation");
		menuBar.add(menuCodeGeneration);
		
		JMenuItem menuItemValidate = new JMenuItem("Validate");
		menuCodeGeneration.add(menuItemValidate);
		menuItemValidate.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "Validate Clicked.");				
			}
		});
		JMenuItem menuItemGenerateCCode = new JMenuItem("Generate C Code");
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
				//JOptionPane.showMessageDialog(null, "Generate Java Code Clicked.");	
				System.out.println(workspace.getSaveString());
                
                //CodeGen codeGen = new CodeGen();
                //editor.setText(codeGen.getCode(workspace.getSaveString()));
                
                CodeGen manageCode = new CodeGen();
                StructureCode sCode = new StructureCode();                
                editor.setText(sCode.indentMyCode(manageCode.getGenerateCode(workspace.getSaveString())));
			}
		});
		
		JMenu menuConfiguration = new JMenu("Configuration");
		menuBar.add(menuConfiguration);
		
		JMenu menuHelp= new JMenu("Help");
		menuBar.add(menuHelp);
		JMenuItem menuItemOnlineHelp = new JMenuItem("Online Help");
		menuHelp.add(menuItemOnlineHelp);
		menuItemOnlineHelp.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "Online Help Clicked.");				
			}
		});
		
		JMenu menuAbout= new JMenu("About");
		menuBar.add(menuAbout);
		JMenuItem menuItemSnap = new JMenuItem("Snap");
		menuAbout.add(menuItemSnap);
		menuItemSnap.addActionListener(new ActionListener() 
		{
			public void actionPerformed(ActionEvent e) 
			{
				JOptionPane.showMessageDialog(null, "Snap Clicked.");				
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
                System.out.println(workspace.getSaveString());
                
                //CodeGen codeGen = new CodeGen();
                //editor.setText(codeGen.getCode(workspace.getSaveString()));
                
                CodeGen manageCode = new CodeGen();
                editor.setText(manageCode.getGenerateCode(workspace.getSaveString()));
            }
        });
        
        JPanel topPane = new JPanel();
        
        JToolBar toolbar = new JToolBar("Toolbar", JToolBar.HORIZONTAL);
        //JButton cutbutton = new JButton(new ImageIcon("cut.gif"));
        JButton newbutton = new JButton("New");
        newbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "New button clicked toolbar.");
            }
        });
        toolbar.add(newbutton);
        
        JButton openBbutton = new JButton("Open");
        openBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Open button clicked toolbar.");
            }
        });
        toolbar.add(openBbutton);
        
        JButton saveBbutton = new JButton("Save");
        saveBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Save button clicked toolbar.");
            }
        });
        toolbar.add(saveBbutton);
        
        JButton printBbutton = new JButton("Print");
        printBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Print button clicked toolbar.");
            }
        });
        toolbar.add(printBbutton);
        
        JButton undoBbutton = new JButton("Undo");
        undoBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Undo button clicked toolbar.");
            }
        });
        toolbar.add(undoBbutton);
        JButton redoBbutton = new JButton("Redo");
        redoBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Redo button clicked toolbar.");
            }
        });
        toolbar.add(redoBbutton);
        JButton cutBbutton = new JButton("Cut");
        cutBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Cut button clicked toolbar.");
            }
        });
        toolbar.add(cutBbutton);
        JButton copyBbutton = new JButton("Copy");
        copyBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Copy button clicked toolbar.");
            }
        });
        toolbar.add(copyBbutton);
        JButton pasteBbutton = new JButton("Paste");
        pasteBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Paste button clicked toolbar.");
            }
        });
        toolbar.add(pasteBbutton);
        JButton deleteBbutton = new JButton("Delete");
        deleteBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Delete button clicked toolbar.");
            }
        });
        toolbar.add(deleteBbutton);
        
        JButton validateBbutton = new JButton("Validate");
        validateBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Validate button clicked toolbar.");
            }
        });
        toolbar.add(validateBbutton);        
        JButton generateCCodeBbutton = new JButton("Generate C Code");
        generateCCodeBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	JOptionPane.showMessageDialog(null, "Generate C Code button clicked toolbar.");
            }
        });
        toolbar.add(generateCCodeBbutton);
        JButton generateJavaCodeBbutton = new JButton("Generate Java Code");
        generateJavaCodeBbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent evt){
            	//JOptionPane.showMessageDialog(null, "Generate Java Code button clicked toolbar.");
            	System.out.println(workspace.getSaveString());
                
                //CodeGen codeGen = new CodeGen();
                //editor.setText(codeGen.getCode(workspace.getSaveString()));
                
                CodeGen manageCode = new CodeGen();
                StructureCode sCode = new StructureCode();                
                editor.setText(sCode.indentMyCode(manageCode.getGenerateCode(workspace.getSaveString())));
            }
        });
        toolbar.add(generateJavaCodeBbutton);
        
        
        frame.getContentPane().add(toolbar,BorderLayout.NORTH);
        
        
        searchBar.getComponent().setPreferredSize(new Dimension(130, 23));
        topPane.add(searchBar.getComponent());
        topPane.add(saveButton);
        //frame.add(topPane, BorderLayout.PAGE_START);        
        frame.add(wc.getWorkspacePanel(), BorderLayout.CENTER);
        
        
        Page editorPage = workspace.getPageNamed("Code");
        editor = new JTextPane();
        editor.setBackground(editorPage.getJComponent().getBackground());
        editor.setForeground(Color.green);
        editor.setFont(new Font("monospaced", Font.BOLD, 15));
        
        int width = (int)editorPage.getJComponent().getBounds().getWidth();
        int height = (int)editorPage.getJComponent().getBounds().getHeight();
        Rectangle updatedDimensionRect = new Rectangle(20,0,width,height);
        
        editor.setBounds(updatedDimensionRect);
        editorPage.getRBParent().addToBlockLayer(editor);
        
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
}
