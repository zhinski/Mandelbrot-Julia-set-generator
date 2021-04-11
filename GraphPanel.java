import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

@SuppressWarnings("serial")
public class GraphPanel extends JPanel {
		private MainFrame mf;
		protected ComplexPlaneGraphicsPanel complexPlane;
		
		protected JLabel rMin;
		protected JTextField rMinField;
		protected JLabel rMax;
		protected JTextField rMaxField;
		protected JLabel iMin;
		protected JTextField iMinField;
		protected JLabel iMax;
		protected JTextField iMaxField;
		
		protected JPanel rightFillerPanel;
		protected JPanel bottomFillerPanel;
		protected JPanel bottomRightFillerPanel;
		
		protected GridBagConstraints cmplxPlane_gc = new GridBagConstraints() {{
			fill = GridBagConstraints.BOTH;
			anchor = GridBagConstraints.CENTER;
			
			gridx = 0;
			gridy = 0;
			gridheight = 3;
			gridwidth = 3;
			weightx = 1.0;
			weighty = 1.0;
		}};
		
		protected GridBagConstraints iMin_gc = new GridBagConstraints() {{
			 fill = GridBagConstraints.NONE;
			 anchor = GridBagConstraints.CENTER;
			
			 gridx = 3;
			 gridy = 2;
			 gridheight = 1;
			 gridwidth = 1;
			 weightx = 0.0;
			 weighty = 0.0;
			 insets = new Insets(0, 8, 0, 8);
		}};
		protected GridBagConstraints iMax_gc = new GridBagConstraints() {{
			 fill = GridBagConstraints.NONE;
			 anchor = GridBagConstraints.CENTER;
			
			 gridx = 3;
			 gridy = 0;
			 gridheight = 1;
			 gridwidth = 1;
			 weightx = 0.0;
			 weighty = 0.0;
			 insets = new Insets(0, 8, 0, 8);
		}};
		protected GridBagConstraints rMin_gc = new GridBagConstraints() {{
			 fill = GridBagConstraints.NONE;
			 anchor = GridBagConstraints.CENTER;
			
			 gridx = 0;
			 gridy = 3;
			 gridheight = 1;
			 gridwidth = 1;
			 weightx = 0.0;
			 weighty = 0.0;
			 insets = new Insets(8, 0, 8, 0);
		}};
		protected GridBagConstraints rMax_gc = new GridBagConstraints() {{
			 fill = GridBagConstraints.NONE;
			 anchor = GridBagConstraints.CENTER;
			
			 gridx = 2;
			 gridy = 3;
			 gridheight = 1;
			 gridwidth = 1;
			 weightx = 0.0;
			 weighty = 0.0;
			 insets = new Insets(8, 0, 8, 0);
		}};
		protected GridBagConstraints bottomFiller_gc = new GridBagConstraints() {{
			 fill = GridBagConstraints.HORIZONTAL;
			 gridx = 1;
			 gridy = 3;
			 gridheight = 1;
			 gridwidth = 1;
			 weightx = 1.0;
			 weighty = 0.0;
		}};
		protected GridBagConstraints rightFiller_gc = new GridBagConstraints() {{
			 fill = GridBagConstraints.VERTICAL;
			 gridx = 3;
			 gridy = 1;
			 gridheight = 1;
			 gridwidth = 1;
			 weightx = 0.0;
			 weighty = 1.0;
		}};
		
		
		public GraphPanel(MainFrame mf) {
			super();
			this.mf = mf;
//			System.out.println(mf.getMandelbrotInput().getText());
			setLayout(new GridBagLayout());
			
			// Complex Plane (the actual graph)
			complexPlane = new ComplexPlaneGraphicsPanel(mf);
			add(complexPlane, cmplxPlane_gc);
			
			// Imaginary Minimum
			iMin = new JLabel("2.0");
			iMinField = new JTextField(String.valueOf(complexPlane.getMin().getImaginary()));
			iMinField.setVisible(false);
			iMin.setVisible(true);
			iMin.addMouseListener(new MouseListener() {
				
				@Override public void mouseReleased(MouseEvent e) {}
				@Override public void mousePressed(MouseEvent e) {}
				@Override public void mouseExited(MouseEvent e) {}
				@Override public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					iMin_gc.ipadx = iMin.getWidth();
					iMinField.setVisible(true);
					iMin.setVisible(false);
					remove(iMinField);
					add(iMinField, iMin_gc);
				}
			});
			iMinField.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						double input;
						try {
							input = Double.parseDouble(iMinField.getText());
							complexPlane.setMin(new ComplexNumber(complexPlane.getMin().getReal(), input));
							complexPlane.repaint();
							iMinField.setText(String.valueOf(input));
							iMin.setText(String.valueOf(input));
						}
						catch(NumberFormatException nFE) {}
						catch(NullPointerException nPE) {}
						iMinField.setVisible(false);
						iMin.setVisible(true);
					}
				}
			});
			add(iMin, iMin_gc);
			add(iMinField, iMin_gc);
			
			// Imaginary Maximum
			iMax = new JLabel("2.0");
			iMaxField = new JTextField(String.valueOf(complexPlane.getMax().getImaginary()));
			iMaxField.addKeyListener(
				new KeyListener() {
					@Override public void keyTyped(KeyEvent e) {}
					@Override public void keyReleased(KeyEvent e) {}
					@Override
					public void keyPressed(KeyEvent e) {
						if (e.getKeyCode() == KeyEvent.VK_ENTER) {	
							double input;
							try {
								input = Double.parseDouble(iMaxField.getText());
								complexPlane.setMax(new ComplexNumber(complexPlane.getMax().getReal(), input));
								complexPlane.repaint();
								iMaxField.setText(String.valueOf(input));
								iMax.setText(String.valueOf(input));
							}
							catch(NumberFormatException nFE) {}
							catch(NullPointerException nPE) {}
							
							((JTextField)(e.getSource())).setVisible(false);
							iMax.setVisible(true);
						}
					}
				}
			);
			iMax.addMouseListener(new MouseListener() {

				@Override public void mouseReleased(MouseEvent e) {}
				@Override public void mousePressed(MouseEvent e) {}
				@Override public void mouseExited(MouseEvent e) {}
				@Override public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					iMax_gc.ipadx = iMax.getWidth();
					iMaxField.setVisible(true);
					iMax.setVisible(false);
					remove(iMaxField);
					add(iMaxField, iMax_gc);
				}
			});
			iMax.setVisible(true);
			iMaxField.setVisible(false);
			add(iMax, iMax_gc);
			add(iMaxField, iMax_gc);
			
			
			
			// Real Minimum
			rMin = new JLabel("2.0");
			rMinField = new JTextField(String.valueOf(complexPlane.getMin().getReal()));
			rMinField.setVisible(false);
			rMin.setVisible(true);
			rMin.addMouseListener(new MouseListener() {
				
				@Override public void mouseReleased(MouseEvent e) {}
				@Override public void mousePressed(MouseEvent e) {}
				@Override public void mouseExited(MouseEvent e) {}
				@Override public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					rMin_gc.ipadx = iMin.getWidth();
					rMinField.setVisible(true);
					rMin.setVisible(false);
					remove(rMinField);
					add(rMinField, rMin_gc);
				}
			});
			rMinField.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						double input;
						try {
							input = Double.parseDouble(rMinField.getText());
							complexPlane.setMin(new ComplexNumber(input, complexPlane.getMin().getImaginary()));
							complexPlane.repaint();
							rMinField.setText(String.valueOf(input));
							rMin.setText(String.valueOf(input));
						}
						catch(NumberFormatException nFE) {}
						catch(NullPointerException nPE) {}
						rMinField.setVisible(false);
						rMin.setVisible(true);
					}
				}
			});
			add(rMin, rMin_gc);
			add(rMinField, rMin_gc);
			
			// Real Maximum
			rMax = new JLabel("2.0");
			rMaxField = new JTextField(String.valueOf(complexPlane.getMax().getReal()));
			rMaxField.setVisible(false);
			rMax.setVisible(true);
			rMax.addMouseListener(new MouseListener() {
				
				@Override public void mouseReleased(MouseEvent e) {}
				@Override public void mousePressed(MouseEvent e) {}
				@Override public void mouseExited(MouseEvent e) {}
				@Override public void mouseEntered(MouseEvent e) {}
				@Override
				public void mouseClicked(MouseEvent e) {
					rMax_gc.ipadx = iMin.getWidth();
					rMaxField.setVisible(true);
					rMax.setVisible(false);
					remove(rMaxField);
					add(rMaxField, rMax_gc);
				}
			});
			rMaxField.addKeyListener(new KeyListener() {
				@Override public void keyTyped(KeyEvent e) {}
				@Override public void keyReleased(KeyEvent e) {}
				@Override
				public void keyPressed(KeyEvent e) {
					if (e.getKeyCode() == KeyEvent.VK_ENTER) {
						double input;
						try {
							input = Double.parseDouble(rMaxField.getText());
							complexPlane.setMax(new ComplexNumber(input, complexPlane.getMax().getImaginary()));
							complexPlane.repaint();
							rMaxField.setText(String.valueOf(input));
							rMax.setText(String.valueOf(input));
						}
						catch(NumberFormatException nFE) {}
						catch(NullPointerException nPE) {}
						rMaxField.setVisible(false);
						rMax.setVisible(true);
					}
				}
			});
			add(rMax, rMax_gc);
			add(rMaxField, rMax_gc);
			
			
			
			// Filler Panels
			bottomFillerPanel = new JPanel();
			add(bottomFillerPanel, bottomFiller_gc);
			
			rightFillerPanel = new JPanel();
			add(rightFillerPanel, rightFiller_gc);
			
		}
			
	}