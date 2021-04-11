import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.stream.Collectors;

import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButtonMenuItem;

public class Menu extends JMenuBar {
	private MainFrame mf;
	private JMenu type;
	private JRadioButtonMenuItem mandelRButton;
	private JRadioButtonMenuItem juliaRButton;
	private JMenu colorScheme;
	protected ColorBar colorBar;
	
	private JPanel filler = new JPanel();
	
	private GridBagConstraints type_gc = new GridBagConstraints() {{
		gridx = 0;
		weightx = 0.0;
		weighty = 0.0;
		insets = new Insets(0, 5, 0, 5);
	}};
	private GridBagConstraints colorScheme_gc = new GridBagConstraints() {{
		gridx = 1;
		weightx = 0.0;
		weighty = 0.0;
		insets = new Insets(0, 5, 0, 5);
	}};
	private GridBagConstraints filler_gc = new GridBagConstraints() {{
		gridx = 2;
		weightx = 1.0;
		weighty = 0.0;
	}};
	
	public Menu(MainFrame mf) {
		super();
		this.mf = mf;
		setLayout(new GridBagLayout());
		type = new JMenu("Type");
		colorScheme = new JMenu("Color Scheme");
		
		ButtonGroup group = new ButtonGroup();
		mandelRButton = new JRadioButtonMenuItem("Mandelbrot Set");
		juliaRButton = new JRadioButtonMenuItem("Julia Set");
		group.add(juliaRButton);
		group.add(mandelRButton);
		mandelRButton.setSelected(true);
		mandelRButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mf.mandelbrotSet.setVisible(true);
				mf.juliaSet.setVisible(false);
				mf.juliaSetInput.setVisible(false);
				mf.graphPanel.complexPlane.isMandel = true;
				mf.graphPanel.complexPlane.repaint();
			}});
		juliaRButton.setSelected(false);
		juliaRButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				mf.mandelbrotSet.setVisible(false);
				mf.juliaSet.setVisible(true);
				mf.juliaSetInput.setVisible(true);
			}});
		type.add(juliaRButton);
		type.add(mandelRButton);
		
		colorBar = new ColorBar(mf);
		colorScheme.add(colorBar);
		
		add(type, type_gc);
		add(colorScheme, colorScheme_gc);
		add(filler, filler_gc);
	}

	class ColorBar extends JPanel {
		private MainFrame mf;
		private ColorPanel color1;
		private ColorPanel color2;
		private ColorPanel color3;
		private ColorPanel color4;
		private JButton addColorBtn;
		
		private GridBagConstraints color1_gc = new GridBagConstraints() {{
			weightx = 0.0;
			weighty = 0.0;
			gridx = 0;
			gridy = 0;
			ipadx = MainFrame.FONT_SIZE;
			anchor = GridBagConstraints.PAGE_START;
		}};
		private GridBagConstraints color2_gc = new GridBagConstraints() {{
			weightx = 0.0;
			weighty = 0.0;
			gridx = 1;
			gridy = 0;
			ipadx = MainFrame.FONT_SIZE;
		}};
		private GridBagConstraints color3_gc = new GridBagConstraints() {{
			weightx = 0.0;
			weighty = 0.0;
			gridx = 2;
			gridy = 0;
			ipadx = MainFrame.FONT_SIZE;
		}};
		private GridBagConstraints color4_gc = new GridBagConstraints() {{
			weightx = 0.0;
			weighty = 0.0;
			gridx = 3;
			gridy = 0;
			ipadx = MainFrame.FONT_SIZE;
		}};
		private GridBagConstraints addColorBtn_gc = new GridBagConstraints() {{
			weightx = 0.0;
			weighty = 0.0;
			gridx = 4;
			gridy = 0;
			insets = new Insets(MainFrame.FONT_SIZE * 4 + MainFrame.FONT_SIZE/3, 0, MainFrame.FONT_SIZE/3, MainFrame.FONT_SIZE/3);
			anchor = GridBagConstraints.PAGE_START;
			ipadx = MainFrame.FONT_SIZE;
		}};
		
		protected ColorBar(MainFrame mf) {
			this.mf = mf;
			setLayout(new GridBagLayout());
			color1 = new ColorPanel(Color.WHITE);
			color1.removeRemoveBtn();
			add(color1, color1_gc);
			
			addColorBtn = new JButton("+");
			addColorBtn.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					Color chosenColor = JColorChooser.showDialog(mf, "Colors!", Color.BLACK);
					if (color2 == null) {
						color2 = new ColorPanel(chosenColor);
						add(color2, color2_gc);
						color2.removeColor.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								remove(color2);
								color2 = null;
								repaint();
								revalidate();
								mf.graphPanel.complexPlane.repaint();
							}
						});
					} else if (color3 == null) {
						color3 = new ColorPanel(chosenColor);
						add(color3, color3_gc);
						color3.removeColor.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								remove(color3);
								color3 = null;
								repaint();
								revalidate();
								mf.graphPanel.complexPlane.repaint();
							}
						});
					} else if (color4 == null) {
						color4 = new ColorPanel(chosenColor);
						add(color4, color4_gc);
						color4.removeColor.addActionListener(new ActionListener() {
							@Override
							public void actionPerformed(ActionEvent e) {
								remove(color4);
								color4 = null;
								repaint();
								revalidate();
								mf.graphPanel.complexPlane.repaint();
							}
						});
					}
					else {
						JOptionPane.showMessageDialog(mf, "Only four color options can be specified", "Error", JOptionPane.ERROR_MESSAGE);
					}
					mf.graphPanel.complexPlane.repaint();
				}
			});
			addColorBtn.setPreferredSize(new Dimension(addColorBtn.getPreferredSize().height, addColorBtn.getPreferredSize().height));
			add(addColorBtn, addColorBtn_gc);
			
			
		}
		
		public Color[] getColorScheme() {
			ArrayList<Color> col = new ArrayList<>();
			Color[] colors = {
			color1.getBoxColor(),
			(color2 != null) ? color2.getBoxColor() : null,
			(color3 != null) ? color3.getBoxColor() : null,
			(color4 != null) ? color4.getBoxColor() : null};
			// keep in mind that some will be null (color not assigned)
			return colors;
		}
		
//		public void setColorScheme(Color...colors) {
//			Color[] n = Arrays.stream(colors)
//				.filter(c->c != null)
//				.limit(4)
//				.toArray(Color[]::new);
//			color1.;
//			
//		}
		
		private class ColorPanel extends JPanel {
			private ColorBox colorBox;
			private JButton removeColor;
			private JButton changeColor;
			
			private GridBagConstraints colorBox_gc = new GridBagConstraints() {{
				weightx = 0.0;
				weighty = 0.0;
				gridx = 0;
				gridy = 0;
				anchor = GridBagConstraints.CENTER;
			}};
			private GridBagConstraints rmvBtn_gc = new GridBagConstraints() {{
				weightx = 0.0;
				weighty = 0.0;
				gridx = 0;
				gridy = 1;
				insets = new Insets(MainFrame.FONT_SIZE/3, 0, 0, 0);
				anchor = GridBagConstraints.CENTER;
			}};
			private GridBagConstraints chngBtn_gc = new GridBagConstraints() {{
				weightx = 0.0;
				weighty = 0.0;
				gridx = 0;
				gridy = 2;
				insets = new Insets(MainFrame.FONT_SIZE/3, 0, MainFrame.FONT_SIZE/3, 0);
				anchor = GridBagConstraints.CENTER;
			}};
			
			private ColorPanel(Color color) {
				setLayout(new GridBagLayout());
				colorBox = new ColorBox(color);
				add(colorBox, colorBox_gc);
				Font oldFont = getFont();
				
				removeColor = new JButton("Remove");
				add(removeColor, rmvBtn_gc);
				
				changeColor = new JButton("Change");
				changeColor.addActionListener(new ActionListener() {
					@Override
					public void actionPerformed(ActionEvent e) {
						Color chosenColor = JColorChooser.showDialog(mf, "Colors!", Color.WHITE);
						colorBox.changeColor(chosenColor);
						colorBox.repaint();
						mf.graphPanel.complexPlane.repaint();
					}
				});
				add(changeColor, chngBtn_gc);
				
			}
			// For the first color only
			private void removeRemoveBtn() {
				remove(removeColor);
			}
			
			private Color getBoxColor() {
				if (colorBox == null) return null;
				return colorBox.boxColor;
			}
			
			
			private class ColorBox extends JPanel {
				private Color boxColor;

				private ColorBox(Color color) {
					super();
					this.boxColor = color;
				}

				public void changeColor(Color c) {
					boxColor = c;
				}

				@Override
				public void paintComponent(Graphics g) {
					super.paintComponent(g);
					g.setColor(boxColor);
					g.fillRect(0, 0, getWidth(), getHeight());
				}

				@Override
				public Dimension getPreferredSize() {
					// Change the insets of the 'addColorBtn' as well if you want to resize the colorBox
					return new Dimension(MainFrame.FONT_SIZE * 4, MainFrame.FONT_SIZE * 4);
				}

			}
			
		}
		
	}
	
	
	
}
