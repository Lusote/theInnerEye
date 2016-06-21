package rlmain;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.JTextPane;

import screens.Screen;
import screens.StartScreen;
import squidpony.squidgrid.gui.SGPane;
import squidpony.squidgrid.gui.swing.SwingPane;
import util.ConfigFileManager;

public class RoguelikeMain extends JFrame{// implements KeyListener{
	private static final long serialVersionUID = 765634756565656L;

	private static SGPane console;
	private static RoguelikeMain game;
	private static JTextArea textArea;
	private static JTextPane textPane;
	private static Screen screen;
	private static boolean blindMode;
	private static int fontSize;
	private static String language;
	private static Locale locale;
	public static ResourceBundle messages;
	//private static StyledDocument doc;

	public static SGPane init_console() {
        SGPane console = new SwingPane();
        console.initialize(80, 45,  new Font("Arial Unicode MS", Font.PLAIN, fontSize));
        //console.initialize(5, 5, 80, 35,  new Font("Arial Unicode MS", Font.PLAIN, 14));
        return console;
    }
	
	public static JTextPane init_textPane(){
		final JTextPane textP = new JTextPane();
		textP.setFont( new Font("Arial Unicode MS", Font.PLAIN, fontSize));
        /*StyledDocument doc = textP.getStyledDocument();
        Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
        Style regular = doc.addStyle("regular", def);
        Style monospaced =*/ 
    	int w = 54 * console.getCellWidth();
        int h = 14 * console.getCellHeight();
        Dimension panelDimension = new Dimension(w, h);

    	textP.setSize(panelDimension);
    	textP.setMinimumSize(panelDimension);
    	textP.setPreferredSize(panelDimension);
    	textP.setEditable(true);
    	textP.setFocusable(true);
		textP.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {
			    int keyCode = e.getKeyCode();
			    switch( keyCode ) { 
			        case KeyEvent.VK_UP:
			        case KeyEvent.VK_DOWN:
			        case KeyEvent.VK_LEFT:
			        case KeyEvent.VK_RIGHT: 
			        return;
			     }
				e.consume();
			}
			
			@Override
			public void keyReleased(KeyEvent e) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void keyPressed(KeyEvent key) {
			    int keyCode = key.getKeyCode();
			    switch( keyCode ) { 
			        case KeyEvent.VK_UP:
			        case KeyEvent.VK_DOWN:
			        case KeyEvent.VK_LEFT:
			        case KeyEvent.VK_RIGHT: 
			        return;
			     }
				setScreen(screen.reactToInput(key));
				System.out.println(screen.toString());
				System.out.println(key.getKeyChar());
				game.repaint();
			}
		});
    	/*textP.addMouseListener(new MouseListener() {
			
			@Override
			public void mouseReleased(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mousePressed(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseExited(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseEntered(MouseEvent arg0) {
				// TODO Auto-generated method stub
				
			}
			
			@Override
			public void mouseClicked(MouseEvent arg0) {
				game.requestFocus();
				
			}
		});*/
    	textP.setVisible(true);
    	//doc = textP.getStyledDocument();
		return textP;
	}
	
	public static JTextArea init_textArea(){
		final JTextArea textA = new JTextArea();
		textA.setFont( new Font("Arial Unicode MS", Font.PLAIN, fontSize));
    	textA.setVisible(true);
    	int w = 54 * console.getCellWidth();
        int h = 16 * console.getCellHeight();
        Dimension panelDimension = new Dimension(w, h);

    	textA.setSize(panelDimension);
    	textA.setMinimumSize(panelDimension);
    	textA.setRows(29);
    	textA.setPreferredSize(panelDimension);
    	textA.setEditable(true);
    	textA.setFocusable(true);
		textA.addKeyListener(new KeyListener() {
			
			@Override
			public void keyTyped(KeyEvent e) {e.consume();}
			
			@Override
			public void keyReleased(KeyEvent e) {}
			
			@Override
			public void keyPressed(java.awt.event.KeyEvent key) {
				
			    int keyCode = key.getKeyCode();
			    switch( keyCode ) { 
			        case KeyEvent.VK_UP:
			        case KeyEvent.VK_DOWN:
			        case KeyEvent.VK_LEFT:
			        case KeyEvent.VK_RIGHT: 
			        return;
			     }
				setScreen(screen.reactToInput(key));
				//System.out.println("TA: "+screen.toString());
				//System.out.println("TA: "+key.getKeyChar());
				game.repaint();
			}
		});
		return textA;
	}
	
    public RoguelikeMain(){
    	super("NewRoguelike");
    	setLocale();
    	setBlindMode();
    	setFontSize();
		screen = new StartScreen();
    	if(isBlindMode()){
    		setFocusable(true);
	        //addKeyListener(this);   	
	        setLayout(new BorderLayout());
	        console = init_console();
	        textArea = init_textArea();
	        textArea.requestFocus();
	        //textPane = init_textPane();
	        //textPane.requestFocus();
	        add((Component)console,BorderLayout.CENTER);
	        //add((Component)textPane,BorderLayout.EAST);
	        add((Component)textArea,BorderLayout.EAST);
	    	int w = 135 * console.getCellWidth();
	        int h = 48 * console.getCellHeight();
	        Dimension panelDimension = new Dimension(w, h);
	        
	    	setSize(panelDimension);
	    	setMinimumSize(panelDimension);
	    	setPreferredSize(panelDimension);
			pack();
			setResizable(false);		
			repaint();
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
		}
    	else{        	
    		this.addKeyListener(new KeyListener() {
    			
    			@Override
    			public void keyTyped(KeyEvent e) {
    			    int keyCode = e.getKeyCode();
    			    switch( keyCode ) { 
    			        case KeyEvent.VK_UP:
    			        case KeyEvent.VK_DOWN:
    			        case KeyEvent.VK_LEFT:
    			        case KeyEvent.VK_RIGHT: 
    			        return;
    			     }
    			}
    			
    			@Override
    			public void keyReleased(KeyEvent e) {
    				// TODO Auto-generated method stub
    				
    			}
    			
    			@Override
    			public void keyPressed(KeyEvent key) {
    				key.consume();
    			    int keyCode = key.getKeyCode();
    			    switch( keyCode ) { 
    			        case KeyEvent.VK_UP:
    			        case KeyEvent.VK_DOWN:
    			        case KeyEvent.VK_LEFT:
    			        case KeyEvent.VK_RIGHT: 
    			        return;
    			     }
    				setScreen(screen.reactToInput(key));
    				//System.out.println("** "+screen.toString());
    				//System.out.println("** "+key.getKeyChar());
    				game.repaint();
    			}
    		});
    		console = init_console();
    		add((Component) console);
    		pack();
    		repaint();    		
	        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			setVisible(true);
			/*
	    	addMouseListener(new MouseListener() {
			
				@Override
				public void mouseReleased(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mousePressed(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseExited(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseEntered(MouseEvent arg0) {
					// TODO Auto-generated method stub
					
				}
				
				@Override
				public void mouseClicked(MouseEvent arg0) {
					game.requestFocus();
					
				}
			});*/
    	}
    }
    
    /*  
    public void toogleView(){
    	if(getTextArea().isVisible()){
    	}else{
    		
    	}
    }
    
   public static Speaker getSpeaker(){
    	return speaker;
    }
*/   
    public static RoguelikeMain getGame(){
    	return game;
    }
    
    public static JTextArea getTextArea(){
    	return textArea;
    }
    
    public static void setTextArea(JTextArea text){
    	textArea = text;
    }
    
    public static JTextPane getTextPane(){
    	return textPane;
    }
    
    public static void setTextPane(JTextPane text){
    	textPane = text;
    }
    
    public static int getFontSize(){
    	return fontSize;
    }    
    public static void setFontSize(){
    	try{
    	fontSize = Integer.parseInt(ConfigFileManager.getFontSize());
    	}catch(Exception e){fontSize = 12;}
    }
    
    public static SGPane getConsole(){
    	return console;
    }
    
    public static void setConsole(SGPane con){
    	console = con;
    }
    
    public static void setScreen(Screen s){
    	screen =  s;
    }
    
    public static void exitGame(){
    	getGame().setVisible(false);
    	getGame().dispose();
    }
    
    public int getPositionStartLine(int line){
    	try{
    		
    	}
    	catch(Exception e){
    		
    	}
    	return 9;
    }
    
	@Override
	public void repaint(){
    	if(isBlindMode())
    		//textPane.setText("");
    		getTextArea().setText("");
		for(int i = 0; i < console.getGridHeight(); i++)
			for(int j = 0; j < console.getGridWidth(); j++)
				console.clearCell(j, i);
		console.refresh();
		screen.display(console);
		super.repaint();
	}
	public static void main(String[] args) {
		game = new RoguelikeMain();
		game.requestFocus();
	}
	
	public static long getSerialVersionUID(){
		return serialVersionUID;
	}

	public static boolean isBlindMode() {
		return blindMode;
	}

	public static void setBlindMode() {
		
		RoguelikeMain.blindMode = ConfigFileManager.getTextArea();
	}
	
	public static void setLocale(){
		setLanguage();
		locale = new Locale(language);
        messages = ResourceBundle.getBundle("util.Messages", locale);
	}
	
	public static void setLanguage(){
		RoguelikeMain.language = ConfigFileManager.getDefaultLanguage();
	}
}
