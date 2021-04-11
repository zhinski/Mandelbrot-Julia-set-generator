import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.GridBagLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;


public class GradientTester {
	
	private final Dimension INITIAL_D = new Dimension(2500, 1500);
	private JFrame frame;
	private ColorPanel cp;
	public InterPol col = new InterPol(Color.BLACK, Color.ORANGE, Color.GREEN, Color.BLUE, Color.CYAN, Color.GRAY);
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					GradientTester gt = new GradientTester();
					gt.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
	
	public GradientTester() {
		frame = new JFrame();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setTitle("Gradient Tester");
		frame.setLayout(new GridBagLayout());
		cp = new ColorPanel();
		cp.setSize(INITIAL_D);
		frame.getContentPane().add(cp);
		frame.pack();
	}
	
	private class ColorPanel extends JPanel {
		private static final long serialVersionUID = -5678720961890148091L;
		
		private Color getColor(int iterations, int maxIterations,Color...colors) {
			
			//TODO: Change Me!
//			int value = (int)(255.0*(1.0 - ((double)iterations/(double)maxIterations)));
			
			return col.iterToColor(iterations);
		}
		
		@Override
		public void paintComponent(Graphics g) {
			
			int maxIterations = getWidth();
			Color c;
			
			for(int iterations = 0; iterations <= maxIterations; iterations ++) {
				c = getColor((int) ((double) iterations / (double) maxIterations * (double) Mandelbrot.MAX_DEPTH), maxIterations);
				g.setColor(c);
				g.drawLine(iterations, 0, iterations, getHeight());
			}
		}
		
		@Override
		public Dimension getPreferredSize() {
			return INITIAL_D;
		}
	}
}
