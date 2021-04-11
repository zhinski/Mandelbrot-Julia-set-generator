import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;

///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//////&&&////&//////////&////////////&&&&&&&&&///////&&&&&&&&//////////&&&&&///////////&&&&&////////&&&&&&&&///////&&&&&&&&&//////&&&&&///////////&&&&&////
///////&/////&&/////////&////////////&////////&&/////&///////&&///////&/////&/////////&/////&///////&///////&&/////&/////////////&/////&/////////&/////&///
///////&/////&/&////////&////////////&/////////&&////&////////&&/////&///////&///////&///////&//////&////////&&////&////////////&///////&///////&///////&//
///////&/////&//&///////&////////////&/////////&&////&////////&&////&/////////&/////&///////////////&////////&&////&////////////&///////////////&//////////
///////&/////&///&//////&////////////&////////&&/////&///////&&/////&/////////&/////&///////////////&///////&&/////&/////////////&///////////////&/////////
///////&/////&////&/////&////////////&&&&&&&&&///////&&&&&&&&///////&/////////&/////&///////////////&&&&&&&&///////&//////////////&///////////////&////////
///////&/////&/////&////&////////////&///////////////&&&////////////&/////////&/////&///////////////&&&////////////&&&&/////////////&///////////////&//////
///////&/////&//////&///&////////////&///////////////&/&&///////////&/////////&/////&//////&&&&/////&/&&///////////&//////////////////&///////////////&////
///////&/////&///////&//&////////////&///////////////&///&&/////////&/////////&/////&/////////&/////&///&&/////////&///////////////////&///////////////&///
///////&/////&////////&/&////////////&///////////////&/////&&////////&///////&///////&///////&&/////&/////&&///////&////////////&///////&///////&///////&//
///////&/////&/////////&&////////////&///////////////&///////&&///////&/////&/////////&/////&/&/////&///////&&/////&/////////////&/////&/////////&/////&///
//////&&&////&//////////&////////////&///////////////&////////&&///////&&&&&///////////&&&&&////////&////////&&////&&&&&&&&&//////&&&&&///////////&&&&&////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
///////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

public class MainFrame extends JFrame{
	public static final int FONT_SIZE = 20;
	protected GraphPanel graphPanel;
	protected JPanel rulePanel;
	protected Menu menu;
	protected EquationParser juliaRule = new EquationParser("z^2");
	
	protected JLabel mandelbrotSet;
	protected JLabel juliaSet;
	protected JTextField juliaSetInput;
	/////////////////////////////////////////////////////////////////////////////////
	protected InterPol interpol = new InterPol(Color.WHITE);
	/////////////////////////////////////////////////////////////////////////////////
	
	@SuppressWarnings("serial")
	public MainFrame(String title) {
		super(title);
		
		GridBagConstraints ruleLabel_gc = new GridBagConstraints() {{  // Note: this is inside the rulePanel
			gridx = 0;
			weightx = 0.0;
			insets = new Insets(0, 0, 0, 10);
			fill = NONE;
		}};
		GridBagConstraints ruleTxt_gc = new GridBagConstraints() {{ // Note: this is inside the rulePanel
			gridx = 1;
			weightx = 0.0;
			fill = NONE;
		}};
		GridBagConstraints feedback_gc = new GridBagConstraints() {{ // Note: this is inside the rulePanel
			// =============== NOT YET COMPLETE ===================
			// TO BE DEVELOPED FURTHER
			gridx = 2;
			weightx = 1.0;
			fill = HORIZONTAL;
		}};
		GridBagConstraints rulePanel_gc = new GridBagConstraints() {{  
			gridy = 1;
			weightx = 1.0;
			weighty = 0.0;
			insets = new Insets(20, 20, 20, 20);
			fill = HORIZONTAL;
		}};
		GridBagConstraints toolbar_gc = new GridBagConstraints() {{
			gridy = 0;
			weightx = 0.0;
			weighty = 0.0;
			fill = HORIZONTAL;
			gridwidth = 4;
		}};
		GridBagConstraints graph_gc = new GridBagConstraints() {{
			gridy = 2;
			weightx = 1.0;
			weighty = 1.0;
			fill = BOTH;
			gridwidth = 4;
		}};
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		
		getContentPane().setLayout(new GridBagLayout());
		
		
		menu = new Menu(this);
		setJMenuBar(menu);
		menu.setPreferredSize(new Dimension(menu.getPreferredSize().width, FONT_SIZE*2));
		
		
		
		///////////////////////////////////////////////////////////
		mandelbrotSet = new JLabel("The Mandelbrot Set");
		juliaSet = new JLabel("f ᶻ⁺¹(z) =");
		juliaSet.setVisible(false);
		mandelbrotSet.setVisible(true);
		rulePanel = new JPanel(new GridBagLayout());
		getContentPane().add(rulePanel, rulePanel_gc);
		
		rulePanel.add(mandelbrotSet, ruleLabel_gc);
		rulePanel.add(juliaSet, ruleLabel_gc);
		
		
		juliaSetInput = new JTextField(30);
		juliaSetInput.setVisible(false);
		juliaSetInput.addKeyListener(new KeyListener() {
			@Override public void keyTyped(KeyEvent e) {}
			@Override public void keyReleased(KeyEvent e) {}
			@Override public void keyPressed(KeyEvent e) {
				if (e.getKeyCode() == KeyEvent.VK_ENTER) {
					juliaRule.setNewRule(juliaSetInput.getText());
					graphPanel.complexPlane.isMandel = false;
					graphPanel.complexPlane.repaint();
				}
			}
		});
		rulePanel.add(juliaSetInput, ruleTxt_gc);
		rulePanel.add(new JPanel(), feedback_gc);
		
		////////////////////////////////////////////////////////////
		graphPanel = new GraphPanel(this);
		graphPanel.setPreferredSize(new Dimension(500, 500));
		getContentPane().add(graphPanel, graph_gc);
		
		pack();
		
	}
	
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					setUIFont (new javax.swing.plaf.FontUIResource("Cambria Math", Font.PLAIN, FONT_SIZE));
					MainFrame fractal = new MainFrame("Fractals");
					fractal.setVisible(true);
				} 
				catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public static void setUIFont(javax.swing.plaf.FontUIResource f) {
		java.util.Enumeration keys = UIManager.getDefaults().keys();
		while (keys.hasMoreElements()) {
			Object key = keys.nextElement();
			Object value = UIManager.get(key);
			if (value instanceof javax.swing.plaf.FontUIResource)
				UIManager.put(key, f);
		}
	}
	
	
}
